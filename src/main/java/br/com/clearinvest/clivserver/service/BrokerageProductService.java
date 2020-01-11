package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageProduct;
import br.com.clearinvest.clivserver.repository.BrokerageProductRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageProductDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing BrokerageProduct.
 */
@Service
@Transactional
public class BrokerageProductService {

    private final Logger log = LoggerFactory.getLogger(BrokerageProductService.class);

    private final BrokerageProductRepository brokerageProductRepository;

    private final BrokerageProductMapper brokerageProductMapper;

    public BrokerageProductService(BrokerageProductRepository brokerageProductRepository, BrokerageProductMapper brokerageProductMapper) {
        this.brokerageProductRepository = brokerageProductRepository;
        this.brokerageProductMapper = brokerageProductMapper;
    }

    /**
     * Save a brokerageProduct.
     *
     * @param brokerageProductDTO the entity to save
     * @return the persisted entity
     */
    public BrokerageProductDTO save(BrokerageProductDTO brokerageProductDTO) {
        log.debug("Request to save BrokerageProduct : {}", brokerageProductDTO);

        BrokerageProduct brokerageProduct = brokerageProductMapper.toEntity(brokerageProductDTO);
        brokerageProduct = brokerageProductRepository.save(brokerageProduct);
        return brokerageProductMapper.toDto(brokerageProduct);
    }

    /**
     * Get all the brokerageProducts.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BrokerageProductDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BrokerageProducts");
        return brokerageProductRepository.findAll(pageable)
            .map(brokerageProductMapper::toDto);
    }


    /**
     * Get one brokerageProduct by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BrokerageProductDTO> findOne(Long id) {
        log.debug("Request to get BrokerageProduct : {}", id);
        return brokerageProductRepository.findById(id)
            .map(brokerageProductMapper::toDto);
    }

    /**
     * Delete the brokerageProduct by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BrokerageProduct : {}", id);
        brokerageProductRepository.deleteById(id);
    }
}
