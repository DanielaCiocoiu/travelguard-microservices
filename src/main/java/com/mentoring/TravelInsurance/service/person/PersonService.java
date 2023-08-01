package com.mentoring.TravelInsurance.service.person;

import com.mentoring.TravelInsurance.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {

    public List<Person> findAll();
    public Optional<Person> findById(int theId);
    public Person save(Person person);
    public Person deleteById(int personId);
}
