package com.mentoring.TravelInsurance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.mappers.PersonMapperImpl;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.person.PersonExcel;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        policyOb = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0);
        policyOb1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00);
        policyOb2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00);
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

    @Before("")
    public void before() {
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();
        inputStream = personController.getClass().getClassLoader().getResourceAsStream("persons.xlsx");
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
        Person person = person1;
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

    @Test
    public void testDownloadFile() throws Exception {
        List<Person> people = new ArrayList<>();
        people.add(person2);
        when(personService.findAll()).thenReturn(people);

        ByteArrayInputStream in = PersonExcel.tutorialsToExcel(people);
        when(personService.exportExcel()).thenReturn(in);
        MvcResult result = mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/exportToExcel")
                        .contentType(MediaType.APPLICATION_OCTET_STREAM))
                .andExpect(MockMvcResultMatchers.status()
                        .is(200))
                .andReturn();
        assertNotNull(result);
        assertEquals(200, result.getResponse().getStatus());
        assertEquals(3647, result.getResponse().getContentAsByteArray().length);
        assertEquals("application/vnd.ms-excel", result.getResponse().getContentType());
    }
    @Test
    public void testExportExcelIsNull() {
        List<Person> personList = new ArrayList<>();
        Mockito.when(personService.findAll()).thenReturn(personList);
        ByteArrayInputStream in = PersonExcel.tutorialsToExcel(personList);
        assertNotNull(in);
    }
    @Test
    public void testExportExcelFindAllPersons() {
        personList = List.of(person, person1, person2);
        Mockito.when(personService.findAll()).thenReturn(personList);
        ByteArrayInputStream in = PersonExcel.tutorialsToExcel(personList);
        assertEquals(3,personList.size());
    }
}
