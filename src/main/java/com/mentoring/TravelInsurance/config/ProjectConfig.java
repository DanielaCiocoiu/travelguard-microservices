package com.mentoring.TravelInsurance.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(basePackages = {"com.mentoring.TravelInsurance.service.policy", "com.mentoring.TravelInsurance.aspects"})
@EnableAspectJAutoProxy
public class ProjectConfig {

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    //customize the auto-configured default ObjectMapper implicitly used by Spring Boot.
    //overwrite the default ObjectMapper and not all the Jackson mappers created by Jackson2ObjectMapperBuilder builder
    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        return builder.build().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .registerModule(new JavaTimeModule());

    }

}
