package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.ExecReportService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.ExecReportDTO;
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
 * REST controller for managing ExecReport.
 */
@RestController
@RequestMapping("/api")
public class ExecReportResource {

    private final Logger log = LoggerFactory.getLogger(ExecReportResource.class);

    private static final String ENTITY_NAME = "execReport";

    private final ExecReportService execReportService;

    public ExecReportResource(ExecReportService execReportService) {
        this.execReportService = execReportService;
    }

    /**
     * POST  /exec-reports : Create a new execReport.
     *
     * @param execReportDTO the execReportDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new execReportDTO, or with status 400 (Bad Request) if the execReport has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exec-reports")
    @Timed
    public ResponseEntity<ExecReportDTO> createExecReport(@Valid @RequestBody ExecReportDTO execReportDTO) throws URISyntaxException {
        log.debug("REST request to save ExecReport : {}", execReportDTO);
        if (execReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new execReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExecReportDTO result = execReportService.save(execReportDTO);
        return ResponseEntity.created(new URI("/api/exec-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exec-reports : Updates an existing execReport.
     *
     * @param execReportDTO the execReportDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated execReportDTO,
     * or with status 400 (Bad Request) if the execReportDTO is not valid,
     * or with status 500 (Internal Server Error) if the execReportDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exec-reports")
    @Timed
    public ResponseEntity<ExecReportDTO> updateExecReport(@Valid @RequestBody ExecReportDTO execReportDTO) throws URISyntaxException {
        log.debug("REST request to update ExecReport : {}", execReportDTO);
        if (execReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ExecReportDTO result = execReportService.save(execReportDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, execReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exec-reports : get all the execReports.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of execReports in body
     */
    @GetMapping("/exec-reports")
    @Timed
    public List<ExecReportDTO> getAllExecReports() {
        log.debug("REST request to get all ExecReports");
        return execReportService.findAll();
    }

    /**
     * GET  /exec-reports/:id : get the "id" execReport.
     *
     * @param id the id of the execReportDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the execReportDTO, or with status 404 (Not Found)
     */
    @GetMapping("/exec-reports/{id}")
    @Timed
    public ResponseEntity<ExecReportDTO> getExecReport(@PathVariable Long id) {
        log.debug("REST request to get ExecReport : {}", id);
        Optional<ExecReportDTO> execReportDTO = execReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(execReportDTO);
    }

    /**
     * DELETE  /exec-reports/:id : delete the "id" execReport.
     *
     * @param id the id of the execReportDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exec-reports/{id}")
    @Timed
    public ResponseEntity<Void> deleteExecReport(@PathVariable Long id) {
        log.debug("REST request to delete ExecReport : {}", id);
        execReportService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
