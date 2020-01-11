package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageAssistance;
import br.com.clearinvest.clivserver.repository.BrokerageAssistanceRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageAssistanceDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageAssistanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BrokerageAssistance.
 */
@Service
@Transactional
public class BrokerageAssistanceService {

    private final Logger log = LoggerFactory.getLogger(BrokerageAssistanceService.class);

    private final BrokerageAssistanceRepository brokerageAssistanceRepository;

    private final BrokerageAssistanceMapper brokerageAssistanceMapper;

    public BrokerageAssistanceService(BrokerageAssistanceRepository brokerageAssistanceRepository, BrokerageAssistanceMapper brokerageAssistanceMapper) {
        this.brokerageAssistanceRepository = brokerageAssistanceRepository;
        this.brokerageAssistanceMapper = brokerageAssistanceMapper;
    }

    /**
     * Save a brokerageAssistance.
     *
     * @param brokerageAssistanceDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageAssistanceDTO save(BrokerageAssistanceDTO brokerageAssistanceDTO) {
        log.debug("Request to save BrokerageAssistance : {}", brokerageAssistanceDTO);

        BrokerageAssistance brokerageAssistance = brokerageAssistanceMapper.toEntity(brokerageAssistanceDTO);
        brokerageAssistance = brokerageAssistanceRepository.save(brokerageAssistance);
        return brokerageAssistanceMapper.toDto(brokerageAssistance);
    }

    /**
     * Get all the brokerageAssistances.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageAssistanceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BrokerageAssistances");
        return brokerageAssistanceRepository.findAll(pageable)
            .map(brokerageAssistanceMapper::toDto);
    }


    /**
     * Get one brokerageAssistance by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageAssistanceDTO> findOne(Long id) {
        log.debug("Request to get BrokerageAssistance : {}", id);
        return brokerageAssistanceRepository.findById(id)
            .map(brokerageAssistanceMapper::toDto);
    }

    /**
     * Delete the brokerageAssistance by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BrokerageAssistance : {}", id);
        brokerageAssistanceRepository.deleteById(id);
    }
}
