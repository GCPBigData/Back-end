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

import br.com.clearinvest.clivserver.domain.StockFlow;
import br.com.clearinvest.clivserver.domain.*; // for static metamodels
import br.com.clearinvest.clivserver.repository.StockFlowRepository;
import br.com.clearinvest.clivserver.service.dto.StockFlowCriteria;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.StockFlowMapper;

/**
 * Service for executing complex queries for StockFlow entities in the database.
 * The main input is a {@link StockFlowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockFlowDTO} or a {@link Page} of {@link StockFlowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockFlowQueryService extends QueryService<StockFlow> {

    private final Logger log = LoggerFactory.getLogger(StockFlowQueryService.class);

    private final StockFlowRepository stockFlowRepository;

    private final StockFlowMapper stockFlowMapper;

    public StockFlowQueryService(StockFlowRepository stockFlowRepository, StockFlowMapper stockFlowMapper) {
        this.stockFlowRepository = stockFlowRepository;
        this.stockFlowMapper = stockFlowMapper;
    }

    /**
     * Return a {@link List} of {@link StockFlowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockFlowDTO> findByCriteria(StockFlowCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockFlow> specification = createSpecification(criteria);
        return stockFlowMapper.toDto(stockFlowRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockFlowDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockFlowDTO> findByCriteria(StockFlowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockFlow> specification = createSpecification(criteria);
        return stockFlowRepository.findAll(specification, page)
            .map(stockFlowMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockFlowCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockFlow> specification = createSpecification(criteria);
        return stockFlowRepository.count(specification);
    }

    /**
     * Function to convert StockFlowCriteria to a {@link Specification}
     */
    private Specification<StockFlow> createSpecification(StockFlowCriteria criteria) {
        Specification<StockFlow> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockFlow_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), StockFlow_.createdAt));
            }
            if (criteria.getTradeDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeDate(), StockFlow_.tradeDate));
            }
            if (criteria.getSide() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSide(), StockFlow_.side));
            }
            if (criteria.getSymbol() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSymbol(), StockFlow_.symbol));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), StockFlow_.quantity));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), StockFlow_.unitPrice));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), StockFlow_.totalPrice));
            }
            if (criteria.getManualEntry() != null) {
                specification = specification.and(buildSpecification(criteria.getManualEntry(), StockFlow_.manualEntry));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(StockFlow_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getBrokerageAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBrokerageAccountId(),
                    root -> root.join(StockFlow_.brokerageAccount, JoinType.LEFT).get(BrokerageAccount_.id)));
            }
            if (criteria.getTradeId() != null) {
                specification = specification.and(buildSpecification(criteria.getTradeId(),
                    root -> root.join(StockFlow_.trade, JoinType.LEFT).get(StockTrade_.id)));
            }
            if (criteria.getExecReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getExecReportId(),
                    root -> root.join(StockFlow_.execReport, JoinType.LEFT).get(ExecutionReport_.id)));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(StockFlow_.stock, JoinType.LEFT).get(Stock_.id)));
            }
        }
        return specification;
    }
}
