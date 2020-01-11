package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.Brokerage;
import br.com.clearinvest.clivserver.repository.BrokerageRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Brokerage.
 */
@Service
@Transactional
public class BrokerageService {

    private final Logger log = LoggerFactory.getLogger(BrokerageService.class);

    private final BrokerageRepository brokerageRepository;

    private final BrokerageMapper brokerageMapper;

    public BrokerageService(BrokerageRepository brokerageRepository, BrokerageMapper brokerageMapper) {
        this.brokerageRepository = brokerageRepository;
        this.brokerageMapper = brokerageMapper;
    }

    /**
     * Save a brokerage.
     *
     * @param brokerageDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageDTO save(BrokerageDTO brokerageDTO) {
        log.debug("Request to save Brokerage : {}", brokerageDTO);

        Brokerage brokerage = brokerageMapper.toEntity(brokerageDTO);
        brokerage = brokerageRepository.save(brokerage);
        return brokerageMapper.toDto(brokerage);
    }

    /**
     * Get all the brokerages.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Brokerages");
        return brokerageRepository.findAll(pageable)
            .map(brokerageMapper::toDto);
    }

    /**
     * Get all the Brokerage with eager load of many-to-many relationships.
     *
     * @return the list of entities
     */
    public Page<BrokerageDTO> findAllWithEagerRelationships(Pageable pageable) {
        return brokerageRepository.findAllWithEagerRelationships(pageable).map(brokerageMapper::toDto);
    }
    

    /**
     * Get one brokerage by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageDTO> findOne(Long id) {
        log.debug("Request to get Brokerage : {}", id);
        return brokerageRepository.findOneWithEagerRelationships(id)
            .map(brokerageMapper::toDto);
    }

    /**
     * Delete the brokerage by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Brokerage : {}", id);
        brokerageRepository.deleteById(id);
    }
}
