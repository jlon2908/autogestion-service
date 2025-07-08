package com.arka.autogestion_service.infraestructure.driver.controller;

import java.util.List;

import lombok.Data;

@Data
public class CatalogItemDTO {
    private String sku;
    private String name;
    private String description;
    private Double unitPrice;
    private String brandName;
    private List<String> categories;
    private List<String> features;
}
