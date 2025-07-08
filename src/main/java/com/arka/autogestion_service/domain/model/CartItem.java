package com.arka.autogestion_service.domain.model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CartItem {
    private UUID id;
    private String sku;
    private int quantity;
    private UUID cartId;
}
