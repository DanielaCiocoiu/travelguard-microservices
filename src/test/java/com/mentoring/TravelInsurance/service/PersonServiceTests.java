package com.mentoring.TravelInsurance.service;

import com.mentoring.TravelInsurance.controller.PersonController;
import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.dao.PersonRepository;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonServiceTests {
    private static final Logger logger
            = LoggerFactory.getLogger(PersonServiceTests.class);
    @Autowired
    private PersonController personController;
    @Autowired
    private PersonService personService;
    @Mock
    private PersonRepository personRepository;
    List<Policy> policiesList;
    List<Person> personList;
    private static Policy policyOb, policyOb1, policyOb2;
    private static Person person, person1, person2;

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
        policiesList = new ArrayList<>();
        personList = new ArrayList<>();
    }

    @Test
    void contextLoads() {
        logger.info("Example log from {}", PersonServiceTests.class.getSimpleName());
        Assertions.assertThat(personController).isNotNull();
    }

    @Test
    @Order(3)
    public void getAllPersons() {
        personList = personService.findAll();
        personList.forEach((person -> logger.info("person id: " + person.getId())));
        assertEquals(15, personList.size());
    }

    @Test
    @Order(2)
    public void getPersonById() {
        person.setId(0);
        personService.save(person);
        Person actual = personService.findById(person.getId());
        assertEquals(person.getId(), actual.getId());
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    public void savePerson() {
        Person person = person1;
        var result = personService.save(person);
        Assertions.assertThat(person.getId()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updatePersonTest() {
        person.setName("Irina");
        personService.save(person);
        Person personUpdated = personService.findById(person.getId());
        Assertions.assertThat(personUpdated.getName()).isEqualTo("Irina");
    }

    @Test
    void deleteEmployee() {
        Person person21 = person2;
        person21.setId(13);

        personService.save(person21);
        personService.deleteById(person21.getId());

        List<Person> result = new ArrayList<>();
        personService.findAll().forEach(e -> result.add(e));
        logger.info("Number of persons after deletion: " + (long) result.size());
        assertEquals(result.size(), 14);
    }

    @Test
    public void testExportExcel() {
        personList = List.of(person, person1, person2);
        Mockito.when(personRepository.findAll()).thenReturn(personList);
        ByteArrayInputStream in = personService.exportExcel();
        assertNotNull(in);
    }

    @Test
    public void testGetPersonsInPages() {
        Pageable pageable = PageRequest.of(0, 10);
        personList = List.of(person, person1, person2);
        Page<Person> page = new PageImpl<>(personList, pageable, personList.size());
        Mockito.when(personRepository.findAll(pageable)).thenReturn(page);
        Iterable<Person> result = personService.getPersonsInPages(pageable);
        assertNotNull(result);
    }
}
