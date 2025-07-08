package com.arka.autogestion_service.application.ports.output;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface ICartClearPort {
    Mono<Void> clearCartByUserId(UUID userId);
}

