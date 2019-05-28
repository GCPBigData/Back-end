package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.StockFlow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockFlow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockFlowRepository extends JpaRepository<StockFlow, Long> {

    @Query("select stock_flow from StockFlow stock_flow where stock_flow.user.login = ?#{principal.username}")
    List<StockFlow> findByUserIsCurrentUser();

}
