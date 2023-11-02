package com.mentoring.mappers;

import com.mentoring.dto.PersonDto;
import com.mentoring.model.Person;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto toDto(Person person);
    @InheritInverseConfiguration
    Person toEntity(PersonDto personDto);
    List<PersonDto> toDtoList(List<Person> personList);
}
