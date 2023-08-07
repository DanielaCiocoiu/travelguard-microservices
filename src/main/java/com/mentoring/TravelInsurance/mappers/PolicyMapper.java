package com.mentoring.TravelInsurance.mappers;

import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.model.Policy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    PolicyDto toDto(Policy policy);

    Policy toEntity(PolicyDto policyDto);

    List<PolicyDto> toDtoList(List<Policy> policyList);
}
