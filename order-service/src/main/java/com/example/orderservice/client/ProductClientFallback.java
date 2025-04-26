package com.example.orderservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductClientFallback implements ProductClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductClientFallback.class);

    @Override
    public ProductResponse getProductById(String id) {
        logger.warn("Fallback method executed for product with id: {}", id);
        // Trả về sản phẩm mặc định khi không thể kết nối đến product-service
        ProductResponse response = new ProductResponse();
        response.setId(id);
        response.setName("Fallback Product");
        response.setDescription("This is a fallback product when product-service is down");
        response.setPrice(BigDecimal.ZERO);
        return response;
    }
}
