package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PolicyService {

    List<Policy> findAll();

    List<Policy> findAllByValidity();

    Map<Type, Long> findAllByType();

    Optional<Policy> findById(int theId);

    Policy save(Policy policy);

    void deleteById(int theId);

    List<Policy> findPolicyByPriceBefore(double limit);
}
