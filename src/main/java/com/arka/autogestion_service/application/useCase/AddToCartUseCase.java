package com.arka.autogestion_service.application.useCase;

import com.arka.autogestion_service.application.ports.input.IAddToCartUseCase;
import com.arka.autogestion_service.application.ports.output.ICartPersistencePort;
import com.arka.autogestion_service.application.ports.output.IInventoryPort;
import com.arka.autogestion_service.domain.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.arka.autogestion_service.infraestructure.driven.dataBase.mapper.CartMapper;
import java.util.UUID;
import com.arka.autogestion_service.domain.exception.StockNotEnough;

@Service
@RequiredArgsConstructor
public class AddToCartUseCase implements IAddToCartUseCase {

    private final IInventoryPort inventoryPort;
    private final ICartPersistencePort cartPersistencePort;

    @Override
    public Mono<Void> addToCart(String userId, CartItem cartItem) {
        UUID userUUID = UUID.fromString(userId);
        return cartPersistencePort.findOrCreateCartByUserId(userUUID)
                .flatMap(cartEntity ->
                        cartPersistencePort.getQuantityInCartByUserIdAndSku(userUUID, cartItem.getSku())
                                .zipWith(inventoryPort.getAvailableQuantity(cartItem.getSku()), (quantityInCart, availableQuantity) -> {
                                    int totalRequested = cartItem.getQuantity() + quantityInCart;
                                    if (availableQuantity >= totalRequested) {
                                        return cartEntity;
                                    } else {
                                        throw new StockNotEnough();
                                    }
                                })
                                .flatMap(cart -> {
                                    CartItem itemWithCartId = cartItem.toBuilder().cartId(cart.getId()).build();
                                    return cartPersistencePort.saveCartItem(CartMapper.toEntity(itemWithCartId));
                                })
                )
                .then();
    }

    @Override
    public Mono<Boolean> validateStock(String sku, int quantity) {
        return inventoryPort.getAvailableQuantity(sku)
                .map(availableQuantity -> availableQuantity >= quantity);
    }

    @Override
    public Flux<CartItem> getCartItems(String userId) {
        UUID userUUID = UUID.fromString(userId);
        return cartPersistencePort.getCartItemsByUserId(userUUID)
                .switchIfEmpty(Flux.error(new com.arka.autogestion_service.domain.exception.CartEmptyException()));
    }
}
