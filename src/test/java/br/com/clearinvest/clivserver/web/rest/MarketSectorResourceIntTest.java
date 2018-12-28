package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.MarketSector;
import br.com.clearinvest.clivserver.repository.MarketSectorRepository;
import br.com.clearinvest.clivserver.service.MarketSectorService;
import br.com.clearinvest.clivserver.service.dto.MarketSectorDTO;
import br.com.clearinvest.clivserver.service.mapper.MarketSectorMapper;
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
 * Test class for the MarketSectorResource REST controller.
 *
 * @see MarketSectorResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class MarketSectorResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private MarketSectorRepository marketSectorRepository;

    @Autowired
    private MarketSectorMapper marketSectorMapper;

    @Autowired
    private MarketSectorService marketSectorService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMarketSectorMockMvc;

    private MarketSector marketSector;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MarketSectorResource marketSectorResource = new MarketSectorResource(marketSectorService);
        this.restMarketSectorMockMvc = MockMvcBuilders.standaloneSetup(marketSectorResource)
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
    public static MarketSector createEntity(EntityManager em) {
        MarketSector marketSector = new MarketSector()
            .name(DEFAULT_NAME);
        return marketSector;
    }

    @Before
    public void initTest() {
        marketSector = createEntity(em);
    }

    @Test
    @Transactional
    public void createMarketSector() throws Exception {
        int databaseSizeBeforeCreate = marketSectorRepository.findAll().size();

        // Create the MarketSector
        MarketSectorDTO marketSectorDTO = marketSectorMapper.toDto(marketSector);
        restMarketSectorMockMvc.perform(post("/api/market-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketSectorDTO)))
            .andExpect(status().isCreated());

        // Validate the MarketSector in the database
        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeCreate + 1);
        MarketSector testMarketSector = marketSectorList.get(marketSectorList.size() - 1);
        assertThat(testMarketSector.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createMarketSectorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = marketSectorRepository.findAll().size();

        // Create the MarketSector with an existing ID
        marketSector.setId(1L);
        MarketSectorDTO marketSectorDTO = marketSectorMapper.toDto(marketSector);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMarketSectorMockMvc.perform(post("/api/market-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketSectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = marketSectorRepository.findAll().size();
        // set the field null
        marketSector.setName(null);

        // Create the MarketSector, which fails.
        MarketSectorDTO marketSectorDTO = marketSectorMapper.toDto(marketSector);

        restMarketSectorMockMvc.perform(post("/api/market-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketSectorDTO)))
            .andExpect(status().isBadRequest());

        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMarketSectors() throws Exception {
        // Initialize the database
        marketSectorRepository.saveAndFlush(marketSector);

        // Get all the marketSectorList
        restMarketSectorMockMvc.perform(get("/api/market-sectors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(marketSector.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getMarketSector() throws Exception {
        // Initialize the database
        marketSectorRepository.saveAndFlush(marketSector);

        // Get the marketSector
        restMarketSectorMockMvc.perform(get("/api/market-sectors/{id}", marketSector.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(marketSector.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMarketSector() throws Exception {
        // Get the marketSector
        restMarketSectorMockMvc.perform(get("/api/market-sectors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMarketSector() throws Exception {
        // Initialize the database
        marketSectorRepository.saveAndFlush(marketSector);

        int databaseSizeBeforeUpdate = marketSectorRepository.findAll().size();

        // Update the marketSector
        MarketSector updatedMarketSector = marketSectorRepository.findById(marketSector.getId()).get();
        // Disconnect from session so that the updates on updatedMarketSector are not directly saved in db
        em.detach(updatedMarketSector);
        updatedMarketSector
            .name(UPDATED_NAME);
        MarketSectorDTO marketSectorDTO = marketSectorMapper.toDto(updatedMarketSector);

        restMarketSectorMockMvc.perform(put("/api/market-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketSectorDTO)))
            .andExpect(status().isOk());

        // Validate the MarketSector in the database
        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeUpdate);
        MarketSector testMarketSector = marketSectorList.get(marketSectorList.size() - 1);
        assertThat(testMarketSector.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingMarketSector() throws Exception {
        int databaseSizeBeforeUpdate = marketSectorRepository.findAll().size();

        // Create the MarketSector
        MarketSectorDTO marketSectorDTO = marketSectorMapper.toDto(marketSector);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMarketSectorMockMvc.perform(put("/api/market-sectors")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(marketSectorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MarketSector in the database
        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMarketSector() throws Exception {
        // Initialize the database
        marketSectorRepository.saveAndFlush(marketSector);

        int databaseSizeBeforeDelete = marketSectorRepository.findAll().size();

        // Get the marketSector
        restMarketSectorMockMvc.perform(delete("/api/market-sectors/{id}", marketSector.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<MarketSector> marketSectorList = marketSectorRepository.findAll();
        assertThat(marketSectorList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketSector.class);
        MarketSector marketSector1 = new MarketSector();
        marketSector1.setId(1L);
        MarketSector marketSector2 = new MarketSector();
        marketSector2.setId(marketSector1.getId());
        assertThat(marketSector1).isEqualTo(marketSector2);
        marketSector2.setId(2L);
        assertThat(marketSector1).isNotEqualTo(marketSector2);
        marketSector1.setId(null);
        assertThat(marketSector1).isNotEqualTo(marketSector2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MarketSectorDTO.class);
        MarketSectorDTO marketSectorDTO1 = new MarketSectorDTO();
        marketSectorDTO1.setId(1L);
        MarketSectorDTO marketSectorDTO2 = new MarketSectorDTO();
        assertThat(marketSectorDTO1).isNotEqualTo(marketSectorDTO2);
        marketSectorDTO2.setId(marketSectorDTO1.getId());
        assertThat(marketSectorDTO1).isEqualTo(marketSectorDTO2);
        marketSectorDTO2.setId(2L);
        assertThat(marketSectorDTO1).isNotEqualTo(marketSectorDTO2);
        marketSectorDTO1.setId(null);
        assertThat(marketSectorDTO1).isNotEqualTo(marketSectorDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(marketSectorMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(marketSectorMapper.fromId(null)).isNull();
    }
}
