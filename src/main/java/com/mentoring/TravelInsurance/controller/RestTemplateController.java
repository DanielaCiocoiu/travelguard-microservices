package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.service.RestTemplateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/RestTemplate")
public class RestTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private final RestTemplateService restTemplateService;

    public RestTemplateController(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    //   -  http://localhost:8080/RestTemplate/getAllPersons
    @GetMapping("/getAllPersons")
    public ResponseEntity<String> getAllPersons() {
        return restTemplateService.allPersons();
    }

    //   -  http://localhost:8080/RestTemplate/addPerson
    @PostMapping("/addPerson")
    public ResponseEntity<String> createPerson(@RequestBody Person person) {
        return restTemplateService.createPerson(person);
    }

    //   -  http://localhost:8080/RestTemplate/getPersonById/4
    @GetMapping("/getPersonById/{id}")
    public Person getPersonById(@PathVariable(value = "id") int id) {
        logger.info("Get person with id: " + id);
        return restTemplateService.getPersonById(id);
    }

    //   -  http://localhost:8080/RestTemplate/deletePersonById/4
    @DeleteMapping("/deletePersonById/{id}")
    public void deletePersonById(@PathVariable(value = "id") int id) {
        logger.info("Delete person with id: " + id);
        restTemplateService.deletePersonById(id);
    }
}
