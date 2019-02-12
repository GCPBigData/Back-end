package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.AppPreference;
import br.com.clearinvest.clivserver.domain.StockWatch;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the AppPreference entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppPreferenceRepository extends JpaRepository<AppPreference, Long> {

    @Query("select app_preference from AppPreference app_preference where app_preference.user.login = ?#{principal.username}")
    List<AppPreference> findByUserIsCurrentUser();

    @Query("select e from AppPreference e where e.key = :key and e.user.login = ?#{principal.username}")
    Optional<AppPreference> findByKeyAndCurrentUser(@Param("key") String key);

}
