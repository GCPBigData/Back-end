package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.Brokerage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Brokerage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageRepository extends JpaRepository<Brokerage, Long> {

    @Query(value = "select distinct brokerage from Brokerage brokerage left join fetch brokerage.brokerageClients left join fetch brokerage.brokerageProducts left join fetch brokerage.brokerageAssistances",
        countQuery = "select count(distinct brokerage) from Brokerage brokerage")
    Page<Brokerage> findAllWithEagerRelationships(Pageable pageable);

    @Query(value = "select distinct brokerage from Brokerage brokerage left join fetch brokerage.brokerageClients left join fetch brokerage.brokerageProducts left join fetch brokerage.brokerageAssistances")
    List<Brokerage> findAllWithEagerRelationships();

    @Query("select brokerage from Brokerage brokerage left join fetch brokerage.brokerageClients left join fetch brokerage.brokerageProducts left join fetch brokerage.brokerageAssistances where brokerage.id =:id")
    Optional<Brokerage> findOneWithEagerRelationships(@Param("id") Long id);

}
