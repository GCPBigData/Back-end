package br.com.clearinvest.clivserver.service;

import br.com.clearinvest.clivserver.domain.BrokerageAccount;
import br.com.clearinvest.clivserver.domain.Stock;
import br.com.clearinvest.clivserver.domain.StockOrder;
import br.com.clearinvest.clivserver.repository.BrokerageAccountRepository;
import br.com.clearinvest.clivserver.repository.StockOrderRepository;
import br.com.clearinvest.clivserver.service.dto.StockOrderDTO;
import br.com.clearinvest.clivserver.service.mapper.StockOrderMapper;
import br.com.clearinvest.clivserver.web.rest.errors.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.field.*;
import quickfix.fix44.NewOrderSingle;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing StockOrder.
 */
@Service
@Transactional
public class StockOrderService {

    private static final char FIX_ORD_TYPE_MARKET = '1';

    private final Logger log = LoggerFactory.getLogger(StockOrderService.class);

    private final StockOrderRepository stockOrderRepository;

    private final BrokerageAccountRepository brokerageAccountRepository;

    private final StockOrderMapper stockOrderMapper;

    public StockOrderService(StockOrderRepository stockOrderRepository, BrokerageAccountRepository brokerageAccountRepository,
            StockOrderMapper stockOrderMapper) {
        this.stockOrderRepository = stockOrderRepository;
        this.brokerageAccountRepository = brokerageAccountRepository;
        this.stockOrderMapper = stockOrderMapper;
    }

    /**
     * Save a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    public StockOrderDTO save(StockOrderDTO stockOrderDTO) {
        log.debug("Request to save StockOrder : {}", stockOrderDTO);

        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDTO);
        stockOrder.setDay(LocalDate.now());
        stockOrder.setStatus(StockOrder.STATUS_RECEIVED);

        Optional<BrokerageAccount> accountOptional = brokerageAccountRepository
            .findByIdAndCurrentUser(stockOrder.getBrokerageAccount().getId());

        if (accountOptional.isPresent()) {
            BigDecimal totalPrice = stockOrder.getUnitPrice().multiply(new BigDecimal(stockOrder.getQuantity()));

            BrokerageAccount account = accountOptional.get();
            BigDecimal accountBalance = account.getBalance() == null ? new BigDecimal(0) : account.getBalance();
            if (totalPrice.compareTo(accountBalance) > 0) {
                throw new BusinessException("Saldo na corretora insuficiente.");
            }

            stockOrder.setTotalPrice(totalPrice);
            stockOrder = stockOrderRepository.save(stockOrder);

            DateTimeFormatter localMktDateFormatter = DateTimeFormatter.ofPattern("yyyyddMM");

            NewOrderSingle order = new NewOrderSingle(
                new ClOrdID(stockOrder.getId().toString()),
                new Side(stockOrder.getSide().charAt(0)),
                new TransactTime(LocalDateTime.now()),
                new OrdType(FIX_ORD_TYPE_MARKET));

            order.set(new OrderQty(stockOrder.getQuantity()));

            // instrument block
            Stock stock = stockOrder.getStock();
            order.set(new Symbol(stockOrderDTO.getStockSymbol()));
            order.set(new SecurityID(stockOrderDTO.getStockSymbol()));
            order.set(new SecurityIDSource("8"));
            order.set(new SecurityExchange("XBSP"));
            order.set(new MaturityDate(localMktDateFormatter.format(LocalDate.now())));

            order.set(new Price(stockOrder.getUnitPrice().doubleValue()));
            order.set(new NoAllocs(1));

            NewOrderSingle.NoAllocs noAllocs = new NewOrderSingle.NoAllocs();
            noAllocs.set(new AllocAccount("160119"));
            noAllocs.set(new AllocAcctIDSource(99));
            order.addGroup(noAllocs);

            // https://stackoverflow.com/questions/51520982/fix-message-creating-party-message-how-to-do-it
            NewOrderSingle.NoPartyIDs parties = new NewOrderSingle.NoPartyIDs();
            parties.set(new PartyID("1"));
            parties.set(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
            parties.set(new PartyRole(PartyRole.ENTERING_TRADER));
            order.addGroup(parties);

            order.setString(10122, "DAYTRADE");

            try {
                Session.sendToTarget(order, "160119", "CDRFIX");
            } catch (SessionNotFound sessionNotFound) {
                throw new BusinessException("Sem comunicação com corretora.");
            }

            return stockOrderMapper.toDto(stockOrder);

        } else {
            throw new BusinessException("Conta não encontrada.");
        }
    }

    /**
     * Update a stockOrder.
     *
     * @param stockOrderDTO the entity to save
     * @return the persisted entity
     */
    public StockOrderDTO update(StockOrderDTO stockOrderDTO) {
        log.debug("Request to save StockOrder : {}", stockOrderDTO);

        StockOrder stockOrder = stockOrderMapper.toEntity(stockOrderDTO);
        stockOrder = stockOrderRepository.save(stockOrder);
        return stockOrderMapper.toDto(stockOrder);
    }

    /**
     * Get all the stockOrders.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<StockOrderDTO> findAll() {
        log.debug("Request to get all StockOrders");
        return stockOrderRepository.findAll().stream()
            .map(stockOrderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one stockOrder by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<StockOrderDTO> findOne(Long id) {
        log.debug("Request to get StockOrder : {}", id);
        return stockOrderRepository.findById(id)
            .map(stockOrderMapper::toDto);
    }

    /**
     * Delete the stockOrder by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete StockOrder : {}", id);
        stockOrderRepository.deleteById(id);
    }
}
