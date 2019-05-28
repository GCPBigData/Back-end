package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.StockFlowService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
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
 * REST controller for managing StockFlow.
 */
@RestController
@RequestMapping("/api")
public class StockFlowResource {

    private final Logger log = LoggerFactory.getLogger(StockFlowResource.class);

    private static final String ENTITY_NAME = "stockFlow";

    private final StockFlowService stockFlowService;

    public StockFlowResource(StockFlowService stockFlowService) {
        this.stockFlowService = stockFlowService;
    }

    /**
     * POST  /stock-flows : Create a new stockFlow.
     *
     * @param stockFlowDTO the stockFlowDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockFlowDTO, or with status 400 (Bad Request) if the stockFlow has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-flows")
    @Timed
    public ResponseEntity<StockFlowDTO> createStockFlow(@Valid @RequestBody StockFlowDTO stockFlowDTO) throws URISyntaxException {
        log.debug("REST request to save StockFlow : {}", stockFlowDTO);
        if (stockFlowDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockFlow cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockFlowDTO result = stockFlowService.save(stockFlowDTO);
        return ResponseEntity.created(new URI("/api/stock-flows/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-flows : Updates an existing stockFlow.
     *
     * @param stockFlowDTO the stockFlowDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockFlowDTO,
     * or with status 400 (Bad Request) if the stockFlowDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockFlowDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-flows")
    @Timed
    public ResponseEntity<StockFlowDTO> updateStockFlow(@Valid @RequestBody StockFlowDTO stockFlowDTO) throws URISyntaxException {
        log.debug("REST request to update StockFlow : {}", stockFlowDTO);
        if (stockFlowDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockFlowDTO result = stockFlowService.save(stockFlowDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockFlowDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-flows : get all the stockFlows.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockFlows in body
     */
    @GetMapping("/stock-flows")
    @Timed
    public List<StockFlowDTO> getAllStockFlows() {
        log.debug("REST request to get all StockFlows");
        return stockFlowService.findAll();
    }

    /**
     * GET  /stock-flows/:id : get the "id" stockFlow.
     *
     * @param id the id of the stockFlowDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockFlowDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-flows/{id}")
    @Timed
    public ResponseEntity<StockFlowDTO> getStockFlow(@PathVariable Long id) {
        log.debug("REST request to get StockFlow : {}", id);
        Optional<StockFlowDTO> stockFlowDTO = stockFlowService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockFlowDTO);
    }

    /**
     * DELETE  /stock-flows/:id : delete the "id" stockFlow.
     *
     * @param id the id of the stockFlowDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-flows/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockFlow(@PathVariable Long id) {
        log.debug("REST request to delete StockFlow : {}", id);
        stockFlowService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
