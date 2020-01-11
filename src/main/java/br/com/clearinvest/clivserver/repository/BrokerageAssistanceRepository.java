package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageAssistance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BrokerageAssistance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageAssistanceRepository extends JpaRepository<BrokerageAssistance, Long> {

}
