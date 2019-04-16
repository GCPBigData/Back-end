package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.repository.BrokerageAccountRepository;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;
import br.com.clearinvest.clivserver.service.mapper.StockOrderMapper;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOrder.
 */
@Service
@Transactional
public class StockOrderService {

    private static final char FIX_ORD_TYPE_MARKET = '1';

    private final Logger log = LoggerFactory.getLogger(StockOrderService.class);

    private final StockOrderRepository stockOrderRepository;

    private final BrokerageAccountRepository brokerageAccountRepository;

    private final StockOrderMapper stockOrderMapper;

    public StockOrderService(StockOrderRepository stockOrderRepository, BrokerageAccountRepository brokerageAccountRepository,
            StockOrderMapper stockOrderMapper) {
        this.stockOrderRepository = stockOrderRepository;
        this.brokerageAccountRepository = brokerageAccountRepository;
        this.stockOrderMapper = stockOrderMapper;
    }

    /**
     * Save a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    public StockOrderDTO save(StockOrderDTO stockOrderDTO) {
        log.debug("Request to save StockOrder : {}", stockOrderDTO);

        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDTO);
        stockOrder.setCreatedAt(ZonedDateTime.now());
        stockOrder.setStatus(StockOrder.STATUS_LOCAL_NEW);

        Optional<BrokerageAccount> accountOptional = brokerageAccountRepository
            .findByIdAndCurrentUser(stockOrder.getBrokerageAccount().getId());

        if (accountOptional.isPresent()) {
            BigDecimal totalPrice = stockOrder.getUnitPrice().multiply(new BigDecimal(stockOrder.getQuantity()));

            BrokerageAccount account = accountOptional.get();
            BigDecimal accountBalance = account.getBalance() == null ? new BigDecimal(0) : account.getBalance();
            if (totalPrice.compareTo(accountBalance) > 0) {
                throw new BusinessException("Saldo na corretora insuficiente.");
            }

            stockOrder.setTotalPrice(totalPrice);
            //stockOrder.setOperationType("DAYTRADE");
            stockOrder = stockOrderRepository.save(stockOrder);

            DateTimeFormatter localMktDateFormatter = DateTimeFormatter.ofPattern("yyyyddMM");

            final String omsAccount = "160119";

            NewOrderSingle orderSingle = new NewOrderSingle(
                new ClOrdID(stockOrder.getId().toString()),
                new Side(stockOrder.getSide().charAt(0)),
                new TransactTime(LocalDateTime.now()),
                new OrdType(FIX_ORD_TYPE_MARKET));

            orderSingle.set(new OrderQty(stockOrder.getQuantity()));

            // instrument block
            Stock stock = stockOrder.getStock();
            orderSingle.set(new Symbol(stockOrderDTO.getStockSymbol()));
            orderSingle.set(new SecurityID(stockOrderDTO.getStockSymbol()));
            orderSingle.set(new SecurityIDSource("8"));
            orderSingle.set(new SecurityExchange("XBSP"));
            orderSingle.set(new MaturityDate(localMktDateFormatter.format(LocalDate.now())));

            orderSingle.set(new Price(stockOrder.getUnitPrice().doubleValue()));

            orderSingle.set(new NoAllocs(1));
            NewOrderSingle.NoAllocs allocs = new NewOrderSingle.NoAllocs();
            allocs.set(new AllocAccount(omsAccount));
            allocs.set(new AllocAcctIDSource(99));
            orderSingle.addGroup(allocs);

            // https://stackoverflow.com/questions/51520982/fix-message-creating-party-message-how-to-do-its
            orderSingle.set(new NoPartyIDs(1));
            NewOrderSingle.NoPartyIDs parties = new NewOrderSingle.NoPartyIDs();
            parties.set(new PartyID(omsAccount));
            parties.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
            parties.set(new PartyRole(PartyRole.ENTERING_TRADER));
            orderSingle.addGroup(parties);

            orderSingle.setString(10122, "DAYTRADE");

            try {
                Session.sendToTarget(orderSingle, omsAccount, "CDRFIX");
            } catch (SessionNotFound sessionNotFound) {
                throw new BusinessException("Sem comunicação com corretora.");
            }

            return toDtoFillingDerived(stockOrder);

        } else {
            throw new BusinessException("Conta não encontrada.");
        }
    }

    public StockOrderDTO toDtoFillingDerived(StockOrder stockOrder) {
        StockOrderDTO dto = stockOrderMapper.toDto(stockOrder);

        if (stockOrder.getStatus() != null) {
            String status = stockOrder.getStatus();
            String statusDescr = null;

            switch (status) {
                case (StockOrder.STATUS_LOCAL_NEW):
                    statusDescr = "Salva";
                    break;
                case (StockOrder.STATUS_FIX_NEW):
                    statusDescr = "Recebida";
                    break;
                case (StockOrder.STATUS_FIX_PARTIALLY_FILLED):
                    statusDescr = "Parcialmente Executada";
                    break;
                case (StockOrder.STATUS_FIX_FILLED):
                    statusDescr = "Executada";
                    break;
                case (StockOrder.STATUS_FIX_CANCELED):
                    statusDescr = "Cancelada";
                    break;
                case (StockOrder.STATUS_FIX_REPLACED):
                    statusDescr = "Editada";
                    break;
                case (StockOrder.STATUS_FIX_PENDING_CANCEL):
                    statusDescr = "Cancelamento Pendente";
                    break;
                case (StockOrder.STATUS_FIX_REJECTED):
                    statusDescr = "Rejeitada";
                    break;
                case (StockOrder.STATUS_FIX_SUSPENDED):
                    statusDescr = "Suspensa";
                    break;
                case (StockOrder.STATUS_FIX_PENDING_NEW):
                    statusDescr = "Esperando Abertura do Mercado";
                    break;
                case (StockOrder.STATUS_FIX_EXPIRED):
                    statusDescr = "Expirada";
                    break;
                case (StockOrder.STATUS_FIX_PENDING_REPLACE):
                    statusDescr = "Esperando Edição";
                    break;
                case (StockOrder.STATUS_FIX_RECEIVED):
                    statusDescr = "Recebida";
                    break;
            }

            dto.setStatusDescr(statusDescr);

            boolean canCancel = status.equals(StockOrder.STATUS_LOCAL_NEW)
                || status.equals(StockOrder.STATUS_FIX_NEW)
                || status.equals(StockOrder.STATUS_FIX_PARTIALLY_FILLED)
                || status.equals(StockOrder.STATUS_FIX_REPLACED)
                || status.equals(StockOrder.STATUS_FIX_PENDING_NEW)
                || status.equals(StockOrder.STATUS_FIX_PENDING_REPLACE)
                || status.equals(StockOrder.STATUS_FIX_RECEIVED);

            dto.setCanCancel(canCancel);
        }

        return dto;
    }

    /**
     * Update a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    public StockOrderDTO update(StockOrderDTO stockOrderDTO) {
        log.debug("Request to save StockOrder : {}", stockOrderDTO);

        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDTO);
        stockOrder = stockOrderRepository.save(stockOrder);
        return toDtoFillingDerived(stockOrder);
    }

    /**
     * Get all the stockOrders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StockOrderDTO> findAll() {
        log.debug("Request to get all StockOrders");
        return stockOrderRepository.findAll().stream()
            .map(this::toDtoFillingDerived)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one stockOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockOrderDTO> findOne(Long id) {
        log.debug("Request to get StockOrder : {}", id);
        return stockOrderRepository.findById(id)
            .map(this::toDtoFillingDerived);
    }

    /**
     * Delete the stockOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockOrder : {}", id);
        stockOrderRepository.deleteById(id);
    }

    @Async
    public void proccessExecutionReport(ExecutionReport executionReport) {
        try {
            ClOrdID clOrdID = executionReport.getClOrdID();
            Optional<StockOrder> orderOptional = stockOrderRepository.findById(Long.parseLong(clOrdID.getValue()));
            if (!orderOptional.isPresent()) {
                ExecID execID = executionReport.getExecID();
                log.info("ignoring execution report with ExecID {}: no stock order found with clOrdID {}",
                    execID.getValue(), clOrdID.getValue());
                return;
            }

            StockOrder order = orderOptional.get();
            order.setLastExecReportTime(ZonedDateTime.now());
            order.setExecQuantity((long) executionReport.getCumQty().getValue());
            order.setAveragePrice(new BigDecimal(executionReport.getAvgPx().getValue()));

            String ordStatusStr = String.valueOf(executionReport.getOrdStatus().getValue());
            order.setStatus(ordStatusStr);

            try {
                if (ordStatusStr.equals(StockOrder.STATUS_FIX_REJECTED)) {
                    int ordRejReasonValue = executionReport.getOrdRejReason().getValue();

                    String message = "";

                    switch (ordRejReasonValue) {
                        case 0:
                            message = "Opção do servidor (Código " + ordRejReasonValue + ")";
                            break;
                        case 1:
                            message = "Símbolo Desconhecido (Código " + ordRejReasonValue + ")";
                            break;
                        case 2:
                            message = "Pregão fechado (Código " + ordRejReasonValue + ")";
                            break;
                        case 3:
                            message = "Ordem excedeu limite (Código " + ordRejReasonValue + ")";
                            break;
                        case 4:
                            message = "Tarde demais para entrar (Código " + ordRejReasonValue + ")";
                            break;
                        case 5:
                            message = "Ordem desconhecida (Código " + ordRejReasonValue + ")";
                            break;
                        case 6:
                            message = "Ordem duplicada (Código " + ordRejReasonValue + ")";
                            break;
                        case 7:
                            message = "Duplicata de uma ordem verbalmente comunicada (Código " + ordRejReasonValue + ")";
                            break;
                        case 8:
                            message = "Ordem obsoleta (Código " + ordRejReasonValue + ")";
                            break;
                        case 11:
                            message = "Característica da ordem não suportada (Código " + ordRejReasonValue + ")";
                            break;
                        case 13:
                            message = "Quantidade incorreta (Código " + ordRejReasonValue + ")";
                            break;
                        case 15:
                            message = "Conta desconhecida (Código " + ordRejReasonValue + ")";
                            break;
                        case 99:
                            String text = executionReport.getText().getValue();
                            message = text + " (Código " + ordRejReasonValue + ")";
                            break;
                    }
                    order.setLastExecReportDescr(message);
                } else {
                    order.setLastExecReportDescr(null);
                }
            } catch (FieldNotFound fne) {
                order.setLastExecReportDescr(null);
            }

            log.debug("processed execution report; resulting stock order: {}", order);
            stockOrderRepository.save(order);

        } catch (Exception e) {
            log.error("unexpected error", e);
        }
    }

}
