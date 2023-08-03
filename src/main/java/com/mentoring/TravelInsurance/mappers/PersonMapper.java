package com.mentoring.TravelInsurance.mappers;

import com.mentoring.TravelInsurance.dto.PersonDto;
import com.mentoring.TravelInsurance.model.Person;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PersonMapper {

    @Mapping(target = "id", source = "person.id")
    PersonDto toDto(Person person);

    @Mapping(target = "id", source = "personDto.id")
    @InheritInverseConfiguration
    Person toEntity(PersonDto personDto);

    List<PersonDto> toDtoList(List<Person> personList);


}
