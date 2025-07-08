package com.arka.autogestion_service.infraestructure.driver.rabbit;

import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put(
            "com.arka.purchase_service.infraestructure.driven.cart.dto.CartClearEvent",
            com.arka.autogestion_service.infraestructure.driver.rabbit.CartClearEvent.class
        );
        typeMapper.setIdClassMapping(idClassMapping);
        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }
}

