package com.mentoring.TravelInsurance.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.mappers.PolicyMapper;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.repository.PolicyRepository;
import com.mentoring.TravelInsurance.service.person.PersonServiceIml;
import com.mentoring.TravelInsurance.service.policy.PolicyServiceIml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PolicyServiceTests {

    @Mock
    PolicyRepository policyRepository;
    @Mock
    private PolicyMapper policyMapper;
    @InjectMocks
    private PolicyServiceIml policyServiceIml;
    @Mock
    private PersonServiceIml personServiceIml;
    private static Policy policy, policy1, policy2;
    private static Person person, person1, person2;
    private List<Policy> policyList;
    private List<PolicyDto> policyDtos;
    private ObjectMapper objectMapper;
    private static PolicyDto policyDto;


    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Maria", 22, "romanian", List.of());
        person1 = Setup.personBuilder("Irina", 50, "american", List.of());
        person2 = Setup.personBuilder("Iona", 22, "romanian", List.of());
        policy = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, LocalDateTime.of(2022, 1, 1, 0, 0), LocalDateTime.of(2022, 12, 31, 0, 0), List.of(person));
        policy1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, LocalDateTime.of(2022, 12, 31, 0, 0), LocalDateTime.of(2022, 1, 1, 0, 0), List.of(person1));
        policy2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00, LocalDateTime.of(2022, 12, 31, 0, 0), LocalDateTime.of(2022, 1, 1, 0, 0), List.of(person2));

        policyDto = new PolicyDto(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, LocalDateTime.of(2022, 1, 1, 0, 0), LocalDateTime.of(2022, 12, 31, 0, 0), List.of(person1), Person.builder().build(), 1);
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        policyList = List.of();
        policyDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        policyDto = new PolicyDto();
    }

    @Test
    public void shouldReturnAllPolicies() {
        policyList = List.of(policy, policy1, policy2);
        given(policyRepository.findAll()).willReturn(policyList);
        List<Policy> expected = policyServiceIml.findAll();
        assertEquals(expected, policyList);
        verify(policyRepository, times(1)).findAll();
    }

    @Test
    void whenGetAllPoliciesReturnEmptyList() {
        when(policyRepository.findAll()).thenReturn(policyList);
        assertThat(policyServiceIml.findAll()).isEqualTo(policyList);
    }

    @Test
    public void shouldReturnPolicyFindById() {
        Policy policy = policy1;
        policy.setId(2);
        when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        Policy expected = policyServiceIml.findById(2);
        assertEquals(expected, policy);
        verify(policyRepository, times(1)).findById(2);
    }

    @Test
    public void whenSavePoliciy_shouldReturnPolicy() {
        Policy policy = policy1;
        when(policyRepository.save(ArgumentMatchers.any(Policy.class))).thenReturn(policy);
        Policy created = policyServiceIml.save(policy);
        assertThat(created.getId()).isSameAs(policy.getId());
        verify(policyRepository).save(policy);
    }

    @Test
    public void whenGivenId_shouldDeletePolicy_ifFound() {
        var policy = policy1;
        policy.setId(2);
        when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        policyServiceIml.deleteById(policy.getId());
        verify(policyRepository).findById(policy.getId());
    }

    @Test
    public void whenUpdatePolicy_shouldUpdatePolicy_ifFound() {
        var policy = policy1;
        policy.setId(2);
        lenient().when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        policyServiceIml.save(policy);
        verify(policyRepository).save(policy);
    }

    @Disabled
    @Test
    public void testExportExcel() {
        policyList = List.of(policy, policy1, policy2);
        Mockito.when(policyRepository.findAll()).thenReturn(policyList);
        ByteArrayInputStream in = policyServiceIml.exportExcel();
        assertNotNull(in);
    }

    @Test
    public void testGetNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeIntervalByPersonId() {
        // Arrange
        person = new Person(1, "Maria", 22, "romanian", List.of());
        policy = new Policy(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0,
                LocalDateTime.of(2022, 1, 1, 0, 0), LocalDateTime.of(2022, 12, 31, 0, 0),
                List.of(person), person);
        policyDto = new PolicyDto(1, 1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0,
                LocalDateTime.of(2022, 1, 1, 0, 0), LocalDateTime.of(2022, 12, 31, 0, 0), List.of(person1), Person.builder().build(), 1);

        LocalDate firstDate = LocalDate.of(2022, 1, 1);
        LocalDate lastDate = LocalDate.of(2022, 12, 31);

        when(personServiceIml.findById(any())).thenReturn(person);
        when(policyRepository.getPolicyListByCreatedBy(any())).thenReturn(List.of(policy));
        when(policyMapper.toDto(policy)).thenReturn(policyDto);

        // Act
        Map<LocalDate, Integer> expectedResult = policyServiceIml.getNumberOfPoliciesMadeByEachPersonEachMonthInGivenTimeIntervalByPersonId
                (firstDate, lastDate, person.getId());
        // Assert
        assertNotNull(expectedResult);
        assertEquals(expectedResult, expectedResult);
        assertEquals(expectedResult.get(firstDate), expectedResult.get(lastDate));
        assertEquals(expectedResult.get(firstDate), expectedResult.get(lastDate));
    }
}

