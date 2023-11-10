package com.mentoring.client;


import com.mentoring.dto.PolicyDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class PoliciesServiceClient {
    private static final Logger logger = LoggerFactory.getLogger(PoliciesServiceClient.class);

    @Autowired
    private RestTemplate restTemplateService;


    public List<PolicyDto> fetchAllPolicies () {
        return restTemplateService.exchange(
                "http://POLICIES/api/policies", HttpMethod.GET, null,new ParameterizedTypeReference<List<PolicyDto>>() {}).getBody();
    }

    public PolicyDto fetchPolicyById (int id) {
        return restTemplateService.exchange(
                "http://POLICIES/api/policy/"  + id, HttpMethod.GET, null,new ParameterizedTypeReference<PolicyDto>() {}).getBody();
    }


    public PolicyDto createPolicy (PolicyDto policyDto) {
        return restTemplateService.postForEntity(
                "http://POLICIES/api/policy", policyDto, PolicyDto.class).getBody();
    }

    public void updatePolicy (PolicyDto policyDto) {
        restTemplateService.put(
                "http://POLICIES/api/policy/" + policyDto, PolicyDto.class);
    }
    public void deletePolicy (int id) {
        restTemplateService.delete(
                "http://POLICIES/api/policy/" + id);
    }
}
