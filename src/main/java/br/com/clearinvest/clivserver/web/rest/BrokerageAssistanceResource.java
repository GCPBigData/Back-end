package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageAssistanceService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageAssistanceDTO;
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
 * REST controller for managing BrokerageAssistance.
 */
@RestController
@RequestMapping("/api")
public class BrokerageAssistanceResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageAssistanceResource.class);

    private static final String ENTITY_NAME = "brokerageAssistance";

    private final BrokerageAssistanceService brokerageAssistanceService;

    public BrokerageAssistanceResource(BrokerageAssistanceService brokerageAssistanceService) {
        this.brokerageAssistanceService = brokerageAssistanceService;
    }

    /**
     * POST  /brokerage-assistances : Create a new brokerageAssistance.
     *
     * @param brokerageAssistanceDTO the brokerageAssistanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageAssistanceDTO, or with status 400 (Bad Request) if the brokerageAssistance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerage-assistances")
    @Timed
    public ResponseEntity<BrokerageAssistanceDTO> createBrokerageAssistance(@Valid @RequestBody BrokerageAssistanceDTO brokerageAssistanceDTO) throws URISyntaxException {
        log.debug("REST request to save BrokerageAssistance : {}", brokerageAssistanceDTO);
        if (brokerageAssistanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerageAssistance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageAssistanceDTO result = brokerageAssistanceService.save(brokerageAssistanceDTO);
        return ResponseEntity.created(new URI("/api/brokerage-assistances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerage-assistances : Updates an existing brokerageAssistance.
     *
     * @param brokerageAssistanceDTO the brokerageAssistanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageAssistanceDTO,
     * or with status 400 (Bad Request) if the brokerageAssistanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageAssistanceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerage-assistances")
    @Timed
    public ResponseEntity<BrokerageAssistanceDTO> updateBrokerageAssistance(@Valid @RequestBody BrokerageAssistanceDTO brokerageAssistanceDTO) throws URISyntaxException {
        log.debug("REST request to update BrokerageAssistance : {}", brokerageAssistanceDTO);
        if (brokerageAssistanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageAssistanceDTO result = brokerageAssistanceService.save(brokerageAssistanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageAssistanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerage-assistances : get all the brokerageAssistances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of brokerageAssistances in body
     */
    @GetMapping("/brokerage-assistances")
    @Timed
    public ResponseEntity<List<BrokerageAssistanceDTO>> getAllBrokerageAssistances(Pageable pageable) {
        log.debug("REST request to get a page of BrokerageAssistances");
        Page<BrokerageAssistanceDTO> page = brokerageAssistanceService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokerage-assistances");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /brokerage-assistances/:id : get the "id" brokerageAssistance.
     *
     * @param id the id of the brokerageAssistanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageAssistanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerage-assistances/{id}")
    @Timed
    public ResponseEntity<BrokerageAssistanceDTO> getBrokerageAssistance(@PathVariable Long id) {
        log.debug("REST request to get BrokerageAssistance : {}", id);
        Optional<BrokerageAssistanceDTO> brokerageAssistanceDTO = brokerageAssistanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageAssistanceDTO);
    }

    /**
     * DELETE  /brokerage-assistances/:id : delete the "id" brokerageAssistance.
     *
     * @param id the id of the brokerageAssistanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerage-assistances/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerageAssistance(@PathVariable Long id) {
        log.debug("REST request to delete BrokerageAssistance : {}", id);
        brokerageAssistanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
