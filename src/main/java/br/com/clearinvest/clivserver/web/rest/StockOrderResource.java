package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.StockOrderService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;
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
 * REST controller for managing StockOrder.
 */
@RestController
@RequestMapping("/api")
public class StockOrderResource {

    private final Logger log = LoggerFactory.getLogger(StockOrderResource.class);

    private static final String ENTITY_NAME = "stockOrder";

    private final StockOrderService stockOrderService;

    public StockOrderResource(StockOrderService stockOrderService) {
        this.stockOrderService = stockOrderService;
    }

    /**
     * POST  /stock-orders : Create a new stockOrder.
     *
     * @param stockOrderDTO the stockOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockOrderDTO, or with status 400 (Bad Request) if the stockOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-orders")
    @Timed
    public ResponseEntity<StockOrderDTO> createStockOrder(@Valid @RequestBody StockOrderDTO stockOrderDTO) throws URISyntaxException {
        log.debug("REST request to save StockOrder : {}", stockOrderDTO);
        if (stockOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockOrderDTO result = stockOrderService.save(stockOrderDTO);
        return ResponseEntity.created(new URI("/api/stock-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-orders : Updates an existing stockOrder.
     *
     * @param stockOrderDTO the stockOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockOrderDTO,
     * or with status 400 (Bad Request) if the stockOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockOrderDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-orders")
    @Timed
    public ResponseEntity<StockOrderDTO> updateStockOrder(@Valid @RequestBody StockOrderDTO stockOrderDTO) throws URISyntaxException {
        log.debug("REST request to update StockOrder : {}", stockOrderDTO);
        if (stockOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockOrderDTO result = stockOrderService.update(stockOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-orders : get all the stockOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of stockOrders in body
     */
    @GetMapping("/stock-orders")
    @Timed
    public List<StockOrderDTO> getAllStockOrders() {
        log.debug("REST request to get all StockOrders");
        return stockOrderService.findAll();
    }

    /**
     * GET  /stock-orders/:id : get the "id" stockOrder.
     *
     * @param id the id of the stockOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-orders/{id}")
    @Timed
    public ResponseEntity<StockOrderDTO> getStockOrder(@PathVariable Long id) {
        log.debug("REST request to get StockOrder : {}", id);
        Optional<StockOrderDTO> stockOrderDTO = stockOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockOrderDTO);
    }

    /**
     * DELETE  /stock-orders/:id : delete the "id" stockOrder.
     *
     * @param id the id of the stockOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockOrder(@PathVariable Long id) {
        log.debug("REST request to delete StockOrder : {}", id);
        stockOrderService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
