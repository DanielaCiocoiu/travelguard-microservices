package com.mentoring.TravelInsurance.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.mentoring.TravelInsurance.service.person", "com.mentoring.TravelInsurance.aspects"})
@EnableAspectJAutoProxy
public class ProjectConfig {
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
