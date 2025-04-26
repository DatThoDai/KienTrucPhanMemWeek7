package com.example.orderservice.controller;

import com.example.orderservice.client.ProductResponse;
import com.example.orderservice.service.impl.ProductServiceClient;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/circuit-breaker")
@RequiredArgsConstructor
@Slf4j
public class CircuitBreakerController {

    private final ProductServiceClient productServiceClient;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    @GetMapping("/status")
    public String getCircuitBreakerStatus() {
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("productService");
        return "Circuit Breaker State: " + circuitBreaker.getState();
    }

    @GetMapping("/test/{productId}")
    public String testCircuitBreaker(@PathVariable String productId) {
        try {
            ProductResponse product = productServiceClient.getProduct(productId);
            return "Product found: " + product.getName() + ", Circuit Breaker State: " + 
                    circuitBreakerRegistry.circuitBreaker("productService").getState();
        } catch (Exception e) {
            log.error("Error occurred: ", e);
            return "Error: " + e.getMessage() + ", Circuit Breaker State: " + 
                    circuitBreakerRegistry.circuitBreaker("productService").getState();
        }
    }
}
