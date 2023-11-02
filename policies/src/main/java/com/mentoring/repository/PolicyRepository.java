package com.mentoring.repository;

import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    @Query("SELECT p from Policy p WHERE p.price<:price")
    List<Policy> findPolicyByPriceBefore(@Param("price") double price);

    List<Policy> getPolicyListByCreatedBy(Person person);
}
