package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import quickfix.field.*;
import quickfix.fix44.ExecutionReport;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Service for fake trades.
 */
@Service
public class FakeTradeService {

    private final Logger log = LoggerFactory.getLogger(FakeTradeService.class);

    @Autowired
    StockOrderRepository stockOrderRepository;

    @Autowired
    AsyncService asyncService;

    @Autowired
    StockOrderService stockOrderService;

    @Async
    public void executeOrder(Long orderId, Long delayMillis) {
        if (delayMillis != null) {
            try {
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                log.error("unexpected error", e);
            }
        }

        StockOrder order = stockOrderRepository.findById(orderId).get();
        StockTrade trade = order.getTrade();

        ExecutionReport executionReport = null;
        long lastQty = 0;
        if (order.getStatus().equals(StockOrder.STATUS_LOCAL_NEW)) {
            executionReport = createExecutionReport(order, 0, null,
                StockOrder.FIX_EXEC_TYPE_RECEIVED, StockOrder.STATUS_FIX_RECEIVED.charAt(0));

        } else if (order.getStatus().equals(StockOrder.STATUS_FIX_RECEIVED)) {
            executionReport = createExecutionReport(order, 0, null,
                StockOrder.FIX_EXEC_TYPE_NEW, StockOrder.STATUS_FIX_NEW.charAt(0));

        } else if (trade.getExecQuantity() + 100 < trade.getQuantity()) {
            lastQty = 100;
            executionReport = createExecutionReport(order, lastQty, order.getUnitPrice(),
                StockOrder.FIX_EXEC_TYPE_TRADE, StockOrder.STATUS_FIX_PARTIALLY_FILLED.charAt(0));

        } else if (trade.getExecQuantity() + 100 == trade.getQuantity()) {
            lastQty = 100;
            executionReport = createExecutionReport(order, lastQty, order.getUnitPrice(),
                StockOrder.FIX_EXEC_TYPE_TRADE, StockOrder.STATUS_FIX_FILLED.charAt(0));
        }

        if (executionReport != null) {
            stockOrderService.proccessExecutionReport(executionReport);

            if (trade.getExecQuantity() + lastQty < trade.getQuantity()) {
                asyncService.fakeTradeExecuteOrder(orderId, delayMillis);
            }
        }
    }

    private ExecutionReport createExecutionReport(StockOrder order, long lastQty, BigDecimal lastPx, char execType,
            char ordStatus) {
        ExecutionReport executionReport = new ExecutionReport(
            new OrderID("FER" + order.getId()),
            new ExecID("FERE" + order.getId() + "-" + System.currentTimeMillis()),
            new ExecType(execType), // received
            new OrdStatus(ordStatus), // received
            new Side(order.getSide().charAt(0)),
            new LeavesQty(order.getQuantity() - (order.getExecQuantity() + lastQty)),
            new CumQty(order.getExecQuantity() + lastQty),
            new AvgPx(order.getUnitPrice().doubleValue())
        );
        executionReport.set(new ClOrdID(order.getId().toString()));
        executionReport.set(new LastQty(lastQty));

        if (lastPx != null) {
            executionReport.set(new LastPx(lastPx.doubleValue()));
        }

        executionReport.set(new TransactTime(ZonedDateTime.now().toLocalDateTime()));

        return executionReport;
    }

}
