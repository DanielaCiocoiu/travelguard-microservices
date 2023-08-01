package com.mentoring.TravelInsurance.mappers;

import com.mentoring.TravelInsurance.dto.PolicyDto;
import com.mentoring.TravelInsurance.model.Policy;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface PolicyMapper {

    PolicyMapper INSTANCE = Mappers.getMapper(PolicyMapper.class);
    PolicyDto toDto(Policy policy);

    Policy toEntity(PolicyDto policyDto);

    List<PolicyDto> toDtoList(List<Policy> policyList);
}
