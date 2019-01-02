package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.Stock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Stock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query(
        "select s from Stock s " +
            "left join StockWatch sw on sw.stock = s " +
            "where sw.user.login = ?#{principal.username} " +
            "order by s.symbol")
    Page<Stock> findAllWatched(Pageable pageable);

}
