package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.repository.StockTradeRepository;
import br.com.clearinvest.clivserver.service.StockTradeService;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;
import br.com.clearinvest.clivserver.service.mapper.StockTradeMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;
import br.com.clearinvest.clivserver.service.dto.StockTradeCriteria;
import br.com.clearinvest.clivserver.service.StockTradeQueryService;

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

    private static final String DEFAULT_CREATED_BY_IP = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_IP = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TRADE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRADE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_KIND = "AAAAAAAAAA";
    private static final String UPDATED_KIND = "BBBBBBBBBB";

    private static final String DEFAULT_MARKET = "A";
    private static final String UPDATED_MARKET = "B";

    private static final String DEFAULT_SIDE = "A";
    private static final String UPDATED_SIDE = "B";

    private static final Boolean DEFAULT_MANUAL_ENTRY = false;
    private static final Boolean UPDATED_MANUAL_ENTRY = true;

    private static final ZonedDateTime DEFAULT_EXPIRE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Long DEFAULT_EXEC_QUANTITY = 1L;
    private static final Long UPDATED_EXEC_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0.01);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_STOP_PRICE = new BigDecimal(0.01);
    private static final BigDecimal UPDATED_STOP_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AVERAGE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PRICE_ACTUAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE_ACTUAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BROKERAGE_FEE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BROKERAGE_FEE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_BROKERAGE_FEE_ISS = new BigDecimal(1);
    private static final BigDecimal UPDATED_BROKERAGE_FEE_ISS = new BigDecimal(2);

    private static final BigDecimal DEFAULT_NEGOTIATION_PERC = new BigDecimal(1);
    private static final BigDecimal UPDATED_NEGOTIATION_PERC = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LIQUIDATION_PERC = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIQUIDATION_PERC = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REGISTRY_PERC = new BigDecimal(1);
    private static final BigDecimal UPDATED_REGISTRY_PERC = new BigDecimal(2);

    private static final ZonedDateTime DEFAULT_LAST_EXEC_REPORT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_EXEC_REPORT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_EXEC_REPORT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_LAST_EXEC_REPORT_DESCR = "BBBBBBBBBB";

    @Autowired
    private StockTradeRepository stockTradeRepository;

    @Autowired
    private StockTradeMapper stockTradeMapper;

    @Autowired
    private StockTradeService stockTradeService;

    @Autowired
    private StockTradeQueryService stockTradeQueryService;

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
        final StockTradeResource stockTradeResource = new StockTradeResource(stockTradeService, stockTradeQueryService);
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
            .createdByIp(DEFAULT_CREATED_BY_IP)
            .status(DEFAULT_STATUS)
            .tradeDate(DEFAULT_TRADE_DATE)
            .kind(DEFAULT_KIND)
            .market(DEFAULT_MARKET)
            .side(DEFAULT_SIDE)
            .manualEntry(DEFAULT_MANUAL_ENTRY)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .quantity(DEFAULT_QUANTITY)
            .execQuantity(DEFAULT_EXEC_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .stopPrice(DEFAULT_STOP_PRICE)
            .averagePrice(DEFAULT_AVERAGE_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .totalPriceActual(DEFAULT_TOTAL_PRICE_ACTUAL)
            .brokerageFee(DEFAULT_BROKERAGE_FEE)
            .brokerageFeeIss(DEFAULT_BROKERAGE_FEE_ISS)
            .negotiationPerc(DEFAULT_NEGOTIATION_PERC)
            .liquidationPerc(DEFAULT_LIQUIDATION_PERC)
            .registryPerc(DEFAULT_REGISTRY_PERC)
            .lastExecReportTime(DEFAULT_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(DEFAULT_LAST_EXEC_REPORT_DESCR);
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
        assertThat(testStockTrade.getCreatedByIp()).isEqualTo(DEFAULT_CREATED_BY_IP);
        assertThat(testStockTrade.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockTrade.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
        assertThat(testStockTrade.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testStockTrade.getMarket()).isEqualTo(DEFAULT_MARKET);
        assertThat(testStockTrade.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testStockTrade.isManualEntry()).isEqualTo(DEFAULT_MANUAL_ENTRY);
        assertThat(testStockTrade.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testStockTrade.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockTrade.getExecQuantity()).isEqualTo(DEFAULT_EXEC_QUANTITY);
        assertThat(testStockTrade.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testStockTrade.getStopPrice()).isEqualTo(DEFAULT_STOP_PRICE);
        assertThat(testStockTrade.getAveragePrice()).isEqualTo(DEFAULT_AVERAGE_PRICE);
        assertThat(testStockTrade.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testStockTrade.getTotalPriceActual()).isEqualTo(DEFAULT_TOTAL_PRICE_ACTUAL);
        assertThat(testStockTrade.getBrokerageFee()).isEqualTo(DEFAULT_BROKERAGE_FEE);
        assertThat(testStockTrade.getBrokerageFeeIss()).isEqualTo(DEFAULT_BROKERAGE_FEE_ISS);
        assertThat(testStockTrade.getNegotiationPerc()).isEqualTo(DEFAULT_NEGOTIATION_PERC);
        assertThat(testStockTrade.getLiquidationPerc()).isEqualTo(DEFAULT_LIQUIDATION_PERC);
        assertThat(testStockTrade.getRegistryPerc()).isEqualTo(DEFAULT_REGISTRY_PERC);
        assertThat(testStockTrade.getLastExecReportTime()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_TIME);
        assertThat(testStockTrade.getLastExecReportDescr()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_DESCR);
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
    public void checkTradeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setTradeDate(null);

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
    public void checkKindIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setKind(null);

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
    public void checkMarketIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setMarket(null);

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
    public void checkManualEntryIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockTradeRepository.findAll().size();
        // set the field null
        stockTrade.setManualEntry(null);

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
    public void getAllStockTrades() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList
        restStockTradeMockMvc.perform(get("/api/stock-trades?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].createdByIp").value(hasItem(DEFAULT_CREATED_BY_IP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(sameInstant(DEFAULT_TRADE_DATE))))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].market").value(hasItem(DEFAULT_MARKET.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(sameInstant(DEFAULT_EXPIRE_TIME))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].execQuantity").value(hasItem(DEFAULT_EXEC_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].stopPrice").value(hasItem(DEFAULT_STOP_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].averagePrice").value(hasItem(DEFAULT_AVERAGE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPriceActual").value(hasItem(DEFAULT_TOTAL_PRICE_ACTUAL.intValue())))
            .andExpect(jsonPath("$.[*].brokerageFee").value(hasItem(DEFAULT_BROKERAGE_FEE.intValue())))
            .andExpect(jsonPath("$.[*].brokerageFeeIss").value(hasItem(DEFAULT_BROKERAGE_FEE_ISS.intValue())))
            .andExpect(jsonPath("$.[*].negotiationPerc").value(hasItem(DEFAULT_NEGOTIATION_PERC.intValue())))
            .andExpect(jsonPath("$.[*].liquidationPerc").value(hasItem(DEFAULT_LIQUIDATION_PERC.intValue())))
            .andExpect(jsonPath("$.[*].registryPerc").value(hasItem(DEFAULT_REGISTRY_PERC.intValue())))
            .andExpect(jsonPath("$.[*].lastExecReportTime").value(hasItem(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME))))
            .andExpect(jsonPath("$.[*].lastExecReportDescr").value(hasItem(DEFAULT_LAST_EXEC_REPORT_DESCR.toString())));
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
            .andExpect(jsonPath("$.createdByIp").value(DEFAULT_CREATED_BY_IP.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.tradeDate").value(sameInstant(DEFAULT_TRADE_DATE)))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.market").value(DEFAULT_MARKET.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.manualEntry").value(DEFAULT_MANUAL_ENTRY.booleanValue()))
            .andExpect(jsonPath("$.expireTime").value(sameInstant(DEFAULT_EXPIRE_TIME)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.execQuantity").value(DEFAULT_EXEC_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.stopPrice").value(DEFAULT_STOP_PRICE.intValue()))
            .andExpect(jsonPath("$.averagePrice").value(DEFAULT_AVERAGE_PRICE.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.totalPriceActual").value(DEFAULT_TOTAL_PRICE_ACTUAL.intValue()))
            .andExpect(jsonPath("$.brokerageFee").value(DEFAULT_BROKERAGE_FEE.intValue()))
            .andExpect(jsonPath("$.brokerageFeeIss").value(DEFAULT_BROKERAGE_FEE_ISS.intValue()))
            .andExpect(jsonPath("$.negotiationPerc").value(DEFAULT_NEGOTIATION_PERC.intValue()))
            .andExpect(jsonPath("$.liquidationPerc").value(DEFAULT_LIQUIDATION_PERC.intValue()))
            .andExpect(jsonPath("$.registryPerc").value(DEFAULT_REGISTRY_PERC.intValue()))
            .andExpect(jsonPath("$.lastExecReportTime").value(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME)))
            .andExpect(jsonPath("$.lastExecReportDescr").value(DEFAULT_LAST_EXEC_REPORT_DESCR.toString()));
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdAt equals to DEFAULT_CREATED_AT
        defaultStockTradeShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the stockTradeList where createdAt equals to UPDATED_CREATED_AT
        defaultStockTradeShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultStockTradeShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the stockTradeList where createdAt equals to UPDATED_CREATED_AT
        defaultStockTradeShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdAt is not null
        defaultStockTradeShouldBeFound("createdAt.specified=true");

        // Get all the stockTradeList where createdAt is null
        defaultStockTradeShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdAt greater than or equals to DEFAULT_CREATED_AT
        defaultStockTradeShouldBeFound("createdAt.greaterOrEqualThan=" + DEFAULT_CREATED_AT);

        // Get all the stockTradeList where createdAt greater than or equals to UPDATED_CREATED_AT
        defaultStockTradeShouldNotBeFound("createdAt.greaterOrEqualThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdAt less than or equals to DEFAULT_CREATED_AT
        defaultStockTradeShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the stockTradeList where createdAt less than or equals to UPDATED_CREATED_AT
        defaultStockTradeShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllStockTradesByCreatedByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdByIp equals to DEFAULT_CREATED_BY_IP
        defaultStockTradeShouldBeFound("createdByIp.equals=" + DEFAULT_CREATED_BY_IP);

        // Get all the stockTradeList where createdByIp equals to UPDATED_CREATED_BY_IP
        defaultStockTradeShouldNotBeFound("createdByIp.equals=" + UPDATED_CREATED_BY_IP);
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedByIpIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdByIp in DEFAULT_CREATED_BY_IP or UPDATED_CREATED_BY_IP
        defaultStockTradeShouldBeFound("createdByIp.in=" + DEFAULT_CREATED_BY_IP + "," + UPDATED_CREATED_BY_IP);

        // Get all the stockTradeList where createdByIp equals to UPDATED_CREATED_BY_IP
        defaultStockTradeShouldNotBeFound("createdByIp.in=" + UPDATED_CREATED_BY_IP);
    }

    @Test
    @Transactional
    public void getAllStockTradesByCreatedByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where createdByIp is not null
        defaultStockTradeShouldBeFound("createdByIp.specified=true");

        // Get all the stockTradeList where createdByIp is null
        defaultStockTradeShouldNotBeFound("createdByIp.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where status equals to DEFAULT_STATUS
        defaultStockTradeShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the stockTradeList where status equals to UPDATED_STATUS
        defaultStockTradeShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllStockTradesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultStockTradeShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the stockTradeList where status equals to UPDATED_STATUS
        defaultStockTradeShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllStockTradesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where status is not null
        defaultStockTradeShouldBeFound("status.specified=true");

        // Get all the stockTradeList where status is null
        defaultStockTradeShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByTradeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where tradeDate equals to DEFAULT_TRADE_DATE
        defaultStockTradeShouldBeFound("tradeDate.equals=" + DEFAULT_TRADE_DATE);

        // Get all the stockTradeList where tradeDate equals to UPDATED_TRADE_DATE
        defaultStockTradeShouldNotBeFound("tradeDate.equals=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTradeDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where tradeDate in DEFAULT_TRADE_DATE or UPDATED_TRADE_DATE
        defaultStockTradeShouldBeFound("tradeDate.in=" + DEFAULT_TRADE_DATE + "," + UPDATED_TRADE_DATE);

        // Get all the stockTradeList where tradeDate equals to UPDATED_TRADE_DATE
        defaultStockTradeShouldNotBeFound("tradeDate.in=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTradeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where tradeDate is not null
        defaultStockTradeShouldBeFound("tradeDate.specified=true");

        // Get all the stockTradeList where tradeDate is null
        defaultStockTradeShouldNotBeFound("tradeDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByTradeDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where tradeDate greater than or equals to DEFAULT_TRADE_DATE
        defaultStockTradeShouldBeFound("tradeDate.greaterOrEqualThan=" + DEFAULT_TRADE_DATE);

        // Get all the stockTradeList where tradeDate greater than or equals to UPDATED_TRADE_DATE
        defaultStockTradeShouldNotBeFound("tradeDate.greaterOrEqualThan=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTradeDateIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where tradeDate less than or equals to DEFAULT_TRADE_DATE
        defaultStockTradeShouldNotBeFound("tradeDate.lessThan=" + DEFAULT_TRADE_DATE);

        // Get all the stockTradeList where tradeDate less than or equals to UPDATED_TRADE_DATE
        defaultStockTradeShouldBeFound("tradeDate.lessThan=" + UPDATED_TRADE_DATE);
    }


    @Test
    @Transactional
    public void getAllStockTradesByKindIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where kind equals to DEFAULT_KIND
        defaultStockTradeShouldBeFound("kind.equals=" + DEFAULT_KIND);

        // Get all the stockTradeList where kind equals to UPDATED_KIND
        defaultStockTradeShouldNotBeFound("kind.equals=" + UPDATED_KIND);
    }

    @Test
    @Transactional
    public void getAllStockTradesByKindIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where kind in DEFAULT_KIND or UPDATED_KIND
        defaultStockTradeShouldBeFound("kind.in=" + DEFAULT_KIND + "," + UPDATED_KIND);

        // Get all the stockTradeList where kind equals to UPDATED_KIND
        defaultStockTradeShouldNotBeFound("kind.in=" + UPDATED_KIND);
    }

    @Test
    @Transactional
    public void getAllStockTradesByKindIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where kind is not null
        defaultStockTradeShouldBeFound("kind.specified=true");

        // Get all the stockTradeList where kind is null
        defaultStockTradeShouldNotBeFound("kind.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByMarketIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where market equals to DEFAULT_MARKET
        defaultStockTradeShouldBeFound("market.equals=" + DEFAULT_MARKET);

        // Get all the stockTradeList where market equals to UPDATED_MARKET
        defaultStockTradeShouldNotBeFound("market.equals=" + UPDATED_MARKET);
    }

    @Test
    @Transactional
    public void getAllStockTradesByMarketIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where market in DEFAULT_MARKET or UPDATED_MARKET
        defaultStockTradeShouldBeFound("market.in=" + DEFAULT_MARKET + "," + UPDATED_MARKET);

        // Get all the stockTradeList where market equals to UPDATED_MARKET
        defaultStockTradeShouldNotBeFound("market.in=" + UPDATED_MARKET);
    }

    @Test
    @Transactional
    public void getAllStockTradesByMarketIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where market is not null
        defaultStockTradeShouldBeFound("market.specified=true");

        // Get all the stockTradeList where market is null
        defaultStockTradeShouldNotBeFound("market.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesBySideIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where side equals to DEFAULT_SIDE
        defaultStockTradeShouldBeFound("side.equals=" + DEFAULT_SIDE);

        // Get all the stockTradeList where side equals to UPDATED_SIDE
        defaultStockTradeShouldNotBeFound("side.equals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllStockTradesBySideIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where side in DEFAULT_SIDE or UPDATED_SIDE
        defaultStockTradeShouldBeFound("side.in=" + DEFAULT_SIDE + "," + UPDATED_SIDE);

        // Get all the stockTradeList where side equals to UPDATED_SIDE
        defaultStockTradeShouldNotBeFound("side.in=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllStockTradesBySideIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where side is not null
        defaultStockTradeShouldBeFound("side.specified=true");

        // Get all the stockTradeList where side is null
        defaultStockTradeShouldNotBeFound("side.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByManualEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where manualEntry equals to DEFAULT_MANUAL_ENTRY
        defaultStockTradeShouldBeFound("manualEntry.equals=" + DEFAULT_MANUAL_ENTRY);

        // Get all the stockTradeList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultStockTradeShouldNotBeFound("manualEntry.equals=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByManualEntryIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where manualEntry in DEFAULT_MANUAL_ENTRY or UPDATED_MANUAL_ENTRY
        defaultStockTradeShouldBeFound("manualEntry.in=" + DEFAULT_MANUAL_ENTRY + "," + UPDATED_MANUAL_ENTRY);

        // Get all the stockTradeList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultStockTradeShouldNotBeFound("manualEntry.in=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByManualEntryIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where manualEntry is not null
        defaultStockTradeShouldBeFound("manualEntry.specified=true");

        // Get all the stockTradeList where manualEntry is null
        defaultStockTradeShouldNotBeFound("manualEntry.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByExpireTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where expireTime equals to DEFAULT_EXPIRE_TIME
        defaultStockTradeShouldBeFound("expireTime.equals=" + DEFAULT_EXPIRE_TIME);

        // Get all the stockTradeList where expireTime equals to UPDATED_EXPIRE_TIME
        defaultStockTradeShouldNotBeFound("expireTime.equals=" + UPDATED_EXPIRE_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExpireTimeIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where expireTime in DEFAULT_EXPIRE_TIME or UPDATED_EXPIRE_TIME
        defaultStockTradeShouldBeFound("expireTime.in=" + DEFAULT_EXPIRE_TIME + "," + UPDATED_EXPIRE_TIME);

        // Get all the stockTradeList where expireTime equals to UPDATED_EXPIRE_TIME
        defaultStockTradeShouldNotBeFound("expireTime.in=" + UPDATED_EXPIRE_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExpireTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where expireTime is not null
        defaultStockTradeShouldBeFound("expireTime.specified=true");

        // Get all the stockTradeList where expireTime is null
        defaultStockTradeShouldNotBeFound("expireTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByExpireTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where expireTime greater than or equals to DEFAULT_EXPIRE_TIME
        defaultStockTradeShouldBeFound("expireTime.greaterOrEqualThan=" + DEFAULT_EXPIRE_TIME);

        // Get all the stockTradeList where expireTime greater than or equals to UPDATED_EXPIRE_TIME
        defaultStockTradeShouldNotBeFound("expireTime.greaterOrEqualThan=" + UPDATED_EXPIRE_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExpireTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where expireTime less than or equals to DEFAULT_EXPIRE_TIME
        defaultStockTradeShouldNotBeFound("expireTime.lessThan=" + DEFAULT_EXPIRE_TIME);

        // Get all the stockTradeList where expireTime less than or equals to UPDATED_EXPIRE_TIME
        defaultStockTradeShouldBeFound("expireTime.lessThan=" + UPDATED_EXPIRE_TIME);
    }


    @Test
    @Transactional
    public void getAllStockTradesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where quantity equals to DEFAULT_QUANTITY
        defaultStockTradeShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockTradeList where quantity equals to UPDATED_QUANTITY
        defaultStockTradeShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockTradeShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockTradeList where quantity equals to UPDATED_QUANTITY
        defaultStockTradeShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where quantity is not null
        defaultStockTradeShouldBeFound("quantity.specified=true");

        // Get all the stockTradeList where quantity is null
        defaultStockTradeShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultStockTradeShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the stockTradeList where quantity greater than or equals to UPDATED_QUANTITY
        defaultStockTradeShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where quantity less than or equals to DEFAULT_QUANTITY
        defaultStockTradeShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockTradeList where quantity less than or equals to UPDATED_QUANTITY
        defaultStockTradeShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStockTradesByExecQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where execQuantity equals to DEFAULT_EXEC_QUANTITY
        defaultStockTradeShouldBeFound("execQuantity.equals=" + DEFAULT_EXEC_QUANTITY);

        // Get all the stockTradeList where execQuantity equals to UPDATED_EXEC_QUANTITY
        defaultStockTradeShouldNotBeFound("execQuantity.equals=" + UPDATED_EXEC_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExecQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where execQuantity in DEFAULT_EXEC_QUANTITY or UPDATED_EXEC_QUANTITY
        defaultStockTradeShouldBeFound("execQuantity.in=" + DEFAULT_EXEC_QUANTITY + "," + UPDATED_EXEC_QUANTITY);

        // Get all the stockTradeList where execQuantity equals to UPDATED_EXEC_QUANTITY
        defaultStockTradeShouldNotBeFound("execQuantity.in=" + UPDATED_EXEC_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExecQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where execQuantity is not null
        defaultStockTradeShouldBeFound("execQuantity.specified=true");

        // Get all the stockTradeList where execQuantity is null
        defaultStockTradeShouldNotBeFound("execQuantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByExecQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where execQuantity greater than or equals to DEFAULT_EXEC_QUANTITY
        defaultStockTradeShouldBeFound("execQuantity.greaterOrEqualThan=" + DEFAULT_EXEC_QUANTITY);

        // Get all the stockTradeList where execQuantity greater than or equals to UPDATED_EXEC_QUANTITY
        defaultStockTradeShouldNotBeFound("execQuantity.greaterOrEqualThan=" + UPDATED_EXEC_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockTradesByExecQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where execQuantity less than or equals to DEFAULT_EXEC_QUANTITY
        defaultStockTradeShouldNotBeFound("execQuantity.lessThan=" + DEFAULT_EXEC_QUANTITY);

        // Get all the stockTradeList where execQuantity less than or equals to UPDATED_EXEC_QUANTITY
        defaultStockTradeShouldBeFound("execQuantity.lessThan=" + UPDATED_EXEC_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStockTradesByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultStockTradeShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the stockTradeList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockTradeShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultStockTradeShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the stockTradeList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockTradeShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where unitPrice is not null
        defaultStockTradeShouldBeFound("unitPrice.specified=true");

        // Get all the stockTradeList where unitPrice is null
        defaultStockTradeShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByStopPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where stopPrice equals to DEFAULT_STOP_PRICE
        defaultStockTradeShouldBeFound("stopPrice.equals=" + DEFAULT_STOP_PRICE);

        // Get all the stockTradeList where stopPrice equals to UPDATED_STOP_PRICE
        defaultStockTradeShouldNotBeFound("stopPrice.equals=" + UPDATED_STOP_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByStopPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where stopPrice in DEFAULT_STOP_PRICE or UPDATED_STOP_PRICE
        defaultStockTradeShouldBeFound("stopPrice.in=" + DEFAULT_STOP_PRICE + "," + UPDATED_STOP_PRICE);

        // Get all the stockTradeList where stopPrice equals to UPDATED_STOP_PRICE
        defaultStockTradeShouldNotBeFound("stopPrice.in=" + UPDATED_STOP_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByStopPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where stopPrice is not null
        defaultStockTradeShouldBeFound("stopPrice.specified=true");

        // Get all the stockTradeList where stopPrice is null
        defaultStockTradeShouldNotBeFound("stopPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByAveragePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where averagePrice equals to DEFAULT_AVERAGE_PRICE
        defaultStockTradeShouldBeFound("averagePrice.equals=" + DEFAULT_AVERAGE_PRICE);

        // Get all the stockTradeList where averagePrice equals to UPDATED_AVERAGE_PRICE
        defaultStockTradeShouldNotBeFound("averagePrice.equals=" + UPDATED_AVERAGE_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByAveragePriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where averagePrice in DEFAULT_AVERAGE_PRICE or UPDATED_AVERAGE_PRICE
        defaultStockTradeShouldBeFound("averagePrice.in=" + DEFAULT_AVERAGE_PRICE + "," + UPDATED_AVERAGE_PRICE);

        // Get all the stockTradeList where averagePrice equals to UPDATED_AVERAGE_PRICE
        defaultStockTradeShouldNotBeFound("averagePrice.in=" + UPDATED_AVERAGE_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByAveragePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where averagePrice is not null
        defaultStockTradeShouldBeFound("averagePrice.specified=true");

        // Get all the stockTradeList where averagePrice is null
        defaultStockTradeShouldNotBeFound("averagePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultStockTradeShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the stockTradeList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockTradeShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultStockTradeShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the stockTradeList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockTradeShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPrice is not null
        defaultStockTradeShouldBeFound("totalPrice.specified=true");

        // Get all the stockTradeList where totalPrice is null
        defaultStockTradeShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceActualIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPriceActual equals to DEFAULT_TOTAL_PRICE_ACTUAL
        defaultStockTradeShouldBeFound("totalPriceActual.equals=" + DEFAULT_TOTAL_PRICE_ACTUAL);

        // Get all the stockTradeList where totalPriceActual equals to UPDATED_TOTAL_PRICE_ACTUAL
        defaultStockTradeShouldNotBeFound("totalPriceActual.equals=" + UPDATED_TOTAL_PRICE_ACTUAL);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceActualIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPriceActual in DEFAULT_TOTAL_PRICE_ACTUAL or UPDATED_TOTAL_PRICE_ACTUAL
        defaultStockTradeShouldBeFound("totalPriceActual.in=" + DEFAULT_TOTAL_PRICE_ACTUAL + "," + UPDATED_TOTAL_PRICE_ACTUAL);

        // Get all the stockTradeList where totalPriceActual equals to UPDATED_TOTAL_PRICE_ACTUAL
        defaultStockTradeShouldNotBeFound("totalPriceActual.in=" + UPDATED_TOTAL_PRICE_ACTUAL);
    }

    @Test
    @Transactional
    public void getAllStockTradesByTotalPriceActualIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where totalPriceActual is not null
        defaultStockTradeShouldBeFound("totalPriceActual.specified=true");

        // Get all the stockTradeList where totalPriceActual is null
        defaultStockTradeShouldNotBeFound("totalPriceActual.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFee equals to DEFAULT_BROKERAGE_FEE
        defaultStockTradeShouldBeFound("brokerageFee.equals=" + DEFAULT_BROKERAGE_FEE);

        // Get all the stockTradeList where brokerageFee equals to UPDATED_BROKERAGE_FEE
        defaultStockTradeShouldNotBeFound("brokerageFee.equals=" + UPDATED_BROKERAGE_FEE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFee in DEFAULT_BROKERAGE_FEE or UPDATED_BROKERAGE_FEE
        defaultStockTradeShouldBeFound("brokerageFee.in=" + DEFAULT_BROKERAGE_FEE + "," + UPDATED_BROKERAGE_FEE);

        // Get all the stockTradeList where brokerageFee equals to UPDATED_BROKERAGE_FEE
        defaultStockTradeShouldNotBeFound("brokerageFee.in=" + UPDATED_BROKERAGE_FEE);
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFee is not null
        defaultStockTradeShouldBeFound("brokerageFee.specified=true");

        // Get all the stockTradeList where brokerageFee is null
        defaultStockTradeShouldNotBeFound("brokerageFee.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIssIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFeeIss equals to DEFAULT_BROKERAGE_FEE_ISS
        defaultStockTradeShouldBeFound("brokerageFeeIss.equals=" + DEFAULT_BROKERAGE_FEE_ISS);

        // Get all the stockTradeList where brokerageFeeIss equals to UPDATED_BROKERAGE_FEE_ISS
        defaultStockTradeShouldNotBeFound("brokerageFeeIss.equals=" + UPDATED_BROKERAGE_FEE_ISS);
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIssIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFeeIss in DEFAULT_BROKERAGE_FEE_ISS or UPDATED_BROKERAGE_FEE_ISS
        defaultStockTradeShouldBeFound("brokerageFeeIss.in=" + DEFAULT_BROKERAGE_FEE_ISS + "," + UPDATED_BROKERAGE_FEE_ISS);

        // Get all the stockTradeList where brokerageFeeIss equals to UPDATED_BROKERAGE_FEE_ISS
        defaultStockTradeShouldNotBeFound("brokerageFeeIss.in=" + UPDATED_BROKERAGE_FEE_ISS);
    }

    @Test
    @Transactional
    public void getAllStockTradesByBrokerageFeeIssIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where brokerageFeeIss is not null
        defaultStockTradeShouldBeFound("brokerageFeeIss.specified=true");

        // Get all the stockTradeList where brokerageFeeIss is null
        defaultStockTradeShouldNotBeFound("brokerageFeeIss.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByNegotiationPercIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where negotiationPerc equals to DEFAULT_NEGOTIATION_PERC
        defaultStockTradeShouldBeFound("negotiationPerc.equals=" + DEFAULT_NEGOTIATION_PERC);

        // Get all the stockTradeList where negotiationPerc equals to UPDATED_NEGOTIATION_PERC
        defaultStockTradeShouldNotBeFound("negotiationPerc.equals=" + UPDATED_NEGOTIATION_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByNegotiationPercIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where negotiationPerc in DEFAULT_NEGOTIATION_PERC or UPDATED_NEGOTIATION_PERC
        defaultStockTradeShouldBeFound("negotiationPerc.in=" + DEFAULT_NEGOTIATION_PERC + "," + UPDATED_NEGOTIATION_PERC);

        // Get all the stockTradeList where negotiationPerc equals to UPDATED_NEGOTIATION_PERC
        defaultStockTradeShouldNotBeFound("negotiationPerc.in=" + UPDATED_NEGOTIATION_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByNegotiationPercIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where negotiationPerc is not null
        defaultStockTradeShouldBeFound("negotiationPerc.specified=true");

        // Get all the stockTradeList where negotiationPerc is null
        defaultStockTradeShouldNotBeFound("negotiationPerc.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByLiquidationPercIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where liquidationPerc equals to DEFAULT_LIQUIDATION_PERC
        defaultStockTradeShouldBeFound("liquidationPerc.equals=" + DEFAULT_LIQUIDATION_PERC);

        // Get all the stockTradeList where liquidationPerc equals to UPDATED_LIQUIDATION_PERC
        defaultStockTradeShouldNotBeFound("liquidationPerc.equals=" + UPDATED_LIQUIDATION_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLiquidationPercIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where liquidationPerc in DEFAULT_LIQUIDATION_PERC or UPDATED_LIQUIDATION_PERC
        defaultStockTradeShouldBeFound("liquidationPerc.in=" + DEFAULT_LIQUIDATION_PERC + "," + UPDATED_LIQUIDATION_PERC);

        // Get all the stockTradeList where liquidationPerc equals to UPDATED_LIQUIDATION_PERC
        defaultStockTradeShouldNotBeFound("liquidationPerc.in=" + UPDATED_LIQUIDATION_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLiquidationPercIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where liquidationPerc is not null
        defaultStockTradeShouldBeFound("liquidationPerc.specified=true");

        // Get all the stockTradeList where liquidationPerc is null
        defaultStockTradeShouldNotBeFound("liquidationPerc.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByRegistryPercIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where registryPerc equals to DEFAULT_REGISTRY_PERC
        defaultStockTradeShouldBeFound("registryPerc.equals=" + DEFAULT_REGISTRY_PERC);

        // Get all the stockTradeList where registryPerc equals to UPDATED_REGISTRY_PERC
        defaultStockTradeShouldNotBeFound("registryPerc.equals=" + UPDATED_REGISTRY_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByRegistryPercIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where registryPerc in DEFAULT_REGISTRY_PERC or UPDATED_REGISTRY_PERC
        defaultStockTradeShouldBeFound("registryPerc.in=" + DEFAULT_REGISTRY_PERC + "," + UPDATED_REGISTRY_PERC);

        // Get all the stockTradeList where registryPerc equals to UPDATED_REGISTRY_PERC
        defaultStockTradeShouldNotBeFound("registryPerc.in=" + UPDATED_REGISTRY_PERC);
    }

    @Test
    @Transactional
    public void getAllStockTradesByRegistryPercIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where registryPerc is not null
        defaultStockTradeShouldBeFound("registryPerc.specified=true");

        // Get all the stockTradeList where registryPerc is null
        defaultStockTradeShouldNotBeFound("registryPerc.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportTime equals to DEFAULT_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldBeFound("lastExecReportTime.equals=" + DEFAULT_LAST_EXEC_REPORT_TIME);

        // Get all the stockTradeList where lastExecReportTime equals to UPDATED_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldNotBeFound("lastExecReportTime.equals=" + UPDATED_LAST_EXEC_REPORT_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportTimeIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportTime in DEFAULT_LAST_EXEC_REPORT_TIME or UPDATED_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldBeFound("lastExecReportTime.in=" + DEFAULT_LAST_EXEC_REPORT_TIME + "," + UPDATED_LAST_EXEC_REPORT_TIME);

        // Get all the stockTradeList where lastExecReportTime equals to UPDATED_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldNotBeFound("lastExecReportTime.in=" + UPDATED_LAST_EXEC_REPORT_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportTime is not null
        defaultStockTradeShouldBeFound("lastExecReportTime.specified=true");

        // Get all the stockTradeList where lastExecReportTime is null
        defaultStockTradeShouldNotBeFound("lastExecReportTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportTime greater than or equals to DEFAULT_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldBeFound("lastExecReportTime.greaterOrEqualThan=" + DEFAULT_LAST_EXEC_REPORT_TIME);

        // Get all the stockTradeList where lastExecReportTime greater than or equals to UPDATED_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldNotBeFound("lastExecReportTime.greaterOrEqualThan=" + UPDATED_LAST_EXEC_REPORT_TIME);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportTime less than or equals to DEFAULT_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldNotBeFound("lastExecReportTime.lessThan=" + DEFAULT_LAST_EXEC_REPORT_TIME);

        // Get all the stockTradeList where lastExecReportTime less than or equals to UPDATED_LAST_EXEC_REPORT_TIME
        defaultStockTradeShouldBeFound("lastExecReportTime.lessThan=" + UPDATED_LAST_EXEC_REPORT_TIME);
    }


    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportDescrIsEqualToSomething() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportDescr equals to DEFAULT_LAST_EXEC_REPORT_DESCR
        defaultStockTradeShouldBeFound("lastExecReportDescr.equals=" + DEFAULT_LAST_EXEC_REPORT_DESCR);

        // Get all the stockTradeList where lastExecReportDescr equals to UPDATED_LAST_EXEC_REPORT_DESCR
        defaultStockTradeShouldNotBeFound("lastExecReportDescr.equals=" + UPDATED_LAST_EXEC_REPORT_DESCR);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportDescrIsInShouldWork() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportDescr in DEFAULT_LAST_EXEC_REPORT_DESCR or UPDATED_LAST_EXEC_REPORT_DESCR
        defaultStockTradeShouldBeFound("lastExecReportDescr.in=" + DEFAULT_LAST_EXEC_REPORT_DESCR + "," + UPDATED_LAST_EXEC_REPORT_DESCR);

        // Get all the stockTradeList where lastExecReportDescr equals to UPDATED_LAST_EXEC_REPORT_DESCR
        defaultStockTradeShouldNotBeFound("lastExecReportDescr.in=" + UPDATED_LAST_EXEC_REPORT_DESCR);
    }

    @Test
    @Transactional
    public void getAllStockTradesByLastExecReportDescrIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockTradeRepository.saveAndFlush(stockTrade);

        // Get all the stockTradeList where lastExecReportDescr is not null
        defaultStockTradeShouldBeFound("lastExecReportDescr.specified=true");

        // Get all the stockTradeList where lastExecReportDescr is null
        defaultStockTradeShouldNotBeFound("lastExecReportDescr.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockTradesByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        stockTrade.setStock(stock);
        stockTradeRepository.saveAndFlush(stockTrade);
        Long stockId = stock.getId();

        // Get all the stockTradeList where stock equals to stockId
        defaultStockTradeShouldBeFound("stockId.equals=" + stockId);

        // Get all the stockTradeList where stock equals to stockId + 1
        defaultStockTradeShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTradesByBrokerageAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        BrokerageAccount brokerageAccount = BrokerageAccountResourceIntTest.createEntity(em);
        em.persist(brokerageAccount);
        em.flush();
        stockTrade.setBrokerageAccount(brokerageAccount);
        stockTradeRepository.saveAndFlush(stockTrade);
        Long brokerageAccountId = brokerageAccount.getId();

        // Get all the stockTradeList where brokerageAccount equals to brokerageAccountId
        defaultStockTradeShouldBeFound("brokerageAccountId.equals=" + brokerageAccountId);

        // Get all the stockTradeList where brokerageAccount equals to brokerageAccountId + 1
        defaultStockTradeShouldNotBeFound("brokerageAccountId.equals=" + (brokerageAccountId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTradesByMainOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        StockOrder mainOrder = StockOrderResourceIntTest.createEntity(em);
        em.persist(mainOrder);
        em.flush();
        stockTrade.setMainOrder(mainOrder);
        stockTradeRepository.saveAndFlush(stockTrade);
        Long mainOrderId = mainOrder.getId();

        // Get all the stockTradeList where mainOrder equals to mainOrderId
        defaultStockTradeShouldBeFound("mainOrderId.equals=" + mainOrderId);

        // Get all the stockTradeList where mainOrder equals to mainOrderId + 1
        defaultStockTradeShouldNotBeFound("mainOrderId.equals=" + (mainOrderId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTradesByOrdersIsEqualToSomething() throws Exception {
        // Initialize the database
        StockOrder orders = StockOrderResourceIntTest.createEntity(em);
        em.persist(orders);
        em.flush();
        stockTrade.addOrders(orders);
        stockTradeRepository.saveAndFlush(stockTrade);
        Long ordersId = orders.getId();

        // Get all the stockTradeList where orders equals to ordersId
        defaultStockTradeShouldBeFound("ordersId.equals=" + ordersId);

        // Get all the stockTradeList where orders equals to ordersId + 1
        defaultStockTradeShouldNotBeFound("ordersId.equals=" + (ordersId + 1));
    }


    @Test
    @Transactional
    public void getAllStockTradesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        User createdBy = UserResourceIntTest.createEntity(em);
        em.persist(createdBy);
        em.flush();
        stockTrade.setCreatedBy(createdBy);
        stockTradeRepository.saveAndFlush(stockTrade);
        Long createdById = createdBy.getId();

        // Get all the stockTradeList where createdBy equals to createdById
        defaultStockTradeShouldBeFound("createdById.equals=" + createdById);

        // Get all the stockTradeList where createdBy equals to createdById + 1
        defaultStockTradeShouldNotBeFound("createdById.equals=" + (createdById + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockTradeShouldBeFound(String filter) throws Exception {
        restStockTradeMockMvc.perform(get("/api/stock-trades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockTrade.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].createdByIp").value(hasItem(DEFAULT_CREATED_BY_IP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(sameInstant(DEFAULT_TRADE_DATE))))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].market").value(hasItem(DEFAULT_MARKET.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(sameInstant(DEFAULT_EXPIRE_TIME))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].execQuantity").value(hasItem(DEFAULT_EXEC_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].stopPrice").value(hasItem(DEFAULT_STOP_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].averagePrice").value(hasItem(DEFAULT_AVERAGE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPriceActual").value(hasItem(DEFAULT_TOTAL_PRICE_ACTUAL.intValue())))
            .andExpect(jsonPath("$.[*].brokerageFee").value(hasItem(DEFAULT_BROKERAGE_FEE.intValue())))
            .andExpect(jsonPath("$.[*].brokerageFeeIss").value(hasItem(DEFAULT_BROKERAGE_FEE_ISS.intValue())))
            .andExpect(jsonPath("$.[*].negotiationPerc").value(hasItem(DEFAULT_NEGOTIATION_PERC.intValue())))
            .andExpect(jsonPath("$.[*].liquidationPerc").value(hasItem(DEFAULT_LIQUIDATION_PERC.intValue())))
            .andExpect(jsonPath("$.[*].registryPerc").value(hasItem(DEFAULT_REGISTRY_PERC.intValue())))
            .andExpect(jsonPath("$.[*].lastExecReportTime").value(hasItem(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME))))
            .andExpect(jsonPath("$.[*].lastExecReportDescr").value(hasItem(DEFAULT_LAST_EXEC_REPORT_DESCR.toString())));

        // Check, that the count call also returns 1
        restStockTradeMockMvc.perform(get("/api/stock-trades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockTradeShouldNotBeFound(String filter) throws Exception {
        restStockTradeMockMvc.perform(get("/api/stock-trades?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockTradeMockMvc.perform(get("/api/stock-trades/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
            .createdByIp(UPDATED_CREATED_BY_IP)
            .status(UPDATED_STATUS)
            .tradeDate(UPDATED_TRADE_DATE)
            .kind(UPDATED_KIND)
            .market(UPDATED_MARKET)
            .side(UPDATED_SIDE)
            .manualEntry(UPDATED_MANUAL_ENTRY)
            .expireTime(UPDATED_EXPIRE_TIME)
            .quantity(UPDATED_QUANTITY)
            .execQuantity(UPDATED_EXEC_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .stopPrice(UPDATED_STOP_PRICE)
            .averagePrice(UPDATED_AVERAGE_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .totalPriceActual(UPDATED_TOTAL_PRICE_ACTUAL)
            .brokerageFee(UPDATED_BROKERAGE_FEE)
            .brokerageFeeIss(UPDATED_BROKERAGE_FEE_ISS)
            .negotiationPerc(UPDATED_NEGOTIATION_PERC)
            .liquidationPerc(UPDATED_LIQUIDATION_PERC)
            .registryPerc(UPDATED_REGISTRY_PERC)
            .lastExecReportTime(UPDATED_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(UPDATED_LAST_EXEC_REPORT_DESCR);
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
        assertThat(testStockTrade.getCreatedByIp()).isEqualTo(UPDATED_CREATED_BY_IP);
        assertThat(testStockTrade.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockTrade.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
        assertThat(testStockTrade.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testStockTrade.getMarket()).isEqualTo(UPDATED_MARKET);
        assertThat(testStockTrade.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testStockTrade.isManualEntry()).isEqualTo(UPDATED_MANUAL_ENTRY);
        assertThat(testStockTrade.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testStockTrade.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockTrade.getExecQuantity()).isEqualTo(UPDATED_EXEC_QUANTITY);
        assertThat(testStockTrade.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testStockTrade.getStopPrice()).isEqualTo(UPDATED_STOP_PRICE);
        assertThat(testStockTrade.getAveragePrice()).isEqualTo(UPDATED_AVERAGE_PRICE);
        assertThat(testStockTrade.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testStockTrade.getTotalPriceActual()).isEqualTo(UPDATED_TOTAL_PRICE_ACTUAL);
        assertThat(testStockTrade.getBrokerageFee()).isEqualTo(UPDATED_BROKERAGE_FEE);
        assertThat(testStockTrade.getBrokerageFeeIss()).isEqualTo(UPDATED_BROKERAGE_FEE_ISS);
        assertThat(testStockTrade.getNegotiationPerc()).isEqualTo(UPDATED_NEGOTIATION_PERC);
        assertThat(testStockTrade.getLiquidationPerc()).isEqualTo(UPDATED_LIQUIDATION_PERC);
        assertThat(testStockTrade.getRegistryPerc()).isEqualTo(UPDATED_REGISTRY_PERC);
        assertThat(testStockTrade.getLastExecReportTime()).isEqualTo(UPDATED_LAST_EXEC_REPORT_TIME);
        assertThat(testStockTrade.getLastExecReportDescr()).isEqualTo(UPDATED_LAST_EXEC_REPORT_DESCR);
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
