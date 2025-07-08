package com.arka.autogestion_service.infraestructure.driver.rabbit;

import lombok.Data;

@Data
public class CartClearEvent {
    private String userId;
}

