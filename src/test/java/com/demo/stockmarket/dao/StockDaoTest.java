package com.demo.stockmarket.dao;

import com.demo.stockmarket.enums.stock.StockSymbol;
import com.demo.stockmarket.factory.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author kgaurav2323 on 9/10/23
 */
@ExtendWith(MockitoExtension.class)
public class StockDaoTest {
    private StockDao stockDao;

    @BeforeEach
    public void setup(){
        stockDao = new StockDao();
    }

    @Test
    public void getStockDetails_succeed(){
        Map<String, Stock> stockData = stockDao.getStockDetails();
        assertNotNull(stockData);
        assertEquals(5, stockData.size());
        assertTrue(stockData.containsKey(StockSymbol.TEA.name()));
    }
}
