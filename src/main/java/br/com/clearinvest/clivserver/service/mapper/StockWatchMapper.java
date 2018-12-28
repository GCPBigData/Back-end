package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.StockWatchDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockWatch and its DTO StockWatchDTO.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class, UserMapper.class})
public interface StockWatchMapper extends EntityMapper<StockWatchDTO, StockWatch> {

    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.company", target = "stockCompany")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    StockWatchDTO toDto(StockWatch stockWatch);

    @Mapping(source = "stockId", target = "stock")
    @Mapping(source = "userId", target = "user")
    StockWatch toEntity(StockWatchDTO stockWatchDTO);

    default StockWatch fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockWatch stockWatch = new StockWatch();
        stockWatch.setId(id);
        return stockWatch;
    }
}
