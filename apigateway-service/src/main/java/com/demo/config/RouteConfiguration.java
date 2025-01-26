package com.demo.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder){
        return builder.routes()
                .route("inventory-service", r -> r.path("/inventory-service/**")
                        .filters(f -> f.rewritePath("/inventory-service/(?<subUrl>.*)","/api/${subUrl}"))
                        .uri("http://localhost:8081/"))
                .route("order-service", r -> r.path("/order-service/**")
                        .filters(f-> f.rewritePath("/order-service/(?<subUrl>.*)", "/api/${subUrl}"))
                        .uri("http://localhost:8082/"))
                .build();

    }
}
