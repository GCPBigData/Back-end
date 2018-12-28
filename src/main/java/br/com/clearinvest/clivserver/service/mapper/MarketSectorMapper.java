package br.com.clearinvest.clivserver.service.mapper;

import br.com.clearinvest.clivserver.domain.*;
import br.com.clearinvest.clivserver.service.dto.MarketSectorDTO;

import br.com.clearinvest.clivserver.service.dto.StockDTO;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity MarketSector and its DTO MarketSectorDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MarketSectorMapper extends EntityMapper<MarketSectorDTO, MarketSector> {

    default MarketSector fromId(Long id) {
        if (id == null) {
            return null;
        }
        MarketSector marketSector = new MarketSector();
        marketSector.setId(id);
        return marketSector;
    }

    List<StockDTO> stocksToDto(List<Stock> stocks);
    //MarketSectorDTO marketSectorToDto(MarketSector marketSector);

    /*@Override
    default List<MarketSectorDTO> toDto(List<MarketSector> entityList) {
        List<MarketSectorDTO> dtoList = new ArrayList<>();

        entityList.stream()
            .map((entity) -> {
                MarketSectorDTO marketSectorDTO
            })
        ;

    }*/
}
