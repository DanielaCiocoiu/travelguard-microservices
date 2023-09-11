package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.mappers.PolicyMapper;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class PolicyController {
    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    private final PolicyService policyService;

    private final PolicyMapper policyMapper;

    public PolicyController(PolicyService policyService, PolicyMapper policyMapper) {
        this.policyService = policyService;
        this.policyMapper = policyMapper;
    }

    //http://localhost:8080/api/policies
    @GetMapping("/policies")
    public ResponseEntity<List<PolicyDto>> findAll() {
        List<Policy> policyList = policyService.findAll();
        List<PolicyDto> policyDtoList = policyMapper.toDtoList(policyList);
        logger.info("List of all policies: " + (long) policyDtoList.size());
        if (policyDtoList.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(policyDtoList, HttpStatus.OK);
    }

    @GetMapping("/policy/{policyId}")
    public ResponseEntity<PolicyDto> getPolicy(@PathVariable int policyId) {
        Policy policy = policyService.findById(policyId);
        PolicyDto policyDto = policyMapper.toDto(policy);
        policyService.findById(policyDto.getId());
        return new ResponseEntity<>(policyDto, HttpStatus.OK);
    }

    @PostMapping("/policy")
    public ResponseEntity<PolicyDto> addPolicy(@Valid @RequestBody PolicyDto policyDto) {
        policyDto.setId(0);
        Policy policyToBeSaved = policyMapper.toEntity(policyDto);
        Policy policySaved = policyService.save(policyToBeSaved);
        logger.info("Created new policy {}", policySaved);
         policyMapper.toDto(policySaved);
        return new ResponseEntity<>(policyDto, HttpStatus.CREATED);
    }

    @PutMapping("/policy")
    @ResponseStatus(HttpStatus.OK)
    public PolicyDto savePolicy(@RequestBody PolicyDto policyDto) {
        Policy policyToBeSaved = policyMapper.toEntity(policyDto);
        Policy policySaved = policyService.save(policyToBeSaved);
        logger.info("Created new policy {}", policySaved);
        return policyMapper.toDto(policySaved);
    }

    @DeleteMapping("/policy/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePolicy(@PathVariable(value = "id") int id) {
        PolicyDto personDto = policyMapper.toDto(policyService.findById(id));
        policyService.deleteById(personDto.getId());
        logger.info("Deleted policy with id: + " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //http://localhost:8080/api/type
    @GetMapping("/type")
    public ResponseEntity<Map<Type, Long>> findAllPoliciesByType() {
        Map<Type, Long> result = policyService.findAllByType();
        logger.info("List of all policies by type: " + result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //http://localhost:8080/api/policies/price/50.0
    @GetMapping("/policies/price/{price}")
    public List<Policy> findPolicyByPriceBefore(@PathVariable double price) {
        return policyService.findPolicyByPriceBefore(price);
    }

    //http://localhost:8080/api/validity
    @GetMapping("/validity")
    public ResponseEntity<List<PolicyDto>> findAllPoliciesValidity() {
        List<PolicyDto> policyDtoList = policyMapper.toDtoList(policyService.findAllByValidity());
        logger.info("List of all policies by validity: " + policyDtoList.stream().count());
        return new ResponseEntity<>(policyDtoList, HttpStatus.OK);
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
    @GetMapping("/exportToExcel")
    public ResponseEntity<Resource> getExportToExcel() {
        InputStreamResource file = new InputStreamResource(policyService.exportExcel());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename = policies " + currentDateTime + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
