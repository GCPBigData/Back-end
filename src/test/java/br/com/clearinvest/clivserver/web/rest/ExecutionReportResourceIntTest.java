package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.ExecutionReport;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.repository.ExecutionReportRepository;
import br.com.clearinvest.clivserver.service.ExecutionReportService;
import br.com.clearinvest.clivserver.service.dto.ExecutionReportDTO;
import br.com.clearinvest.clivserver.service.mapper.ExecutionReportMapper;
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
import org.springframework.util.Base64Utils;

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
 * Test class for the ExecutionReportResource REST controller.
 *
 * @see ExecutionReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class ExecutionReportResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EXEC_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXEC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ORD_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ORD_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORD_REJ_REASON = 1;
    private static final Integer UPDATED_ORD_REJ_REASON = 2;

    private static final Long DEFAULT_LAST_QTY = 1L;
    private static final Long UPDATED_LAST_QTY = 2L;

    private static final BigDecimal DEFAULT_LAST_PX = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_PX = new BigDecimal(2);

    @Autowired
    private ExecutionReportRepository executionReportRepository;

    @Autowired
    private ExecutionReportMapper executionReportMapper;

    @Autowired
    private ExecutionReportService executionReportService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExecutionReportMockMvc;

    private ExecutionReport executionReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExecutionReportResource executionReportResource = new ExecutionReportResource(executionReportService);
        this.restExecutionReportMockMvc = MockMvcBuilders.standaloneSetup(executionReportResource)
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
    public static ExecutionReport createEntity(EntityManager em) {
        ExecutionReport executionReport = new ExecutionReport()
            .createdAt(DEFAULT_CREATED_AT)
            .execId(DEFAULT_EXEC_ID)
            .execType(DEFAULT_EXEC_TYPE)
            .ordStatus(DEFAULT_ORD_STATUS)
            .ordRejReason(DEFAULT_ORD_REJ_REASON)
            .lastQty(DEFAULT_LAST_QTY)
            .lastPx(DEFAULT_LAST_PX);
        // Add required entity
        StockOrder stockOrder = StockOrderResourceIntTest.createEntity(em);
        em.persist(stockOrder);
        em.flush();
        executionReport.setOrder(stockOrder);
        return executionReport;
    }

    @Before
    public void initTest() {
        executionReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecutionReport() throws Exception {
        int databaseSizeBeforeCreate = executionReportRepository.findAll().size();

        // Create the ExecutionReport
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);
        restExecutionReportMockMvc.perform(post("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isCreated());

        // Validate the ExecutionReport in the database
        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeCreate + 1);
        ExecutionReport testExecutionReport = executionReportList.get(executionReportList.size() - 1);
        assertThat(testExecutionReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testExecutionReport.getExecId()).isEqualTo(DEFAULT_EXEC_ID);
        assertThat(testExecutionReport.getExecType()).isEqualTo(DEFAULT_EXEC_TYPE);
        assertThat(testExecutionReport.getOrdStatus()).isEqualTo(DEFAULT_ORD_STATUS);
        assertThat(testExecutionReport.getOrdRejReason()).isEqualTo(DEFAULT_ORD_REJ_REASON);
        assertThat(testExecutionReport.getLastQty()).isEqualTo(DEFAULT_LAST_QTY);
        assertThat(testExecutionReport.getLastPx()).isEqualTo(DEFAULT_LAST_PX);
    }

    @Test
    @Transactional
    public void createExecutionReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = executionReportRepository.findAll().size();

        // Create the ExecutionReport with an existing ID
        executionReport.setId(1L);
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecutionReportMockMvc.perform(post("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExecutionReport in the database
        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionReportRepository.findAll().size();
        // set the field null
        executionReport.setCreatedAt(null);

        // Create the ExecutionReport, which fails.
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);

        restExecutionReportMockMvc.perform(post("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExecTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionReportRepository.findAll().size();
        // set the field null
        executionReport.setExecType(null);

        // Create the ExecutionReport, which fails.
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);

        restExecutionReportMockMvc.perform(post("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = executionReportRepository.findAll().size();
        // set the field null
        executionReport.setOrdStatus(null);

        // Create the ExecutionReport, which fails.
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);

        restExecutionReportMockMvc.perform(post("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExecutionReports() throws Exception {
        // Initialize the database
        executionReportRepository.saveAndFlush(executionReport);

        // Get all the executionReportList
        restExecutionReportMockMvc.perform(get("/api/execution-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(executionReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].execId").value(hasItem(DEFAULT_EXEC_ID.toString())))
            .andExpect(jsonPath("$.[*].execType").value(hasItem(DEFAULT_EXEC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ordStatus").value(hasItem(DEFAULT_ORD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordRejReason").value(hasItem(DEFAULT_ORD_REJ_REASON)))
            .andExpect(jsonPath("$.[*].lastQty").value(hasItem(DEFAULT_LAST_QTY.intValue())))
            .andExpect(jsonPath("$.[*].lastPx").value(hasItem(DEFAULT_LAST_PX.intValue())));
    }
    
    @Test
    @Transactional
    public void getExecutionReport() throws Exception {
        // Initialize the database
        executionReportRepository.saveAndFlush(executionReport);

        // Get the executionReport
        restExecutionReportMockMvc.perform(get("/api/execution-reports/{id}", executionReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(executionReport.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.execId").value(DEFAULT_EXEC_ID.toString()))
            .andExpect(jsonPath("$.execType").value(DEFAULT_EXEC_TYPE.toString()))
            .andExpect(jsonPath("$.ordStatus").value(DEFAULT_ORD_STATUS.toString()))
            .andExpect(jsonPath("$.ordRejReason").value(DEFAULT_ORD_REJ_REASON))
            .andExpect(jsonPath("$.lastQty").value(DEFAULT_LAST_QTY.intValue()))
            .andExpect(jsonPath("$.lastPx").value(DEFAULT_LAST_PX.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingExecutionReport() throws Exception {
        // Get the executionReport
        restExecutionReportMockMvc.perform(get("/api/execution-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecutionReport() throws Exception {
        // Initialize the database
        executionReportRepository.saveAndFlush(executionReport);

        int databaseSizeBeforeUpdate = executionReportRepository.findAll().size();

        // Update the executionReport
        ExecutionReport updatedExecutionReport = executionReportRepository.findById(executionReport.getId()).get();
        // Disconnect from session so that the updates on updatedExecutionReport are not directly saved in db
        em.detach(updatedExecutionReport);
        updatedExecutionReport
            .createdAt(UPDATED_CREATED_AT)
            .execId(UPDATED_EXEC_ID)
            .execType(UPDATED_EXEC_TYPE)
            .ordStatus(UPDATED_ORD_STATUS)
            .ordRejReason(UPDATED_ORD_REJ_REASON)
            .lastQty(UPDATED_LAST_QTY)
            .lastPx(UPDATED_LAST_PX);
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(updatedExecutionReport);

        restExecutionReportMockMvc.perform(put("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isOk());

        // Validate the ExecutionReport in the database
        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeUpdate);
        ExecutionReport testExecutionReport = executionReportList.get(executionReportList.size() - 1);
        assertThat(testExecutionReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testExecutionReport.getExecId()).isEqualTo(UPDATED_EXEC_ID);
        assertThat(testExecutionReport.getExecType()).isEqualTo(UPDATED_EXEC_TYPE);
        assertThat(testExecutionReport.getOrdStatus()).isEqualTo(UPDATED_ORD_STATUS);
        assertThat(testExecutionReport.getOrdRejReason()).isEqualTo(UPDATED_ORD_REJ_REASON);
        assertThat(testExecutionReport.getLastQty()).isEqualTo(UPDATED_LAST_QTY);
        assertThat(testExecutionReport.getLastPx()).isEqualTo(UPDATED_LAST_PX);
    }

    @Test
    @Transactional
    public void updateNonExistingExecutionReport() throws Exception {
        int databaseSizeBeforeUpdate = executionReportRepository.findAll().size();

        // Create the ExecutionReport
        ExecutionReportDTO executionReportDTO = executionReportMapper.toDto(executionReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecutionReportMockMvc.perform(put("/api/execution-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(executionReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExecutionReport in the database
        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExecutionReport() throws Exception {
        // Initialize the database
        executionReportRepository.saveAndFlush(executionReport);

        int databaseSizeBeforeDelete = executionReportRepository.findAll().size();

        // Get the executionReport
        restExecutionReportMockMvc.perform(delete("/api/execution-reports/{id}", executionReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExecutionReport> executionReportList = executionReportRepository.findAll();
        assertThat(executionReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionReport.class);
        ExecutionReport executionReport1 = new ExecutionReport();
        executionReport1.setId(1L);
        ExecutionReport executionReport2 = new ExecutionReport();
        executionReport2.setId(executionReport1.getId());
        assertThat(executionReport1).isEqualTo(executionReport2);
        executionReport2.setId(2L);
        assertThat(executionReport1).isNotEqualTo(executionReport2);
        executionReport1.setId(null);
        assertThat(executionReport1).isNotEqualTo(executionReport2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecutionReportDTO.class);
        ExecutionReportDTO executionReportDTO1 = new ExecutionReportDTO();
        executionReportDTO1.setId(1L);
        ExecutionReportDTO executionReportDTO2 = new ExecutionReportDTO();
        assertThat(executionReportDTO1).isNotEqualTo(executionReportDTO2);
        executionReportDTO2.setId(executionReportDTO1.getId());
        assertThat(executionReportDTO1).isEqualTo(executionReportDTO2);
        executionReportDTO2.setId(2L);
        assertThat(executionReportDTO1).isNotEqualTo(executionReportDTO2);
        executionReportDTO1.setId(null);
        assertThat(executionReportDTO1).isNotEqualTo(executionReportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(executionReportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(executionReportMapper.fromId(null)).isNull();
    }
}
