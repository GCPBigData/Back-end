package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity StockOrder and its DTO StockOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {StockMapper.class, BrokerageAccountMapper.class, UserMapper.class, StockTradeMapper.class})
public interface StockOrderMapper extends EntityMapper<StockOrderDTO, StockOrder> {

    @Mapping(source = "stock.id", target = "stockId")
    @Mapping(source = "stock.symbol", target = "stockSymbol")
    @Mapping(source = "brokerageAccount.id", target = "brokerageAccountId")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.login", target = "createdByLogin")
    @Mapping(source = "trade.id", target = "tradeId")
    StockOrderDTO toDto(StockOrder stockOrder);

    @Mapping(source = "stockId", target = "stock")
    @Mapping(source = "brokerageAccountId", target = "brokerageAccount")
    @Mapping(source = "createdById", target = "createdBy")
    @Mapping(source = "tradeId", target = "trade")
    StockOrder toEntity(StockOrderDTO stockOrderDTO);

    default StockOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        StockOrder stockOrder = new StockOrder();
        stockOrder.setId(id);
        return stockOrder;
    }
}
