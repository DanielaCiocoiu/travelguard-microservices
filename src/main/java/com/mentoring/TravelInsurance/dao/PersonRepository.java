package com.mentoring.TravelInsurance.dao;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PersonRepository extends JpaRepository<Person, Integer> {

}
