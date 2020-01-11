package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.BrokerageProductDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BrokerageProduct and its DTO BrokerageProductDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BrokerageProductMapper extends EntityMapper<BrokerageProductDTO, BrokerageProduct> {


    @Mapping(target = "brokerages", ignore = true)
    BrokerageProduct toEntity(BrokerageProductDTO brokerageProductDTO);

    default BrokerageProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        BrokerageProduct brokerageProduct = new BrokerageProduct();
        brokerageProduct.setId(id);
        return brokerageProduct;
    }
}
