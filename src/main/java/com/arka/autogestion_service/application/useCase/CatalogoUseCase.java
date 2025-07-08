package com.arka.autogestion_service.application.useCase;

import com.arka.autogestion_service.application.ports.input.ICatalogoUseCase;
import com.arka.autogestion_service.application.ports.output.ICatalogoPort;
import com.arka.autogestion_service.infraestructure.driven.webclient.catalog.CatalogFilterMapper;
import com.arka.autogestion_service.infraestructure.driven.webclient.catalog.CatalogFilterDTO;
import com.arka.autogestion_service.infraestructure.driver.controller.CatalogItemDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CatalogoUseCase implements ICatalogoUseCase {

    private final ICatalogoPort catalogoPort;

    @Override
    public Flux<CatalogItemDTO> getCatalogItems(Map<String, Object> filters) {
        return catalogoPort.getCatalogItems(filters);
    }

    public Flux<CatalogItemDTO> getCatalogItems(CatalogFilterDTO filters) {
        Map<String, Object> filterMap = CatalogFilterMapper.toMap(filters);
        return catalogoPort.getCatalogItems(filterMap);
    }
}
