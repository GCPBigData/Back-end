package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageDTO;
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
 * REST controller for managing Brokerage.
 */
@RestController
@RequestMapping("/api")
public class BrokerageResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageResource.class);

    private static final String ENTITY_NAME = "brokerage";

    private final BrokerageService brokerageService;

    public BrokerageResource(BrokerageService brokerageService) {
        this.brokerageService = brokerageService;
    }

    /**
     * POST  /brokerages : Create a new brokerage.
     *
     * @param brokerageDTO the brokerageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageDTO, or with status 400 (Bad Request) if the brokerage has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerages")
    @Timed
    public ResponseEntity<BrokerageDTO> createBrokerage(@Valid @RequestBody BrokerageDTO brokerageDTO) throws URISyntaxException {
        log.debug("REST request to save Brokerage : {}", brokerageDTO);
        if (brokerageDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageDTO result = brokerageService.save(brokerageDTO);
        return ResponseEntity.created(new URI("/api/brokerages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerages : Updates an existing brokerage.
     *
     * @param brokerageDTO the brokerageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageDTO,
     * or with status 400 (Bad Request) if the brokerageDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerages")
    @Timed
    public ResponseEntity<BrokerageDTO> updateBrokerage(@Valid @RequestBody BrokerageDTO brokerageDTO) throws URISyntaxException {
        log.debug("REST request to update Brokerage : {}", brokerageDTO);
        if (brokerageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageDTO result = brokerageService.save(brokerageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerages : get all the brokerages.
     *
     * @param pageable the pagination information
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many)
     * @return the ResponseEntity with status 200 (OK) and the list of brokerages in body
     */
    @GetMapping("/brokerages")
    @Timed
    public ResponseEntity<List<BrokerageDTO>> getAllBrokerages(Pageable pageable, @RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get a page of Brokerages");
        Page<BrokerageDTO> page;
        if (eagerload) {
            page = brokerageService.findAllWithEagerRelationships(pageable);
        } else {
            page = brokerageService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, String.format("/api/brokerages?eagerload=%b", eagerload));
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /brokerages/:id : get the "id" brokerage.
     *
     * @param id the id of the brokerageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerages/{id}")
    @Timed
    public ResponseEntity<BrokerageDTO> getBrokerage(@PathVariable Long id) {
        log.debug("REST request to get Brokerage : {}", id);
        Optional<BrokerageDTO> brokerageDTO = brokerageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageDTO);
    }

    /**
     * DELETE  /brokerages/:id : delete the "id" brokerage.
     *
     * @param id the id of the brokerageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerages/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerage(@PathVariable Long id) {
        log.debug("REST request to delete Brokerage : {}", id);
        brokerageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
