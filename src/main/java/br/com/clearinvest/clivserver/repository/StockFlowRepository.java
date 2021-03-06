package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockFlow;
import br.com.clearinvest.clivserver.service.dto.StockFlowBalanceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockFlow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockFlowRepository extends JpaRepository<StockFlow, Long>, JpaSpecificationExecutor<StockFlow> {

    @Query("select stock_flow from StockFlow stock_flow where stock_flow.user.login = ?#{principal.username}")
    List<StockFlow> findByUserIsCurrentUser();

    @Query("select sum(f.quantity) from StockFlow f where f.stock = :stock and f.user.login = ?#{principal.username}")
    Long getQuantityByStockAndCurrentUser(Stock stock);

    @Query(" select new br.com.clearinvest.clivserver.service.dto.StockFlowBalanceDTO(" +
            "  f.symbol, " +
            "  sum(f.quantity), " +
            "  sum(f.totalPrice), " +
            "  sum(f.trade.feeBrokerage), " +
            "  sum(f.trade.feeBrokerageIss), " +
            "  sum(f.trade.feeNegotiation), " +
            "  sum(f.trade.feeLiquidation), " +
            "  sum(f.trade.feeRegistry), " +
            "  sum(f.trade.feeIrrf) " +
            ") " +
            "from StockFlow f " +
            "where f.user.login = ?#{principal.username} " +
            "group by f.symbol " +
            "having sum(f.totalPrice) > 0 " +
            "order by sum(f.totalPrice) desc")
    List<StockFlowBalanceDTO> listBalanceByCurrentUserGroupedByStock();

    /*@Query(" select new br.com.clearinvest.clivserver.service.dto.StockFlowBalanceDTO(" +
            "  null, " +
            "  sum(f.quantity), " +
            "  sum(f.totalPrice)" +
            ") " +
            "from StockFlow f " +
            "where f.user.login = ?#{principal.username}")
    StockFlowBalanceDTO getBalanceByCurrentUser();*/

}
