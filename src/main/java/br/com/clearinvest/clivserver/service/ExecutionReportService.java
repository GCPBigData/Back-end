package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.ExecutionReport;
import br.com.clearinvest.clivserver.repository.ExecutionReportRepository;
import br.com.clearinvest.clivserver.service.dto.ExecutionReportDTO;
import br.com.clearinvest.clivserver.service.mapper.ExecutionReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ExecutionReport.
 */
@Service
@Transactional
public class ExecutionReportService {

    private final Logger log = LoggerFactory.getLogger(ExecutionReportService.class);

    private final ExecutionReportRepository executionReportRepository;

    private final ExecutionReportMapper executionReportMapper;

    public ExecutionReportService(ExecutionReportRepository executionReportRepository, ExecutionReportMapper executionReportMapper) {
        this.executionReportRepository = executionReportRepository;
        this.executionReportMapper = executionReportMapper;
    }

    /**
     * Save a executionReport.
     *
     * @param executionReportDTO the entity to save
     * @return the persisted entity
     */
    public ExecutionReportDTO save(ExecutionReportDTO executionReportDTO) {
        log.debug("Request to save ExecutionReport : {}", executionReportDTO);

        ExecutionReport executionReport = executionReportMapper.toEntity(executionReportDTO);
        executionReport = executionReportRepository.save(executionReport);
        return executionReportMapper.toDto(executionReport);
    }

    /**
     * Get all the executionReports.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ExecutionReportDTO> findAll() {
        log.debug("Request to get all ExecutionReports");
        return executionReportRepository.findAll().stream()
            .map(executionReportMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one executionReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ExecutionReportDTO> findOne(Long id) {
        log.debug("Request to get ExecutionReport : {}", id);
        return executionReportRepository.findById(id)
            .map(executionReportMapper::toDto);
    }

    /**
     * Delete the executionReport by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExecutionReport : {}", id);
        executionReportRepository.deleteById(id);
    }
}
