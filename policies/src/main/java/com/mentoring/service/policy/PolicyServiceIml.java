package com.mentoring.service.policy;

import com.mentoring.dto.PolicyDto;
import com.mentoring.error.policy.PolicyNotFoundException;
import com.mentoring.mappers.PolicyMapper;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import com.mentoring.model.Type;
import com.mentoring.repository.PolicyRepository;
import com.mentoring.service.person.PersonServiceIml;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class PolicyServiceIml implements PolicyService {

    private final PolicyRepository policyRepository;
    private final PersonServiceIml personServiceIml;

    private final PolicyMapper policyMapper;

    public PolicyServiceIml(PolicyRepository policyRepository, PersonServiceIml personServiceIml, PolicyMapper policyMapper) {
        this.policyRepository = policyRepository;
        this.personServiceIml = personServiceIml;
        this.policyMapper = policyMapper;
    }

    @Override
    public List<Policy> findAll() {
        return policyRepository.findAll();
    }

    @Override
    public Policy findById(Integer theId) {
        return policyRepository.findById(theId)
                .orElseThrow(() -> new PolicyNotFoundException("Policy id not found - " + theId));
    }

    @Override
    public Policy save(Policy policy) {
        Person person1 = policy.getCreatedBy();
        policy.setCreatedBy(person1);
        List<Person> personList = policy.getPersonList();
        policy.setPersonList(personList);
        policyRepository.save(policy);
        return policy;
    }

    @Override
    public Boolean deleteById(Integer theId) {
        Optional<Policy> optionalPolicy = policyRepository.findById(theId);
        if (optionalPolicy.isPresent()) {
            policyRepository.delete(optionalPolicy.get());
            return true;
        }
        throw new PolicyNotFoundException("Did not find policy with Id: " + theId);
    }

    @Override
    public List<Policy> findPolicyByPriceBefore(double price) {
        return policyRepository.findPolicyByPriceBefore(price);
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

    @Override
    public ByteArrayInputStream exportExcel() {
        List<Policy> personList = policyRepository.findAll();
        PolicyExcel policyExcel = new PolicyExcel();
        return policyExcel.tutorialsToExcel(personList);
    }

    @Override
    public Iterable<Policy> getPoliciesInPages(Pageable pageable) {
        return policyRepository.findAll(pageable).getContent();
    }

    @Override
    public Map<Integer, Map<LocalDate, Integer>> getNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeInterval(
            LocalDate firstDate,
            LocalDate lastDate) {
        List<PolicyDto> listOfPoliciesFilteredByGivenDate =
                policyRepository
                        .findAll()
                        .stream()
                        .map(policyMapper::toDto)
                        .filter(policy -> policy.getIssueDate().toLocalDate().isAfter(firstDate)
                        && policy.getIssueDate().toLocalDate().isBefore(lastDate)).toList();

        Map<Integer, List<PolicyDto>> mapOfPolicyGroupByPerson = listOfPoliciesFilteredByGivenDate
                .stream()
                .collect(Collectors.groupingBy(PolicyDto::getPersonId));

        Map<Integer, List<LocalDate>> mapOfLocalDatesByPerson = new HashMap<>();
        for (int personId : mapOfPolicyGroupByPerson.keySet()) {
            List<PolicyDto> personPolicies = mapOfPolicyGroupByPerson.get(personId);
            List<LocalDate> dates = personPolicies.stream()
                    .map(PolicyDto::getIssueDate)
                    .map(LocalDateTime::toLocalDate)
                    .distinct()
                    .sorted()
                    .collect(Collectors.toList());
            mapOfLocalDatesByPerson.put(personId, dates);
        }

        Map<Integer, Map<LocalDate, Integer>> numberOfPoliciesMadeByEachPersonEveryMonth = new HashMap<>();
        for (int personId : mapOfPolicyGroupByPerson.keySet()) {
            List<PolicyDto> personPolicies = mapOfPolicyGroupByPerson.get(personId);
            List<LocalDate> dates = mapOfLocalDatesByPerson.get(personId);
            Map<LocalDate, Integer> numberOfPoliciesByMonth = new HashMap<>();
            for (LocalDate date : dates) {
                int count = (int) personPolicies.stream()
                        .filter(policy -> policy.getIssueDate().toLocalDate().equals(date))
                        .count();
                numberOfPoliciesByMonth.put(date, count);
            }
            numberOfPoliciesMadeByEachPersonEveryMonth.put(personId, numberOfPoliciesByMonth);
        }

        return numberOfPoliciesMadeByEachPersonEveryMonth;
    }

    @Override
    public Map<LocalDate, Integer> getNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeIntervalByPersonId(LocalDate firstDate,
                                                                                                             LocalDate lastDate,
                                                                                                             Integer personId) {
        List<PolicyDto> policyByPerson = policyRepository.getPolicyListByCreatedBy
                        (personServiceIml.findById(personId))
                .stream()
                .map(policyMapper::toDto)
                .toList();

        Map<LocalDate, Integer> resultingReport = new HashMap<>();

        policyByPerson.forEach(policyDto -> {
            LocalDate date = policyDto.getIssueDate().toLocalDate();
            if (date.isAfter(firstDate) && date.isBefore(lastDate)) {
                resultingReport.put(date, personId);
            }
        });
        return resultingReport;
    }
}
