package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import br.com.clearinvest.clivserver.service.StockOrderService;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;
import br.com.clearinvest.clivserver.service.mapper.StockOrderMapper;
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
 * Test class for the StockOrderResource REST controller.
 *
 * @see StockOrderResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class StockOrderResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Long DEFAULT_DAY_SEQ = 1L;
    private static final Long UPDATED_DAY_SEQ = 2L;

    private static final String DEFAULT_KIND = "AAAAAAAAAA";
    private static final String UPDATED_KIND = "BBBBBBBBBB";

    private static final String DEFAULT_ORDER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ORDER_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SIDE = "A";
    private static final String UPDATED_SIDE = "B";

    private static final String DEFAULT_TIME_IN_FORCE = "A";
    private static final String UPDATED_TIME_IN_FORCE = "B";

    private static final ZonedDateTime DEFAULT_EXPIRE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EXPIRE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_OPERATION_TYPE = "A";
    private static final String UPDATED_OPERATION_TYPE = "B";

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;

    private static final Long DEFAULT_EXEC_QUANTITY = 1L;
    private static final Long UPDATED_EXEC_QUANTITY = 2L;

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(0.01);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_AVERAGE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVERAGE_PRICE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);

    private static final String DEFAULT_OMS_ORDER_ID = "AAAAAAAAAA";
    private static final String UPDATED_OMS_ORDER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_LAST_EXEC_REPORT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_EXEC_REPORT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_LAST_EXEC_REPORT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_LAST_EXEC_REPORT_DESCR = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY_IP = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY_IP = "BBBBBBBBBB";

    @Autowired
    private StockOrderRepository stockOrderRepository;

    @Autowired
    private StockOrderMapper stockOrderMapper;

    @Autowired
    private StockOrderService stockOrderService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStockOrderMockMvc;

    private StockOrder stockOrder;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StockOrderResource stockOrderResource = new StockOrderResource(stockOrderService);
        this.restStockOrderMockMvc = MockMvcBuilders.standaloneSetup(stockOrderResource)
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
    public static StockOrder createEntity(EntityManager em) {
        StockOrder stockOrder = new StockOrder()
            .createdAt(DEFAULT_CREATED_AT)
            .daySeq(DEFAULT_DAY_SEQ)
            .kind(DEFAULT_KIND)
            .orderType(DEFAULT_ORDER_TYPE)
            .side(DEFAULT_SIDE)
            .timeInForce(DEFAULT_TIME_IN_FORCE)
            .expireTime(DEFAULT_EXPIRE_TIME)
            .operationType(DEFAULT_OPERATION_TYPE)
            .quantity(DEFAULT_QUANTITY)
            .execQuantity(DEFAULT_EXEC_QUANTITY)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .averagePrice(DEFAULT_AVERAGE_PRICE)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .omsOrderId(DEFAULT_OMS_ORDER_ID)
            .status(DEFAULT_STATUS)
            .lastExecReportTime(DEFAULT_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(DEFAULT_LAST_EXEC_REPORT_DESCR)
            .createdByIp(DEFAULT_CREATED_BY_IP);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        stockOrder.setCreatedBy(user);
        // Add required entity
        StockTrade stockTrade = StockTradeResourceIntTest.createEntity(em);
        em.persist(stockTrade);
        em.flush();
        stockOrder.setTrade(stockTrade);
        return stockOrder;
    }

    @Before
    public void initTest() {
        stockOrder = createEntity(em);
    }

    @Test
    @Transactional
    public void createStockOrder() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isCreated());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate + 1);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testStockOrder.getDaySeq()).isEqualTo(DEFAULT_DAY_SEQ);
        assertThat(testStockOrder.getKind()).isEqualTo(DEFAULT_KIND);
        assertThat(testStockOrder.getOrderType()).isEqualTo(DEFAULT_ORDER_TYPE);
        assertThat(testStockOrder.getSide()).isEqualTo(DEFAULT_SIDE);
        assertThat(testStockOrder.getTimeInForce()).isEqualTo(DEFAULT_TIME_IN_FORCE);
        assertThat(testStockOrder.getExpireTime()).isEqualTo(DEFAULT_EXPIRE_TIME);
        assertThat(testStockOrder.getOperationType()).isEqualTo(DEFAULT_OPERATION_TYPE);
        assertThat(testStockOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testStockOrder.getExecQuantity()).isEqualTo(DEFAULT_EXEC_QUANTITY);
        assertThat(testStockOrder.getUnitPrice()).isEqualTo(DEFAULT_UNIT_PRICE);
        assertThat(testStockOrder.getAveragePrice()).isEqualTo(DEFAULT_AVERAGE_PRICE);
        assertThat(testStockOrder.getTotalPrice()).isEqualTo(DEFAULT_TOTAL_PRICE);
        assertThat(testStockOrder.getOmsOrderId()).isEqualTo(DEFAULT_OMS_ORDER_ID);
        assertThat(testStockOrder.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStockOrder.getLastExecReportTime()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_TIME);
        assertThat(testStockOrder.getLastExecReportDescr()).isEqualTo(DEFAULT_LAST_EXEC_REPORT_DESCR);
        assertThat(testStockOrder.getCreatedByIp()).isEqualTo(DEFAULT_CREATED_BY_IP);
    }

    @Test
    @Transactional
    public void createStockOrderWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockOrderRepository.findAll().size();

        // Create the StockOrder with an existing ID
        stockOrder.setId(1L);
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkKindIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setKind(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSideIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setSide(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setQuantity(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUnitPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setUnitPrice(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockOrderRepository.findAll().size();
        // set the field null
        stockOrder.setStatus(null);

        // Create the StockOrder, which fails.
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        restStockOrderMockMvc.perform(post("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStockOrders() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get all the stockOrderList
        restStockOrderMockMvc.perform(get("/api/stock-orders?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stockOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].daySeq").value(hasItem(DEFAULT_DAY_SEQ.intValue())))
            .andExpect(jsonPath("$.[*].kind").value(hasItem(DEFAULT_KIND.toString())))
            .andExpect(jsonPath("$.[*].orderType").value(hasItem(DEFAULT_ORDER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].side").value(hasItem(DEFAULT_SIDE.toString())))
            .andExpect(jsonPath("$.[*].timeInForce").value(hasItem(DEFAULT_TIME_IN_FORCE.toString())))
            .andExpect(jsonPath("$.[*].expireTime").value(hasItem(sameInstant(DEFAULT_EXPIRE_TIME))))
            .andExpect(jsonPath("$.[*].operationType").value(hasItem(DEFAULT_OPERATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].execQuantity").value(hasItem(DEFAULT_EXEC_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(DEFAULT_UNIT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].averagePrice").value(hasItem(DEFAULT_AVERAGE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(DEFAULT_TOTAL_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].omsOrderId").value(hasItem(DEFAULT_OMS_ORDER_ID.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastExecReportTime").value(hasItem(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME))))
            .andExpect(jsonPath("$.[*].lastExecReportDescr").value(hasItem(DEFAULT_LAST_EXEC_REPORT_DESCR.toString())))
            .andExpect(jsonPath("$.[*].createdByIp").value(hasItem(DEFAULT_CREATED_BY_IP.toString())));
    }
    
    @Test
    @Transactional
    public void getStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", stockOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(stockOrder.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.daySeq").value(DEFAULT_DAY_SEQ.intValue()))
            .andExpect(jsonPath("$.kind").value(DEFAULT_KIND.toString()))
            .andExpect(jsonPath("$.orderType").value(DEFAULT_ORDER_TYPE.toString()))
            .andExpect(jsonPath("$.side").value(DEFAULT_SIDE.toString()))
            .andExpect(jsonPath("$.timeInForce").value(DEFAULT_TIME_IN_FORCE.toString()))
            .andExpect(jsonPath("$.expireTime").value(sameInstant(DEFAULT_EXPIRE_TIME)))
            .andExpect(jsonPath("$.operationType").value(DEFAULT_OPERATION_TYPE.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.execQuantity").value(DEFAULT_EXEC_QUANTITY.intValue()))
            .andExpect(jsonPath("$.unitPrice").value(DEFAULT_UNIT_PRICE.intValue()))
            .andExpect(jsonPath("$.averagePrice").value(DEFAULT_AVERAGE_PRICE.intValue()))
            .andExpect(jsonPath("$.totalPrice").value(DEFAULT_TOTAL_PRICE.intValue()))
            .andExpect(jsonPath("$.omsOrderId").value(DEFAULT_OMS_ORDER_ID.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.lastExecReportTime").value(sameInstant(DEFAULT_LAST_EXEC_REPORT_TIME)))
            .andExpect(jsonPath("$.lastExecReportDescr").value(DEFAULT_LAST_EXEC_REPORT_DESCR.toString()))
            .andExpect(jsonPath("$.createdByIp").value(DEFAULT_CREATED_BY_IP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStockOrder() throws Exception {
        // Get the stockOrder
        restStockOrderMockMvc.perform(get("/api/stock-orders/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Update the stockOrder
        StockOrder updatedStockOrder = stockOrderRepository.findById(stockOrder.getId()).get();
        // Disconnect from session so that the updates on updatedStockOrder are not directly saved in db
        em.detach(updatedStockOrder);
        updatedStockOrder
            .createdAt(UPDATED_CREATED_AT)
            .daySeq(UPDATED_DAY_SEQ)
            .kind(UPDATED_KIND)
            .orderType(UPDATED_ORDER_TYPE)
            .side(UPDATED_SIDE)
            .timeInForce(UPDATED_TIME_IN_FORCE)
            .expireTime(UPDATED_EXPIRE_TIME)
            .operationType(UPDATED_OPERATION_TYPE)
            .quantity(UPDATED_QUANTITY)
            .execQuantity(UPDATED_EXEC_QUANTITY)
            .unitPrice(UPDATED_UNIT_PRICE)
            .averagePrice(UPDATED_AVERAGE_PRICE)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .omsOrderId(UPDATED_OMS_ORDER_ID)
            .status(UPDATED_STATUS)
            .lastExecReportTime(UPDATED_LAST_EXEC_REPORT_TIME)
            .lastExecReportDescr(UPDATED_LAST_EXEC_REPORT_DESCR)
            .createdByIp(UPDATED_CREATED_BY_IP);
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(updatedStockOrder);

        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isOk());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);
        StockOrder testStockOrder = stockOrderList.get(stockOrderList.size() - 1);
        assertThat(testStockOrder.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testStockOrder.getDaySeq()).isEqualTo(UPDATED_DAY_SEQ);
        assertThat(testStockOrder.getKind()).isEqualTo(UPDATED_KIND);
        assertThat(testStockOrder.getOrderType()).isEqualTo(UPDATED_ORDER_TYPE);
        assertThat(testStockOrder.getSide()).isEqualTo(UPDATED_SIDE);
        assertThat(testStockOrder.getTimeInForce()).isEqualTo(UPDATED_TIME_IN_FORCE);
        assertThat(testStockOrder.getExpireTime()).isEqualTo(UPDATED_EXPIRE_TIME);
        assertThat(testStockOrder.getOperationType()).isEqualTo(UPDATED_OPERATION_TYPE);
        assertThat(testStockOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testStockOrder.getExecQuantity()).isEqualTo(UPDATED_EXEC_QUANTITY);
        assertThat(testStockOrder.getUnitPrice()).isEqualTo(UPDATED_UNIT_PRICE);
        assertThat(testStockOrder.getAveragePrice()).isEqualTo(UPDATED_AVERAGE_PRICE);
        assertThat(testStockOrder.getTotalPrice()).isEqualTo(UPDATED_TOTAL_PRICE);
        assertThat(testStockOrder.getOmsOrderId()).isEqualTo(UPDATED_OMS_ORDER_ID);
        assertThat(testStockOrder.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStockOrder.getLastExecReportTime()).isEqualTo(UPDATED_LAST_EXEC_REPORT_TIME);
        assertThat(testStockOrder.getLastExecReportDescr()).isEqualTo(UPDATED_LAST_EXEC_REPORT_DESCR);
        assertThat(testStockOrder.getCreatedByIp()).isEqualTo(UPDATED_CREATED_BY_IP);
    }

    @Test
    @Transactional
    public void updateNonExistingStockOrder() throws Exception {
        int databaseSizeBeforeUpdate = stockOrderRepository.findAll().size();

        // Create the StockOrder
        StockOrderDTO stockOrderDTO = stockOrderMapper.toDto(stockOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockOrderMockMvc.perform(put("/api/stock-orders")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(stockOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the StockOrder in the database
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStockOrder() throws Exception {
        // Initialize the database
        stockOrderRepository.saveAndFlush(stockOrder);

        int databaseSizeBeforeDelete = stockOrderRepository.findAll().size();

        // Get the stockOrder
        restStockOrderMockMvc.perform(delete("/api/stock-orders/{id}", stockOrder.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<StockOrder> stockOrderList = stockOrderRepository.findAll();
        assertThat(stockOrderList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrder.class);
        StockOrder stockOrder1 = new StockOrder();
        stockOrder1.setId(1L);
        StockOrder stockOrder2 = new StockOrder();
        stockOrder2.setId(stockOrder1.getId());
        assertThat(stockOrder1).isEqualTo(stockOrder2);
        stockOrder2.setId(2L);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
        stockOrder1.setId(null);
        assertThat(stockOrder1).isNotEqualTo(stockOrder2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StockOrderDTO.class);
        StockOrderDTO stockOrderDTO1 = new StockOrderDTO();
        stockOrderDTO1.setId(1L);
        StockOrderDTO stockOrderDTO2 = new StockOrderDTO();
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
        stockOrderDTO2.setId(stockOrderDTO1.getId());
        assertThat(stockOrderDTO1).isEqualTo(stockOrderDTO2);
        stockOrderDTO2.setId(2L);
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
        stockOrderDTO1.setId(null);
        assertThat(stockOrderDTO1).isNotEqualTo(stockOrderDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(stockOrderMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(stockOrderMapper.fromId(null)).isNull();
    }
}
