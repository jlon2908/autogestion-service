package com.arka.autogestion_service.application.ports.output;


import java.util.Map;

import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;

import reactor.core.publisher.Flux;

public interface ICatalogoPort {
    Flux<CatalogItemDTO> getCatalogItems(Map<String, Object> filters);
}
