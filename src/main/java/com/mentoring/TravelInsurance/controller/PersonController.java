package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.error.person.PersonNotFoundException;
import com.mentoring.TravelInsurance.error.person.PersonWrongAgeException;
import com.mentoring.TravelInsurance.mappers.PersonMapper;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.service.person.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
    private PersonService personService;
    private PersonMapper personMapper;


    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    //http://localhost:8080/api/persons
    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> findAll() {
        List<Person> personList = personService.findAll();
        List<PersonDto> personDtoList = personMapper.toDtoList(personList);
        logger.info("List of person + " + personDtoList.stream().count());
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }

    @GetMapping("/person/{personId}")
    public Optional<Person> getPersonId(@PathVariable int personId) {
        Optional<Person> person = personService.findById(personId);
        if (person.isEmpty() || personId < 0) {
            throw new PersonNotFoundException("Person id not found - " + personId);
        }
        return person;
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto addNewPerson(@Valid @RequestBody PersonDto personDto) {
        personDto.setId(0);
        Person personToBeSaved = personMapper.toEntity(personDto);
        Person personSaved = personService.save(personToBeSaved);
        logger.info("Created new person {}", personSaved);
        logger.debug("Created new person {}", personSaved);
        if (personDto.getAge() < 0){
            throw new PersonWrongAgeException("Wrong age for person: " + personDto.getId());
        }
        return personMapper.toDto(personSaved);
    }

    @PutMapping("/person")
    @ResponseStatus(HttpStatus.OK)
    public PersonDto savePerson(@RequestBody PersonDto personDto) {
        Person personToBeSaved = personMapper.toEntity(personDto);
        Person personSaved = personService.save(personToBeSaved);
        logger.info("Created new person {}", personSaved);
        return personMapper.toDto(personSaved);
    }


    @DeleteMapping("/person/{id}")
    public ResponseEntity<String> deleteById(@PathVariable(value = "id") int id) {
        PersonDto personDto  = personMapper.toDto(personService.findById(id).get());
        personService.deleteById(personDto.getId());
        logger.info("Deleted person with id: + " + id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //http://localhost:8080/api/persons/9/Ilinca
    @PatchMapping("/persons/{id}/{name}")
    public ResponseEntity<Person> updatePersonName(@PathVariable int id, @PathVariable String name) {
        var thePerson = personService.findAll()
                .stream()
                .filter(d -> ((Integer) d.getId()).equals(id))
                .findFirst()
                .orElse(null);

        if (thePerson != null) {
            thePerson.setName(name);
            personService.save(thePerson);
            logger.info("Updated new person with name: " + name);
            return ResponseEntity.ok(thePerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
