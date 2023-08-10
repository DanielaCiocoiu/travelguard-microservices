package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.dao.PersonRepository;
import com.mentoring.TravelInsurance.error.person.PersonNotFoundException;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
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
    public Optional<Person> findById(int theId) {
        Optional<Person> result = personRepository.findById(theId);
        if (result.isPresent()) {
            result.get();
        } else {
            throw new PersonNotFoundException("Person id not found - " + theId);
        }
        return result;
    }

    @Override
    public Person save(Person person) {
        List<Policy> policy = person.getPolicyList();
        person.setPolicyList(policy);
        personRepository.save(person);
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
