package com.mentoring.TravelInsurance.dao;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface PersonRepository extends JpaRepository<Person, Integer>, PagingAndSortingRepository<Person, Integer> {
    List<Person> findPersonsByNationality(String nationality, Pageable pageable);
}
