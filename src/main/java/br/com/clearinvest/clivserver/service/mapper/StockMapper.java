package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.MarketSectorDTO;
import br.com.clearinvest.clivserver.service.dto.StockDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Stock and its DTO StockDTO.
 */
@Mapper(componentModel = "spring", uses = {MarketSectorMapper.class})
public interface StockMapper extends EntityMapper<StockDTO, Stock> {

    @Mapping(source = "marketSector.id", target = "marketSectorId")
    @Mapping(source = "marketSector.name", target = "marketSectorName")
    StockDTO toDto(Stock stock);

    @Mapping(source = "marketSectorId", target = "marketSector")
    Stock toEntity(StockDTO stockDTO);

    default Stock fromId(Long id) {
        if (id == null) {
            return null;
        }
        Stock stock = new Stock();
        stock.setId(id);
        return stock;
    }

}
