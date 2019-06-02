package br.com.clearinvest.clivserver.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class AsyncService {

    private final Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Autowired
    FakeTradeService fakeTradeService;

    public void fakeTradeExecuteOrder(Long orderId, Long delayMillis) {
        fakeTradeService.executeOrder(orderId, delayMillis);
    }

}
