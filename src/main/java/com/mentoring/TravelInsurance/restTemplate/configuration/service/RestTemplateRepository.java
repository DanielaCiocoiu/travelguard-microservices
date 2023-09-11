package com.mentoring.TravelInsurance.restTemplate.configuration.service;

import com.mentoring.TravelInsurance.model.Person;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface RestTemplateRepository extends JpaRepository<Person, Integer>, PagingAndSortingRepository<Person, Integer> {
}
