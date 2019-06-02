package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageFlow;
import br.com.clearinvest.clivserver.repository.BrokerageFlowRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageFlowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BrokerageFlow.
 */
@Service
@Transactional
public class BrokerageFlowService {

    private final Logger log = LoggerFactory.getLogger(BrokerageFlowService.class);

    private final BrokerageFlowRepository brokerageFlowRepository;

    private final BrokerageFlowMapper brokerageFlowMapper;

    public BrokerageFlowService(BrokerageFlowRepository brokerageFlowRepository, BrokerageFlowMapper brokerageFlowMapper) {
        this.brokerageFlowRepository = brokerageFlowRepository;
        this.brokerageFlowMapper = brokerageFlowMapper;
    }

    /**
     * Save a brokerageFlow.
     *
     * @param brokerageFlowDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageFlowDTO save(BrokerageFlowDTO brokerageFlowDTO) {
        log.debug("Request to save BrokerageFlow : {}", brokerageFlowDTO);

        BrokerageFlow brokerageFlow = brokerageFlowMapper.toEntity(brokerageFlowDTO);
        brokerageFlow = brokerageFlowRepository.save(brokerageFlow);
        return brokerageFlowMapper.toDto(brokerageFlow);
    }

    /**
     * Get all the brokerageFlows.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageFlowDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BrokerageFlows");
        return brokerageFlowRepository.findAll(pageable)
            .map(brokerageFlowMapper::toDto);
    }


    /**
     * Get one brokerageFlow by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageFlowDTO> findOne(Long id) {
        log.debug("Request to get BrokerageFlow : {}", id);
        return brokerageFlowRepository.findById(id)
            .map(brokerageFlowMapper::toDto);
    }

    /**
     * Delete the brokerageFlow by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BrokerageFlow : {}", id);
        brokerageFlowRepository.deleteById(id);
    }
}
