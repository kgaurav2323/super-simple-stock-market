package com.demo.stockmarket.factory;

import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.enums.stock.StockType;
import com.demo.stockmarket.exception.StockProcessingException;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Getter
public class PreferredStock extends Stock {
    private final BigDecimal fixedDividend;
    public PreferredStock(StockSymbol stockSymbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
        super(stockSymbol, StockType.PREFERRED, lastDividend, parValue);
        this.fixedDividend = fixedDividend;
        if (Objects.isNull(fixedDividend) || BigDecimal.ZERO.equals(fixedDividend)) {
            throw new StockProcessingException("Invalid Fixed Dividend value - " + fixedDividend + " found, should be greater than 0");
        }
    }

    @Override
    public BigDecimal dividendYield(BigDecimal price) {
        return roundToDecimalPlace(getFixedDividend().multiply(getParValue()).divide(price));
    }
}
