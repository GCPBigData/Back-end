package br.com.clearinvest.clivserver.web.rest;

import br.com.clearinvest.clivserver.ClivServerApp;

import br.com.clearinvest.clivserver.domain.ExecReport;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.repository.ExecReportRepository;
import br.com.clearinvest.clivserver.service.ExecReportService;
import br.com.clearinvest.clivserver.service.dto.ExecReportDTO;
import br.com.clearinvest.clivserver.service.mapper.ExecReportMapper;
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
 * Test class for the ExecReportResource REST controller.
 *
 * @see ExecReportResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ClivServerApp.class)
public class ExecReportResourceIntTest {

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_TRANSACT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TRANSACT_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_EXEC_ID = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EXEC_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ORD_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ORD_STATUS = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORD_REJ_REASON = 1;
    private static final Integer UPDATED_ORD_REJ_REASON = 2;

    private static final String DEFAULT_EXEC_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EXEC_TEXT = "BBBBBBBBBB";

    private static final Long DEFAULT_LAST_QTY = 1L;
    private static final Long UPDATED_LAST_QTY = 2L;

    private static final Long DEFAULT_LEAVES_QTY = 1L;
    private static final Long UPDATED_LEAVES_QTY = 2L;

    private static final Long DEFAULT_CUM_QTY = 1L;
    private static final Long UPDATED_CUM_QTY = 2L;

    private static final BigDecimal DEFAULT_LAST_PX = new BigDecimal(1);
    private static final BigDecimal UPDATED_LAST_PX = new BigDecimal(2);

    private static final BigDecimal DEFAULT_AVG_PX = new BigDecimal(1);
    private static final BigDecimal UPDATED_AVG_PX = new BigDecimal(2);

    private static final String DEFAULT_FIX_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_FIX_MESSAGE = "BBBBBBBBBB";

    @Autowired
    private ExecReportRepository execReportRepository;

    @Autowired
    private ExecReportMapper execReportMapper;

    @Autowired
    private ExecReportService execReportService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExecReportMockMvc;

    private ExecReport execReport;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExecReportResource execReportResource = new ExecReportResource(execReportService);
        this.restExecReportMockMvc = MockMvcBuilders.standaloneSetup(execReportResource)
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
    public static ExecReport createEntity(EntityManager em) {
        ExecReport execReport = new ExecReport()
            .createdAt(DEFAULT_CREATED_AT)
            .transactTime(DEFAULT_TRANSACT_TIME)
            .execId(DEFAULT_EXEC_ID)
            .execType(DEFAULT_EXEC_TYPE)
            .ordStatus(DEFAULT_ORD_STATUS)
            .ordRejReason(DEFAULT_ORD_REJ_REASON)
            .execText(DEFAULT_EXEC_TEXT)
            .lastQty(DEFAULT_LAST_QTY)
            .leavesQty(DEFAULT_LEAVES_QTY)
            .cumQty(DEFAULT_CUM_QTY)
            .lastPx(DEFAULT_LAST_PX)
            .avgPx(DEFAULT_AVG_PX)
            .fixMessage(DEFAULT_FIX_MESSAGE);
        // Add required entity
        StockOrder stockOrder = StockOrderResourceIntTest.createEntity(em);
        em.persist(stockOrder);
        em.flush();
        execReport.setOrder(stockOrder);
        return execReport;
    }

    @Before
    public void initTest() {
        execReport = createEntity(em);
    }

    @Test
    @Transactional
    public void createExecReport() throws Exception {
        int databaseSizeBeforeCreate = execReportRepository.findAll().size();

        // Create the ExecReport
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);
        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isCreated());

        // Validate the ExecReport in the database
        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeCreate + 1);
        ExecReport testExecReport = execReportList.get(execReportList.size() - 1);
        assertThat(testExecReport.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testExecReport.getTransactTime()).isEqualTo(DEFAULT_TRANSACT_TIME);
        assertThat(testExecReport.getExecId()).isEqualTo(DEFAULT_EXEC_ID);
        assertThat(testExecReport.getExecType()).isEqualTo(DEFAULT_EXEC_TYPE);
        assertThat(testExecReport.getOrdStatus()).isEqualTo(DEFAULT_ORD_STATUS);
        assertThat(testExecReport.getOrdRejReason()).isEqualTo(DEFAULT_ORD_REJ_REASON);
        assertThat(testExecReport.getExecText()).isEqualTo(DEFAULT_EXEC_TEXT);
        assertThat(testExecReport.getLastQty()).isEqualTo(DEFAULT_LAST_QTY);
        assertThat(testExecReport.getLeavesQty()).isEqualTo(DEFAULT_LEAVES_QTY);
        assertThat(testExecReport.getCumQty()).isEqualTo(DEFAULT_CUM_QTY);
        assertThat(testExecReport.getLastPx()).isEqualTo(DEFAULT_LAST_PX);
        assertThat(testExecReport.getAvgPx()).isEqualTo(DEFAULT_AVG_PX);
        assertThat(testExecReport.getFixMessage()).isEqualTo(DEFAULT_FIX_MESSAGE);
    }

    @Test
    @Transactional
    public void createExecReportWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = execReportRepository.findAll().size();

        // Create the ExecReport with an existing ID
        execReport.setId(1L);
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExecReport in the database
        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = execReportRepository.findAll().size();
        // set the field null
        execReport.setCreatedAt(null);

        // Create the ExecReport, which fails.
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTransactTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = execReportRepository.findAll().size();
        // set the field null
        execReport.setTransactTime(null);

        // Create the ExecReport, which fails.
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkExecTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = execReportRepository.findAll().size();
        // set the field null
        execReport.setExecType(null);

        // Create the ExecReport, which fails.
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkOrdStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = execReportRepository.findAll().size();
        // set the field null
        execReport.setOrdStatus(null);

        // Create the ExecReport, which fails.
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        restExecReportMockMvc.perform(post("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExecReports() throws Exception {
        // Initialize the database
        execReportRepository.saveAndFlush(execReport);

        // Get all the execReportList
        restExecReportMockMvc.perform(get("/api/exec-reports?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(execReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].transactTime").value(hasItem(sameInstant(DEFAULT_TRANSACT_TIME))))
            .andExpect(jsonPath("$.[*].execId").value(hasItem(DEFAULT_EXEC_ID.toString())))
            .andExpect(jsonPath("$.[*].execType").value(hasItem(DEFAULT_EXEC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ordStatus").value(hasItem(DEFAULT_ORD_STATUS.toString())))
            .andExpect(jsonPath("$.[*].ordRejReason").value(hasItem(DEFAULT_ORD_REJ_REASON)))
            .andExpect(jsonPath("$.[*].execText").value(hasItem(DEFAULT_EXEC_TEXT.toString())))
            .andExpect(jsonPath("$.[*].lastQty").value(hasItem(DEFAULT_LAST_QTY.intValue())))
            .andExpect(jsonPath("$.[*].leavesQty").value(hasItem(DEFAULT_LEAVES_QTY.intValue())))
            .andExpect(jsonPath("$.[*].cumQty").value(hasItem(DEFAULT_CUM_QTY.intValue())))
            .andExpect(jsonPath("$.[*].lastPx").value(hasItem(DEFAULT_LAST_PX.intValue())))
            .andExpect(jsonPath("$.[*].avgPx").value(hasItem(DEFAULT_AVG_PX.intValue())))
            .andExpect(jsonPath("$.[*].fixMessage").value(hasItem(DEFAULT_FIX_MESSAGE.toString())));
    }
    
    @Test
    @Transactional
    public void getExecReport() throws Exception {
        // Initialize the database
        execReportRepository.saveAndFlush(execReport);

        // Get the execReport
        restExecReportMockMvc.perform(get("/api/exec-reports/{id}", execReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(execReport.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.transactTime").value(sameInstant(DEFAULT_TRANSACT_TIME)))
            .andExpect(jsonPath("$.execId").value(DEFAULT_EXEC_ID.toString()))
            .andExpect(jsonPath("$.execType").value(DEFAULT_EXEC_TYPE.toString()))
            .andExpect(jsonPath("$.ordStatus").value(DEFAULT_ORD_STATUS.toString()))
            .andExpect(jsonPath("$.ordRejReason").value(DEFAULT_ORD_REJ_REASON))
            .andExpect(jsonPath("$.execText").value(DEFAULT_EXEC_TEXT.toString()))
            .andExpect(jsonPath("$.lastQty").value(DEFAULT_LAST_QTY.intValue()))
            .andExpect(jsonPath("$.leavesQty").value(DEFAULT_LEAVES_QTY.intValue()))
            .andExpect(jsonPath("$.cumQty").value(DEFAULT_CUM_QTY.intValue()))
            .andExpect(jsonPath("$.lastPx").value(DEFAULT_LAST_PX.intValue()))
            .andExpect(jsonPath("$.avgPx").value(DEFAULT_AVG_PX.intValue()))
            .andExpect(jsonPath("$.fixMessage").value(DEFAULT_FIX_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExecReport() throws Exception {
        // Get the execReport
        restExecReportMockMvc.perform(get("/api/exec-reports/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExecReport() throws Exception {
        // Initialize the database
        execReportRepository.saveAndFlush(execReport);

        int databaseSizeBeforeUpdate = execReportRepository.findAll().size();

        // Update the execReport
        ExecReport updatedExecReport = execReportRepository.findById(execReport.getId()).get();
        // Disconnect from session so that the updates on updatedExecReport are not directly saved in db
        em.detach(updatedExecReport);
        updatedExecReport
            .createdAt(UPDATED_CREATED_AT)
            .transactTime(UPDATED_TRANSACT_TIME)
            .execId(UPDATED_EXEC_ID)
            .execType(UPDATED_EXEC_TYPE)
            .ordStatus(UPDATED_ORD_STATUS)
            .ordRejReason(UPDATED_ORD_REJ_REASON)
            .execText(UPDATED_EXEC_TEXT)
            .lastQty(UPDATED_LAST_QTY)
            .leavesQty(UPDATED_LEAVES_QTY)
            .cumQty(UPDATED_CUM_QTY)
            .lastPx(UPDATED_LAST_PX)
            .avgPx(UPDATED_AVG_PX)
            .fixMessage(UPDATED_FIX_MESSAGE);
        ExecReportDTO execReportDTO = execReportMapper.toDto(updatedExecReport);

        restExecReportMockMvc.perform(put("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isOk());

        // Validate the ExecReport in the database
        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeUpdate);
        ExecReport testExecReport = execReportList.get(execReportList.size() - 1);
        assertThat(testExecReport.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testExecReport.getTransactTime()).isEqualTo(UPDATED_TRANSACT_TIME);
        assertThat(testExecReport.getExecId()).isEqualTo(UPDATED_EXEC_ID);
        assertThat(testExecReport.getExecType()).isEqualTo(UPDATED_EXEC_TYPE);
        assertThat(testExecReport.getOrdStatus()).isEqualTo(UPDATED_ORD_STATUS);
        assertThat(testExecReport.getOrdRejReason()).isEqualTo(UPDATED_ORD_REJ_REASON);
        assertThat(testExecReport.getExecText()).isEqualTo(UPDATED_EXEC_TEXT);
        assertThat(testExecReport.getLastQty()).isEqualTo(UPDATED_LAST_QTY);
        assertThat(testExecReport.getLeavesQty()).isEqualTo(UPDATED_LEAVES_QTY);
        assertThat(testExecReport.getCumQty()).isEqualTo(UPDATED_CUM_QTY);
        assertThat(testExecReport.getLastPx()).isEqualTo(UPDATED_LAST_PX);
        assertThat(testExecReport.getAvgPx()).isEqualTo(UPDATED_AVG_PX);
        assertThat(testExecReport.getFixMessage()).isEqualTo(UPDATED_FIX_MESSAGE);
    }

    @Test
    @Transactional
    public void updateNonExistingExecReport() throws Exception {
        int databaseSizeBeforeUpdate = execReportRepository.findAll().size();

        // Create the ExecReport
        ExecReportDTO execReportDTO = execReportMapper.toDto(execReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExecReportMockMvc.perform(put("/api/exec-reports")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(execReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExecReport in the database
        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExecReport() throws Exception {
        // Initialize the database
        execReportRepository.saveAndFlush(execReport);

        int databaseSizeBeforeDelete = execReportRepository.findAll().size();

        // Get the execReport
        restExecReportMockMvc.perform(delete("/api/exec-reports/{id}", execReport.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ExecReport> execReportList = execReportRepository.findAll();
        assertThat(execReportList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecReport.class);
        ExecReport execReport1 = new ExecReport();
        execReport1.setId(1L);
        ExecReport execReport2 = new ExecReport();
        execReport2.setId(execReport1.getId());
        assertThat(execReport1).isEqualTo(execReport2);
        execReport2.setId(2L);
        assertThat(execReport1).isNotEqualTo(execReport2);
        execReport1.setId(null);
        assertThat(execReport1).isNotEqualTo(execReport2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ExecReportDTO.class);
        ExecReportDTO execReportDTO1 = new ExecReportDTO();
        execReportDTO1.setId(1L);
        ExecReportDTO execReportDTO2 = new ExecReportDTO();
        assertThat(execReportDTO1).isNotEqualTo(execReportDTO2);
        execReportDTO2.setId(execReportDTO1.getId());
        assertThat(execReportDTO1).isEqualTo(execReportDTO2);
        execReportDTO2.setId(2L);
        assertThat(execReportDTO1).isNotEqualTo(execReportDTO2);
        execReportDTO1.setId(null);
        assertThat(execReportDTO1).isNotEqualTo(execReportDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(execReportMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(execReportMapper.fromId(null)).isNull();
    }
}
