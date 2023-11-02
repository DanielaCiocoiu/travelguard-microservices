package com.mentoring.mappers;

import com.mentoring.dto.PolicyDto;
import com.mentoring.model.Policy;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    @Mapping(target = "policyId", source = "policy.id")
    @Mapping(target = "personId", source = "createdBy.id")
    PolicyDto toDto(Policy policy);

    @Mapping(target = "id", source = "dto.policyId")
    @Mapping(target = "createdBy.id", source = "personId")
    @InheritInverseConfiguration
    Policy toEntity(PolicyDto dto);

    List<PolicyDto> toDtoList(List<Policy> policyList);
}
