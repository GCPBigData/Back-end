package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockWatch;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.StockWatchRepository;
import br.com.clearinvest.clivserver.service.StockWatchService;
import br.com.clearinvest.clivserver.service.dto.StockWatchDTO;
import br.com.clearinvest.clivserver.service.mapper.StockWatchMapper;
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
 * Test class for the StockWatchResource REST controller.
 *
 * @see StockWatchResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class StockWatchResourceIntTest {

    @Autowired
    private StockWatchRepository stockWatchRepository;

    @Autowired
    private StockWatchMapper stockWatchMapper;

    @Autowired
    private StockWatchService stockWatchService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockWatchMockMvc;

    private StockWatch stockWatch;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockWatchResource stockWatchResource = new StockWatchResource(stockWatchService);
        this.restStockWatchMockMvc = MockMvcBuilders.standaloneSetup(stockWatchResource)
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
    public static StockWatch createEntity(EntityManager em) {
        StockWatch stockWatch = new StockWatch();
        // Add required entity
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        stockWatch.setStock(stock);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockWatch.setUser(user);
        return stockWatch;
    }

    @Before
    public void initTest() {
        stockWatch = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockWatch() throws Exception {
        int databaseSizeBeforeCreate = stockWatchRepository.findAll().size();

        // Create the StockWatch
        StockWatchDTO stockWatchDTO = stockWatchMapper.toDto(stockWatch);
        restStockWatchMockMvc.perform(post("/api/stock-watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockWatchDTO)))
            .andExpect(status().isCreated());

        // Validate the StockWatch in the database
        List<StockWatch> stockWatchList = stockWatchRepository.findAll();
        assertThat(stockWatchList).hasSize(databaseSizeBeforeCreate + 1);
        StockWatch testStockWatch = stockWatchList.get(stockWatchList.size() - 1);
    }

    @Test
    @Transactional
    public void createStockWatchWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockWatchRepository.findAll().size();

        // Create the StockWatch with an existing ID
        stockWatch.setId(1L);
        StockWatchDTO stockWatchDTO = stockWatchMapper.toDto(stockWatch);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockWatchMockMvc.perform(post("/api/stock-watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockWatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockWatch in the database
        List<StockWatch> stockWatchList = stockWatchRepository.findAll();
        assertThat(stockWatchList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStockWatches() throws Exception {
        // Initialize the database
        stockWatchRepository.saveAndFlush(stockWatch);

        // Get all the stockWatchList
        restStockWatchMockMvc.perform(get("/api/stock-watches?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockWatch.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getStockWatch() throws Exception {
        // Initialize the database
        stockWatchRepository.saveAndFlush(stockWatch);

        // Get the stockWatch
        restStockWatchMockMvc.perform(get("/api/stock-watches/{id}", stockWatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockWatch.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockWatch() throws Exception {
        // Get the stockWatch
        restStockWatchMockMvc.perform(get("/api/stock-watches/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockWatch() throws Exception {
        // Initialize the database
        stockWatchRepository.saveAndFlush(stockWatch);

        int databaseSizeBeforeUpdate = stockWatchRepository.findAll().size();

        // Update the stockWatch
        StockWatch updatedStockWatch = stockWatchRepository.findById(stockWatch.getId()).get();
        // Disconnect from session so that the updates on updatedStockWatch are not directly saved in db
        em.detach(updatedStockWatch);
        StockWatchDTO stockWatchDTO = stockWatchMapper.toDto(updatedStockWatch);

        restStockWatchMockMvc.perform(put("/api/stock-watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockWatchDTO)))
            .andExpect(status().isOk());

        // Validate the StockWatch in the database
        List<StockWatch> stockWatchList = stockWatchRepository.findAll();
        assertThat(stockWatchList).hasSize(databaseSizeBeforeUpdate);
        StockWatch testStockWatch = stockWatchList.get(stockWatchList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingStockWatch() throws Exception {
        int databaseSizeBeforeUpdate = stockWatchRepository.findAll().size();

        // Create the StockWatch
        StockWatchDTO stockWatchDTO = stockWatchMapper.toDto(stockWatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockWatchMockMvc.perform(put("/api/stock-watches")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockWatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockWatch in the database
        List<StockWatch> stockWatchList = stockWatchRepository.findAll();
        assertThat(stockWatchList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockWatch() throws Exception {
        // Initialize the database
        stockWatchRepository.saveAndFlush(stockWatch);

        int databaseSizeBeforeDelete = stockWatchRepository.findAll().size();

        // Get the stockWatch
        restStockWatchMockMvc.perform(delete("/api/stock-watches/{id}", stockWatch.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockWatch> stockWatchList = stockWatchRepository.findAll();
        assertThat(stockWatchList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockWatch.class);
        StockWatch stockWatch1 = new StockWatch();
        stockWatch1.setId(1L);
        StockWatch stockWatch2 = new StockWatch();
        stockWatch2.setId(stockWatch1.getId());
        assertThat(stockWatch1).isEqualTo(stockWatch2);
        stockWatch2.setId(2L);
        assertThat(stockWatch1).isNotEqualTo(stockWatch2);
        stockWatch1.setId(null);
        assertThat(stockWatch1).isNotEqualTo(stockWatch2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockWatchDTO.class);
        StockWatchDTO stockWatchDTO1 = new StockWatchDTO();
        stockWatchDTO1.setId(1L);
        StockWatchDTO stockWatchDTO2 = new StockWatchDTO();
        assertThat(stockWatchDTO1).isNotEqualTo(stockWatchDTO2);
        stockWatchDTO2.setId(stockWatchDTO1.getId());
        assertThat(stockWatchDTO1).isEqualTo(stockWatchDTO2);
        stockWatchDTO2.setId(2L);
        assertThat(stockWatchDTO1).isNotEqualTo(stockWatchDTO2);
        stockWatchDTO1.setId(null);
        assertThat(stockWatchDTO1).isNotEqualTo(stockWatchDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockWatchMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockWatchMapper.fromId(null)).isNull();
    }
}
