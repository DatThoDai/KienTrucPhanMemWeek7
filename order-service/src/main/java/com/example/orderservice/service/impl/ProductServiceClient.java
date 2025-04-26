package com.example.orderservice.service.impl;

import com.example.orderservice.client.ProductClient;
import com.example.orderservice.client.ProductResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

@Service
public class ProductServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);
    
    private final ProductClient productClient;
    
    public ProductServiceClient(ProductClient productClient) {
        this.productClient = productClient;
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductFallback")
    @RateLimiter(name = "productService")
    @Retry(name = "productService")
    public ProductResponse getProduct(String productId) {
        logger.info("Calling product-service to get product with ID: {}", productId);
        return productClient.getProductById(productId);
    }

    @CircuitBreaker(name = "productService", fallbackMethod = "getProductAsyncFallback")
    @TimeLimiter(name = "productService")
    @Retry(name = "productService")
    public CompletableFuture<ProductResponse> getProductAsync(String productId) {
        logger.info("Async call to product-service to get product with ID: {}", productId);
        return CompletableFuture.supplyAsync(() -> productClient.getProductById(productId));
    }

    private ProductResponse getProductFallback(String productId, Exception e) {
        logger.warn("Fallback executed for product ID: {}, exception: {}", productId, e.getMessage());
        return createFallbackProduct(productId);
    }

    private CompletableFuture<ProductResponse> getProductAsyncFallback(String productId, Exception e) {
        logger.warn("Async fallback executed for product ID: {}, exception: {}", productId, e.getMessage());
        return CompletableFuture.supplyAsync(() -> createFallbackProduct(productId));
    }

    private ProductResponse createFallbackProduct(String productId) {
        ProductResponse response = new ProductResponse();
        response.setId(productId);
        response.setName("Fallback Product");
        response.setDescription("This is a fallback product when circuit breaker is open");
        response.setPrice(BigDecimal.ZERO);
        return response;
    }
}
