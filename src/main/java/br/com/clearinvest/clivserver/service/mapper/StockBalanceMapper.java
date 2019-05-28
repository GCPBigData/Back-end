package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.StockBalanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockBalance and its DTO StockBalanceDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, StockFlowMapper.class, StockMapper.class})
public interface StockBalanceMapper extends EntityMapper<StockBalanceDTO, StockBalance> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "lastFlowEntry.id", target = "lastFlowEntryId")
    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.symbol", target = "stockSymbol")
    StockBalanceDTO toDto(StockBalance stockBalance);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "lastFlowEntryId", target = "lastFlowEntry")
    @Mapping(source = "stockId", target = "stock")
    StockBalance toEntity(StockBalanceDTO stockBalanceDTO);

    default StockBalance fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockBalance stockBalance = new StockBalance();
        stockBalance.setId(id);
        return stockBalance;
    }
}
