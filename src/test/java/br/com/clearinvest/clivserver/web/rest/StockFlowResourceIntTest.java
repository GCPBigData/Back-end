package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockFlow;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.domain.ExecReport;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.repository.StockFlowRepository;
import br.com.clearinvest.clivserver.service.StockFlowService;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.StockFlowMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;
import br.com.clearinvest.clivserver.service.dto.StockFlowCriteria;
import br.com.clearinvest.clivserver.service.StockFlowQueryService;

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

    private static final ZonedDateTime DEFAULT_TRADE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRADE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SIDE = "A";
    private static final String UPDATED_SIDE = "B";

    private static final String DEFAULT_SYMBOL = "AAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBB";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final Boolean DEFAULT_MANUAL_ENTRY = false;
    private static final Boolean UPDATED_MANUAL_ENTRY = true;

    @Autowired
    private StockFlowRepository stockFlowRepository;

    @Autowired
    private StockFlowMapper stockFlowMapper;

    @Autowired
    private StockFlowService stockFlowService;

    @Autowired
    private StockFlowQueryService stockFlowQueryService;

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
        final StockFlowResource stockFlowResource = new StockFlowResource(stockFlowService, stockFlowQueryService);
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
            .tradeDate(DEFAULT_TRADE_DATE)
            .side(DEFAULT_SIDE)
            .symbol(DEFAULT_SYMBOL)
            .quantity(DEFAULT_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .manualEntry(DEFAULT_MANUAL_ENTRY);
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
        assertThat(testStockFlow.getTradeDate()).isEqualTo(DEFAULT_TRADE_DATE);
        assertThat(testStockFlow.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testStockFlow.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testStockFlow.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockFlow.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testStockFlow.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testStockFlow.isManualEntry()).isEqualTo(DEFAULT_MANUAL_ENTRY);
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
    public void checkTradeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setTradeDate(null);

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
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setSide(null);

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
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setUnitPrice(null);

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
    public void checkTotalPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setTotalPrice(null);

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
    public void checkManualEntryIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockFlowRepository.findAll().size();
        // set the field null
        stockFlow.setManualEntry(null);

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
            .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(sameInstant(DEFAULT_TRADE_DATE))))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())));
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
            .andExpect(jsonPath("$.tradeDate").value(sameInstant(DEFAULT_TRADE_DATE)))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.manualEntry").value(DEFAULT_MANUAL_ENTRY.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllStockFlowsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where createdAt equals to DEFAULT_CREATED_AT
        defaultStockFlowShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the stockFlowList where createdAt equals to UPDATED_CREATED_AT
        defaultStockFlowShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultStockFlowShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the stockFlowList where createdAt equals to UPDATED_CREATED_AT
        defaultStockFlowShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where createdAt is not null
        defaultStockFlowShouldBeFound("createdAt.specified=true");

        // Get all the stockFlowList where createdAt is null
        defaultStockFlowShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where createdAt greater than or equals to DEFAULT_CREATED_AT
        defaultStockFlowShouldBeFound("createdAt.greaterOrEqualThan=" + DEFAULT_CREATED_AT);

        // Get all the stockFlowList where createdAt greater than or equals to UPDATED_CREATED_AT
        defaultStockFlowShouldNotBeFound("createdAt.greaterOrEqualThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where createdAt less than or equals to DEFAULT_CREATED_AT
        defaultStockFlowShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the stockFlowList where createdAt less than or equals to UPDATED_CREATED_AT
        defaultStockFlowShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllStockFlowsByTradeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where tradeDate equals to DEFAULT_TRADE_DATE
        defaultStockFlowShouldBeFound("tradeDate.equals=" + DEFAULT_TRADE_DATE);

        // Get all the stockFlowList where tradeDate equals to UPDATED_TRADE_DATE
        defaultStockFlowShouldNotBeFound("tradeDate.equals=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTradeDateIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where tradeDate in DEFAULT_TRADE_DATE or UPDATED_TRADE_DATE
        defaultStockFlowShouldBeFound("tradeDate.in=" + DEFAULT_TRADE_DATE + "," + UPDATED_TRADE_DATE);

        // Get all the stockFlowList where tradeDate equals to UPDATED_TRADE_DATE
        defaultStockFlowShouldNotBeFound("tradeDate.in=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTradeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where tradeDate is not null
        defaultStockFlowShouldBeFound("tradeDate.specified=true");

        // Get all the stockFlowList where tradeDate is null
        defaultStockFlowShouldNotBeFound("tradeDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTradeDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where tradeDate greater than or equals to DEFAULT_TRADE_DATE
        defaultStockFlowShouldBeFound("tradeDate.greaterOrEqualThan=" + DEFAULT_TRADE_DATE);

        // Get all the stockFlowList where tradeDate greater than or equals to UPDATED_TRADE_DATE
        defaultStockFlowShouldNotBeFound("tradeDate.greaterOrEqualThan=" + UPDATED_TRADE_DATE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTradeDateIsLessThanSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where tradeDate less than or equals to DEFAULT_TRADE_DATE
        defaultStockFlowShouldNotBeFound("tradeDate.lessThan=" + DEFAULT_TRADE_DATE);

        // Get all the stockFlowList where tradeDate less than or equals to UPDATED_TRADE_DATE
        defaultStockFlowShouldBeFound("tradeDate.lessThan=" + UPDATED_TRADE_DATE);
    }


    @Test
    @Transactional
    public void getAllStockFlowsBySideIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where side equals to DEFAULT_SIDE
        defaultStockFlowShouldBeFound("side.equals=" + DEFAULT_SIDE);

        // Get all the stockFlowList where side equals to UPDATED_SIDE
        defaultStockFlowShouldNotBeFound("side.equals=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsBySideIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where side in DEFAULT_SIDE or UPDATED_SIDE
        defaultStockFlowShouldBeFound("side.in=" + DEFAULT_SIDE + "," + UPDATED_SIDE);

        // Get all the stockFlowList where side equals to UPDATED_SIDE
        defaultStockFlowShouldNotBeFound("side.in=" + UPDATED_SIDE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsBySideIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where side is not null
        defaultStockFlowShouldBeFound("side.specified=true");

        // Get all the stockFlowList where side is null
        defaultStockFlowShouldNotBeFound("side.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsBySymbolIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where symbol equals to DEFAULT_SYMBOL
        defaultStockFlowShouldBeFound("symbol.equals=" + DEFAULT_SYMBOL);

        // Get all the stockFlowList where symbol equals to UPDATED_SYMBOL
        defaultStockFlowShouldNotBeFound("symbol.equals=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllStockFlowsBySymbolIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where symbol in DEFAULT_SYMBOL or UPDATED_SYMBOL
        defaultStockFlowShouldBeFound("symbol.in=" + DEFAULT_SYMBOL + "," + UPDATED_SYMBOL);

        // Get all the stockFlowList where symbol equals to UPDATED_SYMBOL
        defaultStockFlowShouldNotBeFound("symbol.in=" + UPDATED_SYMBOL);
    }

    @Test
    @Transactional
    public void getAllStockFlowsBySymbolIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where symbol is not null
        defaultStockFlowShouldBeFound("symbol.specified=true");

        // Get all the stockFlowList where symbol is null
        defaultStockFlowShouldNotBeFound("symbol.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where quantity equals to DEFAULT_QUANTITY
        defaultStockFlowShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the stockFlowList where quantity equals to UPDATED_QUANTITY
        defaultStockFlowShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultStockFlowShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the stockFlowList where quantity equals to UPDATED_QUANTITY
        defaultStockFlowShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where quantity is not null
        defaultStockFlowShouldBeFound("quantity.specified=true");

        // Get all the stockFlowList where quantity is null
        defaultStockFlowShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where quantity greater than or equals to DEFAULT_QUANTITY
        defaultStockFlowShouldBeFound("quantity.greaterOrEqualThan=" + DEFAULT_QUANTITY);

        // Get all the stockFlowList where quantity greater than or equals to UPDATED_QUANTITY
        defaultStockFlowShouldNotBeFound("quantity.greaterOrEqualThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where quantity less than or equals to DEFAULT_QUANTITY
        defaultStockFlowShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the stockFlowList where quantity less than or equals to UPDATED_QUANTITY
        defaultStockFlowShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllStockFlowsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where unitPrice equals to DEFAULT_UNIT_PRICE
        defaultStockFlowShouldBeFound("unitPrice.equals=" + DEFAULT_UNIT_PRICE);

        // Get all the stockFlowList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockFlowShouldNotBeFound("unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where unitPrice in DEFAULT_UNIT_PRICE or UPDATED_UNIT_PRICE
        defaultStockFlowShouldBeFound("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE);

        // Get all the stockFlowList where unitPrice equals to UPDATED_UNIT_PRICE
        defaultStockFlowShouldNotBeFound("unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where unitPrice is not null
        defaultStockFlowShouldBeFound("unitPrice.specified=true");

        // Get all the stockFlowList where unitPrice is null
        defaultStockFlowShouldNotBeFound("unitPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where totalPrice equals to DEFAULT_TOTAL_PRICE
        defaultStockFlowShouldBeFound("totalPrice.equals=" + DEFAULT_TOTAL_PRICE);

        // Get all the stockFlowList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockFlowShouldNotBeFound("totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where totalPrice in DEFAULT_TOTAL_PRICE or UPDATED_TOTAL_PRICE
        defaultStockFlowShouldBeFound("totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE);

        // Get all the stockFlowList where totalPrice equals to UPDATED_TOTAL_PRICE
        defaultStockFlowShouldNotBeFound("totalPrice.in=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where totalPrice is not null
        defaultStockFlowShouldBeFound("totalPrice.specified=true");

        // Get all the stockFlowList where totalPrice is null
        defaultStockFlowShouldNotBeFound("totalPrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByManualEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where manualEntry equals to DEFAULT_MANUAL_ENTRY
        defaultStockFlowShouldBeFound("manualEntry.equals=" + DEFAULT_MANUAL_ENTRY);

        // Get all the stockFlowList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultStockFlowShouldNotBeFound("manualEntry.equals=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByManualEntryIsInShouldWork() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where manualEntry in DEFAULT_MANUAL_ENTRY or UPDATED_MANUAL_ENTRY
        defaultStockFlowShouldBeFound("manualEntry.in=" + DEFAULT_MANUAL_ENTRY + "," + UPDATED_MANUAL_ENTRY);

        // Get all the stockFlowList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultStockFlowShouldNotBeFound("manualEntry.in=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllStockFlowsByManualEntryIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockFlowRepository.saveAndFlush(stockFlow);

        // Get all the stockFlowList where manualEntry is not null
        defaultStockFlowShouldBeFound("manualEntry.specified=true");

        // Get all the stockFlowList where manualEntry is null
        defaultStockFlowShouldNotBeFound("manualEntry.specified=false");
    }

    @Test
    @Transactional
    public void getAllStockFlowsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockFlow.setUser(user);
        stockFlowRepository.saveAndFlush(stockFlow);
        Long userId = user.getId();

        // Get all the stockFlowList where user equals to userId
        defaultStockFlowShouldBeFound("userId.equals=" + userId);

        // Get all the stockFlowList where user equals to userId + 1
        defaultStockFlowShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllStockFlowsByBrokerageAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        BrokerageAccount brokerageAccount = BrokerageAccountResourceIntTest.createEntity(em);
        em.persist(brokerageAccount);
        em.flush();
        stockFlow.setBrokerageAccount(brokerageAccount);
        stockFlowRepository.saveAndFlush(stockFlow);
        Long brokerageAccountId = brokerageAccount.getId();

        // Get all the stockFlowList where brokerageAccount equals to brokerageAccountId
        defaultStockFlowShouldBeFound("brokerageAccountId.equals=" + brokerageAccountId);

        // Get all the stockFlowList where brokerageAccount equals to brokerageAccountId + 1
        defaultStockFlowShouldNotBeFound("brokerageAccountId.equals=" + (brokerageAccountId + 1));
    }


    @Test
    @Transactional
    public void getAllStockFlowsByTradeIsEqualToSomething() throws Exception {
        // Initialize the database
        StockTrade trade = StockTradeResourceIntTest.createEntity(em);
        em.persist(trade);
        em.flush();
        stockFlow.setTrade(trade);
        stockFlowRepository.saveAndFlush(stockFlow);
        Long tradeId = trade.getId();

        // Get all the stockFlowList where trade equals to tradeId
        defaultStockFlowShouldBeFound("tradeId.equals=" + tradeId);

        // Get all the stockFlowList where trade equals to tradeId + 1
        defaultStockFlowShouldNotBeFound("tradeId.equals=" + (tradeId + 1));
    }


    @Test
    @Transactional
    public void getAllStockFlowsByExecReportIsEqualToSomething() throws Exception {
        // Initialize the database
        ExecReport execReport = ExecReportResourceIntTest.createEntity(em);
        em.persist(execReport);
        em.flush();
        stockFlow.setExecReport(execReport);
        stockFlowRepository.saveAndFlush(stockFlow);
        Long execReportId = execReport.getId();

        // Get all the stockFlowList where execReport equals to execReportId
        defaultStockFlowShouldBeFound("execReportId.equals=" + execReportId);

        // Get all the stockFlowList where execReport equals to execReportId + 1
        defaultStockFlowShouldNotBeFound("execReportId.equals=" + (execReportId + 1));
    }


    @Test
    @Transactional
    public void getAllStockFlowsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        stockFlow.setStock(stock);
        stockFlowRepository.saveAndFlush(stockFlow);
        Long stockId = stock.getId();

        // Get all the stockFlowList where stock equals to stockId
        defaultStockFlowShouldBeFound("stockId.equals=" + stockId);

        // Get all the stockFlowList where stock equals to stockId + 1
        defaultStockFlowShouldNotBeFound("stockId.equals=" + (stockId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStockFlowShouldBeFound(String filter) throws Exception {
        restStockFlowMockMvc.perform(get("/api/stock-flows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].tradeDate").value(hasItem(sameInstant(DEFAULT_TRADE_DATE))))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())));

        // Check, that the count call also returns 1
        restStockFlowMockMvc.perform(get("/api/stock-flows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStockFlowShouldNotBeFound(String filter) throws Exception {
        restStockFlowMockMvc.perform(get("/api/stock-flows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockFlowMockMvc.perform(get("/api/stock-flows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
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
            .tradeDate(UPDATED_TRADE_DATE)
            .side(UPDATED_SIDE)
            .symbol(UPDATED_SYMBOL)
            .quantity(UPDATED_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .manualEntry(UPDATED_MANUAL_ENTRY);
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
        assertThat(testStockFlow.getTradeDate()).isEqualTo(UPDATED_TRADE_DATE);
        assertThat(testStockFlow.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testStockFlow.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testStockFlow.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockFlow.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testStockFlow.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testStockFlow.isManualEntry()).isEqualTo(UPDATED_MANUAL_ENTRY);
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
