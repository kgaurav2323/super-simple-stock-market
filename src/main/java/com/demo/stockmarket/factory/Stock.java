package com.demo.stockmarket.factory;

import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.enums.stock.StockType;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Getter
public abstract class Stock {
    private final StockSymbol stockSymbol;
    private final StockType stockType;
    private final BigDecimal lastDividend;
    private final BigDecimal parValue;

    public Stock(StockSymbol symbol, StockType stockType, BigDecimal lastDividend, BigDecimal parValue) {
        this.stockSymbol = symbol;
        this.stockType = stockType;
        this.lastDividend = lastDividend;
        this.parValue = parValue;
    }

    abstract BigDecimal dividendYield(BigDecimal price);

    /**
     * Common method to calculate pByERatio for given price
     * applicable for both common and preferred stock type
     * @param price
     * @return pByERatio
     */
    public BigDecimal pByERatio(BigDecimal price) {
        if(BigDecimal.ZERO.equals(lastDividend)){
            return BigDecimal.ZERO;
        }
        return roundToDecimalPlace(price.divide(lastDividend));

    }

    /**
     * Utility method to format given value into percentage scale
     * @param value
     * @return roundToDecimalPlace
     */
     BigDecimal roundToDecimalPlace(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

}
