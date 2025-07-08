package com.arka.autogestion_service.infraestructure.driven.dataBase.repository;

import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.r2dbc.repository.Query;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CartRepository extends ReactiveCrudRepository<CartEntity, UUID> {
    Mono<CartEntity> findByUserId(UUID userId);

    @Query("INSERT INTO cart (user_id, created_at, updated_at) VALUES (:userId, :createdAt, :updatedAt) RETURNING *")
    Mono<CartEntity> insertCart(UUID userId, java.time.LocalDateTime createdAt, java.time.LocalDateTime updatedAt);

    @Query("UPDATE cart SET updated_at = :updatedAt WHERE id = :id RETURNING *")
    Mono<CartEntity> updateCart(UUID id, java.time.LocalDateTime updatedAt);
}
