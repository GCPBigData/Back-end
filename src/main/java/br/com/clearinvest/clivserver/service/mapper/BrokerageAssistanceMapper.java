package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageAssistanceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BrokerageAssistance and its DTO BrokerageAssistanceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BrokerageAssistanceMapper extends EntityMapper<BrokerageAssistanceDTO, BrokerageAssistance> {


    @Mapping(target = "brokerages", ignore = true)
    BrokerageAssistance toEntity(BrokerageAssistanceDTO brokerageAssistanceDTO);

    default BrokerageAssistance fromId(Long id) {
        if (id == null) {
            return null;
        }
        BrokerageAssistance brokerageAssistance = new BrokerageAssistance();
        brokerageAssistance.setId(id);
        return brokerageAssistance;
    }
}
