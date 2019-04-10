package br.com.clearinvest.clivserver.quickfixj;

import br.com.clearinvest.clivserver.service.StockOrderService;
import io.allune.quickfixj.spring.boot.starter.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import quickfix.Message;
import quickfix.field.Password;
import quickfix.field.Username;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Logon;
import quickfix.fix44.Logout;

@Service
public class ClientApplicationListener {

    private final Logger log = LoggerFactory.getLogger(ClientApplicationListener.class);

    private StockOrderService stockOrderService;

    public ClientApplicationListener(StockOrderService stockOrderService) {
        this.stockOrderService = stockOrderService;
    }

    @EventListener
    public void listenFromAdmin(FromAdmin fromAdmin) {
        log.info("fromAdmin: Message={}", fromAdmin);
    }

    @EventListener
    public void listenToAdmin(ToAdmin toAdmin) {
        log.info("toAdmin: Message={}", toAdmin);

        Message message = toAdmin.getMessage();
        if (message instanceof Logon) {
            Logon logonMessage = (Logon) message;
            logonMessage.set(new Username("160119"));
            logonMessage.set(new Password("Lf@2019"));
        }
    }

    /** Messages received by this app from the OMS. */
    @EventListener
    public void listenFromApp(FromApp fromApp) {
        log.info("fromApp: Message={}", fromApp);

        Message message = fromApp.getMessage();
        if (message instanceof ExecutionReport) {
            stockOrderService.proccessExecutionReport((ExecutionReport) message);
        }
    }

    /** Messages sent by this app to the OMS. */
    @EventListener
    public void listenToApp(ToApp toApp) {
        log.info("toApp: Message={}", toApp);
    }

    @EventListener
    public void listenOnCreate(Create create) {
        log.info("create: Message={}", create);
    }

    @EventListener
    public void listenOnLogon(Logon logon) {
        log.info("logon: Message={}", logon);
    }

    @EventListener
    public void listenOnLogout(Logout logout) {
        log.info("logout: Message={}", logout);
    }

}
