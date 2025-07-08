package com.arka.autogestion_service.controller;

import com.arka.autogestion_service.application.ports.input.IAddToCartUseCase;
import com.arka.autogestion_service.domain.model.CartItem;
import com.arka.autogestion_service.infraestructure.driver.controller.CartController;
import com.arka.autogestion_service.infraestructure.driver.controller.CartItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CartControllerTest {
    @Mock
    private IAddToCartUseCase addToCartUseCase;
    @Mock
    private Jwt jwt;
    @InjectMocks
    private CartController cartController;

    private final String userId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartController = new CartController(addToCartUseCase);
        when(jwt.getClaimAsString(eq("userId"))).thenReturn(userId);
    }

    @Test
    void validateStock_success() {
        when(addToCartUseCase.validateStock(eq("SKU1"), eq(2))).thenReturn(Mono.just(true));
        StepVerifier.create(cartController.validateStock("SKU1", 2))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void addToCart_success() {
        when(addToCartUseCase.addToCart(eq(userId), any(CartItem.class))).thenReturn(Mono.empty());
        StepVerifier.create(cartController.addToCart("SKU1", 2, jwt))
                .verifyComplete();
    }

    @Test
    void getCartItems_success() {
        CartItem item1 = CartItem.builder().sku("SKU1").quantity(2).build();
        CartItem item2 = CartItem.builder().sku("SKU1").quantity(3).build();
        CartItem item3 = CartItem.builder().sku("SKU2").quantity(1).build();
        when(addToCartUseCase.getCartItems(eq(userId))).thenReturn(Flux.fromIterable(List.of(item1, item2, item3)));

        StepVerifier.create(cartController.getCartItems(jwt))
                .expectNextMatches(dto -> dto.getSku().equals("SKU1") && dto.getQuantity() == 5)
                .expectNextMatches(dto -> dto.getSku().equals("SKU2") && dto.getQuantity() == 1)
                .verifyComplete();
    }
}

