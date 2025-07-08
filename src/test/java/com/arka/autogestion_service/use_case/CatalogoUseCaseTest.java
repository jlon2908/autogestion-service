package com.arka.autogestion_service.use_case;

import com.arka.autogestion_service.application.ports.output.ICatalogoPort;
import com.arka.autogestion_service.application.useCase.CatalogoUseCase;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CatalogoUseCaseTest {
    @Mock
    private ICatalogoPort catalogoPort;
    @InjectMocks
    private CatalogoUseCase catalogoUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catalogoUseCase = new CatalogoUseCase(catalogoPort);
    }

    @Test
    void getCatalogItems_success() {
        Map<String, Object> filters = new HashMap<>();
        CatalogItemDTO item = new CatalogItemDTO();
        item.setSku("SKU1");
        item.setName("Producto 1");
        when(catalogoPort.getCatalogItems(any())).thenReturn(Flux.just(item));

        StepVerifier.create(catalogoUseCase.getCatalogItems(filters))
                .expectNext(item)
                .verifyComplete();
    }
}

