package com.arka.autogestion_service.application.ports.output;

import reactor.core.publisher.Mono;

public interface IInventoryPort {
    Mono<Integer> getAvailableQuantity(String sku);
}
