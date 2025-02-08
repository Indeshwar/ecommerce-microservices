package com.demo.config;

import com.demo.filter.AuthenticationPrefilter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfiguration {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, AuthenticationPrefilter auth){
        return builder.routes()
                .route("authorization-service", r -> r.path("/authorization-service/**")
                        .filters(f -> f.rewritePath("/authorization-service/(?<subUrl>.*)","/api/v1/validConnection/${subUrl}").filters(auth.apply(new AuthenticationPrefilter.Config())))
                        .uri("http://localhost:8083/"))
                .route("inventory-service", r -> r.path("/inventory-service/**")
                        .filters(f -> f.rewritePath("/inventory-service/(?<subUrl>.*)","/api/${subUrl}").filters(auth.apply(new AuthenticationPrefilter.Config())))
                        .uri("http://localhost:8081/"))
                .route("order-service", r -> r.path("/order-service/**")
                        .filters(f-> f.rewritePath("/order-service/(?<subUrl>.*)", "/api/${subUrl}").filters(auth.apply(new AuthenticationPrefilter.Config())))
                        .uri("http://localhost:8082/"))
                .build();

    }
}
