package com.demo.stockmarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author kgaurav2323 on 9/10/23
 */
@ControllerAdvice
public class StockControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(StockProcessingException.class)
    public ResponseEntity<Object> handleStockProcessingException(StockProcessingException ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
