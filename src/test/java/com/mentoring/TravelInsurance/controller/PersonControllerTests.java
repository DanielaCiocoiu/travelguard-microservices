package com.mentoring.TravelInsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.error.person.PersonNotFoundException;
import com.mentoring.TravelInsurance.mappers.PersonMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
public class PersonControllerTests {
    private static final Logger logger
            = LoggerFactory.getLogger(PersonControllerTests.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;
    @SpyBean
    private PersonMapperImpl personMapper;

    @Spy
    @InjectMocks
    private PersonController personController;
    private ObjectMapper objectMapper;
    private List<PersonDto> personDtos;
    private PersonDto personDto;

    private static Person person, person1, person2;
    private static Policy policyOb, policyOb1, policyOb2;
    private List<Person> personList;
    private InputStream inputStream;

    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Maria", 22, "romanian");
        person1 = Setup.personBuilder("Irina", 50, "american");
        person2 = Setup.personBuilder("Iona", 22, "romanian");
        policyOb = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, List.of(person, person1, person2));
        policyOb1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, List.of(person, person1, person2));
        policyOb2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00, List.of(person, person1, person2));
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        personList = List.of();
        personDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        personDto = new PersonDto();
    }

    @Before("")
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        inputStream = personController.getClass().getClassLoader().getResourceAsStream("persons.xlsx");
    }

    @Test
    public void whenGetMethodListAllPersons() throws Exception {
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
    public void whenGetMethodPersonById() throws Exception {
        Person person = person1;
        person.setId(1);
        personDto = personMapper.toDto(person);
        given(personService.findById(personDto.getId())).willReturn(person);
        mockMvc.perform(get("/api/person/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name", is(person.getName())));
    }

    @Test
    public void whenGetMethodPersonIdNotFound() throws Exception {
        Person person = person1;
        person.setId(1);
        personDto = personMapper.toDto(person);
        when(personService.findById(person.getId())).thenThrow(new PersonNotFoundException("Person id not found: " + personDto.getId()));
        mockMvc.perform(get("/api/person/" + person.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void getPersonsNoPersonsReturn() throws Exception {
        when(personService.findAll()).thenReturn(personList);

        mockMvc.perform(get("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
