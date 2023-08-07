package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.dao.PolicyRepository;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PolicyServiceIml implements PolicyService {

    private final PolicyRepository policyRepository;

    public PolicyServiceIml(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }
    @Override
    public List<Policy> findAll() {

        return policyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Policy::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public List<Policy> findAllByValidity() {
        return policyRepository.findAll()
                .stream()
                .filter(Policy::isValidity).
                collect(Collectors.toList());
    }

    @Override
    public Map<Type, Long> findAllByType() {
        return policyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Policy::getType, Collectors.counting()));
    }

    @Override
    public Optional<Policy> findById(int theId) {
        Optional<Policy> result = policyRepository.findById(theId);
        if (result.isPresent()) {
            result.get();
        } else {
            throw new PolicyNotFoundException("Policy id not found - " + theId);
        }
        return result;
    }

    @Override
    public Policy save(Policy policy) {
        policyRepository.save(policy);
        return policy;
    }

    @Override
    public void deleteById(int theId) {
        policyRepository.deleteById(theId);
    }

    @Override
    public List<Policy> findPolicyByPriceBefore(double limit) {
       return policyRepository.findPolicyByPriceBefore(limit);
    }

}
