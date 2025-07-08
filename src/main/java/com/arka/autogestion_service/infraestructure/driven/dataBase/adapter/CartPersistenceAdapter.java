package com.arka.autogestion_service.infraestructure.driven.dataBase.adapter;

import com.arka.autogestion_service.application.ports.output.ICartPersistencePort;
import com.arka.autogestion_service.domain.model.CartItem;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.entity.CartItemEntity;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartRepository;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartPersistenceAdapter implements ICartPersistencePort {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Mono<CartEntity> findOrCreateCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .switchIfEmpty(
                        cartRepository.insertCart(userId, LocalDateTime.now(), LocalDateTime.now())
                );
    }

    @Override
    public Mono<CartItemEntity> saveCartItem(CartItemEntity cartItemEntity) {
        return cartItemRepository.insertCartItem(
                cartItemEntity.getCartId(),
                cartItemEntity.getSku(),
                cartItemEntity.getQuantity(),
                cartItemEntity.getAddedAt()
        );
    }

    @Override
    public Mono<Integer> getQuantityInCartByUserIdAndSku(UUID userId, String sku) {
        return cartItemRepository.getQuantityInCartByUserIdAndSku(userId, sku);
    }

    @Override
    public Flux<CartItem> getCartItemsByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .flatMapMany(cart -> cartItemRepository.findAll()
                        .filter(item -> item.getCartId().equals(cart.getId()))
                        .map(com.arka.autogestion_service.infraestructure.driven.dataBase.mapper.CartMapper::toDomain)
                );
    }
}
