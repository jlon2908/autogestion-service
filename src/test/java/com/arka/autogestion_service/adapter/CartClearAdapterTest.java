package com.arka.autogestion_service.adapter;

import com.arka.autogestion_service.infraestructure.driven.dataBase.adapter.CartClearAdapter;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartRepository;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartItemRepository;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CartClearAdapterTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @InjectMocks
    private CartClearAdapter cartClearAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cartClearAdapter = new CartClearAdapter(cartRepository, cartItemRepository);
    }

    @Test
    void clearCartByUserId_success() {
        UUID userId = UUID.randomUUID();
        CartEntity cartEntity = CartEntity.builder().id(UUID.randomUUID()).userId(userId).build();
        when(cartRepository.findByUserId(any())).thenReturn(Mono.just(cartEntity));
        when(cartItemRepository.deleteByCartId(any())).thenReturn(Mono.empty());

        StepVerifier.create(cartClearAdapter.clearCartByUserId(userId))
                .verifyComplete();
    }
}

