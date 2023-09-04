package com.mentoring.TravelInsurance.mappers;

import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.model.Policy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    @Mapping(target = "id", source = "policy.id")
    PolicyDto toDto(Policy policy);

    @Mapping(target = "id", source = "policyDto.id")
    @InheritInverseConfiguration
    Policy toEntity(PolicyDto policyDto);
    List<PolicyDto> toDtoList(List<Policy> policyList);
}
