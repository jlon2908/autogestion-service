package com.arka.autogestion_service.infraestructure.global;

import com.arka.autogestion_service.domain.exception.CartEmptyException;
import com.arka.autogestion_service.domain.exception.StockNotEnough;
import com.arka.autogestion_service.domain.util.ExceptionMessages;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StockNotEnough.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ErrorResponse> handleStockNotEnough(StockNotEnough ex, ServerWebExchange exchange) {
        return Mono.just(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .path(exchange.getRequest().getPath().value())
                        .message(ex.getMessage())
                        .errorCode("STOCK_NOT_ENOUGH")
                        .build()
        );
    }

    @ExceptionHandler(CartEmptyException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<ErrorResponse> handleCartEmpty(CartEmptyException ex, ServerWebExchange exchange) {
        return Mono.just(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .path(exchange.getRequest().getPath().value())
                        .message(ExceptionMessages.CART_EMPTY)
                        .errorCode("CART_EMPTY")
                        .build()
        );
    }
}
