package com.arka.autogestion_service.infraestructure.driven.webclient.inventory;

import com.arka.autogestion_service.application.ports.output.IInventoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class InventoryAdapter implements IInventoryPort {

    private final WebClient webClient;

    @Value("${inventory.base-url}")
    private String inventoryBaseUrl;

    @Override
    public Mono<Integer> getAvailableQuantity(String sku) {
        return webClient.get()
                .uri(inventoryBaseUrl + "/api/inventory/{sku}/stock", sku)
                .retrieve()
                .bodyToFlux(java.util.Map.class)
                .map(item -> (Integer) item.get("quantity"))
                .reduce(0, Integer::sum);
    }
}
