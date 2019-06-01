package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.StockTradeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockTrade and its DTO StockTradeDTO.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class, BrokerageAccountMapper.class, StockOrderMapper.class, UserMapper.class})
public interface StockTradeMapper extends EntityMapper<StockTradeDTO, StockTrade> {

    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.symbol", target = "stockSymbol")
    @Mapping(source = "brokerageAccount.id", target = "brokerageAccountId")
    @Mapping(source = "mainOrder.id", target = "mainOrderId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    StockTradeDTO toDto(StockTrade stockTrade);

    @Mapping(source = "stockId", target = "stock")
    @Mapping(source = "brokerageAccountId", target = "brokerageAccount")
    @Mapping(source = "mainOrderId", target = "mainOrder")
    @Mapping(target = "orders", ignore = true)
    @Mapping(source = "createdById", target = "createdBy")
    StockTrade toEntity(StockTradeDTO stockTradeDTO);

    default StockTrade fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockTrade stockTrade = new StockTrade();
        stockTrade.setId(id);
        return stockTrade;
    }
}
