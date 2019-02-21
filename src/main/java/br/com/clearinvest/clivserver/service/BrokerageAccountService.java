package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.repository.BrokerageAccountRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageAccountDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageAccountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BrokerageAccount.
 */
@Service
@Transactional
public class BrokerageAccountService {

    private final Logger log = LoggerFactory.getLogger(BrokerageAccountService.class);

    private final BrokerageAccountRepository brokerageAccountRepository;

    private final BrokerageAccountMapper brokerageAccountMapper;

    public BrokerageAccountService(BrokerageAccountRepository brokerageAccountRepository, BrokerageAccountMapper brokerageAccountMapper) {
        this.brokerageAccountRepository = brokerageAccountRepository;
        this.brokerageAccountMapper = brokerageAccountMapper;
    }

    /**
     * Save a brokerageAccount.
     *
     * @param brokerageAccountDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageAccountDTO save(BrokerageAccountDTO brokerageAccountDTO) {
        log.debug("Request to save BrokerageAccount : {}", brokerageAccountDTO);

        BrokerageAccount brokerageAccount = brokerageAccountMapper.toEntity(brokerageAccountDTO);
        brokerageAccount = brokerageAccountRepository.save(brokerageAccount);
        return brokerageAccountMapper.toDto(brokerageAccount);
    }

    /**
     * Get all the brokerageAccounts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageAccountDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BrokerageAccounts");
        return brokerageAccountRepository.findAll(pageable)
            .map(brokerageAccountMapper::toDto);
    }


    /**
     * Get one brokerageAccount by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageAccountDTO> findOne(Long id) {
        log.debug("Request to get BrokerageAccount : {}", id);
        return brokerageAccountRepository.findById(id)
            .map(brokerageAccountMapper::toDto);
    }

    /**
     * Delete the brokerageAccount by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BrokerageAccount : {}", id);
        brokerageAccountRepository.deleteById(id);
    }
}
