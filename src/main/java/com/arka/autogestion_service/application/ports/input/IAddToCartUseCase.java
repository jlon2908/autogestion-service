package com.arka.autogestion_service.application.ports.input;

import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import com.arka.autogestion_service.domain.model.CartItem;

public interface IAddToCartUseCase {
    Mono<Void> addToCart(String userId, CartItem cartItem);
    Mono<Boolean> validateStock(String sku, int quantity);
    Flux<CartItem> getCartItems(String userId);
}
