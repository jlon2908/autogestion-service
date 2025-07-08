package com.arka.autogestion_service.application.ports.output;

import com.arka.autogestion_service.domain.model.CartItem;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ICartPersistencePort {
    Mono<CartEntity> findOrCreateCartByUserId(UUID userId);
    Mono<CartItemEntity> saveCartItem(CartItemEntity cartItemEntity);
    Mono<Integer> getQuantityInCartByUserIdAndSku(UUID userId, String sku);
    Flux<CartItem> getCartItemsByUserId(UUID userId);
}
