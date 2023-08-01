package com.mentoring.TravelInsurance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TravelInsuranceApplication {
	private static final Logger logger = LoggerFactory.getLogger(TravelInsuranceApplication.class);
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TravelInsuranceApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
	}

}
