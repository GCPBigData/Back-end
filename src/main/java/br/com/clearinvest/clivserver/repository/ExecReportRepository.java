package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.ExecReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExecReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecReportRepository extends JpaRepository<ExecReport, Long> {

}
