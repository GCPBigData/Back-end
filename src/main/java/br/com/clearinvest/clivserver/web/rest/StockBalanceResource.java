package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.StockBalanceService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.StockBalanceDTO;
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
 * REST controller for managing StockBalance.
 */
@RestController
@RequestMapping("/api")
public class StockBalanceResource {

    private final Logger log = LoggerFactory.getLogger(StockBalanceResource.class);

    private static final String ENTITY_NAME = "stockBalance";

    private final StockBalanceService stockBalanceService;

    public StockBalanceResource(StockBalanceService stockBalanceService) {
        this.stockBalanceService = stockBalanceService;
    }

    /**
     * POST  /stock-balances : Create a new stockBalance.
     *
     * @param stockBalanceDTO the stockBalanceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockBalanceDTO, or with status 400 (Bad Request) if the stockBalance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-balances")
    @Timed
    public ResponseEntity<StockBalanceDTO> createStockBalance(@Valid @RequestBody StockBalanceDTO stockBalanceDTO) throws URISyntaxException {
        log.debug("REST request to save StockBalance : {}", stockBalanceDTO);
        if (stockBalanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockBalanceDTO result = stockBalanceService.save(stockBalanceDTO);
        return ResponseEntity.created(new URI("/api/stock-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-balances : Updates an existing stockBalance.
     *
     * @param stockBalanceDTO the stockBalanceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockBalanceDTO,
     * or with status 400 (Bad Request) if the stockBalanceDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockBalanceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-balances")
    @Timed
    public ResponseEntity<StockBalanceDTO> updateStockBalance(@Valid @RequestBody StockBalanceDTO stockBalanceDTO) throws URISyntaxException {
        log.debug("REST request to update StockBalance : {}", stockBalanceDTO);
        if (stockBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockBalanceDTO result = stockBalanceService.save(stockBalanceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockBalanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-balances : get all the stockBalances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockBalances in body
     */
    @GetMapping("/stock-balances")
    @Timed
    public List<StockBalanceDTO> getAllStockBalances() {
        log.debug("REST request to get all StockBalances");
        return stockBalanceService.findAll();
    }

    /**
     * GET  /stock-balances/:id : get the "id" stockBalance.
     *
     * @param id the id of the stockBalanceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockBalanceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-balances/{id}")
    @Timed
    public ResponseEntity<StockBalanceDTO> getStockBalance(@PathVariable Long id) {
        log.debug("REST request to get StockBalance : {}", id);
        Optional<StockBalanceDTO> stockBalanceDTO = stockBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockBalanceDTO);
    }

    /**
     * DELETE  /stock-balances/:id : delete the "id" stockBalance.
     *
     * @param id the id of the stockBalanceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-balances/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockBalance(@PathVariable Long id) {
        log.debug("REST request to delete StockBalance : {}", id);
        stockBalanceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
