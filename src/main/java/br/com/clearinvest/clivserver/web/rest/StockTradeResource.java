package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.StockTradeService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.dto.StockTradeCriteria;
import br.com.clearinvest.clivserver.service.StockTradeQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StockTrade.
 */
@RestController
@RequestMapping("/api")
public class StockTradeResource {

    private final Logger log = LoggerFactory.getLogger(StockTradeResource.class);

    private static final String ENTITY_NAME = "stockTrade";

    private final StockTradeService stockTradeService;

    private final StockTradeQueryService stockTradeQueryService;

    public StockTradeResource(StockTradeService stockTradeService, StockTradeQueryService stockTradeQueryService) {
        this.stockTradeService = stockTradeService;
        this.stockTradeQueryService = stockTradeQueryService;
    }

    /**
     * POST  /stock-trades : Create a new stockTrade.
     *
     * @param stockTradeDTO the stockTradeDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockTradeDTO, or with status 400 (Bad Request) if the stockTrade has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-trades")
    @Timed
    public ResponseEntity<StockTradeDTO> createStockTrade(@Valid @RequestBody StockTradeDTO stockTradeDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to save StockTrade : {}", stockTradeDTO);
        if (stockTradeDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockTrade cannot already have an ID", ENTITY_NAME, "idexists");
        }
        stockTradeDTO.setCreatedByIp(request.getRemoteAddr());
        StockTradeDTO result = stockTradeService.save(stockTradeDTO);
        return ResponseEntity.created(new URI("/api/stock-trades/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-trades : Updates an existing stockTrade.
     *
     * @param stockTradeDTO the stockTradeDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockTradeDTO,
     * or with status 400 (Bad Request) if the stockTradeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockTradeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-trades")
    @Timed
    public ResponseEntity<StockTradeDTO> updateStockTrade(@Valid @RequestBody StockTradeDTO stockTradeDTO, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to update StockTrade : {}", stockTradeDTO);
        if (stockTradeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        stockTradeDTO.setCreatedByIp(request.getRemoteAddr());
        StockTradeDTO result = stockTradeService.update(stockTradeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockTradeDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-trades : get all the stockTrades.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of stockTrades in body
     */
    @GetMapping("/stock-trades")
    @Timed
    public ResponseEntity<List<StockTradeDTO>> getAllStockTrades(StockTradeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StockTrades by criteria: {}", criteria);
        Page<StockTradeDTO> page = stockTradeQueryService.findByCriteriaAndCurrentUser(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-trades");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

//    /**
//     * GET  /stock-trades : get all the stockTrades.
//     *
//     * @return the ResponseEntity with status 200 (OK) and the list of stockTrades in body
//     */
//    @GetMapping("/stock-trades")
//    @Timed
//    public List<StockTradeDTO> getAllStockTrades() {
//        log.debug("REST request to get all StockTrades");
//        return stockTradeService.findAll();
//    }

    /**
    * GET  /stock-trades/count : count all the stockTrades.
    *
    * @param criteria the criterias which the requested entities should match
    * @return the ResponseEntity with status 200 (OK) and the count in body
    */
    @GetMapping("/stock-trades/count")
    @Timed
    public ResponseEntity<Long> countStockTrades(StockTradeCriteria criteria) {
        log.debug("REST request to count StockTrades by criteria: {}", criteria);
        return ResponseEntity.ok().body(stockTradeQueryService.countByCriteria(criteria));
    }

    /**
     * GET  /stock-trades/:id : get the "id" stockTrade.
     *
     * @param id the id of the stockTradeDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockTradeDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-trades/{id}")
    @Timed
    public ResponseEntity<StockTradeDTO> getStockTrade(@PathVariable Long id) {
        log.debug("REST request to get StockTrade : {}", id);
        Optional<StockTradeDTO> stockTradeDTO = stockTradeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockTradeDTO);
    }

    /**
     * DELETE  /stock-trades/:id : delete the "id" stockTrade.
     *
     * @param id the id of the stockTradeDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-trades/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockTrade(@PathVariable Long id) {
        log.debug("REST request to delete StockTrade : {}", id);
        stockTradeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * PUT  /stock-trades/:id/cancel : Cancel an existing stockTrade.
     *
     * @param id the id of the stockTradeDTO to cancel
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockTradeDTO,
     * or with status 400 (Bad Request) if the stockTradeDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockTradeDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-trades/{id}/cancel")
    @Timed
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        log.debug("REST request to cancel StockTrade : {}", id);
        stockTradeService.cancel(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, id.toString())).build();
    }
}
