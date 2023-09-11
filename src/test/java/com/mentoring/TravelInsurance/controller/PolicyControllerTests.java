package com.mentoring.TravelInsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.mappers.PolicyMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.policy.PolicyExcel;
import com.mentoring.TravelInsurance.service.policy.PolicyService;
import org.junit.BeforeClass;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assume.assumeFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
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
    private PolicyExcel policyExcel;
    private static Person person, person1, person2;
    private static Policy policy, policy1, policy2;
    private List<Policy> policyList;

    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Maria", 22, "romanian");
        person1 = Setup.personBuilder("Irina", 50, "american");
        person2 = Setup.personBuilder("Iona", 22, "romanian");
        policy = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, List.of(person, person1, person2));
        policy1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, List.of(person, person1, person2));
        policy2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00, List.of(person, person1, person2));
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        policyList = List.of();
        policyDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        policyDto = new PolicyDto();
        policyExcel = new PolicyExcel();
    }
    @BeforeClass
    public static void disableTestsOnCiServer() {
        String profilesFromConsole = System.getProperty("spring.profiles.active", "");
        assumeFalse(profilesFromConsole.contains("flyway"));
    }
    @Test
    public void whenGetMethodListAllPolicies_() throws Exception {
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
    public void whenGetMethodListPolicyById() throws Exception {
        Policy policy3 = policy;
        policy3.setId(1);
        policyDto = policyMapper.toDto(policy3);
        given(policyService.findById(policyDto.getId())).willReturn(policy3);
        mockMvc.perform(get("/api/policy/" + policy3.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetPolicies_NoPoliciesReturn() throws Exception {
        when(policyService.findAll()).thenReturn(policyList);
        mockMvc.perform(get("/api/policies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    public void testDeleteShouldReturn404NotFound() throws Exception {
        int  id = 1111;
            Mockito.doThrow(PolicyNotFoundException.class).when(policyService).deleteById(id);
        mockMvc.perform(delete("/policy/" + id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testDownloadFile() throws Exception {
        List<Policy> people = new ArrayList<>();
        people.add(policy2);
        when(policyService.findAll()).thenReturn(people);

        ByteArrayInputStream in = policyExcel.tutorialsToExcel(people);
        when(policyService.exportExcel()).thenReturn(in);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/exportToExcel")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(MockMvcResultMatchers.status()
                        .is(200))
                .andReturn();
        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("application/vnd.ms-excel", result.getResponse().getContentType());
    }

    @Test
    public void testExportExcelIsNull() {
        List<Policy> policyList = new ArrayList<>();
        Mockito.when(policyService.findAll()).thenReturn(policyList);
        ByteArrayInputStream in = policyExcel.tutorialsToExcel(policyList);
        assertNotNull(in);
    }
    @Test
    public void testExportExcelFindAllPolicies() {
        policyList = List.of(policy, policy1, policy2);
        Mockito.when(policyService.findAll()).thenReturn(policyList);
        ByteArrayInputStream in = policyExcel.tutorialsToExcel(policyList);
        assertEquals(3,policyList.size());
    }
}
