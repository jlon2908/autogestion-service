package com.arka.autogestion_service.application.ports.input;

import reactor.core.publisher.Flux;
import java.util.Map;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;

public interface ICatalogoUseCase {
    Flux<CatalogItemDTO> getCatalogItems(Map<String, Object> filters);
}
