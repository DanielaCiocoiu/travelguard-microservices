package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Optional;

public interface PersonService {

    List<Person> findAll();

    Optional<Person> findById(int theId);

    Person save(Person person);

    Boolean deleteById(int theId);

    Iterable<Person> getPersonsSortedAlphabetically(Sort sort);

    Iterable<Person> getPersonsInPages(Pageable pageable);

    List<Person> findPersonsByNationality(String nationality, Pageable pageable);

    ByteArrayInputStream exportExcel();
}
