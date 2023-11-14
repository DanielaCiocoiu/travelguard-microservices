package com.mentoring.mappers;

import com.mentoring.dto.PersonDto;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2023-11-14T11:11:05+0200",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.8.1 (Amazon.com Inc.)"
)
@Component
public class PersonMapperImpl implements PersonMapper {

    @Override
    public PersonDto toDto(Person person) {
        if ( person == null ) {
            return null;
        }

        PersonDto personDto = new PersonDto();

        personDto.setId( person.getId() );
        personDto.setName( person.getName() );
        personDto.setAge( person.getAge() );
        personDto.setNationality( person.getNationality() );
        List<Policy> list = person.getPolicyList();
        if ( list != null ) {
            personDto.setPolicyList( new ArrayList<Policy>( list ) );
        }

        return personDto;
    }

    @Override
    public Person toEntity(PersonDto personDto) {
        if ( personDto == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        person.id( personDto.getId() );
        person.name( personDto.getName() );
        person.age( personDto.getAge() );
        person.nationality( personDto.getNationality() );
        List<Policy> list = personDto.getPolicyList();
        if ( list != null ) {
            person.policyList( new ArrayList<Policy>( list ) );
        }

        return person.build();
    }

    @Override
    public List<PersonDto> toDtoList(List<Person> personList) {
        if ( personList == null ) {
            return null;
        }

        List<PersonDto> list = new ArrayList<PersonDto>( personList.size() );
        for ( Person person : personList ) {
            list.add( toDto( person ) );
        }

        return list;
    }
}
