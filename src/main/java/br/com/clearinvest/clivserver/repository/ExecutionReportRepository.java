package br.com.clearinvest.clivserver.repository;

import br.com.clearinvest.clivserver.domain.ExecutionReport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ExecutionReport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ExecutionReportRepository extends JpaRepository<ExecutionReport, Long> {

}
