package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BrokerageClient and its DTO BrokerageClientDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BrokerageClientMapper extends EntityMapper<BrokerageClientDTO, BrokerageClient> {


    @Mapping(target = "brokerages", ignore = true)
    BrokerageClient toEntity(BrokerageClientDTO brokerageClientDTO);

    default BrokerageClient fromId(Long id) {
        if (id == null) {
            return null;
        }
        BrokerageClient brokerageClient = new BrokerageClient();
        brokerageClient.setId(id);
        return brokerageClient;
    }
}
