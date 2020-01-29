package br.com.clearinvest.clivserver.service;

import static java.util.stream.Collectors.toList;

import br.com.clearinvest.clivserver.domain.MarketSector;
import br.com.clearinvest.clivserver.repository.MarketSectorRepository;
import br.com.clearinvest.clivserver.service.dto.MarketSectorDTO;
import br.com.clearinvest.clivserver.service.dto.StockDTO;
import br.com.clearinvest.clivserver.service.mapper.MarketSectorMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing MarketSector.
 */
@Service
@Transactional
public class MarketSectorService {

    private final Logger log = LoggerFactory.getLogger(MarketSectorService.class);

    private final MarketSectorRepository marketSectorRepository;

    private final MarketSectorMapper marketSectorMapper;

    public MarketSectorService(MarketSectorRepository marketSectorRepository, MarketSectorMapper marketSectorMapper) {
        this.marketSectorRepository = marketSectorRepository;
        this.marketSectorMapper = marketSectorMapper;
    }

    /**
     * Save a marketSector.
     *
     * @param marketSectorDTO the entity to save
     * @return the persisted entity
     */
    public MarketSectorDTO save(MarketSectorDTO marketSectorDTO) {
        log.debug("Request to save MarketSector : {}", marketSectorDTO);

        MarketSector marketSector = marketSectorMapper.toEntity(marketSectorDTO);
        marketSector = marketSectorRepository.save(marketSector);
        return marketSectorMapper.toDto(marketSector);
    }

    /**
     * Get all the marketSectors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MarketSectorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarketSectors");
        Page<MarketSectorDTO> marketSectorDTOS = marketSectorRepository.findAll(pageable).map(marketSectorMapper::toDto);
        marketSectorDTOS.forEach(x -> x.setStocks(x.getStocks().stream().filter(StockDTO::isActivated).collect(toList())));
        return marketSectorDTOS;
    }

    /**
     * Get all the marketSectors.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<MarketSectorDTO> findAllWithStocks() {
        List<MarketSectorDTO> marketSectorDTOS = marketSectorRepository.findAllWithStocks()
                .stream()
                .map(marketSectorMapper::toDto)
                .collect(toList());
        marketSectorDTOS.forEach(x -> x.setStocks(x.getStocks().stream().filter(StockDTO::isActivated).collect(toList())));
        return marketSectorDTOS;
    }
    
    /**
     * Get one marketSector by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<MarketSectorDTO> findOne(Long id) {
        log.debug("Request to get MarketSector : {}", id);
        Optional<MarketSectorDTO> marketSectorDTO = marketSectorRepository.findById(id)
                .map(marketSectorMapper::toDto);
        marketSectorDTO.ifPresent(mktDTO -> mktDTO.setStocks(marketSectorDTO.get().getStocks().stream().filter(StockDTO::isActivated).collect(toList())));
        return marketSectorDTO;
    }

    /**
     * Delete the marketSector by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete MarketSector : {}", id);
        marketSectorRepository.deleteById(id);
    }
}
