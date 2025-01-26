package com.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
public class LoggingFilter implements GlobalFilter {
    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("************************************");
        log.info(String.format("Request time is ----------> " + new Date()));
        log.info("Path of request is Received ---->" + exchange.getRequest().getPath());
        log.info("Request ---->" + exchange.getRequest().getBody().toString());
        log.info("IP request of Requestor ---->" + exchange.getRequest().getLocalAddress().getAddress());
        log.info("Port of Requestor ---->" + exchange.getRequest().getLocalAddress().getPort());
        log.info("************************************");
        return chain.filter(exchange);
    }
}
