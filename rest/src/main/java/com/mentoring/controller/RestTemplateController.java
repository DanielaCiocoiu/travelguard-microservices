package com.mentoring.controller;

import com.mentoring.dto.PolicyDto;
import com.mentoring.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restTemplate")
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    @Autowired
    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @GetMapping("/getAllPolicies")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public List<PolicyDto> getListPolicies() {
        return restTemplateService.allListPolicies();
    }

    @GetMapping("/getPolicyById/{id}")
    public PolicyDto getPolicyById(@PathVariable(value = "id") int id) {
        return restTemplateService.getPolicyById(id);
    }

    @PostMapping("/addPolicy")
    @ResponseStatus(HttpStatus.CREATED)
    public PolicyDto addPolicy(@RequestBody PolicyDto policyDto) {
        return restTemplateService.createPolicy(policyDto);
    }

    @PutMapping("/updatePolicy")
    @ResponseStatus(HttpStatus.OK)
    public void updatePolicy(@RequestBody PolicyDto policyDto) {
        restTemplateService.updatePolicy(policyDto);
    }

    @DeleteMapping("/deletePolicy/{id}")
    public void deletePolicyById(@PathVariable int id) {
        restTemplateService.deletePolicyById(id);
    }
}
