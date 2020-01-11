package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.Brokerage;
import br.com.clearinvest.clivserver.repository.BrokerageRepository;
import br.com.clearinvest.clivserver.service.BrokerageService;
import br.com.clearinvest.clivserver.service.dto.BrokerageDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageMapper;
import br.com.clearinvest.clivserver.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


import static br.com.clearinvest.clivserver.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the BrokerageResource REST controller.
 *
 * @see BrokerageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CNPJ = "AAAAAAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_NEIGHBORHOOD = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_NEIGHBORHOOD = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_CITY = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_STATE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_STATE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SWING_TRADE = false;
    private static final Boolean UPDATED_SWING_TRADE = true;

    private static final Boolean DEFAULT_DAY_TRADE = false;
    private static final Boolean UPDATED_DAY_TRADE = true;

    private static final Boolean DEFAULT_LOGIN_EMAIL = false;
    private static final Boolean UPDATED_LOGIN_EMAIL = true;

    private static final Boolean DEFAULT_LOGIN_ACCESS_CODE = false;
    private static final Boolean UPDATED_LOGIN_ACCESS_CODE = true;

    private static final Boolean DEFAULT_LOGIN_CPF = false;
    private static final Boolean UPDATED_LOGIN_CPF = true;

    private static final Boolean DEFAULT_LOGIN_PASSWORD = false;
    private static final Boolean UPDATED_LOGIN_PASSWORD = true;

    private static final Boolean DEFAULT_LOGIN_TOKEN = false;
    private static final Boolean UPDATED_LOGIN_TOKEN = true;

    private static final BigDecimal DEFAULT_FEE = new BigDecimal(0);
    private static final BigDecimal UPDATED_FEE = new BigDecimal(1);

    private static final BigDecimal DEFAULT_ISS = new BigDecimal(0);
    private static final BigDecimal UPDATED_ISS = new BigDecimal(1);

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBSITE = "AAAAAAAAAA";
    private static final String UPDATED_WEBSITE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private BrokerageRepository brokerageRepository;

    @Mock
    private BrokerageRepository brokerageRepositoryMock;

    @Autowired
    private BrokerageMapper brokerageMapper;

    @Mock
    private BrokerageService brokerageServiceMock;

    @Autowired
    private BrokerageService brokerageService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageMockMvc;

    private Brokerage brokerage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageResource brokerageResource = new BrokerageResource(brokerageService);
        this.restBrokerageMockMvc = MockMvcBuilders.standaloneSetup(brokerageResource)
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
    public static Brokerage createEntity(EntityManager em) {
        Brokerage brokerage = new Brokerage()
            .name(DEFAULT_NAME)
            .cnpj(DEFAULT_CNPJ)
            .address(DEFAULT_ADDRESS)
            .addressNeighborhood(DEFAULT_ADDRESS_NEIGHBORHOOD)
            .addressCity(DEFAULT_ADDRESS_CITY)
            .addressState(DEFAULT_ADDRESS_STATE)
            .swingTrade(DEFAULT_SWING_TRADE)
            .dayTrade(DEFAULT_DAY_TRADE)
            .loginEmail(DEFAULT_LOGIN_EMAIL)
            .loginAccessCode(DEFAULT_LOGIN_ACCESS_CODE)
            .loginCpf(DEFAULT_LOGIN_CPF)
            .loginPassword(DEFAULT_LOGIN_PASSWORD)
            .loginToken(DEFAULT_LOGIN_TOKEN)
            .fee(DEFAULT_FEE)
            .iss(DEFAULT_ISS)
            .phone(DEFAULT_PHONE)
            .website(DEFAULT_WEBSITE)
            .email(DEFAULT_EMAIL);
        return brokerage;
    }

    @Before
    public void initTest() {
        brokerage = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerage() throws Exception {
        int databaseSizeBeforeCreate = brokerageRepository.findAll().size();

        // Create the Brokerage
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);
        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isCreated());

        // Validate the Brokerage in the database
        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeCreate + 1);
        Brokerage testBrokerage = brokerageList.get(brokerageList.size() - 1);
        assertThat(testBrokerage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBrokerage.getCnpj()).isEqualTo(DEFAULT_CNPJ);
        assertThat(testBrokerage.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBrokerage.getAddressNeighborhood()).isEqualTo(DEFAULT_ADDRESS_NEIGHBORHOOD);
        assertThat(testBrokerage.getAddressCity()).isEqualTo(DEFAULT_ADDRESS_CITY);
        assertThat(testBrokerage.getAddressState()).isEqualTo(DEFAULT_ADDRESS_STATE);
        assertThat(testBrokerage.isSwingTrade()).isEqualTo(DEFAULT_SWING_TRADE);
        assertThat(testBrokerage.isDayTrade()).isEqualTo(DEFAULT_DAY_TRADE);
        assertThat(testBrokerage.isLoginEmail()).isEqualTo(DEFAULT_LOGIN_EMAIL);
        assertThat(testBrokerage.isLoginAccessCode()).isEqualTo(DEFAULT_LOGIN_ACCESS_CODE);
        assertThat(testBrokerage.isLoginCpf()).isEqualTo(DEFAULT_LOGIN_CPF);
        assertThat(testBrokerage.isLoginPassword()).isEqualTo(DEFAULT_LOGIN_PASSWORD);
        assertThat(testBrokerage.isLoginToken()).isEqualTo(DEFAULT_LOGIN_TOKEN);
        assertThat(testBrokerage.getFee()).isEqualTo(DEFAULT_FEE);
        assertThat(testBrokerage.getIss()).isEqualTo(DEFAULT_ISS);
        assertThat(testBrokerage.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testBrokerage.getWebsite()).isEqualTo(DEFAULT_WEBSITE);
        assertThat(testBrokerage.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    public void createBrokerageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageRepository.findAll().size();

        // Create the Brokerage with an existing ID
        brokerage.setId(1L);
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brokerage in the database
        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setName(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSwingTradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setSwingTrade(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDayTradeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setDayTrade(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setLoginEmail(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginAccessCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setLoginAccessCode(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginCpfIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setLoginCpf(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginPasswordIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setLoginPassword(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLoginTokenIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setLoginToken(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFeeIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageRepository.findAll().size();
        // set the field null
        brokerage.setFee(null);

        // Create the Brokerage, which fails.
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        restBrokerageMockMvc.perform(post("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerages() throws Exception {
        // Initialize the database
        brokerageRepository.saveAndFlush(brokerage);

        // Get all the brokerageList
        restBrokerageMockMvc.perform(get("/api/brokerages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ.toString())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].addressNeighborhood").value(hasItem(DEFAULT_ADDRESS_NEIGHBORHOOD.toString())))
            .andExpect(jsonPath("$.[*].addressCity").value(hasItem(DEFAULT_ADDRESS_CITY.toString())))
            .andExpect(jsonPath("$.[*].addressState").value(hasItem(DEFAULT_ADDRESS_STATE.toString())))
            .andExpect(jsonPath("$.[*].swingTrade").value(hasItem(DEFAULT_SWING_TRADE.booleanValue())))
            .andExpect(jsonPath("$.[*].dayTrade").value(hasItem(DEFAULT_DAY_TRADE.booleanValue())))
            .andExpect(jsonPath("$.[*].loginEmail").value(hasItem(DEFAULT_LOGIN_EMAIL.booleanValue())))
            .andExpect(jsonPath("$.[*].loginAccessCode").value(hasItem(DEFAULT_LOGIN_ACCESS_CODE.booleanValue())))
            .andExpect(jsonPath("$.[*].loginCpf").value(hasItem(DEFAULT_LOGIN_CPF.booleanValue())))
            .andExpect(jsonPath("$.[*].loginPassword").value(hasItem(DEFAULT_LOGIN_PASSWORD.booleanValue())))
            .andExpect(jsonPath("$.[*].loginToken").value(hasItem(DEFAULT_LOGIN_TOKEN.booleanValue())))
            .andExpect(jsonPath("$.[*].fee").value(hasItem(DEFAULT_FEE.intValue())))
            .andExpect(jsonPath("$.[*].iss").value(hasItem(DEFAULT_ISS.intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
            .andExpect(jsonPath("$.[*].website").value(hasItem(DEFAULT_WEBSITE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllBrokeragesWithEagerRelationshipsIsEnabled() throws Exception {
        BrokerageResource brokerageResource = new BrokerageResource(brokerageServiceMock);
        when(brokerageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restBrokerageMockMvc = MockMvcBuilders.standaloneSetup(brokerageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBrokerageMockMvc.perform(get("/api/brokerages?eagerload=true"))
        .andExpect(status().isOk());

        verify(brokerageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllBrokeragesWithEagerRelationshipsIsNotEnabled() throws Exception {
        BrokerageResource brokerageResource = new BrokerageResource(brokerageServiceMock);
            when(brokerageServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restBrokerageMockMvc = MockMvcBuilders.standaloneSetup(brokerageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restBrokerageMockMvc.perform(get("/api/brokerages?eagerload=true"))
        .andExpect(status().isOk());

            verify(brokerageServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getBrokerage() throws Exception {
        // Initialize the database
        brokerageRepository.saveAndFlush(brokerage);

        // Get the brokerage
        restBrokerageMockMvc.perform(get("/api/brokerages/{id}", brokerage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.addressNeighborhood").value(DEFAULT_ADDRESS_NEIGHBORHOOD.toString()))
            .andExpect(jsonPath("$.addressCity").value(DEFAULT_ADDRESS_CITY.toString()))
            .andExpect(jsonPath("$.addressState").value(DEFAULT_ADDRESS_STATE.toString()))
            .andExpect(jsonPath("$.swingTrade").value(DEFAULT_SWING_TRADE.booleanValue()))
            .andExpect(jsonPath("$.dayTrade").value(DEFAULT_DAY_TRADE.booleanValue()))
            .andExpect(jsonPath("$.loginEmail").value(DEFAULT_LOGIN_EMAIL.booleanValue()))
            .andExpect(jsonPath("$.loginAccessCode").value(DEFAULT_LOGIN_ACCESS_CODE.booleanValue()))
            .andExpect(jsonPath("$.loginCpf").value(DEFAULT_LOGIN_CPF.booleanValue()))
            .andExpect(jsonPath("$.loginPassword").value(DEFAULT_LOGIN_PASSWORD.booleanValue()))
            .andExpect(jsonPath("$.loginToken").value(DEFAULT_LOGIN_TOKEN.booleanValue()))
            .andExpect(jsonPath("$.fee").value(DEFAULT_FEE.intValue()))
            .andExpect(jsonPath("$.iss").value(DEFAULT_ISS.intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.website").value(DEFAULT_WEBSITE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerage() throws Exception {
        // Get the brokerage
        restBrokerageMockMvc.perform(get("/api/brokerages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerage() throws Exception {
        // Initialize the database
        brokerageRepository.saveAndFlush(brokerage);

        int databaseSizeBeforeUpdate = brokerageRepository.findAll().size();

        // Update the brokerage
        Brokerage updatedBrokerage = brokerageRepository.findById(brokerage.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerage are not directly saved in db
        em.detach(updatedBrokerage);
        updatedBrokerage
            .name(UPDATED_NAME)
            .cnpj(UPDATED_CNPJ)
            .address(UPDATED_ADDRESS)
            .addressNeighborhood(UPDATED_ADDRESS_NEIGHBORHOOD)
            .addressCity(UPDATED_ADDRESS_CITY)
            .addressState(UPDATED_ADDRESS_STATE)
            .swingTrade(UPDATED_SWING_TRADE)
            .dayTrade(UPDATED_DAY_TRADE)
            .loginEmail(UPDATED_LOGIN_EMAIL)
            .loginAccessCode(UPDATED_LOGIN_ACCESS_CODE)
            .loginCpf(UPDATED_LOGIN_CPF)
            .loginPassword(UPDATED_LOGIN_PASSWORD)
            .loginToken(UPDATED_LOGIN_TOKEN)
            .fee(UPDATED_FEE)
            .iss(UPDATED_ISS)
            .phone(UPDATED_PHONE)
            .website(UPDATED_WEBSITE)
            .email(UPDATED_EMAIL);
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(updatedBrokerage);

        restBrokerageMockMvc.perform(put("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isOk());

        // Validate the Brokerage in the database
        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeUpdate);
        Brokerage testBrokerage = brokerageList.get(brokerageList.size() - 1);
        assertThat(testBrokerage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBrokerage.getCnpj()).isEqualTo(UPDATED_CNPJ);
        assertThat(testBrokerage.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBrokerage.getAddressNeighborhood()).isEqualTo(UPDATED_ADDRESS_NEIGHBORHOOD);
        assertThat(testBrokerage.getAddressCity()).isEqualTo(UPDATED_ADDRESS_CITY);
        assertThat(testBrokerage.getAddressState()).isEqualTo(UPDATED_ADDRESS_STATE);
        assertThat(testBrokerage.isSwingTrade()).isEqualTo(UPDATED_SWING_TRADE);
        assertThat(testBrokerage.isDayTrade()).isEqualTo(UPDATED_DAY_TRADE);
        assertThat(testBrokerage.isLoginEmail()).isEqualTo(UPDATED_LOGIN_EMAIL);
        assertThat(testBrokerage.isLoginAccessCode()).isEqualTo(UPDATED_LOGIN_ACCESS_CODE);
        assertThat(testBrokerage.isLoginCpf()).isEqualTo(UPDATED_LOGIN_CPF);
        assertThat(testBrokerage.isLoginPassword()).isEqualTo(UPDATED_LOGIN_PASSWORD);
        assertThat(testBrokerage.isLoginToken()).isEqualTo(UPDATED_LOGIN_TOKEN);
        assertThat(testBrokerage.getFee()).isEqualTo(UPDATED_FEE);
        assertThat(testBrokerage.getIss()).isEqualTo(UPDATED_ISS);
        assertThat(testBrokerage.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testBrokerage.getWebsite()).isEqualTo(UPDATED_WEBSITE);
        assertThat(testBrokerage.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerage() throws Exception {
        int databaseSizeBeforeUpdate = brokerageRepository.findAll().size();

        // Create the Brokerage
        BrokerageDTO brokerageDTO = brokerageMapper.toDto(brokerage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageMockMvc.perform(put("/api/brokerages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Brokerage in the database
        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerage() throws Exception {
        // Initialize the database
        brokerageRepository.saveAndFlush(brokerage);

        int databaseSizeBeforeDelete = brokerageRepository.findAll().size();

        // Get the brokerage
        restBrokerageMockMvc.perform(delete("/api/brokerages/{id}", brokerage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Brokerage> brokerageList = brokerageRepository.findAll();
        assertThat(brokerageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Brokerage.class);
        Brokerage brokerage1 = new Brokerage();
        brokerage1.setId(1L);
        Brokerage brokerage2 = new Brokerage();
        brokerage2.setId(brokerage1.getId());
        assertThat(brokerage1).isEqualTo(brokerage2);
        brokerage2.setId(2L);
        assertThat(brokerage1).isNotEqualTo(brokerage2);
        brokerage1.setId(null);
        assertThat(brokerage1).isNotEqualTo(brokerage2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageDTO.class);
        BrokerageDTO brokerageDTO1 = new BrokerageDTO();
        brokerageDTO1.setId(1L);
        BrokerageDTO brokerageDTO2 = new BrokerageDTO();
        assertThat(brokerageDTO1).isNotEqualTo(brokerageDTO2);
        brokerageDTO2.setId(brokerageDTO1.getId());
        assertThat(brokerageDTO1).isEqualTo(brokerageDTO2);
        brokerageDTO2.setId(2L);
        assertThat(brokerageDTO1).isNotEqualTo(brokerageDTO2);
        brokerageDTO1.setId(null);
        assertThat(brokerageDTO1).isNotEqualTo(brokerageDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageMapper.fromId(null)).isNull();
    }
}
