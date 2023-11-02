package com.mentoring.service;

import com.mentoring.client.PoliciesServiceClient;
import com.mentoring.dto.PolicyDto;
import com.mentoring.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestTemplateService {
    RestTemplateRepository restTemplateRepository;
    RestTemplate restTemplate;


    @Autowired
    private PoliciesServiceClient policiesServiceClient;

    @Autowired
    public RestTemplateService(RestTemplateRepository restTemplateRepository, RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateRepository = restTemplateRepository;
        restTemplate = restTemplateBuilder
                .errorHandler(new NotFoundException.RestTemplateResponseErrorHandler())
                .build();
    }

    public List<PolicyDto> allListPolicies() {
        return policiesServiceClient.fetchAllPolicies();
    }

    public PolicyDto getPolicyById(int id) {
          return policiesServiceClient.fetchPolicyById(id);
    }

    public PolicyDto createPolicy(PolicyDto policyDto) {
        return policiesServiceClient.createPolicy(policyDto);
    }

    public void updatePolicy(PolicyDto policyDto) {
        policiesServiceClient.updatePolicy(policyDto);
    }

    public void deletePolicyById(int id) {
          policiesServiceClient.deletePolicy(id);
    }
}

