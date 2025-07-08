package com.arka.autogestion_service.infraestructure.driven.webclient.catalog;


import java.util.HashMap;
import java.util.Map;

public class CatalogFilterMapper {

    public static Map<String, Object> toMap(CatalogFilterDTO dto) {
        Map<String, Object> filters = new HashMap<>();

        if (dto.getBrand() != null) filters.put("brand", dto.getBrand());
        if (dto.getCategory() != null) filters.put("category", dto.getCategory());
        if (dto.getFeature() != null) filters.put("feature", dto.getFeature());
        if (dto.getValue() != null) filters.put("value", dto.getValue());
        if (dto.getSku() != null) filters.put("sku", dto.getSku());
        if (dto.getMin() != null) filters.put("min", dto.getMin());
        if (dto.getMax() != null) filters.put("max", dto.getMax());

        return filters;
    }
}
