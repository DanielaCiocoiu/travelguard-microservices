package com.mentoring.TravelInsurance.dao;

import com.mentoring.TravelInsurance.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PolicyRepository extends JpaRepository<Policy, Integer> {
    @Query("SELECT p from Policy p WHERE p.price<:limit")
    List<Policy> findPolicyByPriceBefore(double limit);
}
