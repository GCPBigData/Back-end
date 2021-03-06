package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageProduct;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the BrokerageProduct entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageProductRepository extends JpaRepository<BrokerageProduct, Long> {

}
