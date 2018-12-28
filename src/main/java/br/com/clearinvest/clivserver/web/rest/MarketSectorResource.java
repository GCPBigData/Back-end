package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.service.MarketSectorService;
import br.com.clearinvest.clivserver.service.dto.MarketSectorDTO;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
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
 * REST controller for managing MarketSector.
 */
@RestController
@RequestMapping("/api")
public class MarketSectorResource {

    private final Logger log = LoggerFactory.getLogger(MarketSectorResource.class);

    private static final String ENTITY_NAME = "marketSector";

    private final MarketSectorService marketSectorService;

    public MarketSectorResource(MarketSectorService marketSectorService) {
        this.marketSectorService = marketSectorService;
    }

    /**
     * POST  /market-sectors : Create a new marketSector.
     *
     * @param marketSectorDTO the marketSectorDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new marketSectorDTO, or with status 400 (Bad Request) if the marketSector has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/market-sectors")
    @Timed
    public ResponseEntity<MarketSectorDTO> createMarketSector(@Valid @RequestBody MarketSectorDTO marketSectorDTO) throws URISyntaxException {
        log.debug("REST request to save MarketSector : {}", marketSectorDTO);
        if (marketSectorDTO.getId() != null) {
            throw new BadRequestAlertException("A new marketSector cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MarketSectorDTO result = marketSectorService.save(marketSectorDTO);
        return ResponseEntity.created(new URI("/api/market-sectors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /market-sectors : Updates an existing marketSector.
     *
     * @param marketSectorDTO the marketSectorDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated marketSectorDTO,
     * or with status 400 (Bad Request) if the marketSectorDTO is not valid,
     * or with status 500 (Internal Server Error) if the marketSectorDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/market-sectors")
    @Timed
    public ResponseEntity<MarketSectorDTO> updateMarketSector(@Valid @RequestBody MarketSectorDTO marketSectorDTO) throws URISyntaxException {
        log.debug("REST request to update MarketSector : {}", marketSectorDTO);
        if (marketSectorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MarketSectorDTO result = marketSectorService.save(marketSectorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, marketSectorDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /market-sectors : get all the marketSectors.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of marketSectors in body
     */
    @GetMapping("/market-sectors")
    @Timed
    public ResponseEntity<List<MarketSectorDTO>> getAllMarketSectors(Pageable pageable) {
        log.debug("REST request to get a page of MarketSectors");
        Page<MarketSectorDTO> page = marketSectorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/market-sectors");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * GET  /market-sectors/with-stocks : get all the marketSectors with stocks.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of marketSectors in body
     */
    @GetMapping("/market-sectors/with-stocks")
    @Timed
    public ResponseEntity<List<MarketSectorDTO>> getAllMarketSectorsWithStocks() {
        List<MarketSectorDTO> list = marketSectorService.findAllWithStocks();
        return ResponseEntity.ok().body(list);
    }

    /**
     * GET  /market-sectors/:id : get the "id" marketSector.
     *
     * @param id the id of the marketSectorDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the marketSectorDTO, or with status 404 (Not Found)
     */
    @GetMapping("/market-sectors/{id}")
    @Timed
    public ResponseEntity<MarketSectorDTO> getMarketSector(@PathVariable Long id) {
        log.debug("REST request to get MarketSector : {}", id);
        Optional<MarketSectorDTO> marketSectorDTO = marketSectorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(marketSectorDTO);
    }

    /**
     * DELETE  /market-sectors/:id : delete the "id" marketSector.
     *
     * @param id the id of the marketSectorDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/market-sectors/{id}")
    @Timed
    public ResponseEntity<Void> deleteMarketSector(@PathVariable Long id) {
        log.debug("REST request to delete MarketSector : {}", id);
        marketSectorService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
