package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import br.com.clearinvest.clivserver.repository.StockTradeRepository;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;
import br.com.clearinvest.clivserver.service.mapper.StockOrderMapper;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.FieldNotFound;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelRequest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.clearinvest.clivserver.domain.StockOrder.FIX_TIME_IN_FORCE_GOOD_TILL_DATE;
import static br.com.clearinvest.clivserver.service.OMSService.OMS_ACCOUNT;

/**
 * Service Implementation for managing StockOrder.
 */
@Service
@Transactional
public class StockOrderService {

    private final Logger log = LoggerFactory.getLogger(StockOrderService.class);

    private final StockOrderRepository stockOrderRepository;

    private final StockTradeRepository stockTradeRepository;

    private final StockOrderMapper stockOrderMapper;

    private final OMSService omsService;

    public StockOrderService(StockOrderRepository stockOrderRepository,
            StockTradeRepository stockTradeRepository, StockOrderMapper stockOrderMapper, OMSService omsService) {
        this.stockOrderRepository = stockOrderRepository;
        this.stockTradeRepository = stockTradeRepository;
        this.stockOrderMapper = stockOrderMapper;
        this.omsService = omsService;
    }

    /**
     * Save a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    public StockOrderDTO save(StockOrderDTO stockOrderDTO) {
        // TODO
        return null;
    }

    /**
     * Create a stockOrder.
     *
     * @param trade the entity to create
     * @return the persisted entity
     */
    public StockOrder create(StockTrade trade) {
        log.debug("Request to create StockOrder from StockTrade : {}", trade);

        StockOrder order = new StockOrder();
        order.setTrade(trade);
        order.setStatus(StockOrder.STATUS_LOCAL_NEW);
        order.setStock(trade.getStock());
        order.setKind(trade.getKind());

        if (order.getKind().equals(StockOrder.KIND_TRADE)) {
            order.setOrderType(StockOrder.FIX_ORD_TYPE_MARKET);

        } else if (order.getKind().equals(StockOrder.KIND_STOP_LOSS)
                || order.getKind().equals(StockOrder.KIND_STOP_GAIN)) {
            order.setOrderType(StockOrder.FIX_ORD_TYPE_STOP_LIMIT);
            order.setStopPrice(trade.getStopPrice());

        } else {
            throw new BusinessException("Ordem inválida");
        }

        order.setSide(trade.getSide());
        order.setTimeInForce(FIX_TIME_IN_FORCE_GOOD_TILL_DATE);
        order.setExpireTime(trade.getExpireTime());
        order.setQuantity(trade.getQuantity());
        order.setExecQuantity(0L);
        order.setUnitPrice(trade.getUnitPrice());
        order.setCreatedAt(trade.getCreatedAt());
        order.setCreatedBy(trade.getCreatedBy());
        order.setCreatedByIp(trade.getCreatedByIp());
        //order.setOperationType("DAYTRADE");
        order = stockOrderRepository.save(order);

        return order;
    }

    public NewOrderSingle createNewOrderSingle(StockOrder order, String symbol) {
        DateTimeFormatter localMktDateFormatter = DateTimeFormatter.ofPattern("yyyyddMM");

        NewOrderSingle orderSingle = new NewOrderSingle(
            new ClOrdID(order.getId().toString()),
            new Side(order.getSide().charAt(0)),
            new TransactTime(LocalDateTime.now()),
            new OrdType(StockOrder.FIX_ORD_TYPE_MARKET.charAt(0)));

        orderSingle.set(new OrderQty(order.getQuantity()));

        // instrument block
        orderSingle.set(new Symbol(symbol));
        orderSingle.set(new SecurityID(symbol));
        orderSingle.set(new SecurityIDSource("8"));
        orderSingle.set(new SecurityExchange("XBSP"));
        orderSingle.set(new MaturityDate(localMktDateFormatter.format(LocalDate.now())));

        if (order.getKind().equals(StockTrade.KIND_TRADE)) {
            orderSingle.set(new Price(order.getUnitPrice().doubleValue()));

        } else if (order.getKind().equals(StockOrder.KIND_STOP_LOSS)) {
            orderSingle.set(new Price(order.getUnitPrice().doubleValue()));
            orderSingle.set(new StopPx(order.getStopPrice().doubleValue()));

        } else if (order.getKind().equals(StockOrder.KIND_STOP_GAIN)) {
            orderSingle.set(new Price2(order.getUnitPrice().doubleValue()));
            orderSingle.setDouble(10306, order.getStopPrice().doubleValue());
        }

        orderSingle.set(new TimeInForce(FIX_TIME_IN_FORCE_GOOD_TILL_DATE.charAt(0)));
        orderSingle.set(new ExpireTime(order.getExpireTime().toLocalDateTime()));

        orderSingle.set(new NoAllocs(1));
        NewOrderSingle.NoAllocs allocs = new NewOrderSingle.NoAllocs();
        allocs.set(new AllocAccount(OMS_ACCOUNT));
        allocs.set(new AllocAcctIDSource(99));
        orderSingle.addGroup(allocs);

        // https://stackoverflow.com/questions/51520982/fix-message-creating-party-message-how-to-do-its
        orderSingle.set(new NoPartyIDs(1));
        NewOrderSingle.NoPartyIDs parties = new NewOrderSingle.NoPartyIDs();
        parties.set(new PartyID(OMS_ACCOUNT));
        parties.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
        parties.set(new PartyRole(PartyRole.ENTERING_TRADER));
        orderSingle.addGroup(parties);

        //orderSingle.setString(10122, "DAYTRADE");

        log.debug("NewOrderSingle: {}", orderSingle.toString());

        return orderSingle;
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
        return stockOrderRepository.findByCreatedByIsCurrentUser().stream()
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

    public void proccessExecutionReport(ExecutionReport executionReport) {
        String clOrdID = "", execId = "";
        try {
            clOrdID = executionReport.getClOrdID().getValue();
            execId = executionReport.getExecID().getValue();
        } catch (Exception e) {
            log.debug("unexpected error", e);
        }
        log.debug("StockOrderService: proccessExecutionReport called with message:: ClOrdID: {}; ExecID: {}", clOrdID, execId);

        try {
            Optional<StockOrder> orderOptional = stockOrderRepository.findById(Long.parseLong(clOrdID));
            if (!orderOptional.isPresent()) {
                ExecID execID = executionReport.getExecID();
                log.info("ignoring execution report with ExecID {}: no stock order found with clOrdID {}",
                    execID.getValue(), clOrdID);
                return;
            }

            ZonedDateTime now = ZonedDateTime.now();

            StockOrder order = orderOptional.get();
            order.setLastExecReportTime(now);
            order.setExecQuantity((long) executionReport.getCumQty().getValue());
            order.setAveragePrice(new BigDecimal(executionReport.getAvgPx().getValue()));

            String ordStatusStr = String.valueOf(executionReport.getOrdStatus().getValue());
            order.setStatus(ordStatusStr);

            StockTrade trade = order.getTrade();
            trade.setLastExecReportTime(now);
            trade.setExecQuantity((long) executionReport.getCumQty().getValue());
            trade.setAveragePrice(new BigDecimal(executionReport.getAvgPx().getValue()));
            trade.setStatus(ordStatusStr);

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
                    trade.setLastExecReportDescr(message);
                } else {
                    order.setLastExecReportDescr(null);
                    trade.setLastExecReportDescr(null);
                }
            } catch (FieldNotFound fne) {
                order.setLastExecReportDescr(null);
            }

            log.debug("StockOrderService: proccessExecutionReport finished; resulting stock order: {}", order);
            stockOrderRepository.save(order);
            stockTradeRepository.save(trade);

        } catch (Exception e) {
            log.error("unexpected error", e);
        }
    }

    void cancel(StockTrade trade) {
        StockOrder orderToCancel = trade.getOrders().stream()
            .sorted(Comparator.comparing(StockOrder::getId))
            .collect(Collectors.toList())
            .get(0);

        // TODO
        StockOrder order = new StockOrder();
        order.setTrade(trade);
        order.setStatus(StockOrder.STATUS_LOCAL_NEW);
        order.setStock(trade.getStock());
        order.setKind(trade.getKind());
        //order.setOrderType(new String(new char[]{StockTrade.FIX_ORD_TYPE_MARKET}));
        order.setSide(trade.getSide());
        //order.setTimeInForce(FIX_TIME_IN_FORCE_GOOD_TILL_DATE);
        //order.setExpireTime(trade.getExpireTime() == null ? ZonedDateTime.now() : trade.getExpireTime());
        order.setQuantity(trade.getQuantity());
        //order.setExecQuantity(0L);
        order.setUnitPrice(trade.getUnitPrice());
        order.setCreatedAt(trade.getCreatedAt() /*ZonedDateTime.now()*/);
        order.setCreatedBy(trade.getCreatedBy());
        order.setCreatedByIp(trade.getCreatedByIp());
        //order.setOperationType("DAYTRADE");
        order = stockOrderRepository.save(order);

        OrderCancelRequest orderCancel = new OrderCancelRequest(
            new OrigClOrdID(orderToCancel.getId().toString()),
            new ClOrdID(order.getId().toString()),
            new Side(orderToCancel.getSide().charAt(0)),
            new TransactTime(LocalDateTime.now()));

        if (orderToCancel.getOmsOrderId() != null) {
            orderCancel.set(new OrderID(orderToCancel.getOmsOrderId()));
        }

        orderCancel.set(new Symbol(orderToCancel.getStock().getSymbol()));
        orderCancel.set((new OrderQty(order.getQuantity())));

        orderCancel.set(new NoPartyIDs(1));
        NewOrderSingle.NoPartyIDs parties = new NewOrderSingle.NoPartyIDs();
        parties.set(new PartyID(OMS_ACCOUNT));
        parties.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
        parties.set(new PartyRole(PartyRole.ENTERING_TRADER));
        orderCancel.addGroup(parties);

        omsService.sendMessegeToOms(orderCancel, order.getId());
    }

}
