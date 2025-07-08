package com.arka.autogestion_service.infraestructure.driven.webclient.catalog;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CatalogFilterDTO {
    @NotNull(message = "Brand cannot be null")
    private String brand;

    private String category;

    private String feature;

    private String value;

    private String sku;

    @Positive(message = "Minimum price must be positive")
    private Double min;

    @Positive(message = "Maximum price must be positive")
    private Double max;
}
