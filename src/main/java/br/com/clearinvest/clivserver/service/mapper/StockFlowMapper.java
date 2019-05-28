package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.StockFlowDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockFlow and its DTO StockFlowDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ExecutionReportMapper.class, StockMapper.class})
public interface StockFlowMapper extends EntityMapper<StockFlowDTO, StockFlow> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "execReport.id", target = "execReportId")
    @Mapping(source = "execReport.execId", target = "execReportExecId")
    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.symbol", target = "stockSymbol")
    StockFlowDTO toDto(StockFlow stockFlow);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "execReportId", target = "execReport")
    @Mapping(source = "stockId", target = "stock")
    StockFlow toEntity(StockFlowDTO stockFlowDTO);

    default StockFlow fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockFlow stockFlow = new StockFlow();
        stockFlow.setId(id);
        return stockFlow;
    }
}
