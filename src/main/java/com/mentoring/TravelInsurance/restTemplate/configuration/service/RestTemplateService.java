package com.mentoring.TravelInsurance.restTemplate.configuration.service;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.restTemplate.configuration.handler.RestTemplateResponseErrorHandler;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestTemplateService {
    RestTemplateRepository restTemplateRepository;
    RestTemplate restTemplate;
    private static final String get_All_Policies_URL = "http://localhost:8080/api/policies";
    private static final String create_Policy_URL = "http://localhost:8080/api/policy";
    private static final String get_Policy_By_id_URL = "http://localhost:8080/api/policy/{id}";
    private static final String update_Policy_By_id_URL = "http://localhost:8080/api/policy";
    private static final String delete_Policy_By_id_URL = "http://localhost:8080/api/policy/{id}";
    HttpEntity<String> entity = new HttpEntity<>("parameters", createHeaders());

    @Autowired
    public RestTemplateService(RestTemplateRepository restTemplateRepository, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateRepository = restTemplateRepository;
        restTemplate = restTemplateBuilder
                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    HttpHeaders createHeaders() {
        return new HttpHeaders() {{
            String auth = "Dana" + ":" + "Dana";
            byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(StandardCharsets.US_ASCII));
            String authHeader = "Basic " + new String(encodedAuth);
            set("Authorization", authHeader);
            setAccept(List.of(MediaType.APPLICATION_JSON));
        }};
    }

    public ResponseEntity<String> allPolicies() {
           return restTemplate.exchange(get_All_Policies_URL, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> getPolicyById(int id) {
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);
        return restTemplate.exchange(get_Policy_By_id_URL, HttpMethod.GET, entity, String.class, param);
    }

    public ResponseEntity<Policy> createPolicy(Policy policy) {
        return restTemplate.postForEntity(create_Policy_URL, policy, Policy.class, entity);
    }

    public void updatePolicy(Policy policy) {
        restTemplate.put(update_Policy_By_id_URL, policy, entity);
    }

    public void deletePolicyById(int id) {
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        restTemplate.delete(delete_Policy_By_id_URL, params, entity);
    }
}

