package br.com.clearinvest.clivserver.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import br.com.clearinvest.clivserver.repository.UserRepository;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import io.github.jhipster.service.filter.LongFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.domain.*; // for static metamodels
import br.com.clearinvest.clivserver.repository.StockTradeRepository;
import br.com.clearinvest.clivserver.service.dto.StockTradeCriteria;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.mapper.StockTradeMapper;

import static br.com.clearinvest.clivserver.service.StockTradeService.toDtoFillingDerived;

/**
 * Service for executing complex queries for StockTrade entities in the database.
 * The main input is a {@link StockTradeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockTradeDTO} or a {@link Page} of {@link StockTradeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockTradeQueryService extends QueryService<StockTrade> {

    private final Logger log = LoggerFactory.getLogger(StockTradeQueryService.class);

    private final StockTradeRepository stockTradeRepository;

    private final UserRepository userRepository;

    private final StockTradeMapper stockTradeMapper;

    public StockTradeQueryService(StockTradeRepository stockTradeRepository, UserRepository userRepository, StockTradeMapper stockTradeMapper) {
        this.stockTradeRepository = stockTradeRepository;
        this.userRepository = userRepository;
        this.stockTradeMapper = stockTradeMapper;
    }

    /**
     * Return a {@link List} of {@link StockTradeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockTradeDTO> findByCriteria(StockTradeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StockTrade> specification = createSpecification(criteria);
        return stockTradeMapper.toDto(stockTradeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockTradeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockTradeDTO> findByCriteria(StockTradeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StockTrade> specification = createSpecification(criteria);
        return stockTradeRepository.findAll(specification, page)
            .map((t) -> toDtoFillingDerived(t, stockTradeMapper));
    }

    /**
     * Return a {@link Page} of {@link StockTradeDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockTradeDTO> findByCriteriaAndCurrentUser(StockTradeCriteria criteria, Pageable page) {
        User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin().get())
            .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        LongFilter createdByFilter = new LongFilter();
        createdByFilter.setEquals(user.getId());
        criteria.setCreatedById(createdByFilter);

        log.debug("find by criteria : {}, page: {}", criteria, page);

        final Specification<StockTrade> specification = createSpecification(criteria);
        return stockTradeRepository.findAll(specification, page)
            .map((t) -> toDtoFillingDerived(t, stockTradeMapper));
    }

    /**
     * Return the number of matching entities in the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockTradeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StockTrade> specification = createSpecification(criteria);
        return stockTradeRepository.count(specification);
    }

    /**
     * Function to convert StockTradeCriteria to a {@link Specification}
     */
    private Specification<StockTrade> createSpecification(StockTradeCriteria criteria) {
        Specification<StockTrade> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), StockTrade_.id));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), StockTrade_.createdAt));
            }
            if (criteria.getCreatedByIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedByIp(), StockTrade_.createdByIp));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), StockTrade_.status));
            }
            if (criteria.getTradeDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTradeDate(), StockTrade_.tradeDate));
            }
            if (criteria.getKind() != null) {
                specification = specification.and(buildStringSpecification(criteria.getKind(), StockTrade_.kind));
            }
            if (criteria.getMarket() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMarket(), StockTrade_.market));
            }
            if (criteria.getSide() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSide(), StockTrade_.side));
            }
            if (criteria.getManualEntry() != null) {
                specification = specification.and(buildSpecification(criteria.getManualEntry(), StockTrade_.manualEntry));
            }
            if (criteria.getExpireTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpireTime(), StockTrade_.expireTime));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), StockTrade_.quantity));
            }
            if (criteria.getExecQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExecQuantity(), StockTrade_.execQuantity));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), StockTrade_.unitPrice));
            }
            if (criteria.getStopPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStopPrice(), StockTrade_.stopPrice));
            }
            if (criteria.getAveragePrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAveragePrice(), StockTrade_.averagePrice));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), StockTrade_.totalPrice));
            }
            if (criteria.getTotalPriceActual() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPriceActual(), StockTrade_.totalPriceActual));
            }
            if (criteria.getFeeBrokerage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeBrokerage(), StockTrade_.feeBrokerage));
            }
            if (criteria.getFeeBrokerageIss() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeBrokerageIss(), StockTrade_.feeBrokerageIss));
            }
            if (criteria.getFeeNegotiation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeNegotiation(), StockTrade_.feeNegotiation));
            }
            if (criteria.getFeeLiquidation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeLiquidation(), StockTrade_.feeLiquidation));
            }
            if (criteria.getFeeRegistry() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeRegistry(), StockTrade_.feeRegistry));
            }
            if (criteria.getFeeIrrf() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFeeIrrf(), StockTrade_.feeIrrf));
            }
            if (criteria.getLastExecReportTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastExecReportTime(), StockTrade_.lastExecReportTime));
            }
            if (criteria.getLastExecReportDescr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastExecReportDescr(), StockTrade_.lastExecReportDescr));
            }
            if (criteria.getStockId() != null) {
                specification = specification.and(buildSpecification(criteria.getStockId(),
                    root -> root.join(StockTrade_.stock, JoinType.LEFT).get(Stock_.id)));
            }
            if (criteria.getBrokerageAccountId() != null) {
                specification = specification.and(buildSpecification(criteria.getBrokerageAccountId(),
                    root -> root.join(StockTrade_.brokerageAccount, JoinType.LEFT).get(BrokerageAccount_.id)));
            }
            if (criteria.getMainOrderId() != null) {
                specification = specification.and(buildSpecification(criteria.getMainOrderId(),
                    root -> root.join(StockTrade_.mainOrder, JoinType.LEFT).get(StockOrder_.id)));
            }
            if (criteria.getOrdersId() != null) {
                specification = specification.and(buildSpecification(criteria.getOrdersId(),
                    root -> root.join(StockTrade_.orders, JoinType.LEFT).get(StockOrder_.id)));
            }
            if (criteria.getCreatedById() != null) {
                specification = specification.and(buildSpecification(criteria.getCreatedById(),
                    root -> root.join(StockTrade_.createdBy, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
