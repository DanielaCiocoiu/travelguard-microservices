package com.mentoring;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//run with connection
@ActiveProfiles("tag_test")
public class TestRestTemplateBasicLiveTest {

    private static final String get_All_Policies_URL = "http://localhost:8082/api/policies";
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    private MockMvc mvc;
    private RestTemplate restTemplate;

    @BeforeEach
    public void beforeTest() {
        restTemplate = new RestTemplate();
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void givenTestRestTemplate_whenSendExchange_thenStatusOk()throws Exception  {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        testRestTemplate = new TestRestTemplate("Vasia", "12345678");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET, requestEntity, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void givenRestTemplateWrapper_whenSendExchange_thenStatusOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        restTemplateBuilder.configure(restTemplate);
        TestRestTemplate testRestTemplate = new TestRestTemplate(restTemplateBuilder, "Vasia", "12345678");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET, requestEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void givenTestRestTemplateWithCredentials_whenSendExchange_thenStatusOk() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        TestRestTemplate testRestTemplate = new TestRestTemplate("Vasia", "12345678");
        ResponseEntity<String> response = testRestTemplate.exchange(get_All_Policies_URL, HttpMethod.GET,requestEntity, String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
    }
}
