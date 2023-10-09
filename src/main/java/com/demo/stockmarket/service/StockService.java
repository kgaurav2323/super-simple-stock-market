package com.demo.stockmarket.service;

import com.demo.stockmarket.domain.Trade;
import com.demo.stockmarket.factory.Stock;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author kgaurav2323 on 9/10/23
 */
public interface StockService {

    /**
     * Calculates dividend yield for the given price
     * @param stockSymbol   - This property helps to identify stock uniquely
     * @param price         - Given price to calculate dividend yield
     * @return              - The rounded dividendYield in BigDecimal format
     */
    BigDecimal getDividendYield(String stockSymbol, BigDecimal price);

    /**
     * Calculate pByE Ratio for the given price
     * @param stockSymbol  - This property helps to identify stock uniquely
     * @param price        - Given price to calculate dividend yield
     * @return             - The rounded pByERatio in BigDecimal format
     */
    BigDecimal getPByERatio(String stockSymbol, BigDecimal price);


    /**
     * Calculates Volume Weighted Stock Price based on trades in past given intervalInMinutes
     * considering all trade transaction records in {@link com.demo.stockmarket.facade.TradeTransactionFacade}
     * @param stockSymbol       - use stock symbol to find stocks on which trade records should be considered
     * @param intervalInMinutes - trade cutoff time interval in minutes
     * @return                  - The rounded big decimal value for volume Weighted Stock Price
     */
    BigDecimal getVolumeWeightedStockPrice(String stockSymbol, int intervalInMinutes);

    /**
     * Calculates GBCEAllShareIndex price considering all trade transaction records in {@link com.demo.stockmarket.facade.TradeTransactionFacade}
     * @return Calculated value of GBCEAllShareIndex price
     */
    BigDecimal getGBCEAllShareIndex();

    /**
     * Buy stock and record its entry in Java memory
     * @param stock         - stock on which the transaction is being performed
     * @param price         - price in which stocks are bought
     * @param quantity      - no. of stocks bought
     * @param timestamp     - timestamp of trade
     * @return Trade        - return trade object
     */
    Trade buyStock(Stock stock, BigDecimal price, int quantity, LocalDateTime timestamp);


    /**
     * Sell stock and record its entry in Java memory
     * @param stock     - stock on which the transaction is being performed
     * @param price     - price in which stocks are sold
     * @param quantity  - no. of stocks sold
     * @param timestamp - timestamp of trade
     * @return Trade    - return trade object
     */
    Trade sellStock(Stock stock, BigDecimal price, int quantity, LocalDateTime timestamp);
}
