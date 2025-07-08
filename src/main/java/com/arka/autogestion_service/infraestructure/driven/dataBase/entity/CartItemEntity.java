package com.arka.autogestion_service.infraestructure.driven.dataBase.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table("cart_item")
public class CartItemEntity {
    @Id
    private UUID id;

    @Column("cart_id")
    private UUID cartId;

    private String sku;

    private int quantity;

    @Column("added_at")
    private LocalDateTime addedAt;
}

