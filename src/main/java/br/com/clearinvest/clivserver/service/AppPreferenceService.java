package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.AppPreference;
import br.com.clearinvest.clivserver.repository.AppPreferenceRepository;
import br.com.clearinvest.clivserver.repository.MarketSectorRepository;
import br.com.clearinvest.clivserver.repository.UserRepository;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.service.dto.AppPreferenceDTO;
import br.com.clearinvest.clivserver.service.dto.StockListTabDTO;
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
    private static final String PREF_STOCK_LIST_TABS = "stockListTabs";
    private static final String PREF_HOME_SECTIONS = "homeSections";

    private static final Long TAB_ID_WATCHED_STOCKS = 0L;

    private final Logger log = LoggerFactory.getLogger(AppPreferenceService.class);

    private final AppPreferenceRepository appPreferenceRepository;

    private final AppPreferenceMapper appPreferenceMapper;

    private final UserRepository userRepository;

    private final MarketSectorRepository marketSectorRepository;

    public AppPreferenceService(AppPreferenceRepository appPreferenceRepository, AppPreferenceMapper appPreferenceMapper,
            UserRepository userRepository, MarketSectorRepository marketSectorRepository) {
        this.appPreferenceRepository = appPreferenceRepository;
        this.appPreferenceMapper = appPreferenceMapper;
        this.userRepository = userRepository;
        this.marketSectorRepository = marketSectorRepository;
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

        AppPreference stockListColumnsPref = prefsByKey.get(PREF_STOCK_LIST_COLUMNS);
        if (stockListColumnsPref == null || stockListColumnsPref.getValue() == null
                || stockListColumnsPref.getValue().isEmpty()) {
            String columnsString =
                String.join(",", "symbol", "lastPrice", "variationPercent", "initialPrice");

            stockListColumnsPref = new AppPreference(PREF_STOCK_LIST_COLUMNS, columnsString);
            prefsByKey.put(PREF_STOCK_LIST_COLUMNS, stockListColumnsPref);
        }

        // TODO manter apenas abas que existem no banco de dados, com exeção da aba 0 (papéis em vista) que deve estar presente sempre
        AppPreference stockListTabsPref = prefsByKey.get(PREF_STOCK_LIST_TABS);
        if (stockListTabsPref == null || stockListTabsPref.getValue() == null
                || stockListTabsPref.getValue().isEmpty()) {

            List<StockListTabDTO> tabList = marketSectorRepository.findAll().stream()
                .map(x -> new StockListTabDTO(x.getId(), x.getName()))
                .collect(Collectors.toList());
            tabList.add(0, new StockListTabDTO(TAB_ID_WATCHED_STOCKS, "Papéis em Vista"));

            String tabsJson = new Gson().toJson(tabList);
            stockListTabsPref = new AppPreference(PREF_STOCK_LIST_TABS, tabsJson);
            prefsByKey.put(PREF_STOCK_LIST_TABS, stockListTabsPref);
        }

        AppPreference homeSectionsPref = prefsByKey.get(PREF_HOME_SECTIONS);
        if (homeSectionsPref == null || homeSectionsPref.getValue() == null
                || homeSectionsPref.getValue().isEmpty()) {
            String valuesString =
                String.join(",", "balance", "portfolio", "stockList");

            homeSectionsPref = new AppPreference(PREF_HOME_SECTIONS, valuesString);
            prefsByKey.put(PREF_HOME_SECTIONS, homeSectionsPref);
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
