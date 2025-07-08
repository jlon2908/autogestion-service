package com.arka.autogestion_service.infraestructure.driver.controller;

import lombok.Data;

@Data
public class CartItemDTO {
    private String sku;
    private int quantity;
}

