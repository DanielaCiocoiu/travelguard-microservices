package com.mentoring.service;

import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RestTemplateRepository extends JpaRepository<Policy, Integer>, PagingAndSortingRepository<Policy, Integer> {
}
