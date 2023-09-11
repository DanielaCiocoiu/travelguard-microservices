package com.mentoring.TravelInsurance.controller;

import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.mentoring.TravelInsurance.mappers.PersonMapper;
import com.mentoring.TravelInsurance.repository.PersonRepository;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class PersonSecurityTests {
	@Autowired
	MockMvc mvc;
	@MockBean
	PersonService personService;
	@MockBean
	PersonMapper personMapper;

	@BeforeEach
	public void setup() {
		this.mvc = MockMvcBuilders.standaloneSetup(new PersonController(personService,personMapper )).build();
	}
	@Test
	public void goalsWithHttpBasicShouldReturnError() throws Exception {
		this.mvc.perform(get("/api/persons")
				.with(httpBasic("user", "password")))
				.andExpect(status().is4xxClientError());
	}

	@Test
	public void goalsWithJwtShouldReturnOk() throws Exception {
		this.mvc.perform(get("/api/persons")
			.with(jwt()))
			.andExpect(status().isOk());
	}
	@Test
	public void givenUserIsAnonymous_whenGetLiveness_thenClientError() throws Exception {
		mvc.perform(get("/api/actuator/health/liveness"))
				.andExpect(status().is4xxClientError());
	}
	@Test
	public void givenUserIsAnonymous_whenGetMachin_thenUnauthorized() throws Exception {
		mvc.perform(get("/api/persons"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockJwtAuth("SCOPE_data:write")
	public void givenUserIsGrantedWithDataWrite_whenGetMachin_thenOk() throws Exception {
		mvc.perform(get("/api/persons"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockJwtAuth("SCOPE_data:write")
	public void givenUserIsAuthenticatedButNotGrantedWithDataWrite_whenGetMachin_thenOk() throws Exception {
		mvc.perform(get("/api/persons"))
			.andExpect(status().isOk());
	}

}
