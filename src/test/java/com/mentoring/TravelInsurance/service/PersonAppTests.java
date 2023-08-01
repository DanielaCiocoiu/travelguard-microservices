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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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

    @Mock
    private PersonRepository personRepository;
    List<Policy> policiesList;
    List<Person> personList;
    private static Policy policyOb;
    private static Person person, person1, person2;

    @BeforeAll
    static void initBeforeAll() {
        policyOb = Setup.policyBuilder(2,true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.HAZARDOUS_WORK,160.00);
        person = Person.builder().name("Maria").age(22).policy(List.of(policyOb)).build();
        person1 = Person.builder().name("Mincu Ion").age(30).policy(List.of(policyOb)).build();
        person2 = Person.builder().name("Vlad Minculescu").age(25).policy(List.of(policyOb)).build();
    }

    @BeforeEach
    public void beforeEachTestExecution() {
        policiesList = new ArrayList<>();
        personList = new ArrayList<>();
    }

    @Test
    void contextLoads() throws Exception {

        logger.info("Example log from {}", PersonAppTests.class.getSimpleName());
        Assertions.assertThat(personController).isNotNull();
    }

    @Test
    @Order(3)
    public void getAllPersons() {
        personList = personService.findAll();
        personList.forEach((person -> {
            logger.info("person id: " + person.getId());
        }));
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
        Person person = Setup.personBuilder( "Ioana Vasile", 45);
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
    @Order(5)
    @Rollback(value = false)
    public void deletePersonTest() {
        Person person = Setup.personBuilder( "Ioana Vasile", 45);
        Person deletedPerson = personService.deleteById(person.getId());
        assertThat(deletedPerson).isNull();
    }

}
