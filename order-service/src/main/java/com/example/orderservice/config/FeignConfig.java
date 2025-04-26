package com.example.orderservice.config;

import com.example.orderservice.client.ProductClientFallback;
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    // Cấu hình Feign Client để kích hoạt fallback
    
    @Bean
    public ProductClientFallback productClientFallback() {
        return new ProductClientFallback();
    }
    
    @Bean
    public Logger.Level feignLoggerLevel() {
        // Để log chi tiết các request/response của Feign client
        return Logger.Level.FULL;
    }
}
