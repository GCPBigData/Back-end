package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.ClivServerApp;
import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.BrokerageAccountRepository;
import br.com.clearinvest.clivserver.repository.StockTradeRepository;
import br.com.clearinvest.clivserver.repository.UserRepository;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.mapper.StockTradeMapper;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
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

    private final PlatformTransactionManager platformTransactionManager;


    public StockTradeService(StockTradeRepository stockTradeRepository, StockTradeMapper stockTradeMapper,
            BrokerageAccountRepository brokerageAccountRepository, StockOrderService stockOrderService,
            OMSService omsService, UserRepository userRepository,
            PlatformTransactionManager platformTransactionManager) {
        this.stockTradeRepository = stockTradeRepository;
        this.stockTradeMapper = stockTradeMapper;
        this.brokerageAccountRepository = brokerageAccountRepository;
        this.stockOrderService = stockOrderService;
        this.omsService = omsService;
        this.userRepository = userRepository;
        this.platformTransactionManager = platformTransactionManager;
    }

    /**
     * Save a stockTrade.
     *
     * @param tradeDTO the entity to save
     * @return the persisted entity
     */
    public StockTradeDTO save(StockTradeDTO tradeDTO) {
        log.debug("Request to save StockTrade : {}", tradeDTO);

        TransactionTemplate transactionTemplate = new TransactionTemplate(platformTransactionManager);
        final Object[] result = transactionTemplate.execute(status -> createTradeAndOrder(tradeDTO));
        StockTrade trade = (StockTrade) result[0];
        StockOrder order = (StockOrder) result[1];

        NewOrderSingle orderSingle = stockOrderService.createNewOrderSingleMessage(order, trade.getStock().getSymbol());
        omsService.sendMessegeToOMS(orderSingle, order.getId());

        return toDtoFillingDerived(trade);
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
        StockOrder orderToEdit = trade.getOrders().stream()
            .sorted(Comparator.comparing(StockOrder::getId))
            .collect(Collectors.toList())
            .get(0);

        OrderCancelReplaceRequest orderReplace = stockOrderService.createOrderCancelReplaceRequest(localOrder,
            orderToEdit, trade.getStock().getSymbol());
        omsService.sendMessegeToOMS(orderReplace, localOrder.getId());

        return toDtoFillingDerived(trade);
    }

    public Object[] createTradeAndOrder(StockTradeDTO tradeDTO) {
        StockTrade trade = stockTradeMapper.toEntity(tradeDTO);
        trade.setCreatedAt(ZonedDateTime.now());
        trade.setStatus(StockTrade.STATUS_LOCAL_NEW);

        ZonedDateTime expireTime = trade.getExpireTime() == null ? ZonedDateTime.now() : trade.getExpireTime();
        expireTime = ZonedDateTime.of(expireTime.getYear(), expireTime.getMonth().getValue(), expireTime.getDayOfMonth(),
            18, 0, 0, 0, ClivServerApp.getZoneIdDefault());
        trade.setExpireTime(expireTime);

        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(trade::setCreatedBy);

        Optional<BrokerageAccount> accountOptional = brokerageAccountRepository
            .findByIdAndCurrentUser(trade.getBrokerageAccount().getId());

        if (accountOptional.isPresent()) {
            BigDecimal stockTotalPrice = trade.getUnitPrice().multiply(new BigDecimal(trade.getQuantity()));

            BrokerageAccount account = accountOptional.get();
            BigDecimal accountBalance = account.getBalance() == null ? new BigDecimal(0) : account.getBalance();
            if (stockTotalPrice.compareTo(accountBalance) > 0) {
                throw new BusinessException("Saldo na corretora insuficiente.");
            }

            //trade.setStockTotalPrice(stockTotalPrice);
            trade = stockTradeRepository.save(trade);

            // TODO add a "mainOrder" field to StockOrder in order to have a reference to the main current order of the trade?

            trade.getStock().setSymbol(tradeDTO.getStockSymbol());
            StockOrder order = stockOrderService.createOrder(trade);
            return new Object[]{trade, order};

        } else {
            throw new BusinessException("Conta não encontrada.");
        }
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

    public StockTradeDTO toDtoFillingDerived(StockTrade stockTrade) {
        StockTradeDTO dto = stockTradeMapper.toDto(stockTrade);

        if (stockTrade.getStatus() != null) {
            String status = stockTrade.getStatus();
            String statusDescr = null;

            switch (status) {
                case (StockTrade.STATUS_LOCAL_NEW):
                    statusDescr = "Salva";
                    break;
                case (StockTrade.STATUS_FIX_NEW):
                    statusDescr = "Recebida";
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
        }

        return dto;
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
            .map(this::toDtoFillingDerived)
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
            .map(this::toDtoFillingDerived);
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
