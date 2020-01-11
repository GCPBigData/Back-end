package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.BrokerageProductService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.BrokerageProductDTO;
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
 * REST controller for managing BrokerageProduct.
 */
@RestController
@RequestMapping("/api")
public class BrokerageProductResource {

    private final Logger log = LoggerFactory.getLogger(BrokerageProductResource.class);

    private static final String ENTITY_NAME = "brokerageProduct";

    private final BrokerageProductService brokerageProductService;

    public BrokerageProductResource(BrokerageProductService brokerageProductService) {
        this.brokerageProductService = brokerageProductService;
    }

    /**
     * POST  /brokerage-products : Create a new brokerageProduct.
     *
     * @param brokerageProductDTO the brokerageProductDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new brokerageProductDTO, or with status 400 (Bad Request) if the brokerageProduct has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/brokerage-products")
    @Timed
    public ResponseEntity<BrokerageProductDTO> createBrokerageProduct(@Valid @RequestBody BrokerageProductDTO brokerageProductDTO) throws URISyntaxException {
        log.debug("REST request to save BrokerageProduct : {}", brokerageProductDTO);
        if (brokerageProductDTO.getId() != null) {
            throw new BadRequestAlertException("A new brokerageProduct cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BrokerageProductDTO result = brokerageProductService.save(brokerageProductDTO);
        return ResponseEntity.created(new URI("/api/brokerage-products/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brokerage-products : Updates an existing brokerageProduct.
     *
     * @param brokerageProductDTO the brokerageProductDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated brokerageProductDTO,
     * or with status 400 (Bad Request) if the brokerageProductDTO is not valid,
     * or with status 500 (Internal Server Error) if the brokerageProductDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/brokerage-products")
    @Timed
    public ResponseEntity<BrokerageProductDTO> updateBrokerageProduct(@Valid @RequestBody BrokerageProductDTO brokerageProductDTO) throws URISyntaxException {
        log.debug("REST request to update BrokerageProduct : {}", brokerageProductDTO);
        if (brokerageProductDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BrokerageProductDTO result = brokerageProductService.save(brokerageProductDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, brokerageProductDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brokerage-products : get all the brokerageProducts.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of brokerageProducts in body
     */
    @GetMapping("/brokerage-products")
    @Timed
    public ResponseEntity<List<BrokerageProductDTO>> getAllBrokerageProducts(Pageable pageable) {
        log.debug("REST request to get a page of BrokerageProducts");
        Page<BrokerageProductDTO> page = brokerageProductService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/brokerage-products");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /brokerage-products/:id : get the "id" brokerageProduct.
     *
     * @param id the id of the brokerageProductDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the brokerageProductDTO, or with status 404 (Not Found)
     */
    @GetMapping("/brokerage-products/{id}")
    @Timed
    public ResponseEntity<BrokerageProductDTO> getBrokerageProduct(@PathVariable Long id) {
        log.debug("REST request to get BrokerageProduct : {}", id);
        Optional<BrokerageProductDTO> brokerageProductDTO = brokerageProductService.findOne(id);
        return ResponseUtil.wrapOrNotFound(brokerageProductDTO);
    }

    /**
     * DELETE  /brokerage-products/:id : delete the "id" brokerageProduct.
     *
     * @param id the id of the brokerageProductDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/brokerage-products/{id}")
    @Timed
    public ResponseEntity<Void> deleteBrokerageProduct(@PathVariable Long id) {
        log.debug("REST request to delete BrokerageProduct : {}", id);
        brokerageProductService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
