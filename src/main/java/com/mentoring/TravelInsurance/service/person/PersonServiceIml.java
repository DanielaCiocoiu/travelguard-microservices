package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.dao.PersonRepository;
import com.mentoring.TravelInsurance.error.person.PersonNotFoundException;
import com.mentoring.TravelInsurance.error.person.PersonWrongAgeException;
import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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
    public Person findById(int theId) {
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
    public Boolean deleteById(int theId) {
        Optional<Person> optionalPerson = personRepository.findById(theId);
        if (optionalPerson.isPresent()) {
            personRepository.delete(optionalPerson.get());
            return true;
        }
         throw new PersonNotFoundException("Did not find person with Id: " + theId);
    }

    public ByteArrayInputStream exportExcel() {
        List<Person> personList = personRepository.findAll();
        ByteArrayInputStream in = PersonExcel.tutorialsToExcel(personList);
        return in;
    }

}
