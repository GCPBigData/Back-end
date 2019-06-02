package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageFlowDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BrokerageFlow and its DTO BrokerageFlowDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BrokerageAccountMapper.class, StockTradeMapper.class})
public interface BrokerageFlowMapper extends EntityMapper<BrokerageFlowDTO, BrokerageFlow> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "brokerageAccount.id", target = "brokerageAccountId")
    @Mapping(source = "trade.id", target = "tradeId")
    BrokerageFlowDTO toDto(BrokerageFlow brokerageFlow);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "brokerageAccountId", target = "brokerageAccount")
    @Mapping(source = "tradeId", target = "trade")
    BrokerageFlow toEntity(BrokerageFlowDTO brokerageFlowDTO);

    default BrokerageFlow fromId(Long id) {
        if (id == null) {
            return null;
        }
        BrokerageFlow brokerageFlow = new BrokerageFlow();
        brokerageFlow.setId(id);
        return brokerageFlow;
    }
}
