package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.service.restTemplate.RestTemplateService;
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

    @GetMapping("/getAllPersons")
    public ResponseEntity<String> getAllPersons() {
        return restTemplateService.allPersons();
    }

    @PostMapping("/addPerson")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        return restTemplateService.createPerson(person);
    }

    @PutMapping("/updatePerson")
    public void updatePerson(@RequestBody Person person) {
        restTemplateService.updatePerson(person);
    }

    @GetMapping("/getPersonById/{id}")
    public Person getPersonById(@PathVariable(value = "id") int id) {
        logger.info("Get person with id: " + id);
        return restTemplateService.getPersonById(id);
    }

    @DeleteMapping("/deletePerson/{id}")
    public void deletePersonById(@PathVariable int id) {
        logger.info("Delete person with id: " + id);
        restTemplateService.deletePersonById(id);
    }
}
