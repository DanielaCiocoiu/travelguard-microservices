package com.mentoring.service.person;

import com.mentoring.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PersonService {

    List<Person> findAll();

    Person findById(Integer theId);

    Person save(Person person);

    Boolean deleteById(Integer theId);

    Iterable<Person> getPersonsSortedAlphabetically(Sort sort);

    Iterable<Person> getPersonsInPages(Pageable pageable);

    List<Person> findPersonsByNationality(String nationality, Pageable pageable);

   }
