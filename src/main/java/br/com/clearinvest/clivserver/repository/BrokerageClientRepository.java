package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageClient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BrokerageClient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageClientRepository extends JpaRepository<BrokerageClient, Long> {

}
