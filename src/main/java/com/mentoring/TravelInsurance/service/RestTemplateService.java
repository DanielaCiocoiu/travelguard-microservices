package com.mentoring.TravelInsurance.service;

import com.mentoring.TravelInsurance.controller.PersonController;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private PersonService personService;
    RestTemplate restTemplate = new RestTemplate();
    private static final String get_All_Persons_URL = "http://localhost:8080/api/persons";
    private static final String create_Person_URL = "http://localhost:8080/api/person";
    private static final String get_person_By_id_URL = "http://localhost:8080/api/person/{id}";
    private static final String delete_person_By_id_URL = "http://localhost:8080/api/person/{id}";


    public ResponseEntity<String> allPersons() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // for security stuff -  headers.add("Authorization", headerValue);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(get_All_Persons_URL, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<String> createPerson(Person person) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Person> entity = new HttpEntity<>(person, headers);
        return restTemplate
                .exchange(create_Person_URL,
                        HttpMethod.POST,
                        entity,
                        String.class);
    }

    public Person getPersonById(int id) {
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);
        return restTemplate.getForObject(get_person_By_id_URL, Person.class, param);
    }

    public void deletePersonById(int id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Person> entity = new HttpEntity<>(headers);
        Map<String, Integer> params = new HashMap<>();
        params.put("idd", id);
        restTemplate.delete(delete_person_By_id_URL, params,entity);
    }
}

