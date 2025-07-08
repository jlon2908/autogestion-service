package com.arka.autogestion_service.application.ports.output;

import reactor.core.publisher.Mono;

public interface ICartClearEventListenerPort {
    Mono<Void> handleCartClearEvent(String userId);
}

