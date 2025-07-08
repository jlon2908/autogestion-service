package com.arka.autogestion_service.infraestructure.driver.controller;

import com.arka.autogestion_service.application.ports.input.IAddToCartUseCase;
import com.arka.autogestion_service.domain.model.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final IAddToCartUseCase addToCartUseCase;

    @GetMapping("/validate-stock")
    public Mono<Boolean> validateStock(@RequestParam String sku, @RequestParam int quantity) {
        return addToCartUseCase.validateStock(sku, quantity);
    }

    @PostMapping("/add")
    public Mono<Void> addToCart(@RequestParam String sku,
                                @RequestParam int quantity,
                                @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        CartItem cartItem = CartItem.builder()
                .sku(sku)
                .quantity(quantity)
                .build();
        return addToCartUseCase.addToCart(userId, cartItem);
    }

    @GetMapping("/items")
    public Flux<CartItemDTO> getCartItems(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getClaimAsString("userId");
        return addToCartUseCase.getCartItems(userId)
                .collectList()
                .flatMapMany(list -> {
                    Map<String, Integer> skuToQuantity = list.stream()
                        .collect(Collectors.groupingBy(
                            CartItem::getSku,
                            Collectors.summingInt(CartItem::getQuantity)
                        ));
                    return Flux.fromIterable(skuToQuantity.entrySet())
                        .map(entry -> {
                            CartItemDTO dto = new CartItemDTO();
                            dto.setSku(entry.getKey());
                            dto.setQuantity(entry.getValue());
                            return dto;
                        });
                });
    }
}
