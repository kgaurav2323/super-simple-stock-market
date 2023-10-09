package com.demo.stockmarket.facade;

import com.demo.stockmarket.domain.Trade;
import com.demo.stockmarket.enums.trade.TradeIndicator;
import com.demo.stockmarket.exception.StockProcessingException;
import com.demo.stockmarket.factory.Stock;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Service
public class TradeTransactionFacade {
    private LinkedBlockingQueue<Trade> tradeCollection = new LinkedBlockingQueue<>();

    /**
     * Records a trade transaction in repository.
     *
     * @param stock      - stock in consideration
     * @param indicator  - {@link com.demo.stockmarket.enums.trade.TradeIndicator} to denote BUY / SELL transactions
     * @param quantity   - quantity of stocks being operated
     * @param stockPrice - price per stock
     * @param timestamp  - timestamp of transaction
     * @return Trade     - Trade record after storing in repository
     */
    public Trade recordTradeTransaction(Stock stock, TradeIndicator indicator, BigDecimal stockPrice, int quantity, LocalDateTime timestamp) {
        Trade trade = Trade.builder().id(UUID.randomUUID()).stock(stock).tradeIndicator(indicator).stockPrice(stockPrice).sharesQuantity(quantity).timestamp(timestamp).build();
        tradeCollection.add(trade);
        return trade;
    }

    /**
     * Get All trade record stored in java object
     * @return set of Trade transaction records
     */
    public HashSet<Trade> getAllRecordedTradeTransactions() {
        if(CollectionUtils.isEmpty(tradeCollection)){
            throw new StockProcessingException("No trade has been recorded yet");
        }
        return new HashSet<>(tradeCollection);
    }

}
