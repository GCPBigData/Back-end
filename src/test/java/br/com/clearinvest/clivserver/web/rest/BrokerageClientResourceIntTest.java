package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.BrokerageClient;
import br.com.clearinvest.clivserver.repository.BrokerageClientRepository;
import br.com.clearinvest.clivserver.service.BrokerageClientService;
import br.com.clearinvest.clivserver.service.dto.BrokerageClientDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageClientMapper;
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
 * Test class for the BrokerageClientResource REST controller.
 *
 * @see BrokerageClientResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageClientResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BrokerageClientRepository brokerageClientRepository;

    @Autowired
    private BrokerageClientMapper brokerageClientMapper;

    @Autowired
    private BrokerageClientService brokerageClientService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageClientMockMvc;

    private BrokerageClient brokerageClient;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageClientResource brokerageClientResource = new BrokerageClientResource(brokerageClientService);
        this.restBrokerageClientMockMvc = MockMvcBuilders.standaloneSetup(brokerageClientResource)
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
    public static BrokerageClient createEntity(EntityManager em) {
        BrokerageClient brokerageClient = new BrokerageClient()
            .name(DEFAULT_NAME);
        return brokerageClient;
    }

    @Before
    public void initTest() {
        brokerageClient = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerageClient() throws Exception {
        int databaseSizeBeforeCreate = brokerageClientRepository.findAll().size();

        // Create the BrokerageClient
        BrokerageClientDTO brokerageClientDTO = brokerageClientMapper.toDto(brokerageClient);
        restBrokerageClientMockMvc.perform(post("/api/brokerage-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageClientDTO)))
            .andExpect(status().isCreated());

        // Validate the BrokerageClient in the database
        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeCreate + 1);
        BrokerageClient testBrokerageClient = brokerageClientList.get(brokerageClientList.size() - 1);
        assertThat(testBrokerageClient.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBrokerageClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageClientRepository.findAll().size();

        // Create the BrokerageClient with an existing ID
        brokerageClient.setId(1L);
        BrokerageClientDTO brokerageClientDTO = brokerageClientMapper.toDto(brokerageClient);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageClientMockMvc.perform(post("/api/brokerage-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageClient in the database
        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageClientRepository.findAll().size();
        // set the field null
        brokerageClient.setName(null);

        // Create the BrokerageClient, which fails.
        BrokerageClientDTO brokerageClientDTO = brokerageClientMapper.toDto(brokerageClient);

        restBrokerageClientMockMvc.perform(post("/api/brokerage-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageClientDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerageClients() throws Exception {
        // Initialize the database
        brokerageClientRepository.saveAndFlush(brokerageClient);

        // Get all the brokerageClientList
        restBrokerageClientMockMvc.perform(get("/api/brokerage-clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageClient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBrokerageClient() throws Exception {
        // Initialize the database
        brokerageClientRepository.saveAndFlush(brokerageClient);

        // Get the brokerageClient
        restBrokerageClientMockMvc.perform(get("/api/brokerage-clients/{id}", brokerageClient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerageClient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerageClient() throws Exception {
        // Get the brokerageClient
        restBrokerageClientMockMvc.perform(get("/api/brokerage-clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerageClient() throws Exception {
        // Initialize the database
        brokerageClientRepository.saveAndFlush(brokerageClient);

        int databaseSizeBeforeUpdate = brokerageClientRepository.findAll().size();

        // Update the brokerageClient
        BrokerageClient updatedBrokerageClient = brokerageClientRepository.findById(brokerageClient.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerageClient are not directly saved in db
        em.detach(updatedBrokerageClient);
        updatedBrokerageClient
            .name(UPDATED_NAME);
        BrokerageClientDTO brokerageClientDTO = brokerageClientMapper.toDto(updatedBrokerageClient);

        restBrokerageClientMockMvc.perform(put("/api/brokerage-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageClientDTO)))
            .andExpect(status().isOk());

        // Validate the BrokerageClient in the database
        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeUpdate);
        BrokerageClient testBrokerageClient = brokerageClientList.get(brokerageClientList.size() - 1);
        assertThat(testBrokerageClient.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerageClient() throws Exception {
        int databaseSizeBeforeUpdate = brokerageClientRepository.findAll().size();

        // Create the BrokerageClient
        BrokerageClientDTO brokerageClientDTO = brokerageClientMapper.toDto(brokerageClient);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageClientMockMvc.perform(put("/api/brokerage-clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageClientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageClient in the database
        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerageClient() throws Exception {
        // Initialize the database
        brokerageClientRepository.saveAndFlush(brokerageClient);

        int databaseSizeBeforeDelete = brokerageClientRepository.findAll().size();

        // Get the brokerageClient
        restBrokerageClientMockMvc.perform(delete("/api/brokerage-clients/{id}", brokerageClient.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerageClient> brokerageClientList = brokerageClientRepository.findAll();
        assertThat(brokerageClientList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageClient.class);
        BrokerageClient brokerageClient1 = new BrokerageClient();
        brokerageClient1.setId(1L);
        BrokerageClient brokerageClient2 = new BrokerageClient();
        brokerageClient2.setId(brokerageClient1.getId());
        assertThat(brokerageClient1).isEqualTo(brokerageClient2);
        brokerageClient2.setId(2L);
        assertThat(brokerageClient1).isNotEqualTo(brokerageClient2);
        brokerageClient1.setId(null);
        assertThat(brokerageClient1).isNotEqualTo(brokerageClient2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageClientDTO.class);
        BrokerageClientDTO brokerageClientDTO1 = new BrokerageClientDTO();
        brokerageClientDTO1.setId(1L);
        BrokerageClientDTO brokerageClientDTO2 = new BrokerageClientDTO();
        assertThat(brokerageClientDTO1).isNotEqualTo(brokerageClientDTO2);
        brokerageClientDTO2.setId(brokerageClientDTO1.getId());
        assertThat(brokerageClientDTO1).isEqualTo(brokerageClientDTO2);
        brokerageClientDTO2.setId(2L);
        assertThat(brokerageClientDTO1).isNotEqualTo(brokerageClientDTO2);
        brokerageClientDTO1.setId(null);
        assertThat(brokerageClientDTO1).isNotEqualTo(brokerageClientDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageClientMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageClientMapper.fromId(null)).isNull();
    }
}
