package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageClientService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageClientDTO;
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
 * REST controller for managing BrokerageClient.
 */
@RestController
@RequestMapping("/api")
public class BrokerageClientResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageClientResource.class);

    private static final String ENTITY_NAME = "brokerageClient";

    private final BrokerageClientService brokerageClientService;

    public BrokerageClientResource(BrokerageClientService brokerageClientService) {
        this.brokerageClientService = brokerageClientService;
    }

    /**
     * POST  /brokerage-clients : Create a new brokerageClient.
     *
     * @param brokerageClientDTO the brokerageClientDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageClientDTO, or with status 400 (Bad Request) if the brokerageClient has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerage-clients")
    @Timed
    public ResponseEntity<BrokerageClientDTO> createBrokerageClient(@Valid @RequestBody BrokerageClientDTO brokerageClientDTO) throws URISyntaxException {
        log.debug("REST request to save BrokerageClient : {}", brokerageClientDTO);
        if (brokerageClientDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerageClient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageClientDTO result = brokerageClientService.save(brokerageClientDTO);
        return ResponseEntity.created(new URI("/api/brokerage-clients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerage-clients : Updates an existing brokerageClient.
     *
     * @param brokerageClientDTO the brokerageClientDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageClientDTO,
     * or with status 400 (Bad Request) if the brokerageClientDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageClientDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerage-clients")
    @Timed
    public ResponseEntity<BrokerageClientDTO> updateBrokerageClient(@Valid @RequestBody BrokerageClientDTO brokerageClientDTO) throws URISyntaxException {
        log.debug("REST request to update BrokerageClient : {}", brokerageClientDTO);
        if (brokerageClientDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageClientDTO result = brokerageClientService.save(brokerageClientDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageClientDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerage-clients : get all the brokerageClients.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of brokerageClients in body
     */
    @GetMapping("/brokerage-clients")
    @Timed
    public ResponseEntity<List<BrokerageClientDTO>> getAllBrokerageClients(Pageable pageable) {
        log.debug("REST request to get a page of BrokerageClients");
        Page<BrokerageClientDTO> page = brokerageClientService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokerage-clients");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /brokerage-clients/:id : get the "id" brokerageClient.
     *
     * @param id the id of the brokerageClientDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageClientDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerage-clients/{id}")
    @Timed
    public ResponseEntity<BrokerageClientDTO> getBrokerageClient(@PathVariable Long id) {
        log.debug("REST request to get BrokerageClient : {}", id);
        Optional<BrokerageClientDTO> brokerageClientDTO = brokerageClientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageClientDTO);
    }

    /**
     * DELETE  /brokerage-clients/:id : delete the "id" brokerageClient.
     *
     * @param id the id of the brokerageClientDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerage-clients/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerageClient(@PathVariable Long id) {
        log.debug("REST request to delete BrokerageClient : {}", id);
        brokerageClientService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
