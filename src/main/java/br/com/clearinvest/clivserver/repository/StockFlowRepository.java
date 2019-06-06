package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockFlow;
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
    Long getQuantityOfCurrentUserAndStock(Stock stock);

}
