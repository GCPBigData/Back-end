package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.StockFlow;
import br.com.clearinvest.clivserver.repository.StockFlowRepository;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.StockFlowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Get all the stockFlows.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StockFlowDTO> findAll() {
        log.debug("Request to get all StockFlows");
        return stockFlowRepository.findAll().stream()
            .map(stockFlowMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
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
