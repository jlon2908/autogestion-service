package com.arka.autogestion_service.infraestructure.driver.rabbit;

import com.arka.autogestion_service.application.ports.input.IClearCartUseCase;
import com.arka.autogestion_service.application.ports.output.ICartClearEventListenerPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartClearEventListener implements ICartClearEventListenerPort {
    private final IClearCartUseCase clearCartUseCase;

    @RabbitListener(queues = "cart.clear.queue")
    public void onMessage(CartClearEvent event) {
        handleCartClearEvent(event.getUserId()).subscribe();
    }

    @Override
    public Mono<Void> handleCartClearEvent(String userId) {
        log.info("Recibido evento para limpiar carrito de usuario: {}", userId);
        return clearCartUseCase.clearCart(userId)
                .doOnSuccess(v -> log.info("Carrito limpiado para usuario: {}", userId))
                .doOnError(e -> log.error("Error al limpiar carrito para usuario: {}", userId, e));
    }
}
