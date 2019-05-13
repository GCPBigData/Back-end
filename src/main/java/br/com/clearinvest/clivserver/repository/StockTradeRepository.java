package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.StockTrade;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the StockTrade entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockTradeRepository extends JpaRepository<StockTrade, Long> {

    @Query("select stock_trade from StockTrade stock_trade where stock_trade.createdBy.login = ?#{principal.username}")
    List<StockTrade> findByCreatedByIsCurrentUser();

    @Query("select stock_trade from StockTrade stock_trade where stock_trade.id = :id and stock_trade.createdBy.login = ?#{principal.username}")
    Optional<StockTrade> findByIdAndCreatedByIsCurrentUser(@Param("id") Long id);

}
