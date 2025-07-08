package com.arka.autogestion_service.infraestructure.driven.dataBase.repository;

import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CartItemRepository extends ReactiveCrudRepository<CartItemEntity, UUID> {
    @Query("INSERT INTO cart_item (cart_id, sku, quantity, added_at) VALUES (:cartId, :sku, :quantity, :addedAt) RETURNING *")
    Mono<CartItemEntity> insertCartItem(UUID cartId, String sku, int quantity, LocalDateTime addedAt);

    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_item ci JOIN cart c ON ci.cart_id = c.id WHERE c.user_id = :userId AND ci.sku = :sku")
    Mono<Integer> getQuantityInCartByUserIdAndSku(UUID userId, String sku);

    @Query("DELETE FROM cart_item WHERE cart_id = :cartId")
    Mono<Void> deleteByCartId(UUID cartId);
}
