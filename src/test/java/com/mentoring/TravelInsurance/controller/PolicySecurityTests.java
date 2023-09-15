package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.config.SecurityConfig;
import com.mentoring.TravelInsurance.error.policy.PolicyNotFoundException;
import com.mentoring.TravelInsurance.mappers.PolicyMapperImpl;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.service.policy.PolicyService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = { SecurityConfig.class })
@WebMvcTest(SecurityConfig.class)
@RunWith(SpringRunner.class)
public class PolicySecurityTests {
	@Autowired
	private WebApplicationContext context;
	@Autowired
	MockMvc mvc;
	@MockBean
	PolicyService policyService;
	@SpyBean
	private PolicyMapperImpl policyMapper;
	@Autowired
	private WebTestClient client;

	@Spy
	@InjectMocks
	private PolicyController policyController;


	@BeforeEach
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup(new PolicyController(policyService,policyMapper )).build();
	}

	@Test
	public void getPolicyByIdUnauthorized() throws Exception {
		mvc.perform(get("/api/policies")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	public void getPolicyByIdNotFound() throws Exception {
		int  id = 1111;
		Mockito.doThrow(PolicyNotFoundException.class).when(policyService).findById(id);
		mvc.perform(get("/api/policy/1111").with(user("user").roles("USER")))
				.andExpect(status().isNotFound());
	}

	@Test
	public void get_noAuth_returnsClientError() {
		this.client.get().uri("/api/policies")
				.exchange()
				.expectStatus().is4xxClientError();
	}
	@Test
	@WithMockUser(username = "Dana", password = "Dana", roles = {"ADMIN"})
	public void getAllPolicies() throws Exception {
		List<Policy> policyList = policyService.findAll();
		System.out.println(policyList);
	}
	@Test
	public void goalsWithHttpBasicShouldReturnError() throws Exception {
		this.mvc.perform(get("/api/policies")
				.with(httpBasic("user", "password")))
				.andExpect(status().is4xxClientError());
	}
	@Test
	public void givenUserIsAnonymous_whenGetLiveness_thenClientError() throws Exception {
		mvc.perform(get("/api/actuator/health/liveness"))
				.andExpect(status().is4xxClientError());
	}
	@Test
	public void givenUserIsAnonymous_whenGetMachin_thenUnauthorized() throws Exception {
		mvc.perform(get("/api/policies"))
				.andExpect(status().isUnauthorized());
	}

}
