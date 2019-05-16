package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.AppPreference;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.AppPreferenceRepository;
import br.com.clearinvest.clivserver.service.AppPreferenceService;
import br.com.clearinvest.clivserver.service.dto.AppPreferenceDTO;
import br.com.clearinvest.clivserver.service.mapper.AppPreferenceMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.clearinvest.clivserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AppPreferenceResource REST controller.
 *
 * @see AppPreferenceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class AppPreferenceResourceIntTest {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    @Autowired
    private AppPreferenceRepository appPreferenceRepository;

    @Autowired
    private AppPreferenceMapper appPreferenceMapper;

    @Autowired
    private AppPreferenceService appPreferenceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppPreferenceMockMvc;

    private AppPreference appPreference;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppPreferenceResource appPreferenceResource = new AppPreferenceResource(appPreferenceService);
        this.restAppPreferenceMockMvc = MockMvcBuilders.standaloneSetup(appPreferenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AppPreference createEntity(EntityManager em) {
        AppPreference appPreference = new AppPreference()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        appPreference.setUser(user);
        return appPreference;
    }

    @Before
    public void initTest() {
        appPreference = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppPreference() throws Exception {
        int databaseSizeBeforeCreate = appPreferenceRepository.findAll().size();

        // Create the AppPreference
        AppPreferenceDTO appPreferenceDTO = appPreferenceMapper.toDto(appPreference);
        restAppPreferenceMockMvc.perform(post("/api/app-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appPreferenceDTO)))
            .andExpect(status().isCreated());

        // Validate the AppPreference in the database
        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeCreate + 1);
        AppPreference testAppPreference = appPreferenceList.get(appPreferenceList.size() - 1);
        assertThat(testAppPreference.getKey()).isEqualTo(DEFAULT_KEY);
        assertThat(testAppPreference.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void createAppPreferenceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appPreferenceRepository.findAll().size();

        // Create the AppPreference with an existing ID
        appPreference.setId(1L);
        AppPreferenceDTO appPreferenceDTO = appPreferenceMapper.toDto(appPreference);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppPreferenceMockMvc.perform(post("/api/app-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appPreferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppPreference in the database
        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = appPreferenceRepository.findAll().size();
        // set the field null
        appPreference.setKey(null);

        // Create the AppPreference, which fails.
        AppPreferenceDTO appPreferenceDTO = appPreferenceMapper.toDto(appPreference);

        restAppPreferenceMockMvc.perform(post("/api/app-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appPreferenceDTO)))
            .andExpect(status().isBadRequest());

        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAppPreferences() throws Exception {
        // Initialize the database
        appPreferenceRepository.saveAndFlush(appPreference);

        // Get all the appPreferenceList
        restAppPreferenceMockMvc.perform(get("/api/app-preferences?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())));
    }
    
    @Test
    @Transactional
    public void getAppPreference() throws Exception {
        // Initialize the database
        appPreferenceRepository.saveAndFlush(appPreference);

        // Get the appPreference
        restAppPreferenceMockMvc.perform(get("/api/app-preferences/{id}", appPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appPreference.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAppPreference() throws Exception {
        // Get the appPreference
        restAppPreferenceMockMvc.perform(get("/api/app-preferences/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppPreference() throws Exception {
        // Initialize the database
        appPreferenceRepository.saveAndFlush(appPreference);

        int databaseSizeBeforeUpdate = appPreferenceRepository.findAll().size();

        // Update the appPreference
        AppPreference updatedAppPreference = appPreferenceRepository.findById(appPreference.getId()).get();
        // Disconnect from session so that the updates on updatedAppPreference are not directly saved in db
        em.detach(updatedAppPreference);
        updatedAppPreference
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE);
        AppPreferenceDTO appPreferenceDTO = appPreferenceMapper.toDto(updatedAppPreference);

        restAppPreferenceMockMvc.perform(put("/api/app-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appPreferenceDTO)))
            .andExpect(status().isOk());

        // Validate the AppPreference in the database
        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeUpdate);
        AppPreference testAppPreference = appPreferenceList.get(appPreferenceList.size() - 1);
        assertThat(testAppPreference.getKey()).isEqualTo(UPDATED_KEY);
        assertThat(testAppPreference.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingAppPreference() throws Exception {
        int databaseSizeBeforeUpdate = appPreferenceRepository.findAll().size();

        // Create the AppPreference
        AppPreferenceDTO appPreferenceDTO = appPreferenceMapper.toDto(appPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppPreferenceMockMvc.perform(put("/api/app-preferences")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appPreferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AppPreference in the database
        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppPreference() throws Exception {
        // Initialize the database
        appPreferenceRepository.saveAndFlush(appPreference);

        int databaseSizeBeforeDelete = appPreferenceRepository.findAll().size();

        // Get the appPreference
        restAppPreferenceMockMvc.perform(delete("/api/app-preferences/{id}", appPreference.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<AppPreference> appPreferenceList = appPreferenceRepository.findAll();
        assertThat(appPreferenceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppPreference.class);
        AppPreference appPreference1 = new AppPreference();
        appPreference1.setId(1L);
        AppPreference appPreference2 = new AppPreference();
        appPreference2.setId(appPreference1.getId());
        assertThat(appPreference1).isEqualTo(appPreference2);
        appPreference2.setId(2L);
        assertThat(appPreference1).isNotEqualTo(appPreference2);
        appPreference1.setId(null);
        assertThat(appPreference1).isNotEqualTo(appPreference2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppPreferenceDTO.class);
        AppPreferenceDTO appPreferenceDTO1 = new AppPreferenceDTO();
        appPreferenceDTO1.setId(1L);
        AppPreferenceDTO appPreferenceDTO2 = new AppPreferenceDTO();
        assertThat(appPreferenceDTO1).isNotEqualTo(appPreferenceDTO2);
        appPreferenceDTO2.setId(appPreferenceDTO1.getId());
        assertThat(appPreferenceDTO1).isEqualTo(appPreferenceDTO2);
        appPreferenceDTO2.setId(2L);
        assertThat(appPreferenceDTO1).isNotEqualTo(appPreferenceDTO2);
        appPreferenceDTO1.setId(null);
        assertThat(appPreferenceDTO1).isNotEqualTo(appPreferenceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(appPreferenceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(appPreferenceMapper.fromId(null)).isNull();
    }
}
