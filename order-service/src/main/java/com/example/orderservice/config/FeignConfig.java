package com.example.orderservice.config;

import com.example.orderservice.client.ProductClientFallback;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    
    @Bean
    public ProductClientFallback productClientFallback() {
        return new ProductClientFallback();
    }
    
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
