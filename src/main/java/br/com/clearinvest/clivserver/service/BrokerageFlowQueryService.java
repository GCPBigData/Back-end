package br.com.clearinvest.clivserver.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.clearinvest.clivserver.domain.BrokerageFlow;
import br.com.clearinvest.clivserver.domain.*; // for static metamodels
import br.com.clearinvest.clivserver.repository.BrokerageFlowRepository;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowCriteria;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageFlowMapper;

/**
 * Service for executing complex queries for BrokerageFlow entities in the database.
 * The main input is a {@link BrokerageFlowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BrokerageFlowDTO} or a {@link Page} of {@link BrokerageFlowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BrokerageFlowQueryService extends QueryService<BrokerageFlow> {

    private final Logger log = LoggerFactory.getLogger(BrokerageFlowQueryService.class);

    private final BrokerageFlowRepository brokerageFlowRepository;

    private final BrokerageFlowMapper brokerageFlowMapper;

    public BrokerageFlowQueryService(BrokerageFlowRepository brokerageFlowRepository, BrokerageFlowMapper brokerageFlowMapper) {
        this.brokerageFlowRepository = brokerageFlowRepository;
        this.brokerageFlowMapper = brokerageFlowMapper;
    }

    /**
     * Return a {@link List} of {@link BrokerageFlowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BrokerageFlowDTO> findByCriteria(BrokerageFlowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BrokerageFlow> specification = createSpecification(criteria);
        return brokerageFlowMapper.toDto(brokerageFlowRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BrokerageFlowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BrokerageFlowDTO> findByCriteria(BrokerageFlowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BrokerageFlow> specification = createSpecification(criteria);
        return brokerageFlowRepository.findAll(specification, page)
            .map(brokerageFlowMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BrokerageFlowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BrokerageFlow> specification = createSpecification(criteria);
        return brokerageFlowRepository.count(specification);
    }

    /**
     * Function to convert BrokerageFlowCriteria to a {@link Specification}
     */
    private Specification<BrokerageFlow> createSpecification(BrokerageFlowCriteria criteria) {
        Specification<BrokerageFlow> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), BrokerageFlow_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), BrokerageFlow_.createdAt));
            }
            if (criteria.getFlowDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFlowDate(), BrokerageFlow_.flowDate));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), BrokerageFlow_.amount));
            }
            if (criteria.getManualEntry() != null) {
                specification = specification.and(buildSpecification(criteria.getManualEntry(), BrokerageFlow_.manualEntry));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(BrokerageFlow_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBrokerageAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBrokerageAccountId(),
                    root -> root.join(BrokerageFlow_.brokerageAccount, JoinType.LEFT).get(BrokerageAccount_.id)));
            }
            if (criteria.getTradeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTradeId(),
                    root -> root.join(BrokerageFlow_.trade, JoinType.LEFT).get(StockTrade_.id)));
            }
        }
        return specification;
    }
}
