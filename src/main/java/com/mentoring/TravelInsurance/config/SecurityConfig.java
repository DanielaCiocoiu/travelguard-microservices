package com.mentoring.TravelInsurance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        return encoder;
    }
    @Bean
    public UserDetailsManager userDetailsManager() {
        final UserDetailsManager userDetailsManager = new InMemoryUserDetailsManager();
        final UserDetails user1 = User.withUsername("Laura")
                .password(passwordEncoder().encode("Laura"))
                .roles("ANALYST")
                .authorities("READ_AUTHORITY")
                .build();
        final UserDetails user2 = User.withUsername("Maia")
                .password(passwordEncoder().encode("Maia"))
                .roles("ADMIN")
                .authorities("READ_AUTHORITY", "WRITE_AUTHORITY", "UPDATE_AUTHORITY")
                .build();

        final UserDetails user3 = User.withUsername("Dana")
                .password(passwordEncoder().encode("Dana"))
                .roles("ADMIN")
                .authorities("READ_AUTHORITY", "WRITE_AUTHORITY", "UPDATE_AUTHORITY", "DELETE_AUTHORITY")
                .build();
        userDetailsManager.createUser(user1);
        userDetailsManager.createUser(user2);
        userDetailsManager.createUser(user3);
        return userDetailsManager;
    }

    //customized cors
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers(
                        headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        (requests) -> requests
                        .anyRequest()
                        .authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }


    // Defines options for customizing or adding to the default Spring MVC configuration
    @Bean
    WebMvcConfigurer webMvcConfigurer(){
        return  new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/RestTemplate/getAllPolicies")
                        .allowedOrigins("http://localhost:8080")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("Content-Type", "X-CSRF-TOKEN", "X-API-Auth")
                        .allowedHeaders("X-CSRF-TOKEN")
                        .allowCredentials(true)
                        .maxAge(0);
            }
        };
    }

}