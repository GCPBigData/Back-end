package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.StockBalance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the StockBalance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockBalanceRepository extends JpaRepository<StockBalance, Long> {

    @Query("select stock_balance from StockBalance stock_balance where stock_balance.user.login = ?#{principal.username}")
    List<StockBalance> findByUserIsCurrentUser();

}
