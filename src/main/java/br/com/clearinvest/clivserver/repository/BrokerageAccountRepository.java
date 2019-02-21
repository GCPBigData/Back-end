package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the BrokerageAccount entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BrokerageAccountRepository extends JpaRepository<BrokerageAccount, Long> {

    @Query("select brokerage_account from BrokerageAccount brokerage_account where brokerage_account.user.login = ?#{principal.username}")
    List<BrokerageAccount> findByUserIsCurrentUser();

}
