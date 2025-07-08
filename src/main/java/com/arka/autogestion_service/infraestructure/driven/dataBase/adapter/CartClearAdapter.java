package com.arka.autogestion_service.infraestructure.driven.dataBase.adapter;

import com.arka.autogestion_service.application.ports.output.ICartClearPort;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartRepository;
import com.arka.autogestion_service.infraestructure.driven.dataBase.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CartClearAdapter implements ICartClearPort {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public Mono<Void> clearCartByUserId(UUID userId) {
        return cartRepository.findByUserId(userId)
                .flatMap(cart -> cartItemRepository.deleteByCartId(cart.getId()))
                .then();
    }
}

