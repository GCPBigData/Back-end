package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Brokerage and its DTO BrokerageDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BrokerageMapper extends EntityMapper<BrokerageDTO, Brokerage> {



    default Brokerage fromId(Long id) {
        if (id == null) {
            return null;
        }
        Brokerage brokerage = new Brokerage();
        brokerage.setId(id);
        return brokerage;
    }
}
