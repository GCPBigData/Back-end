package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.repository.StockFlowRepository;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.StockFlowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing StockFlow.
 */
@Service
@Transactional
public class StockFlowService {

    private final Logger log = LoggerFactory.getLogger(StockFlowService.class);

    private final StockFlowRepository stockFlowRepository;

    private final StockFlowMapper stockFlowMapper;

    public StockFlowService(StockFlowRepository stockFlowRepository, StockFlowMapper stockFlowMapper) {
        this.stockFlowRepository = stockFlowRepository;
        this.stockFlowMapper = stockFlowMapper;
    }

    /**
     * Save a stockFlow.
     *
     * @param stockFlowDTO the entity to save
     * @return the persisted entity
     */
    public StockFlowDTO save(StockFlowDTO stockFlowDTO) {
        log.debug("Request to save StockFlow : {}", stockFlowDTO);

        StockFlow stockFlow = stockFlowMapper.toEntity(stockFlowDTO);
        stockFlow = stockFlowRepository.save(stockFlow);
        return stockFlowMapper.toDto(stockFlow);
    }

    public StockFlow addManualEntry(StockTrade trade) {
        BigDecimal totalPrice = trade.getUnitPrice().multiply(BigDecimal.valueOf(trade.getQuantity()));
        if (StockOrder.FIX_SIDE_SELL.equals(trade.getSide())) {
            totalPrice = totalPrice.negate();
        }

        StockFlow stockFlow = createStockFlow(trade, trade.getCreatedBy(), trade.getBrokerageAccount(),
                trade.getQuantity(), trade.getUnitPrice(), totalPrice)
                .tradeDate(trade.getTradeDate());

        stockFlow = stockFlowRepository.save(stockFlow);
        return stockFlow;
    }

    public StockFlow add(StockTrade trade, ExecReport execReport) {
        BigDecimal totalPrice = execReport.getLastPx().multiply(BigDecimal.valueOf(execReport.getLastQty()));
        if (StockOrder.FIX_SIDE_SELL.equals(trade.getSide())) {
            totalPrice = totalPrice.negate();
        }

        StockFlow stockFlow = createStockFlow(trade, trade.getCreatedBy(), trade.getBrokerageAccount(),
                execReport.getLastQty(), execReport.getLastPx(), totalPrice)
                .execReport(execReport);

        stockFlow = stockFlowRepository.save(stockFlow);
        return stockFlow;
    }

    private StockFlow createStockFlow(StockTrade trade, User user, BrokerageAccount brokerageAccount, long quantity,
            BigDecimal unitPrice, BigDecimal totalPrice) {
        return new StockFlow()
                .createdAt(ZonedDateTime.now())
                .tradeDate(ZonedDateTime.now())
                .side(trade.getSide())
                .symbol(trade.getStock().getSymbol())
                .quantity(quantity)
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .manualEntry(trade.isManualEntry())
                .user(user)
                .brokerageAccount(brokerageAccount)
                .trade(trade)
                .stock(trade.getStock());
    }

    /**
     * Get all the stockFlows.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockFlowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockFlows");
        return stockFlowRepository.findAll(pageable)
                .map(stockFlowMapper::toDto);
    }


    /**
     * Get one stockFlow by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockFlowDTO> findOne(Long id) {
        log.debug("Request to get StockFlow : {}", id);
        return stockFlowRepository.findById(id)
                .map(stockFlowMapper::toDto);
    }

    /**
     * Delete the stockFlow by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockFlow : {}", id);
        stockFlowRepository.deleteById(id);
    }
}
