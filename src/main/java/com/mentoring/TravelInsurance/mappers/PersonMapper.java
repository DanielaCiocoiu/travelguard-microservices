package com.mentoring.TravelInsurance.mappers;

import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.model.Person;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    PersonDto toDto(Person person);
    Person toEntity(PersonDto personDto);
    List<PersonDto> toDtoList(List<Person> personList);
}
