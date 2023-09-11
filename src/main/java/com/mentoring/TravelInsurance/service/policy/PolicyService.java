package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

public interface PolicyService {

    List<Policy> findAll();

    List<Policy> findAllByPrice();

    List<Policy> findAllByValidity();

    Map<Type, Long> findAllByType();

    Policy findById(int theId);

    Policy save(Policy policy);

    Boolean deleteById(int theId);

    List<Policy> findPolicyByPriceBefore(double limit);

    ByteArrayInputStream exportExcel();
}
