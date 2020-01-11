package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageClient;
import br.com.clearinvest.clivserver.repository.BrokerageClientRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageClientDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageClientMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BrokerageClient.
 */
@Service
@Transactional
public class BrokerageClientService {

    private final Logger log = LoggerFactory.getLogger(BrokerageClientService.class);

    private final BrokerageClientRepository brokerageClientRepository;

    private final BrokerageClientMapper brokerageClientMapper;

    public BrokerageClientService(BrokerageClientRepository brokerageClientRepository, BrokerageClientMapper brokerageClientMapper) {
        this.brokerageClientRepository = brokerageClientRepository;
        this.brokerageClientMapper = brokerageClientMapper;
    }

    /**
     * Save a brokerageClient.
     *
     * @param brokerageClientDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageClientDTO save(BrokerageClientDTO brokerageClientDTO) {
        log.debug("Request to save BrokerageClient : {}", brokerageClientDTO);

        BrokerageClient brokerageClient = brokerageClientMapper.toEntity(brokerageClientDTO);
        brokerageClient = brokerageClientRepository.save(brokerageClient);
        return brokerageClientMapper.toDto(brokerageClient);
    }

    /**
     * Get all the brokerageClients.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageClientDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BrokerageClients");
        return brokerageClientRepository.findAll(pageable)
            .map(brokerageClientMapper::toDto);
    }


    /**
     * Get one brokerageClient by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageClientDTO> findOne(Long id) {
        log.debug("Request to get BrokerageClient : {}", id);
        return brokerageClientRepository.findById(id)
            .map(brokerageClientMapper::toDto);
    }

    /**
     * Delete the brokerageClient by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BrokerageClient : {}", id);
        brokerageClientRepository.deleteById(id);
    }
}
