package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.BrokerageProduct;
import br.com.clearinvest.clivserver.repository.BrokerageProductRepository;
import br.com.clearinvest.clivserver.service.BrokerageProductService;
import br.com.clearinvest.clivserver.service.dto.BrokerageProductDTO;
import br.com.clearinvest.clivserver.service.mapper.BrokerageProductMapper;
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
 * Test class for the BrokerageProductResource REST controller.
 *
 * @see BrokerageProductResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class BrokerageProductResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private BrokerageProductRepository brokerageProductRepository;

    @Autowired
    private BrokerageProductMapper brokerageProductMapper;

    @Autowired
    private BrokerageProductService brokerageProductService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restBrokerageProductMockMvc;

    private BrokerageProduct brokerageProduct;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BrokerageProductResource brokerageProductResource = new BrokerageProductResource(brokerageProductService);
        this.restBrokerageProductMockMvc = MockMvcBuilders.standaloneSetup(brokerageProductResource)
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
    public static BrokerageProduct createEntity(EntityManager em) {
        BrokerageProduct brokerageProduct = new BrokerageProduct()
            .name(DEFAULT_NAME);
        return brokerageProduct;
    }

    @Before
    public void initTest() {
        brokerageProduct = createEntity(em);
    }

    @Test
    @Transactional
    public void createBrokerageProduct() throws Exception {
        int databaseSizeBeforeCreate = brokerageProductRepository.findAll().size();

        // Create the BrokerageProduct
        BrokerageProductDTO brokerageProductDTO = brokerageProductMapper.toDto(brokerageProduct);
        restBrokerageProductMockMvc.perform(post("/api/brokerage-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageProductDTO)))
            .andExpect(status().isCreated());

        // Validate the BrokerageProduct in the database
        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeCreate + 1);
        BrokerageProduct testBrokerageProduct = brokerageProductList.get(brokerageProductList.size() - 1);
        assertThat(testBrokerageProduct.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createBrokerageProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = brokerageProductRepository.findAll().size();

        // Create the BrokerageProduct with an existing ID
        brokerageProduct.setId(1L);
        BrokerageProductDTO brokerageProductDTO = brokerageProductMapper.toDto(brokerageProduct);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBrokerageProductMockMvc.perform(post("/api/brokerage-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageProduct in the database
        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = brokerageProductRepository.findAll().size();
        // set the field null
        brokerageProduct.setName(null);

        // Create the BrokerageProduct, which fails.
        BrokerageProductDTO brokerageProductDTO = brokerageProductMapper.toDto(brokerageProduct);

        restBrokerageProductMockMvc.perform(post("/api/brokerage-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageProductDTO)))
            .andExpect(status().isBadRequest());

        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBrokerageProducts() throws Exception {
        // Initialize the database
        brokerageProductRepository.saveAndFlush(brokerageProduct);

        // Get all the brokerageProductList
        restBrokerageProductMockMvc.perform(get("/api/brokerage-products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(brokerageProduct.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getBrokerageProduct() throws Exception {
        // Initialize the database
        brokerageProductRepository.saveAndFlush(brokerageProduct);

        // Get the brokerageProduct
        restBrokerageProductMockMvc.perform(get("/api/brokerage-products/{id}", brokerageProduct.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(brokerageProduct.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBrokerageProduct() throws Exception {
        // Get the brokerageProduct
        restBrokerageProductMockMvc.perform(get("/api/brokerage-products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBrokerageProduct() throws Exception {
        // Initialize the database
        brokerageProductRepository.saveAndFlush(brokerageProduct);

        int databaseSizeBeforeUpdate = brokerageProductRepository.findAll().size();

        // Update the brokerageProduct
        BrokerageProduct updatedBrokerageProduct = brokerageProductRepository.findById(brokerageProduct.getId()).get();
        // Disconnect from session so that the updates on updatedBrokerageProduct are not directly saved in db
        em.detach(updatedBrokerageProduct);
        updatedBrokerageProduct
            .name(UPDATED_NAME);
        BrokerageProductDTO brokerageProductDTO = brokerageProductMapper.toDto(updatedBrokerageProduct);

        restBrokerageProductMockMvc.perform(put("/api/brokerage-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageProductDTO)))
            .andExpect(status().isOk());

        // Validate the BrokerageProduct in the database
        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeUpdate);
        BrokerageProduct testBrokerageProduct = brokerageProductList.get(brokerageProductList.size() - 1);
        assertThat(testBrokerageProduct.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingBrokerageProduct() throws Exception {
        int databaseSizeBeforeUpdate = brokerageProductRepository.findAll().size();

        // Create the BrokerageProduct
        BrokerageProductDTO brokerageProductDTO = brokerageProductMapper.toDto(brokerageProduct);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBrokerageProductMockMvc.perform(put("/api/brokerage-products")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(brokerageProductDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BrokerageProduct in the database
        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBrokerageProduct() throws Exception {
        // Initialize the database
        brokerageProductRepository.saveAndFlush(brokerageProduct);

        int databaseSizeBeforeDelete = brokerageProductRepository.findAll().size();

        // Get the brokerageProduct
        restBrokerageProductMockMvc.perform(delete("/api/brokerage-products/{id}", brokerageProduct.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<BrokerageProduct> brokerageProductList = brokerageProductRepository.findAll();
        assertThat(brokerageProductList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageProduct.class);
        BrokerageProduct brokerageProduct1 = new BrokerageProduct();
        brokerageProduct1.setId(1L);
        BrokerageProduct brokerageProduct2 = new BrokerageProduct();
        brokerageProduct2.setId(brokerageProduct1.getId());
        assertThat(brokerageProduct1).isEqualTo(brokerageProduct2);
        brokerageProduct2.setId(2L);
        assertThat(brokerageProduct1).isNotEqualTo(brokerageProduct2);
        brokerageProduct1.setId(null);
        assertThat(brokerageProduct1).isNotEqualTo(brokerageProduct2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BrokerageProductDTO.class);
        BrokerageProductDTO brokerageProductDTO1 = new BrokerageProductDTO();
        brokerageProductDTO1.setId(1L);
        BrokerageProductDTO brokerageProductDTO2 = new BrokerageProductDTO();
        assertThat(brokerageProductDTO1).isNotEqualTo(brokerageProductDTO2);
        brokerageProductDTO2.setId(brokerageProductDTO1.getId());
        assertThat(brokerageProductDTO1).isEqualTo(brokerageProductDTO2);
        brokerageProductDTO2.setId(2L);
        assertThat(brokerageProductDTO1).isNotEqualTo(brokerageProductDTO2);
        brokerageProductDTO1.setId(null);
        assertThat(brokerageProductDTO1).isNotEqualTo(brokerageProductDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(brokerageProductMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(brokerageProductMapper.fromId(null)).isNull();
    }
}
