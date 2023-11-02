package com.mentoring.service.person;

import com.mentoring.error.person.PersonNotFoundException;
import com.mentoring.error.person.PersonWrongAgeException;
import com.mentoring.model.Person;
import com.mentoring.repository.PersonRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class PersonServiceIml implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceIml(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public Iterable<Person> getPersonsSortedAlphabetically(Sort sort) {
        return personRepository.findAll(sort);
    }

    @Override
    public Iterable<Person> getPersonsInPages(Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Person> findPersonsByNationality(String nationality, Pageable pageable) {
        return personRepository.findPersonsByNationality(nationality, pageable);
    }

    @Override
    public List<Person> findAll() {
        return personRepository.findAll();
    }

    @Override
    public Person findById(Integer theId) {
         return personRepository.findById(theId)
         .orElseThrow(() -> new PersonNotFoundException("Person id not found - " + theId));
    }

    @Override
    public Person save(Person person) {
        personRepository.save(person);
        if (person.getAge() < 0) {
            throw new PersonWrongAgeException("Wrong age for person: " + person.getId());
        }
        return person;
    }

    @Override
    public Boolean deleteById(Integer theId) {
        Optional<Person> optionalPerson = personRepository.findById(theId);
        if (optionalPerson.isPresent()) {
            personRepository.delete(optionalPerson.get());
            return true;
        }
         throw new PersonNotFoundException("Did not find person with Id: " + theId);
    }
}
