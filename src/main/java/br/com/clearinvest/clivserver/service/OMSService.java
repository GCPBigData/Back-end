package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

import java.util.Optional;

/**
 * Service Implementation for managing StockOrder.
 */
@Service
@Transactional
public class OMSService {

    private final Logger log = LoggerFactory.getLogger(OMSService.class);

    public static final String OMS_ACCOUNT = "160119";

    // TODO move to application.yml
    public static final boolean ORDER_SEND_TO_OMS_IN_DEV_MODE = false;

    private final AppService appService;

    private final StockOrderRepository stockOrderRepository;

    public OMSService(AppService appService, StockOrderRepository stockOrderRepository) {
        this.appService = appService;
        this.stockOrderRepository = stockOrderRepository;
    }

    @Async
    public void sendMessegeToOMS(Message message, Long orderId) throws RuntimeException {
        Optional<StockOrder> orderOptional = Optional.empty();
        for (int i = 0; i < 50; i++) {
            orderOptional = stockOrderRepository.findById(orderId);

            if (orderOptional.isPresent()) {
                log.debug("order found with id {}; sending to OMS",  orderId);
                break;

            } else {
                log.debug("order not found with id {}; waiting for another try",  orderId);
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }

        if (!orderOptional.isPresent()) {
            throw new RuntimeException("couldn't find order after 50 tries");
        }

        try {
            if (appService.isEnvironmentDev() && ORDER_SEND_TO_OMS_IN_DEV_MODE) {
                Session.sendToTarget(message, OMS_ACCOUNT, "CDRFIX");
            } else if (appService.isEnvironmentProd()) {
                Session.sendToTarget(message, OMS_ACCOUNT, "CDRFIX");
            }
        } catch (SessionNotFound sessionNotFound) {
            throw new BusinessException("Sem comunicação com corretora.");
        }
    }

}
