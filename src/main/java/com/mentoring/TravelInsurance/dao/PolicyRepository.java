package com.mentoring.TravelInsurance.dao;

import com.mentoring.TravelInsurance.model.Policy;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PolicyRepository extends JpaRepository<Policy, Integer> {
}
