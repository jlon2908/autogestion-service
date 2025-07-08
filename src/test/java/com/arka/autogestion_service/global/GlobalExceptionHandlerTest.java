package com.arka.autogestion_service.global;

import com.arka.autogestion_service.domain.exception.CartEmptyException;
import com.arka.autogestion_service.domain.exception.StockNotEnough;
import com.arka.autogestion_service.infraestructure.global.ErrorResponse;
import com.arka.autogestion_service.infraestructure.global.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {
    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleStockNotEnough_returnsErrorResponse() {
        StockNotEnough ex = new StockNotEnough();
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        var request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(org.springframework.http.server.RequestPath.parse("/cart/add", "/cart/add"));

        Mono<ErrorResponse> responseMono = globalExceptionHandler.handleStockNotEnough(ex, exchange);
        StepVerifier.create(responseMono)
                .assertNext(errorResponse -> {
                    assert errorResponse.getStatus() == HttpStatus.BAD_REQUEST.value();
                    assert errorResponse.getErrorCode().equals("STOCK_NOT_ENOUGH");
                })
                .verifyComplete();
    }

    @Test
    void handleCartEmpty_returnsErrorResponse() {
        CartEmptyException ex = new CartEmptyException();
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        var request = mock(org.springframework.http.server.reactive.ServerHttpRequest.class);
        when(exchange.getRequest()).thenReturn(request);
        when(request.getPath()).thenReturn(org.springframework.http.server.RequestPath.parse("/cart/items", "/cart/items"));

        Mono<ErrorResponse> responseMono = globalExceptionHandler.handleCartEmpty(ex, exchange);
        StepVerifier.create(responseMono)
                .assertNext(errorResponse -> {
                    assert errorResponse.getStatus() == HttpStatus.NOT_FOUND.value();
                    assert errorResponse.getErrorCode().equals("CART_EMPTY");
                })
                .verifyComplete();
    }
}

