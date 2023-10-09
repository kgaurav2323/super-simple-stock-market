package com.demo.stockmarket.controller;

import com.demo.stockmarket.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author kgaurav2323 on 9/10/23
 */

@RestController
@RequestMapping("/v1/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping("/dividend/{symbol}/{price}")
    public ResponseEntity<BigDecimal> getDividendYield(@PathVariable ("symbol") String symbol,
                                                       @PathVariable ("price") BigDecimal price){
        return ResponseEntity.ok(stockService.getDividendYield(symbol, price));
    }

    @GetMapping("/pe/ratio/{symbol}/{price}")
    public ResponseEntity<BigDecimal> getPByERatio(@PathVariable ("symbol") String symbol,
                                                   @PathVariable ("price") BigDecimal price){
        return ResponseEntity.ok(stockService.getPByERatio(symbol, price));
    }

    @GetMapping("/volume/weighted/price/{symbol}/{interval}")
    public ResponseEntity<BigDecimal> getVolumeWeightedStockPrice(@PathVariable ("symbol") String symbol,
                                                                  @PathVariable ("interval") Integer timeInterval){
        return ResponseEntity.ok(stockService.getVolumeWeightedStockPrice(symbol, timeInterval));
    }

    @GetMapping("/gbce/share/index")
    public ResponseEntity<BigDecimal> getGBCEAllShareIndex(){
        return ResponseEntity.ok(stockService.getGBCEAllShareIndex());
    }

}
