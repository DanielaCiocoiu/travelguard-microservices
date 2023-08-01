package com.mentoring.TravelInsurance.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan(basePackages = {"com.mentoring.TravelInsurance.service.person", "com.mentoring.TravelInsurance.aspects"})
@EnableAspectJAutoProxy
public class ProjectConfig {
}
