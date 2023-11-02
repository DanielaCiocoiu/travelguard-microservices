package com.mentoring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.dto.PersonDto;
import com.mentoring.error.person.PersonNotFoundException;
import com.mentoring.mappers.PersonMapperImpl;
import com.mentoring.model.BenefitAndLimit;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import com.mentoring.model.Type;
import com.mentoring.service.person.PersonService;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PersonController.class)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
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
    @Autowired
    private ObjectMapper objectMapper;
    private List<PersonDto> personDtos;
    private PersonDto personDto;

    private static Person person, person1, person2;
    private static Policy policyOb, policyOb1, policyOb2;
    private List<Person> personList;

    @BeforeAll
    static void initBeforeAll() {
        policyOb = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0, null, null, null);
        policyOb1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00, null, null, null);
        policyOb2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00, null, null, null);

        person = Setup.personBuilder("Maria", 22, "romanian", List.of(policyOb, policyOb1, policyOb2));
        person1 = Setup.personBuilder("Irina", 50, "american", List.of(policyOb, policyOb1, policyOb2));
        person2 = Setup.personBuilder("Iona", 22, "romanian", List.of(policyOb, policyOb1, policyOb2));

    }

    @BeforeEach
    public void beforeEachTestExecution() {
        personList = List.of();
        personDtos = new ArrayList<>();
        objectMapper = new ObjectMapper();
        personDto = new PersonDto();
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
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
        verify(personService, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
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
        verify(personService, times(1)).findById(1);
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void whenGetMethodPersonIdNotFound() throws Exception {
        Person person = person1;
        person.setId(1111);
        personDto = personMapper.toDto(person);
        Mockito.doThrow(PersonNotFoundException.class).when(personService).findById(person.getId());
        mockMvc.perform(get("/api/person/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        // .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "Vasia", password = "12345678", roles = {"admin"})
    public void whenGetPersons_NoPersonReturn() throws Exception {
        when(personService.findAll()).thenReturn(personList);
        mockMvc.perform(get("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
        verify(personService, times(1)).findAll();
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testAddPerson() throws Exception {
        Person newOb = person;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newOb))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
       }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void testUpdatePerson() throws Exception {
        Person newOb = person;
        mockMvc.perform(MockMvcRequestBuilders.post("/api/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(newOb))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeletePerson() throws Exception {
        int id = 1;
        Person person = person1;
        person.setId(1);
        personDto = personMapper.toDto(person);
        given(personService.findById(personDto.getId())).willReturn(person);
        mockMvc.perform(delete("/api/person/" + id))
                .andExpect(status().isOk());
        verify(personService, times(1)).findById(1);
        verify(personService, times(1)).deleteById(1);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

 }
