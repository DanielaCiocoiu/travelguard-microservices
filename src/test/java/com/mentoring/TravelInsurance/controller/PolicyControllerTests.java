package com.mentoring.TravelInsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.mappers.PolicyMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PolicyController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class PolicyControllerTests {
    private static final Logger logger
            = LoggerFactory.getLogger(PolicyControllerTests.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;
    @SpyBean
    private PolicyMapperImpl policyMapper;

    @Spy
    @InjectMocks
    private PolicyController policyController;
    private ObjectMapper objectMapper;
    private List<PolicyDto> policyDtos;
    private PolicyDto policyDto;

   private static Policy policy, policy1, policy2;
    private List<Policy> policyList;

    @BeforeAll
    static void initBeforeAll() {
        policy = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0);
        policy1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00);
        policy2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00);
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        policyList = List.of();
        policyDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        policyDto = new PolicyDto();
    }

    @Test
    public void whenGetMethodListAllPersons_() throws Exception {
        policyList = List.of(policy, policy1, policy2);
        policyDtos = policyMapper.toDtoList(policyList);

        given(policyService
                .findAll())
                .willReturn(policyList);

        logger.info(policyList.toString());
        MvcResult mvcResult = mockMvc.perform(get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(policyDtos));
    }

    @Test
    public void whenGetMethodListPersonById() throws Exception {
        Policy policy3 = policy;
        policy3.setId(1);

        policyDto = policyMapper.toDto(policy3);
        given(policyService.findById(policy3.getId())).willReturn(Optional.of(policy3));

        mockMvc.perform(get("/api/policy/" + policy3.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetPersons_NoPersonsReturn() throws Exception {
        when(policyService.findAll()).thenReturn(policyList);

        mockMvc.perform(get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        int  id = 1111;
            Mockito.doThrow(PolicyNotFoundException.class).when(policyService).deleteById(id);
        mockMvc.perform(delete("/policy/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}
