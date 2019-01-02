package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.StockWatch;
import br.com.clearinvest.clivserver.repository.StockWatchRepository;
import br.com.clearinvest.clivserver.repository.UserRepository;
import br.com.clearinvest.clivserver.security.AuthoritiesConstants;
import br.com.clearinvest.clivserver.security.SecurityUtils;
import br.com.clearinvest.clivserver.service.dto.StockWatchDTO;
import br.com.clearinvest.clivserver.service.mapper.StockWatchMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing StockWatch.
 */
@Service
@Transactional
public class StockWatchService {

    private final Logger log = LoggerFactory.getLogger(StockWatchService.class);

    private final StockWatchRepository stockWatchRepository;

    private final StockWatchMapper stockWatchMapper;

    private final UserRepository userRepository;

    public StockWatchService(StockWatchRepository stockWatchRepository, StockWatchMapper stockWatchMapper, UserRepository userRepository) {
        this.stockWatchRepository = stockWatchRepository;
        this.stockWatchMapper = stockWatchMapper;
        this.userRepository = userRepository;
    }

    /**
     * Save a stockWatch.
     *
     * @param stockWatchDTO the entity to save
     * @return the persisted entity
     */
    public StockWatchDTO save(StockWatchDTO stockWatchDTO) {
        log.debug("Request to save StockWatch : {}", stockWatchDTO);

        if (!SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN)) {
            SecurityUtils.getCurrentUserLogin()
                .flatMap(userRepository::findOneByLogin)
                .ifPresent(user -> {
                    stockWatchDTO.setUserId(user.getId());
                });
        }

        StockWatch stockWatch = stockWatchMapper.toEntity(stockWatchDTO);
        stockWatch = stockWatchRepository.save(stockWatch);
        return stockWatchMapper.toDto(stockWatch);
    }

    /**
     * Get all the stockWatches.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<StockWatchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all StockWatches");
        return stockWatchRepository.findAll(pageable)
            .map(stockWatchMapper::toDto);
    }


    /**
     * Get one stockWatch by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockWatchDTO> findOne(Long id) {
        log.debug("Request to get StockWatch : {}", id);
        return stockWatchRepository.findById(id)
            .map(stockWatchMapper::toDto);
    }

    /**
     * Delete the stockWatch by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockWatch : {}", id);
        stockWatchRepository.deleteById(id);
    }

    /**
     * Remove the stock from the stock watch.
     *
     * @param stockId the id of the stock
     */
    public void deleteByStockId(Long stockId) {
        log.debug("Request to delete StockWatch by stock id: {}", stockId);
        Optional<StockWatch> stockWatch = stockWatchRepository.findByStockIdAndCurrentUser(stockId);
        if (stockWatch.isPresent()) {
            stockWatchRepository.deleteById(stockWatch.get().getId());
        }
    }
}
