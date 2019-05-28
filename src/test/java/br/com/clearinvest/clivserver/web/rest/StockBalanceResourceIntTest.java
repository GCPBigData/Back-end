package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockBalance;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.repository.StockBalanceRepository;
import br.com.clearinvest.clivserver.service.StockBalanceService;
import br.com.clearinvest.clivserver.service.dto.StockBalanceDTO;
import br.com.clearinvest.clivserver.service.mapper.StockBalanceMapper;
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
import java.time.LocalDate;
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
 * Test class for the StockBalanceResource REST controller.
 *
 * @see StockBalanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class StockBalanceResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SYMBOL = "AAAAAAAA";
    private static final String UPDATED_SYMBOL = "BBBBBBBB";

    private static final LocalDate DEFAULT_DAY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DAY = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_BALANCE = new BigDecimal(2);

    @Autowired
    private StockBalanceRepository stockBalanceRepository;

    @Autowired
    private StockBalanceMapper stockBalanceMapper;

    @Autowired
    private StockBalanceService stockBalanceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockBalanceMockMvc;

    private StockBalance stockBalance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockBalanceResource stockBalanceResource = new StockBalanceResource(stockBalanceService);
        this.restStockBalanceMockMvc = MockMvcBuilders.standaloneSetup(stockBalanceResource)
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
    public static StockBalance createEntity(EntityManager em) {
        StockBalance stockBalance = new StockBalance()
            .createdAt(DEFAULT_CREATED_AT)
            .symbol(DEFAULT_SYMBOL)
            .day(DEFAULT_DAY)
            .balance(DEFAULT_BALANCE);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockBalance.setUser(user);
        // Add required entity
        Stock stock = StockResourceIntTest.createEntity(em);
        em.persist(stock);
        em.flush();
        stockBalance.setStock(stock);
        return stockBalance;
    }

    @Before
    public void initTest() {
        stockBalance = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockBalance() throws Exception {
        int databaseSizeBeforeCreate = stockBalanceRepository.findAll().size();

        // Create the StockBalance
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);
        restStockBalanceMockMvc.perform(post("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isCreated());

        // Validate the StockBalance in the database
        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeCreate + 1);
        StockBalance testStockBalance = stockBalanceList.get(stockBalanceList.size() - 1);
        assertThat(testStockBalance.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStockBalance.getSymbol()).isEqualTo(DEFAULT_SYMBOL);
        assertThat(testStockBalance.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testStockBalance.getBalance()).isEqualTo(DEFAULT_BALANCE);
    }

    @Test
    @Transactional
    public void createStockBalanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockBalanceRepository.findAll().size();

        // Create the StockBalance with an existing ID
        stockBalance.setId(1L);
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockBalanceMockMvc.perform(post("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockBalance in the database
        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSymbolIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBalanceRepository.findAll().size();
        // set the field null
        stockBalance.setSymbol(null);

        // Create the StockBalance, which fails.
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);

        restStockBalanceMockMvc.perform(post("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isBadRequest());

        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBalanceRepository.findAll().size();
        // set the field null
        stockBalance.setDay(null);

        // Create the StockBalance, which fails.
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);

        restStockBalanceMockMvc.perform(post("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isBadRequest());

        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBalanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockBalanceRepository.findAll().size();
        // set the field null
        stockBalance.setBalance(null);

        // Create the StockBalance, which fails.
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);

        restStockBalanceMockMvc.perform(post("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isBadRequest());

        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockBalances() throws Exception {
        // Initialize the database
        stockBalanceRepository.saveAndFlush(stockBalance);

        // Get all the stockBalanceList
        restStockBalanceMockMvc.perform(get("/api/stock-balances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockBalance.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].symbol").value(hasItem(DEFAULT_SYMBOL.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE.intValue())));
    }
    
    @Test
    @Transactional
    public void getStockBalance() throws Exception {
        // Initialize the database
        stockBalanceRepository.saveAndFlush(stockBalance);

        // Get the stockBalance
        restStockBalanceMockMvc.perform(get("/api/stock-balances/{id}", stockBalance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockBalance.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.symbol").value(DEFAULT_SYMBOL.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingStockBalance() throws Exception {
        // Get the stockBalance
        restStockBalanceMockMvc.perform(get("/api/stock-balances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockBalance() throws Exception {
        // Initialize the database
        stockBalanceRepository.saveAndFlush(stockBalance);

        int databaseSizeBeforeUpdate = stockBalanceRepository.findAll().size();

        // Update the stockBalance
        StockBalance updatedStockBalance = stockBalanceRepository.findById(stockBalance.getId()).get();
        // Disconnect from session so that the updates on updatedStockBalance are not directly saved in db
        em.detach(updatedStockBalance);
        updatedStockBalance
            .createdAt(UPDATED_CREATED_AT)
            .symbol(UPDATED_SYMBOL)
            .day(UPDATED_DAY)
            .balance(UPDATED_BALANCE);
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(updatedStockBalance);

        restStockBalanceMockMvc.perform(put("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isOk());

        // Validate the StockBalance in the database
        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeUpdate);
        StockBalance testStockBalance = stockBalanceList.get(stockBalanceList.size() - 1);
        assertThat(testStockBalance.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStockBalance.getSymbol()).isEqualTo(UPDATED_SYMBOL);
        assertThat(testStockBalance.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testStockBalance.getBalance()).isEqualTo(UPDATED_BALANCE);
    }

    @Test
    @Transactional
    public void updateNonExistingStockBalance() throws Exception {
        int databaseSizeBeforeUpdate = stockBalanceRepository.findAll().size();

        // Create the StockBalance
        StockBalanceDTO stockBalanceDTO = stockBalanceMapper.toDto(stockBalance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockBalanceMockMvc.perform(put("/api/stock-balances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockBalanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockBalance in the database
        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockBalance() throws Exception {
        // Initialize the database
        stockBalanceRepository.saveAndFlush(stockBalance);

        int databaseSizeBeforeDelete = stockBalanceRepository.findAll().size();

        // Get the stockBalance
        restStockBalanceMockMvc.perform(delete("/api/stock-balances/{id}", stockBalance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockBalance> stockBalanceList = stockBalanceRepository.findAll();
        assertThat(stockBalanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockBalance.class);
        StockBalance stockBalance1 = new StockBalance();
        stockBalance1.setId(1L);
        StockBalance stockBalance2 = new StockBalance();
        stockBalance2.setId(stockBalance1.getId());
        assertThat(stockBalance1).isEqualTo(stockBalance2);
        stockBalance2.setId(2L);
        assertThat(stockBalance1).isNotEqualTo(stockBalance2);
        stockBalance1.setId(null);
        assertThat(stockBalance1).isNotEqualTo(stockBalance2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockBalanceDTO.class);
        StockBalanceDTO stockBalanceDTO1 = new StockBalanceDTO();
        stockBalanceDTO1.setId(1L);
        StockBalanceDTO stockBalanceDTO2 = new StockBalanceDTO();
        assertThat(stockBalanceDTO1).isNotEqualTo(stockBalanceDTO2);
        stockBalanceDTO2.setId(stockBalanceDTO1.getId());
        assertThat(stockBalanceDTO1).isEqualTo(stockBalanceDTO2);
        stockBalanceDTO2.setId(2L);
        assertThat(stockBalanceDTO1).isNotEqualTo(stockBalanceDTO2);
        stockBalanceDTO1.setId(null);
        assertThat(stockBalanceDTO1).isNotEqualTo(stockBalanceDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockBalanceMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockBalanceMapper.fromId(null)).isNull();
    }
}
