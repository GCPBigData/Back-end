package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.ExecutionReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ExecutionReport and its DTO ExecutionReportDTO.
 */
@Mapper(componentModel = "spring", uses = {StockOrderMapper.class})
public interface ExecutionReportMapper extends EntityMapper<ExecutionReportDTO, ExecutionReport> {

    @Mapping(source = "order.id", target = "orderId")
    ExecutionReportDTO toDto(ExecutionReport executionReport);

    @Mapping(source = "orderId", target = "order")
    ExecutionReport toEntity(ExecutionReportDTO executionReportDTO);

    default ExecutionReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExecutionReport executionReport = new ExecutionReport();
        executionReport.setId(id);
        return executionReport;
    }
}
