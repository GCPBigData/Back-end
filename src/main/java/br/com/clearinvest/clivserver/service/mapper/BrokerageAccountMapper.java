package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageAccountDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BrokerageAccount and its DTO BrokerageAccountDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, BrokerageMapper.class})
public interface BrokerageAccountMapper extends EntityMapper<BrokerageAccountDTO, BrokerageAccount> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "brokerage.id", target = "brokerageId")
    @Mapping(source = "brokerage.name", target = "brokerageName")
    BrokerageAccountDTO toDto(BrokerageAccount brokerageAccount);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "brokerageId", target = "brokerage")
    BrokerageAccount toEntity(BrokerageAccountDTO brokerageAccountDTO);

    default BrokerageAccount fromId(Long id) {
        if (id == null) {
            return null;
        }
        BrokerageAccount brokerageAccount = new BrokerageAccount();
        brokerageAccount.setId(id);
        return brokerageAccount;
    }
}
