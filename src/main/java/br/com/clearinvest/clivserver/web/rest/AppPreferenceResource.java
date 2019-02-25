package br.com.clearinvest.clivserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import br.com.clearinvest.clivserver.service.AppPreferenceService;
import br.com.clearinvest.clivserver.web.rest.errors.BadRequestAlertException;
import br.com.clearinvest.clivserver.web.rest.util.HeaderUtil;
import br.com.clearinvest.clivserver.service.dto.AppPreferenceDTO;
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
 * REST controller for managing AppPreference.
 */
@RestController
@RequestMapping("/api")
public class AppPreferenceResource {

    private final Logger log = LoggerFactory.getLogger(AppPreferenceResource.class);

    private static final String ENTITY_NAME = "appPreference";

    private final AppPreferenceService appPreferenceService;

    public AppPreferenceResource(AppPreferenceService appPreferenceService) {
        this.appPreferenceService = appPreferenceService;
    }

    /**
     * POST  /app-preferences : Create or update an appPreference.
     *
     * @param appPreferenceDTO the appPreferenceDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appPreferenceDTO, or with status 400 (Bad Request) if the appPreference has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/app-preferences")
    @Timed
    public ResponseEntity<AppPreferenceDTO> createAppPreference(@Valid @RequestBody AppPreferenceDTO appPreferenceDTO) throws URISyntaxException {
        log.debug("REST request to save AppPreference : {}", appPreferenceDTO);
        if (appPreferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new appPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppPreferenceDTO result = appPreferenceService.saveWithCurrentUser(appPreferenceDTO);
        return ResponseEntity.created(new URI("/api/app-preferences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /app-preferences : Updates an existing appPreference.
     *
     * @param appPreferenceDTO the appPreferenceDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appPreferenceDTO,
     * or with status 400 (Bad Request) if the appPreferenceDTO is not valid,
     * or with status 500 (Internal Server Error) if the appPreferenceDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/app-preferences")
    @Timed
    public ResponseEntity<AppPreferenceDTO> updateAppPreference(@Valid @RequestBody AppPreferenceDTO appPreferenceDTO) throws URISyntaxException {
        log.debug("REST request to update AppPreference : {}", appPreferenceDTO);
        if (appPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppPreferenceDTO result = appPreferenceService.save(appPreferenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appPreferenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /app-preferences : get all the appPreferences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appPreferences in body
     */
    @GetMapping("/app-preferences")
    @Timed
    public List<AppPreferenceDTO> getAllAppPreferences() {
        log.debug("REST request to get all AppPreferences");
        return appPreferenceService.findAll();
    }

    /**
     * GET  /app-preferences/:id : get the "id" appPreference.
     *
     * @param id the id of the appPreferenceDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appPreferenceDTO, or with status 404 (Not Found)
     */
    @GetMapping("/app-preferences/{id}")
    @Timed
    public ResponseEntity<AppPreferenceDTO> getAppPreference(@PathVariable Long id) {
        log.debug("REST request to get AppPreference : {}", id);
        Optional<AppPreferenceDTO> appPreferenceDTO = appPreferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appPreferenceDTO);
    }

    /**
     * DELETE  /app-preferences/:id : delete the "id" appPreference.
     *
     * @param id the id of the appPreferenceDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/app-preferences/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppPreference(@PathVariable Long id) {
        log.debug("REST request to delete AppPreference : {}", id);
        appPreferenceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET  /app-preferences/of-user : get all the appPreferences of current user.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of appPreferences in body
     */
    @GetMapping("/app-preferences/of-user")
    @Timed
    public List<AppPreferenceDTO> getAllByCurrentUserWithDefaults() {
        return appPreferenceService.findAllOfCurrentUserWithDefaults();
    }
}
