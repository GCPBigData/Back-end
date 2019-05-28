package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.ExecutionReportService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.ExecutionReportDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExecutionReport.
 */
@RestController
@RequestMapping("/api")
public class ExecutionReportResource {

    private final Logger log = LoggerFactory.getLogger(ExecutionReportResource.class);

    private static final String ENTITY_NAME = "executionReport";

    private final ExecutionReportService executionReportService;

    public ExecutionReportResource(ExecutionReportService executionReportService) {
        this.executionReportService = executionReportService;
    }

    /**
     * POST  /execution-reports : Create a new executionReport.
     *
     * @param executionReportDTO the executionReportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new executionReportDTO, or with status 400 (Bad Request) if the executionReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/execution-reports")
    @Timed
    public ResponseEntity<ExecutionReportDTO> createExecutionReport(@Valid @RequestBody ExecutionReportDTO executionReportDTO) throws URISyntaxException {
        log.debug("REST request to save ExecutionReport : {}", executionReportDTO);
        if (executionReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new executionReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExecutionReportDTO result = executionReportService.save(executionReportDTO);
        return ResponseEntity.created(new URI("/api/execution-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /execution-reports : Updates an existing executionReport.
     *
     * @param executionReportDTO the executionReportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated executionReportDTO,
     * or with status 400 (Bad Request) if the executionReportDTO is not valid,
     * or with status 500 (Internal Server Error) if the executionReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/execution-reports")
    @Timed
    public ResponseEntity<ExecutionReportDTO> updateExecutionReport(@Valid @RequestBody ExecutionReportDTO executionReportDTO) throws URISyntaxException {
        log.debug("REST request to update ExecutionReport : {}", executionReportDTO);
        if (executionReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExecutionReportDTO result = executionReportService.save(executionReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, executionReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /execution-reports : get all the executionReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of executionReports in body
     */
    @GetMapping("/execution-reports")
    @Timed
    public List<ExecutionReportDTO> getAllExecutionReports() {
        log.debug("REST request to get all ExecutionReports");
        return executionReportService.findAll();
    }

    /**
     * GET  /execution-reports/:id : get the "id" executionReport.
     *
     * @param id the id of the executionReportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the executionReportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/execution-reports/{id}")
    @Timed
    public ResponseEntity<ExecutionReportDTO> getExecutionReport(@PathVariable Long id) {
        log.debug("REST request to get ExecutionReport : {}", id);
        Optional<ExecutionReportDTO> executionReportDTO = executionReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executionReportDTO);
    }

    /**
     * DELETE  /execution-reports/:id : delete the "id" executionReport.
     *
     * @param id the id of the executionReportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/execution-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteExecutionReport(@PathVariable Long id) {
        log.debug("REST request to delete ExecutionReport : {}", id);
        executionReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
