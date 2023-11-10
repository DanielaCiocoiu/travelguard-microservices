package com.mentoring.tests;

import com.mentoring.controller.AuthController;
import com.mentoring.controller.PolicyController;
import com.mentoring.dto.PolicyDto;
import com.mentoring.error.policy.PolicyNotFoundException;
import com.mentoring.mappers.PolicyMapperImpl;
import com.mentoring.model.BenefitAndLimit;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import com.mentoring.model.Type;
import com.mentoring.repository.PolicyRepository;
import com.mentoring.service.WebSecurityConfig;
import com.mentoring.service.jwt.AuthEntryPointJwt;
import com.mentoring.service.jwt.JwtUtils;
import com.mentoring.service.policy.PolicyService;
import com.mentoring.service.user.UserDetailsServiceImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {WebSecurityConfig.class})
@WebMvcTest(value = PolicySecurityTests.class)
@RunWith(SpringRunner.class)
public class PolicySecurityTests {

    private static PolicyDto policyDto;
    @Autowired
    MockMvc mvc;

    @MockBean
    PolicyService policyService;

    @Mock
    PolicyRepository policyRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    JwtUtils jwtUtils;
    @Autowired
    private WebSecurityConfig webSecurityConfig;
    @SpyBean
    private PolicyMapperImpl policyMapper;
    @Autowired
    private WebTestClient webTestClient;
    @Spy
    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        policyDto = new PolicyDto();
        this.mvc = MockMvcBuilders.standaloneSetup(
                new PolicyController(policyService, policyMapper)).build();
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void getAllPolicies() {
        List<Policy> policyList = policyService.findAll();
        System.out.println(policyList);
    }

    @Test
    public void getPolicyById() {
        Mockito.doThrow(PolicyNotFoundException.class).when(policyService).findById(123);
        webTestClient.get()
                .uri("/api/policy/", 123)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void deletePolicyById() throws Exception {
        Person person = new Person(1, "Maria", 22, "romanian", List.of());
        Policy policy3 = new Policy(1, 2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, LocalDateTime.of(
                2022, 12, 31, 0, 0), null, List.of(person), person);
        policy3.setId(1);

        given(policyService.findById(policy3.getId())).willReturn(policy3);
        mvc.perform(delete("/api/policy/" + policy3.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "dcfdf", password = "123xxf45678", roles = {"adzsdszmin"})
    public void deletePolicyByIdForbidden() throws Exception {
        Integer i = 11;
        Mockito.doThrow(HttpClientErrorException.Forbidden.class).when(policyService).deleteById(i);
        mvc.perform(delete("/policy/" + i)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden()).andDo(print());
    }

    @Test
    @WithMockUser(username = "0", password = "0", roles = {"0"})
    public void get_noAuth_returnsClientError() {
        this.webTestClient.get().uri("/api/policies")
                .exchange()
                .expectStatus()
                .is4xxClientError()
                .expectBody()
                .isEmpty();
    }

    @Test
    @WithMockUser(username = "0", password = "0", roles = {"0"})
    public void givenUserIsAnonymous_whenGetLiveness_thenClientError() throws Exception {
        webTestClient.get()
                .uri("/api/actuator/health/liveness")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .isEmpty();
    }


}
