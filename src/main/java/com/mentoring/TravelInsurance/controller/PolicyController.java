package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.mappers.PolicyMapper;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PolicyController {
    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    private PolicyService policyService;

    private PolicyMapper policyMapper;

    public PolicyController(PolicyService policyService, PolicyMapper policyMapper) {
        this.policyService = policyService;
        this.policyMapper = policyMapper;
    }

    //http://localhost:8080/api/policies
    @GetMapping("/policies")
    public ResponseEntity<List<PolicyDto>> findAll() {
        List<PolicyDto> policyDtoList = policyMapper.toDtoList(policyService.findAll());
        logger.info("List of all policies: " + policyDtoList.stream().count());
        return new ResponseEntity<>(policyDtoList, HttpStatus.OK);
    }

    //http://localhost:8080/api/type
    @GetMapping("/type")
    public ResponseEntity<Map<Type, Long>> findAllPoliciesByType() {
        Map<Type, Long> result = policyService.findAllByType();
        logger.info("List of all policies by type: " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //http://localhost:8080/api/validity
    @GetMapping("/validity")
    public ResponseEntity<List<PolicyDto>> findAllPoliciesValidity() {
        List<PolicyDto> policyDtoList = policyMapper.toDtoList(policyService.findAllByValidity());
        logger.info("List of all policies by validity: " + policyDtoList.stream().count());
        return new ResponseEntity<>(policyDtoList, HttpStatus.OK);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<Optional<Policy>> getPolicy(@PathVariable int policyId) {
        Optional<Policy> policy = policyService.findById(policyId);
        if (policy.isEmpty() || policyId < 0) {
            throw new PolicyNotFoundException("Policy id {} not found - " + policyId);
        }
        return new ResponseEntity<>(policy, HttpStatus.OK);
    }

    @PostMapping("/policy")
    @ResponseStatus(HttpStatus.CREATED)
    public PolicyDto addPolicy(@Valid @RequestBody PolicyDto policyDto) {
        policyDto.setId(0);
        Policy policyToBeSaved = policyMapper.toEntity(policyDto);
        Policy policySaved = policyService.save(policyToBeSaved);
        logger.info("Created new policy {}", policySaved);
        return policyMapper.toDto(policySaved);
    }

    @PutMapping("/policy")
    @ResponseStatus(HttpStatus.OK)
    public PolicyDto savePolicy(@RequestBody PolicyDto policyDto) {
        Policy policyToBeSaved = policyMapper.toEntity(policyDto);
        Policy policySaved = policyService.save(policyToBeSaved);
        logger.info("Created new policy {}", policySaved);
        return policyMapper.toDto(policySaved);
    }


    @DeleteMapping("/policy/{Id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePolicy(@PathVariable(value = "id") int id) {
        PolicyDto personDto = policyMapper.toDto(policyService.findById(id).get());
        policyService.deleteById(personDto.getId());
        logger.info("Deleted policy with id: + " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //http://localhost:8080/api/policy/9/250
    @PatchMapping("/policy/{id}/{price}")
    public ResponseEntity<Policy> updatePolicyPrice(@PathVariable int id, @PathVariable Double price) {
        var thePolicy = policyService.findAll()
                .stream()
                .filter(d -> ((Integer) d.getId()).equals(id))
                .findFirst()
                .orElse(null);
        if (thePolicy != null) {
            thePolicy.setPrice(price);
            policyService.save(thePolicy);
            return ResponseEntity.ok(thePolicy);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
