package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.StockBalance;
import br.com.clearinvest.clivserver.repository.StockBalanceRepository;
import br.com.clearinvest.clivserver.service.dto.StockBalanceDTO;
import br.com.clearinvest.clivserver.service.mapper.StockBalanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockBalance.
 */
@Service
@Transactional
public class StockBalanceService {

    private final Logger log = LoggerFactory.getLogger(StockBalanceService.class);

    private final StockBalanceRepository stockBalanceRepository;

    private final StockBalanceMapper stockBalanceMapper;

    public StockBalanceService(StockBalanceRepository stockBalanceRepository, StockBalanceMapper stockBalanceMapper) {
        this.stockBalanceRepository = stockBalanceRepository;
        this.stockBalanceMapper = stockBalanceMapper;
    }

    /**
     * Save a stockBalance.
     *
     * @param stockBalanceDTO the entity to save
     * @return the persisted entity
     */
    public StockBalanceDTO save(StockBalanceDTO stockBalanceDTO) {
        log.debug("Request to save StockBalance : {}", stockBalanceDTO);

        StockBalance stockBalance = stockBalanceMapper.toEntity(stockBalanceDTO);
        stockBalance = stockBalanceRepository.save(stockBalance);
        return stockBalanceMapper.toDto(stockBalance);
    }

    /**
     * Get all the stockBalances.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StockBalanceDTO> findAll() {
        log.debug("Request to get all StockBalances");
        return stockBalanceRepository.findAll().stream()
            .map(stockBalanceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one stockBalance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockBalanceDTO> findOne(Long id) {
        log.debug("Request to get StockBalance : {}", id);
        return stockBalanceRepository.findById(id)
            .map(stockBalanceMapper::toDto);
    }

    /**
     * Delete the stockBalance by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockBalance : {}", id);
        stockBalanceRepository.deleteById(id);
    }

    public void update() {

    }
}
