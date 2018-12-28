package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.MarketSector;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the MarketSector entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarketSectorRepository extends JpaRepository<MarketSector, Long> {

    @Query("select m from MarketSector m left join fetch m.stocks")
    List<MarketSector> findAllWithStocks();

}
