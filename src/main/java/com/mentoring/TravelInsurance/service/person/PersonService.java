package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> findAll();
    Optional<Person> findById(int theId);
    Person save(Person person);
    Person deleteById(int personId);
    Iterable<Person> getPersonsSortedAlfabetically(Sort sort);
    Iterable<Person> getPersonsInPages(Pageable pageable);
    List<Person> findPersonsByNationality(String nationality, Pageable pageable);
}
