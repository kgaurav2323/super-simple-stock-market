package com.demo.stockmarket.dao;

import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.factory.Stock;
import com.demo.stockmarket.factory.StockFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kgaurav2323 on 9/10/23
 */
@Component
@Slf4j
public class StockDao {
    /**
     * Store stock details in java memory
     * @return stock data in Map<String, Stock>
     */
    public Map<String, Stock> getStockDetails(){
        Map<String, Stock> stockData = new HashMap<>();
        StockFactory stockFactory = StockFactory.getInstance();
        stockData.put(StockSymbol.TEA.name(), stockFactory.getCommonStock(StockSymbol.TEA, BigDecimal.ZERO, new BigDecimal(100)));
        stockData.put(StockSymbol.POP.name(), stockFactory.getCommonStock(StockSymbol.POP, new BigDecimal(8), new BigDecimal(100)));
        stockData.put(StockSymbol.ALE.name(), stockFactory.getCommonStock(StockSymbol.ALE, new BigDecimal(23), new BigDecimal(60)));
        stockData.put(StockSymbol.GIN.name(), stockFactory.getPreferredStock(StockSymbol.GIN, new BigDecimal(8), new BigDecimal(100), new BigDecimal(2)));
        stockData.put(StockSymbol.JOE.name(), stockFactory.getCommonStock(StockSymbol.JOE, new BigDecimal(13), new BigDecimal(250)));
        log.info("{} stocks record found to operate on", stockData.size());
        return stockData;
    }
}
