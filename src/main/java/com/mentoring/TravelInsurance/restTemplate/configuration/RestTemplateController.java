package com.mentoring.TravelInsurance.restTemplate.configuration;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.restTemplate.configuration.service.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 50000)
@RestController
@RequestMapping("/restTemplate")
public class RestTemplateController {

    private final RestTemplateService restTemplateService;

    @Autowired
    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
     }

    @GetMapping("/getAllPolicies")
    public ResponseEntity<String> getAllPolicies() {
        return restTemplateService.allPolicies();
    }

    @GetMapping("/getPolicyById/{id}")
    public ResponseEntity<String> getPolicyById(@PathVariable(value = "id") int id) {
            return restTemplateService.getPolicyById(id);
    }

    @PostMapping("/addPolicy")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Policy> Policy(@RequestBody Policy policy) {
        return restTemplateService.createPolicy(policy);
    }

    @PutMapping("/updatePolicy")
    @ResponseStatus(HttpStatus.OK)
    public void updatePolicy(@RequestBody Policy policy) {
        restTemplateService.updatePolicy(policy);
    }

    @DeleteMapping("/deletePolicy/{id}")
    public void deletePolicyById(@PathVariable int id) {
        restTemplateService.deletePolicyById(id);
    }
}
