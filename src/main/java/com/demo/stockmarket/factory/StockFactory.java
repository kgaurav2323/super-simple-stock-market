package com.demo.stockmarket.factory;

import com.demo.stockmarket.enums.stock.StockSymbol;
import java.math.BigDecimal;

/**
 * @author kgaurav2323 on 9/10/23
 */
public class StockFactory {
    private static StockFactory INSTANCE = null;
    private StockFactory (){}
    public static StockFactory getInstance(){
        if(INSTANCE == null){
            INSTANCE = new StockFactory();
        }
        return INSTANCE;
    }
   public CommonStock getCommonStock(StockSymbol symbol, BigDecimal lastDividend, BigDecimal parValue) {
        return new CommonStock(symbol, lastDividend, parValue);
    }

    public PreferredStock getPreferredStock(StockSymbol symbol, BigDecimal lastDividend, BigDecimal parValue, BigDecimal fixedDividend) {
        return new PreferredStock(symbol, lastDividend, parValue, fixedDividend);
    }
}
