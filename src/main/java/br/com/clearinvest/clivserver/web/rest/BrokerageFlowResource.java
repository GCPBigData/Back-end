package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageFlowService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowDTO;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowCriteria;
import br.com.clearinvest.clivserver.service.BrokerageFlowQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BrokerageFlow.
 */
@RestController
@RequestMapping("/api")
public class BrokerageFlowResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageFlowResource.class);

    private static final String ENTITY_NAME = "brokerageFlow";

    private final BrokerageFlowService brokerageFlowService;

    private final BrokerageFlowQueryService brokerageFlowQueryService;

    public BrokerageFlowResource(BrokerageFlowService brokerageFlowService, BrokerageFlowQueryService brokerageFlowQueryService) {
        this.brokerageFlowService = brokerageFlowService;
        this.brokerageFlowQueryService = brokerageFlowQueryService;
    }

    /**
     * POST  /brokerage-flows : Create a new brokerageFlow.
     *
     * @param brokerageFlowDTO the brokerageFlowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageFlowDTO, or with status 400 (Bad Request) if the brokerageFlow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerage-flows")
    @Timed
    public ResponseEntity<BrokerageFlowDTO> createBrokerageFlow(@Valid @RequestBody BrokerageFlowDTO brokerageFlowDTO) throws URISyntaxException {
        log.debug("REST request to save BrokerageFlow : {}", brokerageFlowDTO);
        if (brokerageFlowDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerageFlow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageFlowDTO result = brokerageFlowService.save(brokerageFlowDTO);
        return ResponseEntity.created(new URI("/api/brokerage-flows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerage-flows : Updates an existing brokerageFlow.
     *
     * @param brokerageFlowDTO the brokerageFlowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageFlowDTO,
     * or with status 400 (Bad Request) if the brokerageFlowDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageFlowDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerage-flows")
    @Timed
    public ResponseEntity<BrokerageFlowDTO> updateBrokerageFlow(@Valid @RequestBody BrokerageFlowDTO brokerageFlowDTO) throws URISyntaxException {
        log.debug("REST request to update BrokerageFlow : {}", brokerageFlowDTO);
        if (brokerageFlowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageFlowDTO result = brokerageFlowService.save(brokerageFlowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageFlowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerage-flows : get all the brokerageFlows.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of brokerageFlows in body
     */
    @GetMapping("/brokerage-flows")
    @Timed
    public ResponseEntity<List<BrokerageFlowDTO>> getAllBrokerageFlows(BrokerageFlowCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BrokerageFlows by criteria: {}", criteria);
        Page<BrokerageFlowDTO> page = brokerageFlowQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokerage-flows");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * GET  /brokerage-flows/count : count all the brokerageFlows.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/brokerage-flows/count")
    @Timed
    public ResponseEntity<Long> countBrokerageFlows(BrokerageFlowCriteria criteria) {
        log.debug("REST request to count BrokerageFlows by criteria: {}", criteria);
        return ResponseEntity.ok().body(brokerageFlowQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /brokerage-flows/:id : get the "id" brokerageFlow.
     *
     * @param id the id of the brokerageFlowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageFlowDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerage-flows/{id}")
    @Timed
    public ResponseEntity<BrokerageFlowDTO> getBrokerageFlow(@PathVariable Long id) {
        log.debug("REST request to get BrokerageFlow : {}", id);
        Optional<BrokerageFlowDTO> brokerageFlowDTO = brokerageFlowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageFlowDTO);
    }

    /**
     * DELETE  /brokerage-flows/:id : delete the "id" brokerageFlow.
     *
     * @param id the id of the brokerageFlowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerage-flows/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerageFlow(@PathVariable Long id) {
        log.debug("REST request to delete BrokerageFlow : {}", id);
        brokerageFlowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
