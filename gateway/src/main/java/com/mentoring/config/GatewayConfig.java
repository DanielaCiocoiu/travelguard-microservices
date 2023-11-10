package com.mentoring.config;

import com.mentoring.filter.AuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableHystrix
@RequiredArgsConstructor
public class GatewayConfig {

    private final AuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()


                .route("auth", r -> r.path("/auth/**", "/auth/test/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://AUTH"))

                .route("policies", r -> r.path("/api/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://POLICIES"))

                .route("rest", r -> r.path("/restTemplate/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://REST"))

                .route("email", r -> r.path("/apiEmail/**")
                        .filters(f -> f.filter(filter))
                        .uri("lb://EMAIL"))
                .build();
    }

}
