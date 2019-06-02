package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.ExecReportDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ExecReport and its DTO ExecReportDTO.
 */
@Mapper(componentModel = "spring", uses = {StockOrderMapper.class})
public interface ExecReportMapper extends EntityMapper<ExecReportDTO, ExecReport> {

    @Mapping(source = "order.id", target = "orderId")
    ExecReportDTO toDto(ExecReport execReport);

    @Mapping(source = "orderId", target = "order")
    ExecReport toEntity(ExecReportDTO execReportDTO);

    default ExecReport fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExecReport execReport = new ExecReport();
        execReport.setId(id);
        return execReport;
    }
}
