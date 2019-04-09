package br.com.clearinvest.clivserver.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.listener.AuditApplicationEvent;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ClearStockOrderSeqTask {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ClearStockOrderSeqTask(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void execute() {
        // TODO add an audit event for this action:
        // https://stackoverflow.com/questions/50509886/how-to-use-the-audit-system-of-jhipster-to-audit-password-changes
        // https://www.google.com/search?q=jhipster+custom+audit+event

        //jdbcTemplate.execute("ALTER SEQUENCE stock_order_date_seq RESTART WITH 1");
    }

}
