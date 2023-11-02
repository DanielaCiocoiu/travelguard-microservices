package com.mentoring.controller;

import com.mentoring.dto.PersonDto;
import com.mentoring.mappers.PersonMapper;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import com.mentoring.service.person.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Operation(summary = "Get all persons")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> findAll() {
        List<Person> personList = personService.findAll();
        List<PersonDto> personDtoList = personMapper.toDtoList(personList);
        if (personDtoList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(personDtoList, HttpStatus.OK);
    }
    @Operation(summary = "Get a persons by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the person",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Policy.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Person not found",
                    content = @Content)})
    @GetMapping("/person/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<PersonDto> getPersonId(@PathVariable int id) {
        Person person = personService.findById(id);
        PersonDto personDto = personMapper.toDto(person);
        return new ResponseEntity<>(personDto, HttpStatus.OK);
    }
    @Operation(summary = "Create a person")
    @PostMapping("/person")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public PersonDto addNewPerson(@Valid @RequestBody PersonDto personDto) {
        personDto.setId(0);
        Person personToBeSaved = personMapper.toEntity(personDto);
        Person personSaved = personService.save(personToBeSaved);
        logger.info("Created new person {}", personSaved);
        return personMapper.toDto(personSaved);
    }
    @Operation(summary = "Update a person")
    @PutMapping("/person")
    @ResponseStatus(HttpStatus.OK)
   @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public PersonDto savePerson(@RequestBody PersonDto personDto) {
        Person personToBeSaved = personMapper.toEntity(personDto);
        Person personSaved = personService.save(personToBeSaved);
        logger.info("Created new person {}", personSaved);
        return personMapper.toDto(personSaved);
    }
    @Operation(summary = "Delete a person")
    @DeleteMapping("/person/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<String> deletePerson(@PathVariable(value = "id") int id) {
        PersonDto personDto = personMapper.toDto(personService.findById(id));
        personService.deleteById(personDto.getId());
        logger.info("Deleted person with id: + " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //http://localhost:8082/api/persons/9/Ilinca
    @PatchMapping("/persons/{id}/{name}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Person> updatePersonName(@PathVariable int id, @PathVariable String name) {
        var thePerson = personService.findAll()
                .stream()
                .filter(d -> d.getId().equals(id))
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
    @Operation(summary = "Get all persons sorted alphabetically")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/persons/sort")
    public Iterable<Person> getPersonsSortedAlphabetically() {
        return personService.getPersonsSortedAlphabetically(Sort.by("name").ascending());
    }

    //http://localhost:8082/api/persons/page?page=0&size=2
    @Operation(summary = "Get all persons in pages")
   @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/persons/page")
    public Iterable<Person> getPersonsInPages(@RequestParam int page, @RequestParam int size) {
        return personService.getPersonsInPages(PageRequest.of(page, size));
    }

    //http://localhost:8082/api/nationality/romana?page=0&size=2
    @Operation(summary = "Get persons paginated and sorted by nationality")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/nationality/{nationality}")
    public List<Person> findPersonsByNationality(@PathVariable String nationality, @RequestParam int page, @RequestParam int size) {
        final Sort sort = Sort.by("name").ascending();
        final Pageable pageable = PageRequest.of(page, size, sort);
        return personService.findPersonsByNationality(nationality, pageable);
    }

}
