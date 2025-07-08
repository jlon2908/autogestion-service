package com.arka.autogestion_service.domain.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException() {
        super(com.arka.autogestion_service.domain.util.ExceptionMessages.CART_EMPTY);
    }
}
