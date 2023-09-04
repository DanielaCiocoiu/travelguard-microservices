package com.mentoring.TravelInsurance.service;

import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.dao.PolicyRepository;
import com.mentoring.TravelInsurance.mappers.PolicyMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyServiceIml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class PolicyServiceTests {

    @Mock
    PolicyRepository policyRepository;
    @InjectMocks
    private PolicyServiceIml policyServiceIml;
    @SpyBean
    private PolicyMapperImpl policyMapper;
    private static Policy policy, policy1, policy2;
    private static Person person, person1, person2;
    private List<Policy> policyList;

    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Maria", 22, "romanian");
        person1 = Setup.personBuilder("Irina", 50, "american");
        person2 = Setup.personBuilder("Iona", 22, "romanian");
        policy = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, List.of(person));
        policy1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, List.of(person1));
        policy2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00, List.of(person2));
    }
    @BeforeEach
    public void beforeEachTestExecution() {
        policyList = List.of();
    }

    @Test
    public void shouldReturnAllPersons() {
        policyList = List.of(policy, policy1, policy2);

        given(policyRepository.findAll()).willReturn(policyList);

        List <Policy> expected = policyServiceIml.findAll();

        assertEquals(expected, policyList);
        verify(policyRepository, times(1)).findAll();
    }
    @Test
    void whenGetAllPersonsReturnEmptyList() {
        //Lenient stubs bypass “strict stubbing” validation rules. Avoid stubbing problems, such as the unnecessary stubbing described earlier.
        lenient().when(policyRepository.findAll()).thenReturn(policyList);
        assertThat(policyServiceIml.findAll()).isEqualTo(policyList);
    }

    @Test
    public void whenSavePerson_shouldReturnPerson() {
        Policy policy = policy1;
        when(policyRepository.save(ArgumentMatchers.any(Policy.class))).thenReturn(policy);
        Policy created = policyServiceIml.save(policy);
        assertThat(created.getId()).isSameAs(policy.getId());
        verify(policyRepository).save(policy);
    }

    @Test
    public void whenGivenId_shouldDeletePerson_ifFound(){
        var policy = policy1;
        policy.setId(2);
        lenient().when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        policyServiceIml.deleteById(policy.getId());
       verify(policyRepository).findById(policy.getId());
    }
    @Test
    public void whenUpdatePerson_shouldUpdatePerson_ifFound() {
        var policy = policy1;
        policy.setId(2);
        lenient().when(policyRepository.findById(policy.getId())).thenReturn(Optional.of(policy));
        policyServiceIml.save(policy);
        verify(policyRepository).save(policy);
    }
}

