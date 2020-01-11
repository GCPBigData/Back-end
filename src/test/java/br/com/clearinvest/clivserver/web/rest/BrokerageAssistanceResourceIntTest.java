package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.BrokerageAssistance;
import br.com.clearinvest.clivserver.repository.BrokerageAssistanceRepository;
import br.com.clearinvest.clivserver.service.BrokerageAssistanceService;
import br.com.clearinvest.clivserver.service.dto.BrokerageAssistanceDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageAssistanceMapper;
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

import javax.persistence.EntityManager;
import java.util.List;


import static br.com.clearinvest.clivserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BrokerageAssistanceResource REST controller.
 *
 * @see BrokerageAssistanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageAssistanceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BrokerageAssistanceRepository brokerageAssistanceRepository;

    @Autowired
    private BrokerageAssistanceMapper brokerageAssistanceMapper;

    @Autowired
    private BrokerageAssistanceService brokerageAssistanceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageAssistanceMockMvc;

    private BrokerageAssistance brokerageAssistance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageAssistanceResource brokerageAssistanceResource = new BrokerageAssistanceResource(brokerageAssistanceService);
        this.restBrokerageAssistanceMockMvc = MockMvcBuilders.standaloneSetup(brokerageAssistanceResource)
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
    public static BrokerageAssistance createEntity(EntityManager em) {
        BrokerageAssistance brokerageAssistance = new BrokerageAssistance()
            .name(DEFAULT_NAME);
        return brokerageAssistance;
    }

    @Before
    public void initTest() {
        brokerageAssistance = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerageAssistance() throws Exception {
        int databaseSizeBeforeCreate = brokerageAssistanceRepository.findAll().size();

        // Create the BrokerageAssistance
        BrokerageAssistanceDTO brokerageAssistanceDTO = brokerageAssistanceMapper.toDto(brokerageAssistance);
        restBrokerageAssistanceMockMvc.perform(post("/api/brokerage-assistances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAssistanceDTO)))
            .andExpect(status().isCreated());

        // Validate the BrokerageAssistance in the database
        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeCreate + 1);
        BrokerageAssistance testBrokerageAssistance = brokerageAssistanceList.get(brokerageAssistanceList.size() - 1);
        assertThat(testBrokerageAssistance.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBrokerageAssistanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageAssistanceRepository.findAll().size();

        // Create the BrokerageAssistance with an existing ID
        brokerageAssistance.setId(1L);
        BrokerageAssistanceDTO brokerageAssistanceDTO = brokerageAssistanceMapper.toDto(brokerageAssistance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageAssistanceMockMvc.perform(post("/api/brokerage-assistances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAssistanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageAssistance in the database
        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageAssistanceRepository.findAll().size();
        // set the field null
        brokerageAssistance.setName(null);

        // Create the BrokerageAssistance, which fails.
        BrokerageAssistanceDTO brokerageAssistanceDTO = brokerageAssistanceMapper.toDto(brokerageAssistance);

        restBrokerageAssistanceMockMvc.perform(post("/api/brokerage-assistances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAssistanceDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerageAssistances() throws Exception {
        // Initialize the database
        brokerageAssistanceRepository.saveAndFlush(brokerageAssistance);

        // Get all the brokerageAssistanceList
        restBrokerageAssistanceMockMvc.perform(get("/api/brokerage-assistances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageAssistance.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBrokerageAssistance() throws Exception {
        // Initialize the database
        brokerageAssistanceRepository.saveAndFlush(brokerageAssistance);

        // Get the brokerageAssistance
        restBrokerageAssistanceMockMvc.perform(get("/api/brokerage-assistances/{id}", brokerageAssistance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerageAssistance.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerageAssistance() throws Exception {
        // Get the brokerageAssistance
        restBrokerageAssistanceMockMvc.perform(get("/api/brokerage-assistances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerageAssistance() throws Exception {
        // Initialize the database
        brokerageAssistanceRepository.saveAndFlush(brokerageAssistance);

        int databaseSizeBeforeUpdate = brokerageAssistanceRepository.findAll().size();

        // Update the brokerageAssistance
        BrokerageAssistance updatedBrokerageAssistance = brokerageAssistanceRepository.findById(brokerageAssistance.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerageAssistance are not directly saved in db
        em.detach(updatedBrokerageAssistance);
        updatedBrokerageAssistance
            .name(UPDATED_NAME);
        BrokerageAssistanceDTO brokerageAssistanceDTO = brokerageAssistanceMapper.toDto(updatedBrokerageAssistance);

        restBrokerageAssistanceMockMvc.perform(put("/api/brokerage-assistances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAssistanceDTO)))
            .andExpect(status().isOk());

        // Validate the BrokerageAssistance in the database
        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeUpdate);
        BrokerageAssistance testBrokerageAssistance = brokerageAssistanceList.get(brokerageAssistanceList.size() - 1);
        assertThat(testBrokerageAssistance.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerageAssistance() throws Exception {
        int databaseSizeBeforeUpdate = brokerageAssistanceRepository.findAll().size();

        // Create the BrokerageAssistance
        BrokerageAssistanceDTO brokerageAssistanceDTO = brokerageAssistanceMapper.toDto(brokerageAssistance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageAssistanceMockMvc.perform(put("/api/brokerage-assistances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAssistanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageAssistance in the database
        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerageAssistance() throws Exception {
        // Initialize the database
        brokerageAssistanceRepository.saveAndFlush(brokerageAssistance);

        int databaseSizeBeforeDelete = brokerageAssistanceRepository.findAll().size();

        // Get the brokerageAssistance
        restBrokerageAssistanceMockMvc.perform(delete("/api/brokerage-assistances/{id}", brokerageAssistance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerageAssistance> brokerageAssistanceList = brokerageAssistanceRepository.findAll();
        assertThat(brokerageAssistanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageAssistance.class);
        BrokerageAssistance brokerageAssistance1 = new BrokerageAssistance();
        brokerageAssistance1.setId(1L);
        BrokerageAssistance brokerageAssistance2 = new BrokerageAssistance();
        brokerageAssistance2.setId(brokerageAssistance1.getId());
        assertThat(brokerageAssistance1).isEqualTo(brokerageAssistance2);
        brokerageAssistance2.setId(2L);
        assertThat(brokerageAssistance1).isNotEqualTo(brokerageAssistance2);
        brokerageAssistance1.setId(null);
        assertThat(brokerageAssistance1).isNotEqualTo(brokerageAssistance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageAssistanceDTO.class);
        BrokerageAssistanceDTO brokerageAssistanceDTO1 = new BrokerageAssistanceDTO();
        brokerageAssistanceDTO1.setId(1L);
        BrokerageAssistanceDTO brokerageAssistanceDTO2 = new BrokerageAssistanceDTO();
        assertThat(brokerageAssistanceDTO1).isNotEqualTo(brokerageAssistanceDTO2);
        brokerageAssistanceDTO2.setId(brokerageAssistanceDTO1.getId());
        assertThat(brokerageAssistanceDTO1).isEqualTo(brokerageAssistanceDTO2);
        brokerageAssistanceDTO2.setId(2L);
        assertThat(brokerageAssistanceDTO1).isNotEqualTo(brokerageAssistanceDTO2);
        brokerageAssistanceDTO1.setId(null);
        assertThat(brokerageAssistanceDTO1).isNotEqualTo(brokerageAssistanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageAssistanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageAssistanceMapper.fromId(null)).isNull();
    }
}
