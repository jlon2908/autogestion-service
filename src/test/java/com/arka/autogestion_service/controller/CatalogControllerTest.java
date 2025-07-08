package com.arka.autogestion_service.controller;

import com.arka.autogestion_service.application.ports.input.ICatalogoUseCase;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogController;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CatalogControllerTest {
    @Mock
    private ICatalogoUseCase catalogoUseCase;
    @InjectMocks
    private CatalogController catalogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogController = new CatalogController(catalogoUseCase);
    }

    @Test
    void getCatalogItems_success() {
        MultiValueMap<String, String> filters = new LinkedMultiValueMap<>();
        filters.add("brand", "Nike");
        filters.add("category", "Shoes");
        CatalogItemDTO item = new CatalogItemDTO();
        item.setSku("SKU1");
        item.setName("Zapato 1");
        Map<String, Object> expectedFilters = new HashMap<>();
        expectedFilters.put("brand", "Nike");
        expectedFilters.put("category", "Shoes");
        when(catalogoUseCase.getCatalogItems(any())).thenReturn(Flux.just(item));

        StepVerifier.create(catalogController.getCatalogItems(filters))
                .expectNext(item)
                .verifyComplete();
    }
}

