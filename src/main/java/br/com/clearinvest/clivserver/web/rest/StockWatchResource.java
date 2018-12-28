package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.StockWatchService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import br.com.clearinvest.clivserver.service.dto.StockWatchDTO;
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
 * REST controller for managing StockWatch.
 */
@RestController
@RequestMapping("/api")
public class StockWatchResource {

    private final Logger log = LoggerFactory.getLogger(StockWatchResource.class);

    private static final String ENTITY_NAME = "stockWatch";

    private final StockWatchService stockWatchService;

    public StockWatchResource(StockWatchService stockWatchService) {
        this.stockWatchService = stockWatchService;
    }

    /**
     * POST  /stock-watches : Create a new stockWatch.
     *
     * @param stockWatchDTO the stockWatchDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new stockWatchDTO, or with status 400 (Bad Request) if the stockWatch has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/stock-watches")
    @Timed
    public ResponseEntity<StockWatchDTO> createStockWatch(@Valid @RequestBody StockWatchDTO stockWatchDTO) throws URISyntaxException {
        log.debug("REST request to save StockWatch : {}", stockWatchDTO);
        if (stockWatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new stockWatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockWatchDTO result = stockWatchService.save(stockWatchDTO);
        return ResponseEntity.created(new URI("/api/stock-watches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /stock-watches : Updates an existing stockWatch.
     *
     * @param stockWatchDTO the stockWatchDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated stockWatchDTO,
     * or with status 400 (Bad Request) if the stockWatchDTO is not valid,
     * or with status 500 (Internal Server Error) if the stockWatchDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/stock-watches")
    @Timed
    public ResponseEntity<StockWatchDTO> updateStockWatch(@Valid @RequestBody StockWatchDTO stockWatchDTO) throws URISyntaxException {
        log.debug("REST request to update StockWatch : {}", stockWatchDTO);
        if (stockWatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockWatchDTO result = stockWatchService.save(stockWatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, stockWatchDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /stock-watches : get all the stockWatches.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of stockWatches in body
     */
    @GetMapping("/stock-watches")
    @Timed
    public ResponseEntity<List<StockWatchDTO>> getAllStockWatches(Pageable pageable) {
        log.debug("REST request to get a page of StockWatches");
        Page<StockWatchDTO> page = stockWatchService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/stock-watches");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /stock-watches/:id : get the "id" stockWatch.
     *
     * @param id the id of the stockWatchDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the stockWatchDTO, or with status 404 (Not Found)
     */
    @GetMapping("/stock-watches/{id}")
    @Timed
    public ResponseEntity<StockWatchDTO> getStockWatch(@PathVariable Long id) {
        log.debug("REST request to get StockWatch : {}", id);
        Optional<StockWatchDTO> stockWatchDTO = stockWatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stockWatchDTO);
    }

    /**
     * DELETE  /stock-watches/:id : delete the "id" stockWatch.
     *
     * @param id the id of the stockWatchDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/stock-watches/{id}")
    @Timed
    public ResponseEntity<Void> deleteStockWatch(@PathVariable Long id) {
        log.debug("REST request to delete StockWatch : {}", id);
        stockWatchService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
