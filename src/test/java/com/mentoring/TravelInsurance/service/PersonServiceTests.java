package com.mentoring.TravelInsurance.service;

import com.mentoring.TravelInsurance.controller.Setup;
import com.mentoring.TravelInsurance.mappers.PersonMapperImpl;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.repository.PersonRepository;
import com.mentoring.TravelInsurance.service.person.PersonServiceIml;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {
    @InjectMocks
    private PersonServiceIml personServiceIml;
    @Mock
    private PersonRepository personRepository;

    @SpyBean
    private PersonMapperImpl personMapper;
    List<Person> personList;
    private static Person person, person1, person2;

    @BeforeAll
    static void initBeforeAll() {
        person = Setup.personBuilder("Maria", 22, "romanian");
        person1 = Setup.personBuilder("Irina", 50, "american");
        person2 = Setup.personBuilder("Iona", 22, "romanian");
     }
    @BeforeEach
    public void beforeEachTestExecution() {
        personList = new ArrayList<>();
    }
    @Test
    public void shouldReturnAllPersons() {
        personList = List.of(person, person1, person2);
        given(personRepository.findAll()).willReturn(personList);
        List <Person> expected = personServiceIml.findAll();
        assertEquals(expected, personList);
        verify(personRepository, times(1)).findAll();
    }
    @Test
    void whenGetAllPersonsReturnEmptyList() {
        lenient().when(personRepository.findAll()).thenReturn(personList);
        assertThat(personServiceIml.findAll()).isEqualTo(personList);
        verify(personRepository, times(1)).findAll();
    }
    @Test
    public void whenSavePerson_shouldReturnPerson() {
        Person person = person1;
        when(personRepository.save(ArgumentMatchers.any(Person.class))).thenReturn(person);
        Person created = personServiceIml.save(person);
        assertThat(created.getId()).isSameAs(person.getId());
        verify(personRepository).save(person);
    }
    @Test
    public void whenGivenId_shouldDeletePerson_ifFound(){
        var person = person2;
        person.setId(2);
        lenient().when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        personServiceIml.deleteById(person.getId());
        verify(personRepository).findById(person.getId());
    }
    @Test
    public void whenUpdatePerson_shouldUpdatePerson_ifFound() {
        var person = person1;
        person.setId(2);
        lenient().when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
        personServiceIml.save(person);
        verify(personRepository).save(person);
    }

    @Test
    public void testGetPersonsInPages() {
        Pageable pageable = PageRequest.of(0, 10);
        personList = List.of(person, person1, person2);
        Page<Person> page = new PageImpl<>(personList, pageable, personList.size());
        Mockito.when(personRepository.findAll(pageable)).thenReturn(page);
        Iterable<Person> result = personServiceIml.getPersonsInPages(pageable);
        assertNotNull(result);
    }
}
