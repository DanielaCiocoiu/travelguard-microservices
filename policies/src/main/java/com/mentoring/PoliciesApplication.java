package com.mentoring;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class PoliciesApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PoliciesApplication.class);
        application.setBannerMode(Banner.Mode.OFF);
        application.run(args);
    }
}
