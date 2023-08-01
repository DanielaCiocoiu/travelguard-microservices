package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface PolicyService {

    public List<Policy> findAll();

    public List<Policy> findAllByValidity();

    public Map<Type, Long> findAllByType();
    public Optional<Policy> findById(int theId);

    public Policy save(Policy policy);
    public void deleteById(int theid);
}
