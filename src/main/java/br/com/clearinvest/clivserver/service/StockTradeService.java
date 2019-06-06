package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.ClivServerApp;
import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.repository.*;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.mapper.StockTradeMapper;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockTrade.
 */
@Service
@Transactional
public class StockTradeService {

    private final Logger log = LoggerFactory.getLogger(StockTradeService.class);

    private final StockTradeRepository stockTradeRepository;

    private final StockTradeMapper stockTradeMapper;

    private final BrokerageAccountRepository brokerageAccountRepository;

    private final StockOrderService stockOrderService;

    private final OMSService omsService;

    private final UserRepository userRepository;

    private final StockFlowService stockFlowService;

    private final BrokerageFlowRepository brokerageFlowRepository;

    private final StockFlowRepository stockFlowRepository;

    @Value("${cliv.sendOrderToOmsInDevMode}")
    boolean sendOrderToOmsInDevMode = false;

    @Value("${cliv.createFakeOrderInDevMode}")
    boolean createFakeOrderInDevMode = false;

    public StockTradeService(StockTradeRepository stockTradeRepository, StockTradeMapper stockTradeMapper,
            BrokerageAccountRepository brokerageAccountRepository, StockOrderService stockOrderService,
            OMSService omsService, UserRepository userRepository, StockFlowService stockFlowService,
            BrokerageFlowRepository brokerageFlowRepository, StockFlowRepository stockBalanceRepository) {
        this.stockTradeRepository = stockTradeRepository;
        this.stockTradeMapper = stockTradeMapper;
        this.brokerageAccountRepository = brokerageAccountRepository;
        this.stockOrderService = stockOrderService;
        this.omsService = omsService;
        this.userRepository = userRepository;
        this.stockFlowService = stockFlowService;
        this.brokerageFlowRepository = brokerageFlowRepository;
        this.stockFlowRepository = stockBalanceRepository;
    }

    /**
     * Save a stockTrade.
     *
     * @param tradeDTO the entity to save
     * @return the persisted entity
     */
    public StockTradeDTO save(StockTradeDTO tradeDTO) {
        log.debug("Request to save StockTrade : {}", tradeDTO);

        if (tradeDTO.isManualEntry()) {
            return saveManualEntry(tradeDTO);

        } else {
            final Object[] result = createTradeAndOrder(tradeDTO);
            StockTrade trade = (StockTrade) result[0];
            StockOrder order = (StockOrder) result[1];

            NewOrderSingle orderSingle = stockOrderService.createNewOrderSingleMessage(order, trade.getStock().getSymbol());
            omsService.sendOrderToOMS(orderSingle, order.getId());

            return toDtoFillingDerived(trade, stockTradeMapper);
        }
    }

    private StockTradeDTO saveManualEntry(StockTradeDTO tradeDTO) {
        StockTrade trade = stockTradeMapper.toEntity(tradeDTO);
        trade.setCreatedAt(ZonedDateTime.now());
        trade.setStatus(StockTrade.STATUS_FIX_FILLED);
        trade.setMarket(StockTrade.MARKET_SPOT);

        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(trade::setCreatedBy);

        Optional<BrokerageAccount> accountOptional = brokerageAccountRepository
                .findByIdAndCurrentUser(trade.getBrokerageAccount().getId());

        if (accountOptional.isPresent()) {
            BigDecimal totalPrice = trade.getUnitPrice().multiply(new BigDecimal(trade.getQuantity()));
            trade.setTotalPrice(totalPrice);
            trade.setTotalPriceActual(totalPrice);
            trade.setBrokerageAccount(accountOptional.get());
            trade = stockTradeRepository.save(trade);

            trade.getStock().setSymbol(tradeDTO.getStockSymbol());
            stockFlowService.addManualEntry(trade);

            return toDtoFillingDerived(trade, stockTradeMapper);

        } else {
            throw new BusinessException("Conta não encontrada.");
        }
    }

    /**
     * Update a stockTrade.
     *
     * @param tradeDTO the entity to save
     * @return the persisted entity
     */
    public StockTradeDTO update(StockTradeDTO tradeDTO) {
        log.debug("Request to update StockTrade : {}", tradeDTO);

        StockTrade trade = stockTradeRepository.findByIdAndCreatedByIsCurrentUser(tradeDTO.getId())
                .orElseThrow(() -> new BusinessException("Ordem não encontrada."));

        StockOrder localOrder = createUpdateOrder(trade, tradeDTO);
        StockOrder orderToEdit = trade.getMainOrder();

        OrderCancelReplaceRequest orderReplace = stockOrderService.createOrderCancelReplaceRequest(localOrder,
                orderToEdit, trade.getStock().getSymbol());
        omsService.sendOrderToOMS(orderReplace, localOrder.getId());

        return toDtoFillingDerived(trade, stockTradeMapper);
    }

    public Object[] createTradeAndOrder(StockTradeDTO tradeDTO) {
        StockTrade trade = stockTradeMapper.toEntity(tradeDTO);

        BrokerageAccount brokerageAccount = brokerageAccountRepository
                .findByIdAndCurrentUser(trade.getBrokerageAccount().getId())
                .orElseThrow(() -> new BusinessException("Conta não encontrada."));

        BigDecimal stockTotalPrice = trade.getUnitPrice().multiply(new BigDecimal(trade.getQuantity()));
        if (StockOrder.FIX_SIDE_BUY.equals(trade.getSide())) {
            BigDecimal brokerageBalance = brokerageFlowRepository.getBalanceOfCurrentUser();
            if (stockTotalPrice.compareTo(brokerageBalance) > 0) {
                throw new BusinessException("Saldo na corretora insuficiente.");
            }
        } else if (StockOrder.FIX_SIDE_SELL.equals(trade.getSide())) {
            Long stockQuantity = stockFlowRepository.getQuantityOfCurrentUserAndStock(trade.getStock());
            if (stockQuantity == null || trade.getQuantity() > stockQuantity) {
                throw new BusinessException("Quantidade de papéis na carteira insuficiente.");
            }
        }

        trade.setCreatedAt(ZonedDateTime.now());
        trade.setTradeDate(ZonedDateTime.now());
        trade.setStatus(StockTrade.STATUS_LOCAL_NEW);
        trade.setMarket(StockTrade.MARKET_SPOT);
        trade.setExecQuantity(0L);
        trade.setTotalPrice(BigDecimal.valueOf(0.0));
        trade.setTotalPriceActual(BigDecimal.valueOf(0.0));

        Brokerage brokerage = brokerageAccount.getBrokerage();
        trade.setBrokerageFee(brokerageAccount.getFee() != null ? brokerageAccount.getFee() : brokerage.getFee());
        trade.setBrokerageFeeIss(brokerage.getIss());

        if (StockTrade.MARKET_SPOT.equals(trade.getMarket())) {
            trade.setNegotiationPerc(BigDecimal.valueOf(0.004476));
            trade.setLiquidationPerc(BigDecimal.valueOf(0.0275));
            trade.setRegistryPerc(BigDecimal.valueOf(0.0));
        }

        if (trade.getSide().equals(StockOrder.FIX_SIDE_SELL)) {
            trade.setIrrfPerc(BigDecimal.valueOf(0.005));
        }

        ZonedDateTime expireTime = trade.getExpireTime() == null ? ZonedDateTime.now() : trade.getExpireTime();
        expireTime = ZonedDateTime.of(expireTime.getYear(), expireTime.getMonth().getValue(), expireTime.getDayOfMonth(),
                18, 0, 0, 0, ClivServerApp.getZoneIdDefault());
        trade.setExpireTime(expireTime);

        SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(trade::setCreatedBy);

        trade = stockTradeRepository.save(trade);

        trade.getStock().setSymbol(tradeDTO.getStockSymbol());
        StockOrder order = stockOrderService.createOrder(trade);

        trade.setMainOrder(order);
        trade = stockTradeRepository.save(trade);

        return new Object[]{trade, order};
    }

    public static BigDecimal calculateTotalPriceActual(StockTrade trade) {
        return trade.getTotalPrice()
                .add(calculateFees(trade))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal calculateFees(StockTrade trade) {
        BigDecimal iss = calculateIssVal(trade).orElse(BigDecimal.ZERO);
        BigDecimal negotiation = calculateNegotiationFeeVal(trade).orElse(BigDecimal.ZERO);
        BigDecimal liquidation = calculateLiquidationVal(trade).orElse(BigDecimal.ZERO);
        BigDecimal registry = calculateRegistryVal(trade).orElse(BigDecimal.ZERO);
        BigDecimal irrf = calculateIrrfVal(trade).orElse(BigDecimal.ZERO);

        return (trade.getBrokerageFee() != null ? trade.getBrokerageFee() : BigDecimal.ZERO)
                .add(iss)
                .add(negotiation)
                .add(liquidation)
                .add(registry)
                .add(irrf)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static Optional<BigDecimal> calculateIssVal(StockTrade trade) {
        return trade.getBrokerageFee() != null && trade.getBrokerageFeeIss() != null ?
                Optional.of(trade.getBrokerageFee().multiply(trade.getBrokerageFeeIss().divide(BigDecimal.valueOf(100.0))))
                : Optional.empty();
    }

    public static Optional<BigDecimal> calculateNegotiationFeeVal(StockTrade trade) {
        return trade.getNegotiationPerc() != null ?
                Optional.of(trade.getTotalPrice().multiply(trade.getNegotiationPerc().divide(BigDecimal.valueOf(100.0))))
                : Optional.empty();
    }

    public static Optional<BigDecimal> calculateLiquidationVal(StockTrade trade) {
        return trade.getLiquidationPerc() != null ?
                Optional.of(trade.getTotalPrice().multiply(trade.getLiquidationPerc().divide(BigDecimal.valueOf(100.0))))
                : Optional.empty();
    }

    public static Optional<BigDecimal> calculateRegistryVal(StockTrade trade) {
        return trade.getRegistryPerc() != null ?
                Optional.of(trade.getTotalPrice().multiply(trade.getRegistryPerc().divide(BigDecimal.valueOf(100.0))))
                : Optional.empty();
    }

    public static Optional<BigDecimal> calculateIrrfVal(StockTrade trade) {
        return trade.getIrrfPerc() != null ?
                Optional.of(trade.getTotalPrice().multiply(trade.getIrrfPerc().divide(BigDecimal.valueOf(100.0))))
                : Optional.empty();
    }

    public StockOrder createUpdateOrder(StockTrade trade, StockTradeDTO tradeDTO) {
        Optional<BrokerageAccount> accountOptional = brokerageAccountRepository
                .findByIdAndCurrentUser(trade.getBrokerageAccount().getId());

        if (accountOptional.isPresent()) {
            ZonedDateTime expireTime = trade.getExpireTime() == null ? ZonedDateTime.now() : trade.getExpireTime();
            expireTime = ZonedDateTime.of(expireTime.getYear(), expireTime.getMonth().getValue(), expireTime.getDayOfMonth(),
                    18, 0, 0, 0, ClivServerApp.getZoneIdDefault());
            tradeDTO.setExpireTime(expireTime);

            BigDecimal stockTotalPrice = trade.getUnitPrice().multiply(new BigDecimal(trade.getQuantity()));

            BrokerageAccount account = accountOptional.get();
            BigDecimal accountBalance = account.getBalance() == null ? new BigDecimal(0) : account.getBalance();
            if (stockTotalPrice.compareTo(accountBalance) > 0) {
                throw new BusinessException("Saldo na corretora insuficiente.");
            }

            //trade.setStockTotalPrice(stockTotalPrice);
            trade = stockTradeRepository.save(trade);

            // TODO add a "mainOrder" field to StockOrder in order to have a reference to the main current order of the trade?

            User user = SecurityUtils.getCurrentUserLogin()
                    .flatMap(userRepository::findOneByLogin)
                    .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

            StockOrder order = stockOrderService.createOrderReplace(trade, tradeDTO, user);
            return order;

        } else {
            throw new BusinessException("Conta não encontrada.");
        }
    }

    public static StockTradeDTO toDtoFillingDerived(StockTrade trade, StockTradeMapper stockTradeMapper) {
        StockTradeDTO dto = stockTradeMapper.toDto(trade);

        String status = trade.getStatus();
        String statusDescr = null;

        switch (status) {
            case (StockTrade.STATUS_LOCAL_NEW):
                statusDescr = "Salva";
                break;
            case (StockTrade.STATUS_FIX_NEW):
                statusDescr = "Criada";
                break;
            case (StockTrade.STATUS_FIX_PARTIALLY_FILLED):
                statusDescr = "Parcialmente Executada";
                break;
            case (StockTrade.STATUS_FIX_FILLED):
                statusDescr = "Executada";
                break;
            case (StockTrade.STATUS_FIX_CANCELED):
                statusDescr = "Cancelada";
                break;
            case (StockTrade.STATUS_FIX_REPLACED):
                statusDescr = "Editada";
                break;
            case (StockTrade.STATUS_FIX_PENDING_CANCEL):
                statusDescr = "Cancelamento Pendente";
                break;
            case (StockTrade.STATUS_FIX_REJECTED):
                statusDescr = "Rejeitada";
                break;
            case (StockTrade.STATUS_FIX_SUSPENDED):
                statusDescr = "Suspensa";
                break;
            case (StockTrade.STATUS_FIX_PENDING_NEW):
                statusDescr = "Esperando Abertura do Mercado";
                break;
            case (StockTrade.STATUS_FIX_EXPIRED):
                statusDescr = "Expirada";
                break;
            case (StockTrade.STATUS_FIX_PENDING_REPLACE):
                statusDescr = "Esperando Edição";
                break;
            case (StockTrade.STATUS_FIX_RECEIVED):
                statusDescr = "Recebida";
                break;
        }

        dto.setStatusDescr(statusDescr);

        boolean canCancel = status.equals(StockTrade.STATUS_LOCAL_NEW)
                || status.equals(StockTrade.STATUS_FIX_RECEIVED)
                || status.equals(StockTrade.STATUS_FIX_PENDING_NEW)
                || status.equals(StockTrade.STATUS_FIX_NEW)
                || status.equals(StockTrade.STATUS_FIX_PARTIALLY_FILLED)
                || status.equals(StockTrade.STATUS_FIX_PENDING_REPLACE)
                || status.equals(StockTrade.STATUS_FIX_REPLACED);

        dto.setCanCancel(canCancel);

        boolean canEdit = status.equals(StockTrade.STATUS_LOCAL_NEW)
                || status.equals(StockTrade.STATUS_FIX_RECEIVED)
                || status.equals(StockTrade.STATUS_FIX_PENDING_NEW)
                || status.equals(StockTrade.STATUS_FIX_NEW)
                || status.equals(StockTrade.STATUS_FIX_PARTIALLY_FILLED)
                || status.equals(StockTrade.STATUS_FIX_REPLACED);

        dto.setCanEdit(canEdit);

        dto.setBrokerageFeeIssVal(calculateIssVal(trade).orElse(null));
        dto.setNegotiationVal(calculateNegotiationFeeVal(trade).orElse(null));
        dto.setLiquidationVal(calculateLiquidationVal(trade).orElse(null));
        dto.setRegistryVal(calculateRegistryVal(trade).orElse(null));
        dto.setIrrfVal(calculateIrrfVal(trade).orElse(null));

        return dto;
    }

    /**
     * Get all the stockTrades.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockTradeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockTrades");
        return stockTradeRepository.findAll(pageable)
                .map(stockTradeMapper::toDto);
    }

    /**
     * Get all the stockTrades.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StockTradeDTO> findAll() {
        log.debug("Request to get all StockTrades");
        return stockTradeRepository.findByCreatedByIsCurrentUser().stream()
                .map((t) -> toDtoFillingDerived(t, stockTradeMapper))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one stockTrade by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockTradeDTO> findOne(Long id) {
        log.debug("Request to get StockTrade : {}", id);
        return stockTradeRepository.findById(id)
                .map((t) -> toDtoFillingDerived(t, stockTradeMapper));
    }

    /**
     * Delete the stockTrade by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockTrade : {}", id);
        stockTradeRepository.deleteById(id);
    }

    /**
     * Cancel the stockTrade by id.
     *
     * @param id the id of the entity
     */
    public void cancel(Long id) {
        log.debug("Request to cancel StockTrade : {}", id);

        StockTrade trade = stockTradeRepository
                .findByIdAndCreatedByIsCurrentUser(id)
                .orElseThrow(() -> new BusinessException("Negociação não encontrada."));

        if (StockTrade.STATUS_LOCAL_NEW.equals(trade.getStatus())) {
            trade.setStatus(StockTrade.STATUS_FIX_CANCELED);
            stockTradeRepository.save(trade);

        } else if (canCancelStatusList().contains(trade.getStatus())) {
            stockOrderService.cancel(trade);

        } else {
            throw new BusinessException(String.format("Não é possível cancelar ordem no status %s.", trade.getStatus()));
        }
    }

    public static List<String> canCancelStatusList() {
        return Arrays.asList(
                StockTrade.STATUS_LOCAL_NEW,
                StockTrade.STATUS_FIX_NEW,
                StockTrade.STATUS_FIX_PARTIALLY_FILLED,
                StockTrade.STATUS_FIX_REPLACED,
                StockTrade.STATUS_FIX_SUSPENDED,
                StockTrade.STATUS_FIX_PENDING_NEW,
                StockTrade.STATUS_FIX_PENDING_REPLACE,
                StockTrade.STATUS_FIX_RECEIVED);
    }

}
