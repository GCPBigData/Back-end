package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.Brokerage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Brokerage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageRepository extends JpaRepository<Brokerage, Long> {

}
