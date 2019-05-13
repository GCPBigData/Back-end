package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.StockOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockOrderRepository extends JpaRepository<StockOrder, Long> {

    @Query("select stock_order from StockOrder stock_order where stock_order.createdBy.login = ?#{principal.username}")
    List<StockOrder> findByCreatedByIsCurrentUser();

}
