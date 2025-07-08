package com.arka.autogestion_service.infraestructure.driven.webclient.catalog;

import com.arka.autogestion_service.application.ports.output.ICatalogoPort;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CatalogAdapter implements ICatalogoPort {

    private final WebClient webClient;

    @Autowired
    public CatalogAdapter(@Value("${catalog.base-url}") String catalogBaseUrl, WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(catalogBaseUrl).build();
    }

    @Override
    public Flux<CatalogItemDTO> getCatalogItems(Map<String, Object> filters) {
        return webClient.get()
                .uri(uriBuilder -> {
                    uriBuilder.path("/api/products/filter");
                    filters.forEach((key, value) -> uriBuilder.queryParam(key, value));
                    return uriBuilder.build();
                })
                .retrieve()
                .bodyToFlux(Map.class)
                .map(item -> {
                    CatalogItemDTO dto = new CatalogItemDTO();
                    dto.setSku((String) item.get("sku"));
                    dto.setName((String) item.get("name"));
                    dto.setDescription((String) item.get("description"));
                    dto.setUnitPrice((Double) item.get("unitPrice"));
                    dto.setBrandName((String) ((Map<?, ?>) item.get("brand")).get("nombre"));
                    dto.setCategories(((List<Map<?, ?>>) item.get("categories")).stream()
                            .map(category -> (String) category.get("name"))
                            .collect(Collectors.toList()));
                    dto.setFeatures(((List<Map<?, ?>>) item.get("features")).stream()
                            .map(feature -> (String) feature.get("value"))
                            .collect(Collectors.toList()));
                    return dto;
                });
    }
}
