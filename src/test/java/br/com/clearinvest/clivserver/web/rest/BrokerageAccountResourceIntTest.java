package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.User;
import br.com.clearinvest.clivserver.domain.Brokerage;
import br.com.clearinvest.clivserver.repository.BrokerageAccountRepository;
import br.com.clearinvest.clivserver.service.BrokerageAccountService;
import br.com.clearinvest.clivserver.service.dto.BrokerageAccountDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageAccountMapper;
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
 * Test class for the BrokerageAccountResource REST controller.
 *
 * @see BrokerageAccountResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageAccountResourceIntTest {

    private static final String DEFAULT_LOGIN_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN_ACCESS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_ACCESS_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN_CPF = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private BrokerageAccountRepository brokerageAccountRepository;

    @Autowired
    private BrokerageAccountMapper brokerageAccountMapper;

    @Autowired
    private BrokerageAccountService brokerageAccountService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageAccountMockMvc;

    private BrokerageAccount brokerageAccount;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageAccountResource brokerageAccountResource = new BrokerageAccountResource(brokerageAccountService);
        this.restBrokerageAccountMockMvc = MockMvcBuilders.standaloneSetup(brokerageAccountResource)
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
    public static BrokerageAccount createEntity(EntityManager em) {
        BrokerageAccount brokerageAccount = new BrokerageAccount()
            .loginEmail(DEFAULT_LOGIN_EMAIL)
            .loginAccessCode(DEFAULT_LOGIN_ACCESS_CODE)
            .loginCpf(DEFAULT_LOGIN_CPF)
            .loginPassword(DEFAULT_LOGIN_PASSWORD);
        // Add required entity
        User user = UserResourceIntTest.createEntity(em);
        em.persist(user);
        em.flush();
        brokerageAccount.setUser(user);
        // Add required entity
        Brokerage brokerage = BrokerageResourceIntTest.createEntity(em);
        em.persist(brokerage);
        em.flush();
        brokerageAccount.setBrokerage(brokerage);
        return brokerageAccount;
    }

    @Before
    public void initTest() {
        brokerageAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerageAccount() throws Exception {
        int databaseSizeBeforeCreate = brokerageAccountRepository.findAll().size();

        // Create the BrokerageAccount
        BrokerageAccountDTO brokerageAccountDTO = brokerageAccountMapper.toDto(brokerageAccount);
        restBrokerageAccountMockMvc.perform(post("/api/brokerage-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAccountDTO)))
            .andExpect(status().isCreated());

        // Validate the BrokerageAccount in the database
        List<BrokerageAccount> brokerageAccountList = brokerageAccountRepository.findAll();
        assertThat(brokerageAccountList).hasSize(databaseSizeBeforeCreate + 1);
        BrokerageAccount testBrokerageAccount = brokerageAccountList.get(brokerageAccountList.size() - 1);
        assertThat(testBrokerageAccount.getLoginEmail()).isEqualTo(DEFAULT_LOGIN_EMAIL);
        assertThat(testBrokerageAccount.getLoginAccessCode()).isEqualTo(DEFAULT_LOGIN_ACCESS_CODE);
        assertThat(testBrokerageAccount.getLoginCpf()).isEqualTo(DEFAULT_LOGIN_CPF);
        assertThat(testBrokerageAccount.getLoginPassword()).isEqualTo(DEFAULT_LOGIN_PASSWORD);
    }

    @Test
    @Transactional
    public void createBrokerageAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageAccountRepository.findAll().size();

        // Create the BrokerageAccount with an existing ID
        brokerageAccount.setId(1L);
        BrokerageAccountDTO brokerageAccountDTO = brokerageAccountMapper.toDto(brokerageAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageAccountMockMvc.perform(post("/api/brokerage-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageAccount in the database
        List<BrokerageAccount> brokerageAccountList = brokerageAccountRepository.findAll();
        assertThat(brokerageAccountList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllBrokerageAccounts() throws Exception {
        // Initialize the database
        brokerageAccountRepository.saveAndFlush(brokerageAccount);

        // Get all the brokerageAccountList
        restBrokerageAccountMockMvc.perform(get("/api/brokerage-accounts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageAccount.getId().intValue())))
            .andExpect(jsonPath("$.[*].loginEmail").value(hasItem(DEFAULT_LOGIN_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].loginAccessCode").value(hasItem(DEFAULT_LOGIN_ACCESS_CODE.toString())))
            .andExpect(jsonPath("$.[*].loginCpf").value(hasItem(DEFAULT_LOGIN_CPF.toString())))
            .andExpect(jsonPath("$.[*].loginPassword").value(hasItem(DEFAULT_LOGIN_PASSWORD.toString())));
    }
    
    @Test
    @Transactional
    public void getBrokerageAccount() throws Exception {
        // Initialize the database
        brokerageAccountRepository.saveAndFlush(brokerageAccount);

        // Get the brokerageAccount
        restBrokerageAccountMockMvc.perform(get("/api/brokerage-accounts/{id}", brokerageAccount.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerageAccount.getId().intValue()))
            .andExpect(jsonPath("$.loginEmail").value(DEFAULT_LOGIN_EMAIL.toString()))
            .andExpect(jsonPath("$.loginAccessCode").value(DEFAULT_LOGIN_ACCESS_CODE.toString()))
            .andExpect(jsonPath("$.loginCpf").value(DEFAULT_LOGIN_CPF.toString()))
            .andExpect(jsonPath("$.loginPassword").value(DEFAULT_LOGIN_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerageAccount() throws Exception {
        // Get the brokerageAccount
        restBrokerageAccountMockMvc.perform(get("/api/brokerage-accounts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerageAccount() throws Exception {
        // Initialize the database
        brokerageAccountRepository.saveAndFlush(brokerageAccount);

        int databaseSizeBeforeUpdate = brokerageAccountRepository.findAll().size();

        // Update the brokerageAccount
        BrokerageAccount updatedBrokerageAccount = brokerageAccountRepository.findById(brokerageAccount.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerageAccount are not directly saved in db
        em.detach(updatedBrokerageAccount);
        updatedBrokerageAccount
            .loginEmail(UPDATED_LOGIN_EMAIL)
            .loginAccessCode(UPDATED_LOGIN_ACCESS_CODE)
            .loginCpf(UPDATED_LOGIN_CPF)
            .loginPassword(UPDATED_LOGIN_PASSWORD);
        BrokerageAccountDTO brokerageAccountDTO = brokerageAccountMapper.toDto(updatedBrokerageAccount);

        restBrokerageAccountMockMvc.perform(put("/api/brokerage-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAccountDTO)))
            .andExpect(status().isOk());

        // Validate the BrokerageAccount in the database
        List<BrokerageAccount> brokerageAccountList = brokerageAccountRepository.findAll();
        assertThat(brokerageAccountList).hasSize(databaseSizeBeforeUpdate);
        BrokerageAccount testBrokerageAccount = brokerageAccountList.get(brokerageAccountList.size() - 1);
        assertThat(testBrokerageAccount.getLoginEmail()).isEqualTo(UPDATED_LOGIN_EMAIL);
        assertThat(testBrokerageAccount.getLoginAccessCode()).isEqualTo(UPDATED_LOGIN_ACCESS_CODE);
        assertThat(testBrokerageAccount.getLoginCpf()).isEqualTo(UPDATED_LOGIN_CPF);
        assertThat(testBrokerageAccount.getLoginPassword()).isEqualTo(UPDATED_LOGIN_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerageAccount() throws Exception {
        int databaseSizeBeforeUpdate = brokerageAccountRepository.findAll().size();

        // Create the BrokerageAccount
        BrokerageAccountDTO brokerageAccountDTO = brokerageAccountMapper.toDto(brokerageAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageAccountMockMvc.perform(put("/api/brokerage-accounts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageAccountDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageAccount in the database
        List<BrokerageAccount> brokerageAccountList = brokerageAccountRepository.findAll();
        assertThat(brokerageAccountList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerageAccount() throws Exception {
        // Initialize the database
        brokerageAccountRepository.saveAndFlush(brokerageAccount);

        int databaseSizeBeforeDelete = brokerageAccountRepository.findAll().size();

        // Get the brokerageAccount
        restBrokerageAccountMockMvc.perform(delete("/api/brokerage-accounts/{id}", brokerageAccount.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerageAccount> brokerageAccountList = brokerageAccountRepository.findAll();
        assertThat(brokerageAccountList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageAccount.class);
        BrokerageAccount brokerageAccount1 = new BrokerageAccount();
        brokerageAccount1.setId(1L);
        BrokerageAccount brokerageAccount2 = new BrokerageAccount();
        brokerageAccount2.setId(brokerageAccount1.getId());
        assertThat(brokerageAccount1).isEqualTo(brokerageAccount2);
        brokerageAccount2.setId(2L);
        assertThat(brokerageAccount1).isNotEqualTo(brokerageAccount2);
        brokerageAccount1.setId(null);
        assertThat(brokerageAccount1).isNotEqualTo(brokerageAccount2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageAccountDTO.class);
        BrokerageAccountDTO brokerageAccountDTO1 = new BrokerageAccountDTO();
        brokerageAccountDTO1.setId(1L);
        BrokerageAccountDTO brokerageAccountDTO2 = new BrokerageAccountDTO();
        assertThat(brokerageAccountDTO1).isNotEqualTo(brokerageAccountDTO2);
        brokerageAccountDTO2.setId(brokerageAccountDTO1.getId());
        assertThat(brokerageAccountDTO1).isEqualTo(brokerageAccountDTO2);
        brokerageAccountDTO2.setId(2L);
        assertThat(brokerageAccountDTO1).isNotEqualTo(brokerageAccountDTO2);
        brokerageAccountDTO1.setId(null);
        assertThat(brokerageAccountDTO1).isNotEqualTo(brokerageAccountDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageAccountMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageAccountMapper.fromId(null)).isNull();
    }
}
