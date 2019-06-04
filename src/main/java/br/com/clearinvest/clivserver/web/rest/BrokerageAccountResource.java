package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageAccountService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageAccountDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BrokerageAccount.
 */
@RestController
@RequestMapping("/api")
public class BrokerageAccountResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageAccountResource.class);

    private static final String ENTITY_NAME = "brokerageAccount";

    private final BrokerageAccountService brokerageAccountService;

    public BrokerageAccountResource(BrokerageAccountService brokerageAccountService) {
        this.brokerageAccountService = brokerageAccountService;
    }

    /**
     * POST  /brokerage-accounts : Create a new brokerageAccount.
     *
     * @param brokerageAccountDTO the brokerageAccountDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageAccountDTO, or with status 400 (Bad Request) if the brokerageAccount has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerage-accounts")
    @Timed
    public ResponseEntity<BrokerageAccountDTO> createBrokerageAccount(@Valid @RequestBody BrokerageAccountDTO brokerageAccountDTO) throws URISyntaxException {
        log.debug("REST request to save BrokerageAccount : {}", brokerageAccountDTO);
        if (brokerageAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerageAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageAccountDTO result = brokerageAccountService.add(brokerageAccountDTO);
        return ResponseEntity.created(new URI("/api/brokerage-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerage-accounts : Updates an existing brokerageAccount.
     *
     * @param brokerageAccountDTO the brokerageAccountDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageAccountDTO,
     * or with status 400 (Bad Request) if the brokerageAccountDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageAccountDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerage-accounts")
    @Timed
    public ResponseEntity<BrokerageAccountDTO> updateBrokerageAccount(@Valid @RequestBody BrokerageAccountDTO brokerageAccountDTO) throws URISyntaxException {
        log.debug("REST request to update BrokerageAccount : {}", brokerageAccountDTO);
        if (brokerageAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageAccountDTO result = brokerageAccountService.add(brokerageAccountDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerage-accounts : get all the brokerageAccounts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of brokerageAccounts in body
     */
    @GetMapping("/brokerage-accounts")
    @Timed
    public ResponseEntity<List<BrokerageAccountDTO>> getAllBrokerageAccounts(Pageable pageable) {
        log.debug("REST request to get a page of BrokerageAccounts");
        Page<BrokerageAccountDTO> page = brokerageAccountService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokerage-accounts");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /brokerage-accounts/:id : get the "id" brokerageAccount.
     *
     * @param id the id of the brokerageAccountDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageAccountDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerage-accounts/{id}")
    @Timed
    public ResponseEntity<BrokerageAccountDTO> getBrokerageAccount(@PathVariable Long id) {
        log.debug("REST request to get BrokerageAccount : {}", id);
        Optional<BrokerageAccountDTO> brokerageAccountDTO = brokerageAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageAccountDTO);
    }

    /**
     * DELETE  /brokerage-accounts/:id : delete the "id" brokerageAccount.
     *
     * @param id the id of the brokerageAccountDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerage-accounts/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerageAccount(@PathVariable Long id) {
        log.debug("REST request to delete BrokerageAccount : {}", id);
        brokerageAccountService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
