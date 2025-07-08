package com.arka.autogestion_service.use_case;

import com.arka.autogestion_service.application.ports.output.ICartPersistencePort;
import com.arka.autogestion_service.application.ports.output.IInventoryPort;
import com.arka.autogestion_service.application.useCase.AddToCartUseCase;
import com.arka.autogestion_service.domain.exception.StockNotEnough;
import com.arka.autogestion_service.domain.model.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class AddToCartUseCaseTest {
    @Mock
    private IInventoryPort inventoryPort;
    @Mock
    private ICartPersistencePort cartPersistencePort;
    @InjectMocks
    private AddToCartUseCase addToCartUseCase;

    private final String userId = UUID.randomUUID().toString();
    private final String sku = "SKU123";
    private final int quantity = 2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        addToCartUseCase = new AddToCartUseCase(inventoryPort, cartPersistencePort);
    }

    @Test
    void addToCart_success() {
        var cartEntity = com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity.builder()
                .id(UUID.randomUUID())
                .userId(UUID.fromString(userId))
                .build();
        var cartItem = CartItem.builder().sku(sku).quantity(quantity).build();

        when(cartPersistencePort.findOrCreateCartByUserId(any())).thenReturn(Mono.just(cartEntity));
        when(cartPersistencePort.getQuantityInCartByUserIdAndSku(any(), eq(sku))).thenReturn(Mono.just(0));
        when(inventoryPort.getAvailableQuantity(eq(sku))).thenReturn(Mono.just(10));
        when(cartPersistencePort.saveCartItem(any())).thenReturn(Mono.just(
                com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity.builder().build()
        ));

        StepVerifier.create(addToCartUseCase.addToCart(userId, cartItem))
                .verifyComplete();
    }

    @Test
    void addToCart_stockNotEnough() {
        var cartEntity = com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity.builder()
                .id(UUID.randomUUID())
                .userId(UUID.fromString(userId))
                .build();
        var cartItem = CartItem.builder().sku(sku).quantity(20).build();

        when(cartPersistencePort.findOrCreateCartByUserId(any())).thenReturn(Mono.just(cartEntity));
        when(cartPersistencePort.getQuantityInCartByUserIdAndSku(any(), eq(sku))).thenReturn(Mono.just(0));
        when(inventoryPort.getAvailableQuantity(eq(sku))).thenReturn(Mono.just(5));

        StepVerifier.create(addToCartUseCase.addToCart(userId, cartItem))
                .expectError(StockNotEnough.class)
                .verify();
    }

    @Test
    void validateStock_true() {
        when(inventoryPort.getAvailableQuantity(eq(sku))).thenReturn(Mono.just(10));
        StepVerifier.create(addToCartUseCase.validateStock(sku, 5))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void validateStock_false() {
        when(inventoryPort.getAvailableQuantity(eq(sku))).thenReturn(Mono.just(2));
        StepVerifier.create(addToCartUseCase.validateStock(sku, 5))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    void getCartItems_success() {
        var cartItem = CartItem.builder().sku(sku).quantity(quantity).build();
        when(cartPersistencePort.getCartItemsByUserId(any())).thenReturn(Flux.just(cartItem));
        StepVerifier.create(addToCartUseCase.getCartItems(userId))
                .expectNext(cartItem)
                .verifyComplete();
    }
}

