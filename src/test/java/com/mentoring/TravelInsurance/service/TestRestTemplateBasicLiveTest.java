package com.mentoring.TravelInsurance.service;


import org.junit.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//run with connection

public class TestRestTemplateBasicLiveTest {

    @Autowired
    TestRestTemplate testRestTemplate;
    private RestTemplate restTemplate;
    private static final String get_All_Policies_URL = "http://localhost:8080/api/policies";

    @BeforeEach
    public void beforeTest() {
        restTemplate = new RestTemplate();
    }

    @Test
    public void givenTestRestTemplate_whenSendExchange_thenStatusOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        testRestTemplate = new TestRestTemplate("Dana", "Dana");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void givenRestTemplateWrapper_whenSendExchange_thenStatusOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.configure(restTemplate);
        TestRestTemplate testRestTemplate = new TestRestTemplate(restTemplateBuilder, "Dana", "Dana");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET, requestEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void givenTestRestTemplateWithCredentials_whenSendExchange_thenStatusOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        TestRestTemplate testRestTemplate = new TestRestTemplate("Dana", "Dana");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET,requestEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
