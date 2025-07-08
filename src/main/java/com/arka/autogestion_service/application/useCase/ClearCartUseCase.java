package com.arka.autogestion_service.application.useCase;

import com.arka.autogestion_service.application.ports.input.IClearCartUseCase;
import com.arka.autogestion_service.application.ports.output.ICartClearPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClearCartUseCase implements IClearCartUseCase {
    private final ICartClearPort cartClearPort;

    @Override
    public Mono<Void> clearCart(String userId) {
        UUID userUUID = UUID.fromString(userId);
        return cartClearPort.clearCartByUserId(userUUID);
    }
}

