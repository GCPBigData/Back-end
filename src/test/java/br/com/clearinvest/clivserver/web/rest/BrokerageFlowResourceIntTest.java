package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.BrokerageFlow;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.StockTrade;
import br.com.clearinvest.clivserver.repository.BrokerageFlowRepository;
import br.com.clearinvest.clivserver.service.BrokerageFlowService;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageFlowMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowCriteria;
import br.com.clearinvest.clivserver.service.BrokerageFlowQueryService;

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
 * Test class for the BrokerageFlowResource REST controller.
 *
 * @see BrokerageFlowResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageFlowResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_FLOW_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FLOW_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BigDecimal DEFAULT_VALUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALUE = new BigDecimal(2);

    private static final Boolean DEFAULT_MANUAL_ENTRY = false;
    private static final Boolean UPDATED_MANUAL_ENTRY = true;

    @Autowired
    private BrokerageFlowRepository brokerageFlowRepository;

    @Autowired
    private BrokerageFlowMapper brokerageFlowMapper;

    @Autowired
    private BrokerageFlowService brokerageFlowService;

    @Autowired
    private BrokerageFlowQueryService brokerageFlowQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageFlowMockMvc;

    private BrokerageFlow brokerageFlow;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageFlowResource brokerageFlowResource = new BrokerageFlowResource(brokerageFlowService, brokerageFlowQueryService);
        this.restBrokerageFlowMockMvc = MockMvcBuilders.standaloneSetup(brokerageFlowResource)
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
    public static BrokerageFlow createEntity(EntityManager em) {
        BrokerageFlow brokerageFlow = new BrokerageFlow()
            .createdAt(DEFAULT_CREATED_AT)
            .flowDate(DEFAULT_FLOW_DATE)
            .value(DEFAULT_VALUE)
            .manualEntry(DEFAULT_MANUAL_ENTRY);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        brokerageFlow.setUser(user);
        return brokerageFlow;
    }

    @Before
    public void initTest() {
        brokerageFlow = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerageFlow() throws Exception {
        int databaseSizeBeforeCreate = brokerageFlowRepository.findAll().size();

        // Create the BrokerageFlow
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);
        restBrokerageFlowMockMvc.perform(post("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isCreated());

        // Validate the BrokerageFlow in the database
        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeCreate + 1);
        BrokerageFlow testBrokerageFlow = brokerageFlowList.get(brokerageFlowList.size() - 1);
        assertThat(testBrokerageFlow.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testBrokerageFlow.getFlowDate()).isEqualTo(DEFAULT_FLOW_DATE);
        assertThat(testBrokerageFlow.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testBrokerageFlow.isManualEntry()).isEqualTo(DEFAULT_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void createBrokerageFlowWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageFlowRepository.findAll().size();

        // Create the BrokerageFlow with an existing ID
        brokerageFlow.setId(1L);
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageFlowMockMvc.perform(post("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageFlow in the database
        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkFlowDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageFlowRepository.findAll().size();
        // set the field null
        brokerageFlow.setFlowDate(null);

        // Create the BrokerageFlow, which fails.
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);

        restBrokerageFlowMockMvc.perform(post("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageFlowRepository.findAll().size();
        // set the field null
        brokerageFlow.setValue(null);

        // Create the BrokerageFlow, which fails.
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);

        restBrokerageFlowMockMvc.perform(post("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkManualEntryIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageFlowRepository.findAll().size();
        // set the field null
        brokerageFlow.setManualEntry(null);

        // Create the BrokerageFlow, which fails.
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);

        restBrokerageFlowMockMvc.perform(post("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlows() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].flowDate").value(hasItem(sameInstant(DEFAULT_FLOW_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getBrokerageFlow() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get the brokerageFlow
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows/{id}", brokerageFlow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerageFlow.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.flowDate").value(sameInstant(DEFAULT_FLOW_DATE)))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.intValue()))
            .andExpect(jsonPath("$.manualEntry").value(DEFAULT_MANUAL_ENTRY.booleanValue()));
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where createdAt equals to DEFAULT_CREATED_AT
        defaultBrokerageFlowShouldBeFound("createdAt.equals=" + DEFAULT_CREATED_AT);

        // Get all the brokerageFlowList where createdAt equals to UPDATED_CREATED_AT
        defaultBrokerageFlowShouldNotBeFound("createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where createdAt in DEFAULT_CREATED_AT or UPDATED_CREATED_AT
        defaultBrokerageFlowShouldBeFound("createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT);

        // Get all the brokerageFlowList where createdAt equals to UPDATED_CREATED_AT
        defaultBrokerageFlowShouldNotBeFound("createdAt.in=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where createdAt is not null
        defaultBrokerageFlowShouldBeFound("createdAt.specified=true");

        // Get all the brokerageFlowList where createdAt is null
        defaultBrokerageFlowShouldNotBeFound("createdAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where createdAt greater than or equals to DEFAULT_CREATED_AT
        defaultBrokerageFlowShouldBeFound("createdAt.greaterOrEqualThan=" + DEFAULT_CREATED_AT);

        // Get all the brokerageFlowList where createdAt greater than or equals to UPDATED_CREATED_AT
        defaultBrokerageFlowShouldNotBeFound("createdAt.greaterOrEqualThan=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where createdAt less than or equals to DEFAULT_CREATED_AT
        defaultBrokerageFlowShouldNotBeFound("createdAt.lessThan=" + DEFAULT_CREATED_AT);

        // Get all the brokerageFlowList where createdAt less than or equals to UPDATED_CREATED_AT
        defaultBrokerageFlowShouldBeFound("createdAt.lessThan=" + UPDATED_CREATED_AT);
    }


    @Test
    @Transactional
    public void getAllBrokerageFlowsByFlowDateIsEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where flowDate equals to DEFAULT_FLOW_DATE
        defaultBrokerageFlowShouldBeFound("flowDate.equals=" + DEFAULT_FLOW_DATE);

        // Get all the brokerageFlowList where flowDate equals to UPDATED_FLOW_DATE
        defaultBrokerageFlowShouldNotBeFound("flowDate.equals=" + UPDATED_FLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByFlowDateIsInShouldWork() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where flowDate in DEFAULT_FLOW_DATE or UPDATED_FLOW_DATE
        defaultBrokerageFlowShouldBeFound("flowDate.in=" + DEFAULT_FLOW_DATE + "," + UPDATED_FLOW_DATE);

        // Get all the brokerageFlowList where flowDate equals to UPDATED_FLOW_DATE
        defaultBrokerageFlowShouldNotBeFound("flowDate.in=" + UPDATED_FLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByFlowDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where flowDate is not null
        defaultBrokerageFlowShouldBeFound("flowDate.specified=true");

        // Get all the brokerageFlowList where flowDate is null
        defaultBrokerageFlowShouldNotBeFound("flowDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByFlowDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where flowDate greater than or equals to DEFAULT_FLOW_DATE
        defaultBrokerageFlowShouldBeFound("flowDate.greaterOrEqualThan=" + DEFAULT_FLOW_DATE);

        // Get all the brokerageFlowList where flowDate greater than or equals to UPDATED_FLOW_DATE
        defaultBrokerageFlowShouldNotBeFound("flowDate.greaterOrEqualThan=" + UPDATED_FLOW_DATE);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByFlowDateIsLessThanSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where flowDate less than or equals to DEFAULT_FLOW_DATE
        defaultBrokerageFlowShouldNotBeFound("flowDate.lessThan=" + DEFAULT_FLOW_DATE);

        // Get all the brokerageFlowList where flowDate less than or equals to UPDATED_FLOW_DATE
        defaultBrokerageFlowShouldBeFound("flowDate.lessThan=" + UPDATED_FLOW_DATE);
    }


    @Test
    @Transactional
    public void getAllBrokerageFlowsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where value equals to DEFAULT_VALUE
        defaultBrokerageFlowShouldBeFound("value.equals=" + DEFAULT_VALUE);

        // Get all the brokerageFlowList where value equals to UPDATED_VALUE
        defaultBrokerageFlowShouldNotBeFound("value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where value in DEFAULT_VALUE or UPDATED_VALUE
        defaultBrokerageFlowShouldBeFound("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE);

        // Get all the brokerageFlowList where value equals to UPDATED_VALUE
        defaultBrokerageFlowShouldNotBeFound("value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where value is not null
        defaultBrokerageFlowShouldBeFound("value.specified=true");

        // Get all the brokerageFlowList where value is null
        defaultBrokerageFlowShouldNotBeFound("value.specified=false");
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByManualEntryIsEqualToSomething() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where manualEntry equals to DEFAULT_MANUAL_ENTRY
        defaultBrokerageFlowShouldBeFound("manualEntry.equals=" + DEFAULT_MANUAL_ENTRY);

        // Get all the brokerageFlowList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultBrokerageFlowShouldNotBeFound("manualEntry.equals=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByManualEntryIsInShouldWork() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where manualEntry in DEFAULT_MANUAL_ENTRY or UPDATED_MANUAL_ENTRY
        defaultBrokerageFlowShouldBeFound("manualEntry.in=" + DEFAULT_MANUAL_ENTRY + "," + UPDATED_MANUAL_ENTRY);

        // Get all the brokerageFlowList where manualEntry equals to UPDATED_MANUAL_ENTRY
        defaultBrokerageFlowShouldNotBeFound("manualEntry.in=" + UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByManualEntryIsNullOrNotNull() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        // Get all the brokerageFlowList where manualEntry is not null
        defaultBrokerageFlowShouldBeFound("manualEntry.specified=true");

        // Get all the brokerageFlowList where manualEntry is null
        defaultBrokerageFlowShouldNotBeFound("manualEntry.specified=false");
    }

    @Test
    @Transactional
    public void getAllBrokerageFlowsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        brokerageFlow.setUser(user);
        brokerageFlowRepository.saveAndFlush(brokerageFlow);
        Long userId = user.getId();

        // Get all the brokerageFlowList where user equals to userId
        defaultBrokerageFlowShouldBeFound("userId.equals=" + userId);

        // Get all the brokerageFlowList where user equals to userId + 1
        defaultBrokerageFlowShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllBrokerageFlowsByBrokerageAccountIsEqualToSomething() throws Exception {
        // Initialize the database
        BrokerageAccount brokerageAccount = BrokerageAccountResourceIntTest.createEntity(em);
        em.persist(brokerageAccount);
        em.flush();
        brokerageFlow.setBrokerageAccount(brokerageAccount);
        brokerageFlowRepository.saveAndFlush(brokerageFlow);
        Long brokerageAccountId = brokerageAccount.getId();

        // Get all the brokerageFlowList where brokerageAccount equals to brokerageAccountId
        defaultBrokerageFlowShouldBeFound("brokerageAccountId.equals=" + brokerageAccountId);

        // Get all the brokerageFlowList where brokerageAccount equals to brokerageAccountId + 1
        defaultBrokerageFlowShouldNotBeFound("brokerageAccountId.equals=" + (brokerageAccountId + 1));
    }


    @Test
    @Transactional
    public void getAllBrokerageFlowsByTradeIsEqualToSomething() throws Exception {
        // Initialize the database
        StockTrade trade = StockTradeResourceIntTest.createEntity(em);
        em.persist(trade);
        em.flush();
        brokerageFlow.setTrade(trade);
        brokerageFlowRepository.saveAndFlush(brokerageFlow);
        Long tradeId = trade.getId();

        // Get all the brokerageFlowList where trade equals to tradeId
        defaultBrokerageFlowShouldBeFound("tradeId.equals=" + tradeId);

        // Get all the brokerageFlowList where trade equals to tradeId + 1
        defaultBrokerageFlowShouldNotBeFound("tradeId.equals=" + (tradeId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultBrokerageFlowShouldBeFound(String filter) throws Exception {
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageFlow.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].flowDate").value(hasItem(sameInstant(DEFAULT_FLOW_DATE))))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].manualEntry").value(hasItem(DEFAULT_MANUAL_ENTRY.booleanValue())));

        // Check, that the count call also returns 1
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultBrokerageFlowShouldNotBeFound(String filter) throws Exception {
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingBrokerageFlow() throws Exception {
        // Get the brokerageFlow
        restBrokerageFlowMockMvc.perform(get("/api/brokerage-flows/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerageFlow() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        int databaseSizeBeforeUpdate = brokerageFlowRepository.findAll().size();

        // Update the brokerageFlow
        BrokerageFlow updatedBrokerageFlow = brokerageFlowRepository.findById(brokerageFlow.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerageFlow are not directly saved in db
        em.detach(updatedBrokerageFlow);
        updatedBrokerageFlow
            .createdAt(UPDATED_CREATED_AT)
            .flowDate(UPDATED_FLOW_DATE)
            .value(UPDATED_VALUE)
            .manualEntry(UPDATED_MANUAL_ENTRY);
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(updatedBrokerageFlow);

        restBrokerageFlowMockMvc.perform(put("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isOk());

        // Validate the BrokerageFlow in the database
        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeUpdate);
        BrokerageFlow testBrokerageFlow = brokerageFlowList.get(brokerageFlowList.size() - 1);
        assertThat(testBrokerageFlow.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testBrokerageFlow.getFlowDate()).isEqualTo(UPDATED_FLOW_DATE);
        assertThat(testBrokerageFlow.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testBrokerageFlow.isManualEntry()).isEqualTo(UPDATED_MANUAL_ENTRY);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerageFlow() throws Exception {
        int databaseSizeBeforeUpdate = brokerageFlowRepository.findAll().size();

        // Create the BrokerageFlow
        BrokerageFlowDTO brokerageFlowDTO = brokerageFlowMapper.toDto(brokerageFlow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageFlowMockMvc.perform(put("/api/brokerage-flows")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageFlowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageFlow in the database
        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerageFlow() throws Exception {
        // Initialize the database
        brokerageFlowRepository.saveAndFlush(brokerageFlow);

        int databaseSizeBeforeDelete = brokerageFlowRepository.findAll().size();

        // Get the brokerageFlow
        restBrokerageFlowMockMvc.perform(delete("/api/brokerage-flows/{id}", brokerageFlow.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerageFlow> brokerageFlowList = brokerageFlowRepository.findAll();
        assertThat(brokerageFlowList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageFlow.class);
        BrokerageFlow brokerageFlow1 = new BrokerageFlow();
        brokerageFlow1.setId(1L);
        BrokerageFlow brokerageFlow2 = new BrokerageFlow();
        brokerageFlow2.setId(brokerageFlow1.getId());
        assertThat(brokerageFlow1).isEqualTo(brokerageFlow2);
        brokerageFlow2.setId(2L);
        assertThat(brokerageFlow1).isNotEqualTo(brokerageFlow2);
        brokerageFlow1.setId(null);
        assertThat(brokerageFlow1).isNotEqualTo(brokerageFlow2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageFlowDTO.class);
        BrokerageFlowDTO brokerageFlowDTO1 = new BrokerageFlowDTO();
        brokerageFlowDTO1.setId(1L);
        BrokerageFlowDTO brokerageFlowDTO2 = new BrokerageFlowDTO();
        assertThat(brokerageFlowDTO1).isNotEqualTo(brokerageFlowDTO2);
        brokerageFlowDTO2.setId(brokerageFlowDTO1.getId());
        assertThat(brokerageFlowDTO1).isEqualTo(brokerageFlowDTO2);
        brokerageFlowDTO2.setId(2L);
        assertThat(brokerageFlowDTO1).isNotEqualTo(brokerageFlowDTO2);
        brokerageFlowDTO1.setId(null);
        assertThat(brokerageFlowDTO1).isNotEqualTo(brokerageFlowDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageFlowMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageFlowMapper.fromId(null)).isNull();
    }
}
