package com.arka.autogestion_service.use_case;

import com.arka.autogestion_service.application.ports.output.ICartClearPort;
import com.arka.autogestion_service.application.useCase.ClearCartUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ClearCartUseCaseTest {
    @Mock
    private ICartClearPort cartClearPort;
    @InjectMocks
    private ClearCartUseCase clearCartUseCase;

    private final String userId = UUID.randomUUID().toString();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clearCartUseCase = new ClearCartUseCase(cartClearPort);
    }

    @Test
    void clearCart_success() {
        when(cartClearPort.clearCartByUserId(any())).thenReturn(Mono.empty());
        StepVerifier.create(clearCartUseCase.clearCart(userId))
                .verifyComplete();
    }
}

