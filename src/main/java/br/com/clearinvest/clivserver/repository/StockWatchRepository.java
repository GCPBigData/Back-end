package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.StockWatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the StockWatch entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockWatchRepository extends JpaRepository<StockWatch, Long> {

    @Query("select stock_watch from StockWatch stock_watch where stock_watch.user.login = ?#{principal.username}")
    List<StockWatch> findByUserIsCurrentUser();

    @Query("select s from StockWatch s where s.stock.id = :stockId and s.user.login = ?#{principal.username}")
    Optional<StockWatch> findByStockIdAndCurrentUser(@Param("stockId") Long stockId);

}
