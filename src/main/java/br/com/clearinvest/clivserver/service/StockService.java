package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockWatch;
import br.com.clearinvest.clivserver.repository.StockRepository;
import br.com.clearinvest.clivserver.repository.StockWatchRepository;
import br.com.clearinvest.clivserver.service.dto.StockDTO;
import br.com.clearinvest.clivserver.service.mapper.StockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Stock.
 */
@Service
@Transactional
public class StockService {

    private final Logger log = LoggerFactory.getLogger(StockService.class);

    private final StockRepository stockRepository;
    private final StockWatchRepository stockWatchRepository;

    private final StockMapper stockMapper;

    public StockService(StockRepository stockRepository, StockWatchRepository stockWatchRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockWatchRepository = stockWatchRepository;
        this.stockMapper = stockMapper;
    }

    /**
     * Save a stock.
     *
     * @param stockDTO the entity to save
     * @return the persisted entity
     */
    public StockDTO save(StockDTO stockDTO) {
        log.debug("Request to save Stock : {}", stockDTO);

        Stock stock = stockMapper.toEntity(stockDTO);
        stock = stockRepository.save(stock);
        return stockMapper.toDto(stock);
    }

    /**
     * Get all the stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Stocks");
        return stockRepository.findAll(pageable)
            .map(stockMapper::toDto);
    }

    /**
     * Get all the watched stocks.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findAllWatched(Pageable pageable) {
        return stockRepository.findAllWatched(pageable)
            .map(stockMapper::toDto);
    }


    /**
     * Get one stock by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockDTO> findOne(Long id) {
        log.debug("Request to get Stock : {}", id);
        Optional<StockDTO> stockDTO = stockRepository.findById(id)
            .map(stockMapper::toDto);

        if (stockDTO.isPresent()) {
            Optional<StockWatch> stockWatch = stockWatchRepository.findByStockIdAndCurrentUser(stockDTO.get().getId());
            stockDTO.get().setWatch(stockWatch.isPresent());
        }

        return stockDTO;
    }

    /**
     * Delete the stock by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Stock : {}", id);
        stockRepository.deleteById(id);
    }
}
