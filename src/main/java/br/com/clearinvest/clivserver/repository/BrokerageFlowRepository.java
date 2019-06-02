package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageFlow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BrokerageFlow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageFlowRepository extends JpaRepository<BrokerageFlow, Long>, JpaSpecificationExecutor<BrokerageFlow> {

    @Query("select brokerage_flow from BrokerageFlow brokerage_flow where brokerage_flow.user.login = ?#{principal.username}")
    List<BrokerageFlow> findByUserIsCurrentUser();

}
