package com.mentoring.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingGlobalPreFilter implements GlobalFilter, Ordered {
    //create a custom global filter is to implement the Spring Cloud Gateway GlobalFilter
// interface, and add it to the context as a bean final
    Logger logger =
            LoggerFactory.getLogger(LoggingGlobalPreFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange,
                             GatewayFilterChain chain) {
        logger.info("First Pre Global Filter");
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    logger.info("Last Post Global Filter");
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}