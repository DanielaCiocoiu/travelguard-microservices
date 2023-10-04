package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PolicyService {

    List<Policy> findAll();

    List<Policy> findAllByPrice();

    List<Policy> findAllByValidity();

    Map<Type, Long> findAllByType();

    Policy findById(Integer theId);

    Policy save(Policy policy);

    Boolean deleteById(Integer theId);

    List<Policy> findPolicyByPriceBefore(double limit);

    ByteArrayInputStream exportExcel();

    Iterable<Policy> getPoliciesInPages(Pageable pageable);

    Map<Integer, Map<LocalDate, Integer>> getNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeInterval(LocalDate firstDate, LocalDate lastDate);

    Map<LocalDate, Integer> getNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeIntervalByPersonId(LocalDate firstDate, LocalDate lastDate, Integer personId);
}
