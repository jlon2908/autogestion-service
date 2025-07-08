package com.arka.autogestion_service.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    private UUID id;
    private UUID userId;
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem item) {
        this.items.add(item);
    }
}
