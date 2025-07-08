package com.arka.autogestion_service.infraestructure.driver.controller;

import com.arka.autogestion_service.application.ports.input.ICatalogoUseCase;
import org.springframework.util.MultiValueMap;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final ICatalogoUseCase catalogoUseCase;

    @GetMapping("/items")
    public Flux<CatalogItemDTO> getCatalogItems(@RequestParam MultiValueMap<String, String> filters) {
        Map<String, Object> convertedFilters = new HashMap<>();
        filters.forEach((key, value) -> convertedFilters.put(key, value.get(0)));

        return catalogoUseCase.getCatalogItems(convertedFilters);
    }
}
