package com.demo.stockmarket.exception;

/**
 * @author kgaurav2323 on 9/10/23
 */
public class StockProcessingException extends RuntimeException {
    public StockProcessingException(String message) {
        super(message);
    }
}
