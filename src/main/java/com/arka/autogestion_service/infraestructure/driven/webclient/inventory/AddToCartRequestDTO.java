package com.arka.autogestion_service.infraestructure.driven.webclient.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddToCartRequestDTO {

    @NotBlank(message = "SKU cannot be blank")
    private String sku;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
}
