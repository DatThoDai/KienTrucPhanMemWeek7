package com.example.orderservice.client;

import feign.Request;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class ProductClientConfig {
    
    @Bean
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }
    
    @Bean
    public Request.Options options() {
        return new Request.Options(
                5, TimeUnit.SECONDS, // connectTimeout
                5, TimeUnit.SECONDS, // readTimeout 
                true); // followRedirects
    }
    
    @Bean
    public ErrorDecoder errorDecoder() {
        return new ProductServiceErrorDecoder();
    }
    

    public static class ProductServiceErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();
        
        @Override
        public Exception decode(String methodKey, Response response) {
            log.error("Lỗi khi gọi Product Service: {}, status: {}", methodKey, response.status());

            if (response.status() >= 500) {
                return new RetryableException(
                    response.status(),
                    "Lỗi server khi gọi product-service: " + response.reason(),
                    response.request().httpMethod(),
                    new Date(),
                    response.request()
                );
            } else if (response.status() == 404) {
                return new ProductNotFoundException("Không tìm thấy sản phẩm");
            }
            

            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
    

    public static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String message) {
            super(message);
        }
    }
}
