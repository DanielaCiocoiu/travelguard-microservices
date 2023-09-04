package com.mentoring.TravelInsurance.service.restTemplate;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestTemplateService {
    RestTemplate restTemplate = new RestTemplate();
    private static final String get_All_Persons_URL = "http://localhost:8080/api/persons";
    private static final String create_Person_URL = "http://localhost:8080/api/person";
    private static final String get_person_By_id_URL = "http://localhost:8080/api/person/{id}";
    private static final String update_person_By_id_URL = "http://localhost:8080/api/person";
    private static final String delete_person_By_id_URL = "http://localhost:8080/api/person/{id}";


    public ResponseEntity<String> allPersons() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        // for security stuff -  headers.add("Authorization", headerValue);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        return restTemplate.exchange(get_All_Persons_URL, HttpMethod.GET, entity, String.class);
    }

    public ResponseEntity<Person> createPerson(Person person) {
        return restTemplate.postForEntity(create_Person_URL, person, Person.class);
    }

    public Person getPersonById(int id) {
        Map<String, Integer> param = new HashMap<>();
        param.put("id", id);
        return restTemplate.getForObject(get_person_By_id_URL, Person.class, param);
    }

    public  void updatePerson(Person person){
        restTemplate.put(update_person_By_id_URL, person );
    }
    public void deletePersonById(int id) {
        Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        restTemplate.delete(delete_person_By_id_URL, params);
    }
}

