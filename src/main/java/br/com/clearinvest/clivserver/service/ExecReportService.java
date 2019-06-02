package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.ExecReport;
import br.com.clearinvest.clivserver.repository.ExecReportRepository;
import br.com.clearinvest.clivserver.service.dto.ExecReportDTO;
import br.com.clearinvest.clivserver.service.mapper.ExecReportMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ExecReport.
 */
@Service
@Transactional
public class ExecReportService {

    private final Logger log = LoggerFactory.getLogger(ExecReportService.class);

    private final ExecReportRepository execReportRepository;

    private final ExecReportMapper execReportMapper;

    public ExecReportService(ExecReportRepository execReportRepository, ExecReportMapper execReportMapper) {
        this.execReportRepository = execReportRepository;
        this.execReportMapper = execReportMapper;
    }

    /**
     * Save a execReport.
     *
     * @param execReportDTO the entity to save
     * @return the persisted entity
     */
    public ExecReportDTO save(ExecReportDTO execReportDTO) {
        log.debug("Request to save ExecReport : {}", execReportDTO);

        ExecReport execReport = execReportMapper.toEntity(execReportDTO);
        execReport = execReportRepository.save(execReport);
        return execReportMapper.toDto(execReport);
    }

    /**
     * Get all the execReports.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<ExecReportDTO> findAll() {
        log.debug("Request to get all ExecReports");
        return execReportRepository.findAll().stream()
            .map(execReportMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one execReport by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ExecReportDTO> findOne(Long id) {
        log.debug("Request to get ExecReport : {}", id);
        return execReportRepository.findById(id)
            .map(execReportMapper::toDto);
    }

    /**
     * Delete the execReport by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete ExecReport : {}", id);
        execReportRepository.deleteById(id);
    }
}
