package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.mappers.PersonMapper;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.service.person.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api")
public class PersonController {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;
    private final PersonMapper personMapper;

    public PersonController(PersonService personService, PersonMapper personMapper) {
        this.personService = personService;
        this.personMapper = personMapper;
    }

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> findAll() {
        List<Person> personList = personService.findAll();
        List<PersonDto> personDtoList = personMapper.toDtoList(personList);
        logger.info("List of person + " + (long) personDtoList.size());
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }

    @GetMapping("/person/{id}")
    public ResponseEntity<PersonDto> getPersonId(@PathVariable int id) {
        PersonDto personDto = personMapper.toDto(personService.findById(id));
        personService.findById(personDto.getId());
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }

    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDto addNewPerson(@Valid @RequestBody PersonDto personDto) {
        personDto.setId(0);
        Person personToBeSaved = personMapper.toEntity(personDto);
        Person personSaved = personService.save(personToBeSaved);
        logger.info("Created new person {}", personSaved);
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
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deletePerson(@PathVariable(value = "id") int id) {
        PersonDto personDto = personMapper.toDto(personService.findById(id));
        personService.deleteById(personDto.getId());
        logger.info("Deleted person with id: + " + id);
        return new ResponseEntity<>(HttpStatus.OK);
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

    @GetMapping("/persons/sort")
    public Iterable<Person> getPersonsSortedAlphabetically() {
        return personService.getPersonsSortedAlphabetically(Sort.by("name").ascending());
    }

    //http://localhost:8080/api/persons/page?page=0&size=2
    @GetMapping("/persons/page")
    public Iterable<Person> getPersonsInPages(@RequestParam int page, @RequestParam int size) {
        return personService.getPersonsInPages(PageRequest.of(page, size));
    }

    //http://localhost:8080/api/nationality/romana?page=0&size=2
    @GetMapping("/nationality/{nationality}")
    public List<Person> findPersonsByNationality(@PathVariable String nationality, @RequestParam int page, @RequestParam int size) {
        final Sort sort = Sort.by("name").ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return personService.findPersonsByNationality(nationality, pageable);
    }

    @GetMapping("/exportToExcel")
    public ResponseEntity<Resource> getexportToExcel() {
        String filename = "persons.xlsx";
        InputStreamResource file = new InputStreamResource(personService.exportExcel());
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= person " + currentDateTime + ".xlsx")
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                .body(file);
    }
}
