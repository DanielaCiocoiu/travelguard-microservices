package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.dao.PolicyRepository;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PolicyServiceIml implements PolicyService {

    private PolicyRepository policyRepository;

    @Autowired
    public PolicyServiceIml(PolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    @Override
    public List<Policy> findAll() {
        List <Policy> policyList = policyRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Policy::getPrice))
                .collect(Collectors.toList());

        return policyList ;
    }

    @Override
    public List<Policy> findAllByValidity() {
        List<Policy> result = policyRepository.findAll()
                .stream()
                .filter(Policy::isValidity).
                collect(Collectors.toList());
        return result;
    }

    @Override
    public Map<Type, Long> findAllByType() {
        Map<Type, Long> result = policyRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Policy::getType, Collectors.counting()));
        return result;
    }

    @Override
    public Optional<Policy> findById(int theId) {
        Optional<Policy> result = policyRepository.findById(theId);
        Policy policy = null;
        if (result.isPresent()) {
            policy = result.get();
        } else {
            throw new PolicyNotFoundException("Policy id {} not found - " + theId);
        }
        return result;
    }

    @Override
    public Policy save(Policy policy) {

        policyRepository.save(policy);
        return policy;
    }

    @Override
    public void deleteById(int policyId) {
        policyRepository.deleteById(policyId);
    }

}
