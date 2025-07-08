package com.arka.autogestion_service.application.ports.input;

import reactor.core.publisher.Mono;

public interface IClearCartUseCase {
    Mono<Void> clearCart(String userId);
}

