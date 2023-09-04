package com.mentoring.TravelInsurance.service.policy;

import com.mentoring.TravelInsurance.dao.PolicyRepository;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.model.Person;
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
        return policyRepository.findAll();
    }

    @Override
    public Policy findById(int theId) {
        return policyRepository.findById(theId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy id not found - " + theId));
    }

    @Override
    public Policy save(Policy policy) {
        List<Person> person = policy.getPersonList();
        policy.setPersonList(person);
        policyRepository.save(policy);
        return policy;
    }

    @Override
    public Boolean deleteById(int theId) {
        Optional<Policy> optionalPolicy = policyRepository.findById(theId);
        if (optionalPolicy.isPresent()) {
            policyRepository.delete(optionalPolicy.get());
            return true;
        }
        throw new PolicyNotFoundException("Did not find policy with Id: " + theId);
    }

    @Override
    public List<Policy> findPolicyByPriceBefore(double limit) {
       return policyRepository.findPolicyByPriceBefore(limit);
    }

    @Override
    public List<Policy> findAllByPrice() {
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
}
