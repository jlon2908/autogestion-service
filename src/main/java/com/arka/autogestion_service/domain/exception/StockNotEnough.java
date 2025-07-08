package com.arka.autogestion_service.domain.exception;

import com.arka.autogestion_service.domain.util.ExceptionMessages;

public class StockNotEnough extends RuntimeException{
    public StockNotEnough(){
        super(ExceptionMessages.STOCK_NOT_FOUND);
    }
}
