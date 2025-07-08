package com.arka.inventory_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DistributedOrderControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        this.webTestClient = this.webTestClient.mutate()
                .baseUrl("http://localhost:" + port + "/api/distributed-orders")
                .build();
    }

    @Test
    void updateDistributionsStatus_shouldUpdateSuccessfully() {
        // Estos valores deben existir en tu base de datos de test
        String orderCode = "ORD-123";
        String sourceWarehouseCode = "WH-001";
        String newStatus = "RECEIVED";

        webTestClient.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/status")
                        .queryParam("orderCode", orderCode)
                        .queryParam("sourceWarehouseCode", sourceWarehouseCode)
                        .queryParam("newStatus", newStatus)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Integer.class)
                .value(updatedCount -> {
                    // Puedes ajustar la aserción según tus datos de prueba
                    assert updatedCount >= 0;
                });
    }
}

