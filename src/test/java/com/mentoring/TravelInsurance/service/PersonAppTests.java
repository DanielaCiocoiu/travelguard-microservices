package com.mentoring.TravelInsurance.service;

import com.mentoring.TravelInsurance.controller.PersonController;
import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;
import com.mentoring.TravelInsurance.service.person.PersonService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonAppTests {
    private static final Logger logger
            = LoggerFactory.getLogger(PersonAppTests.class);
    @Autowired
    private PersonController personController;
    @Autowired
    private PersonService personService;

    List<Policy> policiesList;
    List<Person> personList;
    private static Policy policyOb, policyOb1, policyOb2;
    private static Person person, person1, person2;

    @BeforeAll
    static void initBeforeAll() {
        policyOb = Setup.policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0);
        policyOb1 = Setup.policyBuilder(2, true, Type.GROUP_TRAVEL_INSURANCE, BenefitAndLimit.SPORTS, 160.00);
        policyOb2 = Setup.policyBuilder(3, true, Type.BUSINESS_TRAVEL_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK, 170.00);
        person = Setup.personBuilder("Maria", 22, "romanian", List.of(policyOb));
        person1 = Setup.personBuilder("Irina", 50, "american", List.of(policyOb1));
        person2 = Setup.personBuilder("Iona", 22, "romanian", List.of(policyOb2));
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        policiesList = new ArrayList<>();
        personList = new ArrayList<>();
    }

    @Test
    void contextLoads() {
        logger.info("Example log from {}", PersonAppTests.class.getSimpleName());
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
        Optional<Person> actual = personService.findById(person.getId());
        assertEquals(person.getId(), actual.orElseThrow().getId());
    }

    @Test
    @Order(1)
    @Rollback(value = false)
    public void savePerson() {
        Person person = person1;
        personService.save(person);
        Assertions.assertThat(person.getId()).isGreaterThan(0);
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    public void updatePersonTest() {
        person.setName("Irina");
        personService.save(person);
        Optional<Person> personUpdated = personService.findById(person.getId());
        Assertions.assertThat(personUpdated.get().getName()).isEqualTo("Irina");
    }
    @Test
    public void testDeleteById() {
        Person person = person1;
        person.setId(1);
        Optional<Person> actual = personService.findById(person.getId());
        if (actual.isPresent()) {
            personService.deleteById(person.getId());
            assertEquals(0, actual.stream().count());
        }
    }
}
