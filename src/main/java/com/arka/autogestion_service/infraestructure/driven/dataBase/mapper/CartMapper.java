package com.arka.autogestion_service.infraestructure.driven.dataBase.mapper;

import com.arka.autogestion_service.domain.model.Cart;
import com.arka.autogestion_service.domain.model.CartItem;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class CartMapper {
    public static CartEntity toEntity(Cart cart) {
        return CartEntity.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public static Cart toDomain(CartEntity entity) {
        return Cart.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .build();
    }

    public static CartItemEntity toEntity(CartItem item) {
        return CartItemEntity.builder()
                .id(item.getId() != null ? item.getId() : UUID.randomUUID())
                .cartId(item.getCartId())
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .addedAt(LocalDateTime.now())
                .build();
    }

    public static CartItem toDomain(CartItemEntity entity) {
        return CartItem.builder()
                .id(entity.getId())
                .cartId(entity.getCartId())
                .sku(entity.getSku())
                .quantity(entity.getQuantity())
                .build();
    }
}

