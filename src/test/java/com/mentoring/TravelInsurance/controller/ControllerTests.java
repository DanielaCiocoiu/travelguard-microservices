package com.mentoring.TravelInsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.mappers.PersonMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PersonController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
public class ControllerTests {
    private static final Logger logger
            = LoggerFactory.getLogger(ControllerTests.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;
    @SpyBean
    private PersonMapperImpl personMapper;

    private ObjectMapper objectMapper;
    private List<Policy> policyList;
    private List<PersonDto> personDtos;
    private PersonDto personDto;

    private static Person person, person1, person2;
    private static Policy policyOb;
    private List<Person> personList;

    @BeforeAll
    static void initBeforeAll() {
        policyOb = Setup.policyBuilder(2,true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK,160.00);
        person = Person.builder().name("Maria").age(22).nationality("romana").policy(List.of(policyOb)).build();
        person1 = Person.builder().name("Mincu Ion").age(30).nationality("americana").policy(List.of(policyOb)).build();
        person2 = Person.builder().name("Vlad Minculescu").age(25).nationality("franceza").policy(List.of(policyOb)).build();
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        personList = List.of();
        personDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        personDto = new PersonDto();
    }

    @Test
    public void listAllPersons_whenGetMethod() throws Exception {
        personList = List.of(person, person1, person2);
        personDtos = personMapper.toDtoList(personList);

        given(personService
                .findAll())
                .willReturn(personList);

        logger.info(personList.toString());
        MvcResult mvcResult = mockMvc.perform(get("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is(person.getName())))
                .andReturn();

        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(objectMapper.writeValueAsString(personDtos));
    }

    @Test
    public void listPersonById_whenGetMethod() throws Exception {
        Person person = Setup.personBuilder("Ana Maria", 20, "romana");
        person.setId(1);

        personDto = personMapper.toDto(person);
        given(personService.findById(person.getId())).willReturn(Optional.of(person));

        mockMvc.perform(get("/api/person/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(person.getName())));
    }

    @Test
    public void getPersons_NoPersonsReturn() throws Exception {
        when(personService.findAll()).thenReturn(personList);

        mockMvc.perform(get("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
