package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.AppPreference;
import br.com.clearinvest.clivserver.repository.AppPreferenceRepository;
import br.com.clearinvest.clivserver.repository.UserRepository;
import br.com.clearinvest.clivserver.security.AuthoritiesConstants;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.service.dto.AppPreferenceDTO;
import br.com.clearinvest.clivserver.service.mapper.AppPreferenceMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing AppPreference.
 */
@Service
@Transactional
public class AppPreferenceService {

    private static final String PREF_STOCK_LIST_COLUMNS = "stockListColumns";

    private final Logger log = LoggerFactory.getLogger(AppPreferenceService.class);

    private final AppPreferenceRepository appPreferenceRepository;

    private final AppPreferenceMapper appPreferenceMapper;

    private final UserRepository userRepository;

    public AppPreferenceService(AppPreferenceRepository appPreferenceRepository,
            AppPreferenceMapper appPreferenceMapper, UserRepository userRepository) {
        this.appPreferenceRepository = appPreferenceRepository;
        this.appPreferenceMapper = appPreferenceMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a appPreference.
     *
     * @param appPreferenceDTO the entity to save
     * @return the persisted entity
     */
    public AppPreferenceDTO save(AppPreferenceDTO appPreferenceDTO) {
        log.debug("Request to save AppPreference : {}", appPreferenceDTO);

        AppPreference appPreference = appPreferenceMapper.toEntity(appPreferenceDTO);
        appPreference = appPreferenceRepository.save(appPreference);
        return appPreferenceMapper.toDto(appPreference);
    }

    /**
     * Save a appPreference.
     *
     * @param appPreferenceDTO the entity to save
     * @return the persisted entity
     */
    public AppPreferenceDTO saveWithCurrentUser(AppPreferenceDTO appPreferenceDTO) {
        log.debug("Request to save AppPreference : {}", appPreferenceDTO);

        AppPreference appPreference;
        Optional<AppPreference> prefOptional = appPreferenceRepository.findByKeyAndCurrentUser(appPreferenceDTO.getKey());
        if (prefOptional.isPresent()) {
            appPreference = prefOptional.get();
            appPreference.setValue(appPreferenceDTO.getValue());

        } else {
            appPreference = appPreferenceMapper.toEntity(appPreferenceDTO);

            SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(appPreference::setUser);
        }

        appPreference = appPreferenceRepository.save(appPreference);
        return appPreferenceMapper.toDto(appPreference);
    }

    /**
     * Get all the appPreferences.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AppPreferenceDTO> findAll() {
        log.debug("Request to get all AppPreferences");
        return appPreferenceRepository.findAll().stream()
            .map(appPreferenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the appPreferences.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<AppPreferenceDTO> findAllOfCurrentUserWithDefaults() {
        List<AppPreference> prefs = appPreferenceRepository.findByUserIsCurrentUser();
        Collection<AppPreference> prefsWithDefaults = addDefaults(prefs);
        return prefsWithDefaults.stream()
            .map(appPreferenceMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    private Collection<AppPreference> addDefaults(List<AppPreference> prefs) {
        Map<String, AppPreference> prefsByKey = prefs.stream()
            .collect(Collectors.toMap(AppPreference::getKey, item -> item));

        AppPreference stockListPref = prefsByKey.get(PREF_STOCK_LIST_COLUMNS);
        if (stockListPref == null) {
            String columnsString = Arrays.asList("symbol", "lastPrice", "variationPercent", "initialPrice").stream()
                .collect(Collectors.joining(","));

            stockListPref = new AppPreference(PREF_STOCK_LIST_COLUMNS, columnsString);
            prefsByKey.put(PREF_STOCK_LIST_COLUMNS, stockListPref);
        }

        return prefsByKey.values();
    }

    /**
     * Get one appPreference by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<AppPreferenceDTO> findOne(Long id) {
        log.debug("Request to get AppPreference : {}", id);
        return appPreferenceRepository.findById(id)
            .map(appPreferenceMapper::toDto);
    }

    /**
     * Delete the appPreference by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete AppPreference : {}", id);
        appPreferenceRepository.deleteById(id);
    }
}
