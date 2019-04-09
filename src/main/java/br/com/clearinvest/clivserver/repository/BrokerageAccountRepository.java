package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the BrokerageAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageAccountRepository extends JpaRepository<BrokerageAccount, Long> {

    @Query("select brokerage_account from BrokerageAccount brokerage_account where brokerage_account.user.login = ?#{principal.username}")
    List<BrokerageAccount> findByUserIsCurrentUser();

    @Query("select brokerage_account from BrokerageAccount brokerage_account where brokerage_account.user.login = ?#{principal.username}")
    Page<BrokerageAccount> findAllByUserIsCurrentUser(Pageable pageable);

    @Query("select ba from BrokerageAccount ba where ba.brokerage.id = :brokerageId and ba.user.login = ?#{principal.username}")
    Optional<BrokerageAccount> findByBrokerageIdAndCurrentUser(@Param("brokerageId") Long brokerageId);

    @Query("select ba from BrokerageAccount ba where ba.id = :id and ba.user.login = ?#{principal.username}")
    Optional<BrokerageAccount> findByIdAndCurrentUser(@Param("id") Long brokerageAccountId);

}
