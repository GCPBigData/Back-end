package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockFlow;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.StockFlowRepository;
import br.com.clearinvest.clivserver.service.StockFlowService;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.StockFlowMapper;
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
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static br.com.clearinvest.clivserver.web.rest.TestUtil.sameInstant;
import static br.com.clearinvest.clivserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StockFlowResource REST controller.
 *
 * @see StockFlowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class StockFlowResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SYMBOL = "AAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_TOTAL_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_VALUE = new BigDecimal(2);

    @Autowired
    private StockFlowRepository stockFlowRepository;

    @Autowired
    private StockFlowMapper stockFlowMapper;

    @Autowired
    private StockFlowService stockFlowService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockFlowMockMvc;

    private StockFlow stockFlow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockFlowResource stockFlowResource = new StockFlowResource(stockFlowService);
        this.restStockFlowMockMvc = MockMvcBuilders.standaloneSetup(stockFlowResource)
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
    public static StockFlow createEntity(EntityManager em) {
        StockFlow stockFlow = new StockFlow()
            .createdAt(DEFAULT_CREATED_AT)
            .symbol(DEFAULT_SYMBOL)
            .quantity(DEFAULT_QUANTITY)
            .totalValue(DEFAULT_TOTAL_VALUE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockFlow.setUser(user);
        return stockFlow;
    }

    @Before
    public void initTest() {
        stockFlow = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockFlow() throws Exception {
        int databaseSizeBeforeCreate = stockFlowRepository.findAll().size();

        // Create the StockFlow
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);
        restStockFlowMockMvc.perform(post("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isCreated());

        // Validate the StockFlow in the database
        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeCreate + 1);
        StockFlow testStockFlow = stockFlowList.get(stockFlowList.size() - 1);
        assertThat(testStockFlow.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStockFlow.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testStockFlow.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockFlow.getTotalValue()).isEqualTo(DEFAULT_TOTAL_VALUE);
    }

    @Test
    @Transactional
    public void createStockFlowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockFlowRepository.findAll().size();

        // Create the StockFlow with an existing ID
        stockFlow.setId(1L);
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockFlowMockMvc.perform(post("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockFlow in the database
        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setSymbol(null);

        // Create the StockFlow, which fails.
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);

        restStockFlowMockMvc.perform(post("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isBadRequest());

        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setQuantity(null);

        // Create the StockFlow, which fails.
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);

        restStockFlowMockMvc.perform(post("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isBadRequest());

        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTotalValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setTotalValue(null);

        // Create the StockFlow, which fails.
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);

        restStockFlowMockMvc.perform(post("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isBadRequest());

        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockFlows() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList
        restStockFlowMockMvc.perform(get("/api/stock-flows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].totalValue").value(hasItem(DEFAULT_TOTAL_VALUE.intValue())));
    }
    
    @Test
    @Transactional
    public void getStockFlow() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get the stockFlow
        restStockFlowMockMvc.perform(get("/api/stock-flows/{id}", stockFlow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockFlow.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.totalValue").value(DEFAULT_TOTAL_VALUE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockFlow() throws Exception {
        // Get the stockFlow
        restStockFlowMockMvc.perform(get("/api/stock-flows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockFlow() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        int databaseSizeBeforeUpdate = stockFlowRepository.findAll().size();

        // Update the stockFlow
        StockFlow updatedStockFlow = stockFlowRepository.findById(stockFlow.getId()).get();
        // Disconnect from session so that the updates on updatedStockFlow are not directly saved in db
        em.detach(updatedStockFlow);
        updatedStockFlow
            .createdAt(UPDATED_CREATED_AT)
            .symbol(UPDATED_SYMBOL)
            .quantity(UPDATED_QUANTITY)
            .totalValue(UPDATED_TOTAL_VALUE);
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(updatedStockFlow);

        restStockFlowMockMvc.perform(put("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isOk());

        // Validate the StockFlow in the database
        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeUpdate);
        StockFlow testStockFlow = stockFlowList.get(stockFlowList.size() - 1);
        assertThat(testStockFlow.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStockFlow.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testStockFlow.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockFlow.getTotalValue()).isEqualTo(UPDATED_TOTAL_VALUE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockFlow() throws Exception {
        int databaseSizeBeforeUpdate = stockFlowRepository.findAll().size();

        // Create the StockFlow
        StockFlowDTO stockFlowDTO = stockFlowMapper.toDto(stockFlow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockFlowMockMvc.perform(put("/api/stock-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockFlowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockFlow in the database
        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockFlow() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        int databaseSizeBeforeDelete = stockFlowRepository.findAll().size();

        // Get the stockFlow
        restStockFlowMockMvc.perform(delete("/api/stock-flows/{id}", stockFlow.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockFlow> stockFlowList = stockFlowRepository.findAll();
        assertThat(stockFlowList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockFlow.class);
        StockFlow stockFlow1 = new StockFlow();
        stockFlow1.setId(1L);
        StockFlow stockFlow2 = new StockFlow();
        stockFlow2.setId(stockFlow1.getId());
        assertThat(stockFlow1).isEqualTo(stockFlow2);
        stockFlow2.setId(2L);
        assertThat(stockFlow1).isNotEqualTo(stockFlow2);
        stockFlow1.setId(null);
        assertThat(stockFlow1).isNotEqualTo(stockFlow2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockFlowDTO.class);
        StockFlowDTO stockFlowDTO1 = new StockFlowDTO();
        stockFlowDTO1.setId(1L);
        StockFlowDTO stockFlowDTO2 = new StockFlowDTO();
        assertThat(stockFlowDTO1).isNotEqualTo(stockFlowDTO2);
        stockFlowDTO2.setId(stockFlowDTO1.getId());
        assertThat(stockFlowDTO1).isEqualTo(stockFlowDTO2);
        stockFlowDTO2.setId(2L);
        assertThat(stockFlowDTO1).isNotEqualTo(stockFlowDTO2);
        stockFlowDTO1.setId(null);
        assertThat(stockFlowDTO1).isNotEqualTo(stockFlowDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockFlowMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockFlowMapper.fromId(null)).isNull();
    }
}
