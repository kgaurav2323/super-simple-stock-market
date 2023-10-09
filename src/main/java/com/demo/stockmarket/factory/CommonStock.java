package com.demo.stockmarket.factory;

import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.enums.stock.StockType;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Getter
public class CommonStock extends Stock{
    public CommonStock(StockSymbol stockSymbol, BigDecimal lastDividend, BigDecimal parValue) {
        super(stockSymbol, StockType.COMMON, lastDividend, parValue);
    }

    @Override
    public BigDecimal dividendYield(BigDecimal price) {
        return roundToDecimalPlace(getLastDividend().divide(price));
    }
}
