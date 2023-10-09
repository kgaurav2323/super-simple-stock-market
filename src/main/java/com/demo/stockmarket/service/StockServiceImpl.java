package com.demo.stockmarket.service;

import com.demo.stockmarket.dao.StockDao;
import com.demo.stockmarket.domain.Trade;
import com.demo.stockmarket.enums.trade.TradeIndicator;
import com.demo.stockmarket.exception.StockProcessingException;
import com.demo.stockmarket.facade.TradeTransactionFacade;
import com.demo.stockmarket.factory.CommonStock;
import com.demo.stockmarket.factory.PreferredStock;
import com.demo.stockmarket.factory.Stock;
import com.demo.stockmarket.factory.StockFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.StatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Service
@Slf4j
public class StockServiceImpl implements StockService{
    @Autowired
    private TradeTransactionFacade tradeTransaction;
    @Autowired
    private StockDao stockDao;
    @Override
    public BigDecimal getDividendYield(String stockSymbol, BigDecimal price) {
        log.debug("dividendYield calculation begins for given stock symbol :: {} and price :: {}", stockSymbol, price);
        Stock stock = validateInputAndGetStock(stockSymbol, price);
        if(stock instanceof CommonStock){
            CommonStock commonStock = (CommonStock) stock;
            return commonStock.dividendYield(price);
        }else if(stock instanceof PreferredStock){
            PreferredStock prefStock = (PreferredStock) stock;
            return prefStock.dividendYield(price);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal getPByERatio(String stockSymbol, BigDecimal price) {
        log.debug("getPByERatio calculation begins for given stock symbol :: {} and price :: {}", stockSymbol, price);
        Stock stock = validateInputAndGetStock(stockSymbol, price);
        Optional.ofNullable(stock).orElseThrow(() -> new StockProcessingException("Stock object can not be null"));
        return stock.pByERatio(price);
    }

    private Stock validateInputAndGetStock(String stockSymbol, BigDecimal price){
        if(BigDecimal.ZERO.equals(price)){
            throw new StockProcessingException("Stock price value - " +price +" can not be 0");
        }
        Stock stockRecord = Optional.ofNullable(stockDao.getStockDetails()).orElseThrow(() -> new StockProcessingException("Empty stock data map found")).get(stockSymbol);
        Optional.ofNullable(stockRecord).orElseThrow(() -> new StockProcessingException("Invalid stock symbol provided"));
        StockFactory factory = StockFactory.getInstance();
        switch (stockRecord.getStockType()) {
            case COMMON:
                return factory.getCommonStock(stockRecord.getStockSymbol(), stockRecord.getLastDividend(), stockRecord.getParValue());
            case PREFERRED:
                if(stockRecord instanceof PreferredStock){
                    PreferredStock prefStock = (PreferredStock) stockRecord;
                    return factory.getPreferredStock(prefStock.getStockSymbol(), prefStock.getLastDividend(), prefStock.getParValue(), prefStock.getFixedDividend());
                }
        }
        return null;
    }

    @Override
    public BigDecimal getVolumeWeightedStockPrice(String stockSymbol, int intervalInMinutes) {
        log.debug("getVolumeWeightedStockPrice calculation begins for given stock symbol :: {} and cutOffTimeInterval :: {}", stockSymbol, intervalInMinutes);
        if(intervalInMinutes<0){
            throw new StockProcessingException("Stock time interval can not be negative");
        }
        final LocalDateTime timeIntervalCutOff = LocalDateTime.now().minusMinutes(intervalInMinutes);
        final HashSet<Trade> tradeTransactions = tradeTransaction.getAllRecordedTradeTransactions();
        AtomicInteger quantityTotal = new AtomicInteger(0);
        BigDecimal priceAndQuantitySum = tradeTransactions.stream().parallel()
                .filter(trade -> trade.getTimestamp().isAfter(timeIntervalCutOff))
                .filter(trade -> trade.getStock().getStockSymbol().name().equals(stockSymbol))
                .map(trade -> {
                    quantityTotal.addAndGet(trade.getSharesQuantity());
                    return trade.getStockPrice().multiply(BigDecimal.valueOf(trade.getSharesQuantity()));
                })
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        //Returning the default value as ZERO to avoid 0/0 scenario
        if(BigDecimal.ZERO.equals(priceAndQuantitySum)){
            return BigDecimal.ZERO;
        }
        return roundToDecimalPlace.apply(priceAndQuantitySum.divide(BigDecimal.valueOf(quantityTotal.get())));
    }

    @Override
    public BigDecimal getGBCEAllShareIndex() {
        final HashSet<Trade> tradeTransactions = tradeTransaction.getAllRecordedTradeTransactions();
        log.debug("getGBCEAllShareIndex calculation begins with no.of recorded trade transactions :: {}",tradeTransactions.size());
        return Optional.ofNullable(tradeTransactions)
                .map(this::getTradePriceInArray)
                .map(priceArr -> BigDecimal.valueOf(StatUtils.geometricMean(priceArr)))
                .map(roundToDecimalPlace)
                .orElseThrow(()-> new StockProcessingException("There is no trade transactions has been recorded yet .."));
    }

    @Override
    public Trade buyStock(Stock stock, BigDecimal price, int quantity, LocalDateTime timestamp) {
        return tradeTransaction.recordTradeTransaction(stock, TradeIndicator.BUY, price, quantity, timestamp);
    }

    @Override
    public Trade sellStock(Stock stock, BigDecimal price, int quantity, LocalDateTime timestamp) {
        return tradeTransaction.recordTradeTransaction(stock, TradeIndicator.SELL, price, quantity, timestamp);
    }

    private double[] getTradePriceInArray(HashSet<Trade> tradeTransactions) {
        Double[] priceArr = tradeTransactions.stream()
                .map(Trade::getStockPrice)
                .map(BigDecimal::doubleValue)
                .filter(price -> (price > 0))
                .toArray(Double[]::new);
        return Stream.of(priceArr).mapToDouble(Double::doubleValue).toArray();
    }
    private final Function<BigDecimal, BigDecimal> roundToDecimalPlace = (price) -> price.setScale(2, RoundingMode.HALF_EVEN);

}
