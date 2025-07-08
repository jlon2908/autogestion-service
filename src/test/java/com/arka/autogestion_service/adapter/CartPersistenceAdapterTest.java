package com.arka.autogestion_service.adapter;

import com.arka.autogestion_service.domain.model.CartItem;
import com.arka.autogestion_service.infraestructure.driven.dataBase.adapter.CartPersistenceAdapter;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartItemRepository;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CartPersistenceAdapterTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartPersistenceAdapter cartPersistenceAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartPersistenceAdapter = new CartPersistenceAdapter(cartRepository, cartItemRepository);
    }

    @Test
    void findOrCreateCartByUserId_found() {
        UUID userId = UUID.randomUUID();
        CartEntity cartEntity = CartEntity.builder().id(UUID.randomUUID()).userId(userId).build();
        when(cartRepository.findByUserId(eq(userId))).thenReturn(Mono.just(cartEntity));
        // El mock de insertCart debe devolver un Mono nunca null
        when(cartRepository.insertCart(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Mono.just(cartEntity));
        StepVerifier.create(cartPersistenceAdapter.findOrCreateCartByUserId(userId))
                .expectNext(cartEntity)
                .verifyComplete();
    }

    @Test
    void findOrCreateCartByUserId_create() {
        UUID userId = UUID.randomUUID();
        CartEntity cartEntity = CartEntity.builder().id(UUID.randomUUID()).userId(userId).build();
        when(cartRepository.findByUserId(eq(userId))).thenReturn(Mono.empty());
        when(cartRepository.insertCart(eq(userId), any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Mono.just(cartEntity));
        StepVerifier.create(cartPersistenceAdapter.findOrCreateCartByUserId(userId))
                .expectNext(cartEntity)
                .verifyComplete();
    }

    @Test
    void saveCartItem_success() {
        CartItemEntity entity = CartItemEntity.builder().id(UUID.randomUUID()).cartId(UUID.randomUUID()).sku("SKU1").quantity(2).addedAt(LocalDateTime.now()).build();
        // Usar parámetros concretos en vez de any() para evitar nulls
        when(cartItemRepository.insertCartItem(eq(entity.getCartId()), eq(entity.getSku()), eq(entity.getQuantity()), eq(entity.getAddedAt()))).thenReturn(Mono.just(entity));
        StepVerifier.create(cartPersistenceAdapter.saveCartItem(entity))
                .expectNext(entity)
                .verifyComplete();
    }

    @Test
    void getQuantityInCartByUserIdAndSku_success() {
        UUID userId = UUID.randomUUID();
        when(cartItemRepository.getQuantityInCartByUserIdAndSku(eq(userId), eq("SKU1"))).thenReturn(Mono.just(5));
        StepVerifier.create(cartPersistenceAdapter.getQuantityInCartByUserIdAndSku(userId, "SKU1"))
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    void getCartItemsByUserId_success() {
        UUID userId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        CartEntity cartEntity = CartEntity.builder().id(cartId).userId(userId).build();
        CartItemEntity itemEntity = CartItemEntity.builder().id(UUID.randomUUID()).cartId(cartId).sku("SKU1").quantity(2).addedAt(LocalDateTime.now()).build();
        when(cartRepository.findByUserId(eq(userId))).thenReturn(Mono.just(cartEntity));
        when(cartItemRepository.findAll()).thenReturn(Flux.just(itemEntity));
        StepVerifier.create(cartPersistenceAdapter.getCartItemsByUserId(userId))
                .expectNextMatches(item -> item.getSku().equals("SKU1") && item.getQuantity() == 2)
                .verifyComplete();
    }
}
