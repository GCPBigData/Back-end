package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.StockTradeRepository;
import br.com.clearinvest.clivserver.service.StockTradeService;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.mapper.StockTradeMapper;
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
 * Test class for the StockTradeResource REST controller.
 *
 * @see StockTradeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class StockTradeResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_EXEC_REPORT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_EXEC_REPORT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_EXEC_REPORT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_LAST_EXEC_REPORT_DESCR = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_IP = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_IP = "BBBBBBBBBB";

    private static final String DEFAULT_SIDE = "A";
    private static final String UPDATED_SIDE = "B";

    private static final ZonedDateTime DEFAULT_EXPIRE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Long DEFAULT_EXEC_QUANTITY = 1L;
    private static final Long UPDATED_EXEC_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0.01);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AVERAGE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_STOCK_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_STOCK_TOTAL_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AA";
    private static final String UPDATED_TYPE = "BB";

    @Autowired
    private StockTradeRepository stockTradeRepository;

    @Autowired
    private StockTradeMapper stockTradeMapper;

    @Autowired
    private StockTradeService stockTradeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockTradeMockMvc;

    private StockTrade stockTrade;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockTradeResource stockTradeResource = new StockTradeResource(stockTradeService);
        this.restStockTradeMockMvc = MockMvcBuilders.standaloneSetup(stockTradeResource)
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
    public static StockTrade createEntity(EntityManager em) {
        StockTrade stockTrade = new StockTrade()
            .createdAt(DEFAULT_CREATED_AT)
            .lastExecReportTime(DEFAULT_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(DEFAULT_LAST_EXEC_REPORT_DESCR)
            .createdByIp(DEFAULT_CREATED_BY_IP)
            .side(DEFAULT_SIDE)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .quantity(DEFAULT_QUANTITY)
            .execQuantity(DEFAULT_EXEC_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .averagePrice(DEFAULT_AVERAGE_PRICE)
            .stockTotalPrice(DEFAULT_STOCK_TOTAL_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .status(DEFAULT_STATUS)
            .type(DEFAULT_TYPE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockTrade.setCreatedBy(user);
        return stockTrade;
    }

    @Before
    public void initTest() {
        stockTrade = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockTrade() throws Exception {
        int databaseSizeBeforeCreate = stockTradeRepository.findAll().size();

        // Create the StockTrade
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);
        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isCreated());

        // Validate the StockTrade in the database
        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeCreate + 1);
        StockTrade testStockTrade = stockTradeList.get(stockTradeList.size() - 1);
        assertThat(testStockTrade.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStockTrade.getLastExecReportTime()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_TIME);
        assertThat(testStockTrade.getLastExecReportDescr()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_DESCR);
        assertThat(testStockTrade.getCreatedByIp()).isEqualTo(DEFAULT_CREATED_BY_IP);
        assertThat(testStockTrade.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testStockTrade.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testStockTrade.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockTrade.getExecQuantity()).isEqualTo(DEFAULT_EXEC_QUANTITY);
        assertThat(testStockTrade.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testStockTrade.getAveragePrice()).isEqualTo(DEFAULT_AVERAGE_PRICE);
        assertThat(testStockTrade.getStockTotalPrice()).isEqualTo(DEFAULT_STOCK_TOTAL_PRICE);
        assertThat(testStockTrade.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testStockTrade.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockTrade.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void createStockTradeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockTradeRepository.findAll().size();

        // Create the StockTrade with an existing ID
        stockTrade.setId(1L);
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockTrade in the database
        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setSide(null);

        // Create the StockTrade, which fails.
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setQuantity(null);

        // Create the StockTrade, which fails.
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setUnitPrice(null);

        // Create the StockTrade, which fails.
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setStatus(null);

        // Create the StockTrade, which fails.
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setType(null);

        // Create the StockTrade, which fails.
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        restStockTradeMockMvc.perform(post("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockTrades() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList
        restStockTradeMockMvc.perform(get("/api/stock-trades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].lastExecReportTime").value(hasItem(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME))))
            .andExpect(jsonPath("$.[*].lastExecReportDescr").value(hasItem(DEFAULT_LAST_EXEC_REPORT_DESCR.toString())))
            .andExpect(jsonPath("$.[*].createdByIp").value(hasItem(DEFAULT_CREATED_BY_IP.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(sameInstant(DEFAULT_EXPIRE_TIME))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].execQuantity").value(hasItem(DEFAULT_EXEC_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].averagePrice").value(hasItem(DEFAULT_AVERAGE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].stockTotalPrice").value(hasItem(DEFAULT_STOCK_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get the stockTrade
        restStockTradeMockMvc.perform(get("/api/stock-trades/{id}", stockTrade.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockTrade.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.lastExecReportTime").value(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME)))
            .andExpect(jsonPath("$.lastExecReportDescr").value(DEFAULT_LAST_EXEC_REPORT_DESCR.toString()))
            .andExpect(jsonPath("$.createdByIp").value(DEFAULT_CREATED_BY_IP.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.expireTime").value(sameInstant(DEFAULT_EXPIRE_TIME)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.execQuantity").value(DEFAULT_EXEC_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.averagePrice").value(DEFAULT_AVERAGE_PRICE.intValue()))
            .andExpect(jsonPath("$.stockTotalPrice").value(DEFAULT_STOCK_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockTrade() throws Exception {
        // Get the stockTrade
        restStockTradeMockMvc.perform(get("/api/stock-trades/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        int databaseSizeBeforeUpdate = stockTradeRepository.findAll().size();

        // Update the stockTrade
        StockTrade updatedStockTrade = stockTradeRepository.findById(stockTrade.getId()).get();
        // Disconnect from session so that the updates on updatedStockTrade are not directly saved in db
        em.detach(updatedStockTrade);
        updatedStockTrade
            .createdAt(UPDATED_CREATED_AT)
            .lastExecReportTime(UPDATED_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(UPDATED_LAST_EXEC_REPORT_DESCR)
            .createdByIp(UPDATED_CREATED_BY_IP)
            .side(UPDATED_SIDE)
            .expireTime(UPDATED_EXPIRE_TIME)
            .quantity(UPDATED_QUANTITY)
            .execQuantity(UPDATED_EXEC_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .averagePrice(UPDATED_AVERAGE_PRICE)
            .stockTotalPrice(UPDATED_STOCK_TOTAL_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .status(UPDATED_STATUS)
            .type(UPDATED_TYPE);
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(updatedStockTrade);

        restStockTradeMockMvc.perform(put("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isOk());

        // Validate the StockTrade in the database
        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeUpdate);
        StockTrade testStockTrade = stockTradeList.get(stockTradeList.size() - 1);
        assertThat(testStockTrade.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStockTrade.getLastExecReportTime()).isEqualTo(UPDATED_LAST_EXEC_REPORT_TIME);
        assertThat(testStockTrade.getLastExecReportDescr()).isEqualTo(UPDATED_LAST_EXEC_REPORT_DESCR);
        assertThat(testStockTrade.getCreatedByIp()).isEqualTo(UPDATED_CREATED_BY_IP);
        assertThat(testStockTrade.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testStockTrade.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testStockTrade.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockTrade.getExecQuantity()).isEqualTo(UPDATED_EXEC_QUANTITY);
        assertThat(testStockTrade.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testStockTrade.getAveragePrice()).isEqualTo(UPDATED_AVERAGE_PRICE);
        assertThat(testStockTrade.getStockTotalPrice()).isEqualTo(UPDATED_STOCK_TOTAL_PRICE);
        assertThat(testStockTrade.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testStockTrade.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockTrade.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockTrade() throws Exception {
        int databaseSizeBeforeUpdate = stockTradeRepository.findAll().size();

        // Create the StockTrade
        StockTradeDTO stockTradeDTO = stockTradeMapper.toDto(stockTrade);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockTradeMockMvc.perform(put("/api/stock-trades")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockTradeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockTrade in the database
        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockTrade() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        int databaseSizeBeforeDelete = stockTradeRepository.findAll().size();

        // Get the stockTrade
        restStockTradeMockMvc.perform(delete("/api/stock-trades/{id}", stockTrade.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockTrade> stockTradeList = stockTradeRepository.findAll();
        assertThat(stockTradeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockTrade.class);
        StockTrade stockTrade1 = new StockTrade();
        stockTrade1.setId(1L);
        StockTrade stockTrade2 = new StockTrade();
        stockTrade2.setId(stockTrade1.getId());
        assertThat(stockTrade1).isEqualTo(stockTrade2);
        stockTrade2.setId(2L);
        assertThat(stockTrade1).isNotEqualTo(stockTrade2);
        stockTrade1.setId(null);
        assertThat(stockTrade1).isNotEqualTo(stockTrade2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockTradeDTO.class);
        StockTradeDTO stockTradeDTO1 = new StockTradeDTO();
        stockTradeDTO1.setId(1L);
        StockTradeDTO stockTradeDTO2 = new StockTradeDTO();
        assertThat(stockTradeDTO1).isNotEqualTo(stockTradeDTO2);
        stockTradeDTO2.setId(stockTradeDTO1.getId());
        assertThat(stockTradeDTO1).isEqualTo(stockTradeDTO2);
        stockTradeDTO2.setId(2L);
        assertThat(stockTradeDTO1).isNotEqualTo(stockTradeDTO2);
        stockTradeDTO1.setId(null);
        assertThat(stockTradeDTO1).isNotEqualTo(stockTradeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockTradeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockTradeMapper.fromId(null)).isNull();
    }
}
