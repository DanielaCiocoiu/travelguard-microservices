package com.mentoring.mappers;

import com.mentoring.dto.PolicyDto;
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
public class PolicyMapperImpl implements PolicyMapper {

    @Override
    public PolicyDto toDto(Policy policy) {
        if ( policy == null ) {
            return null;
        }

        PolicyDto policyDto = new PolicyDto();

        policyDto.setPolicyId( policy.getId() );
        policyDto.setPersonId( policyCreatedById( policy ) );
        policyDto.setEligibility( policy.getEligibility() );
        policyDto.setValidity( policy.isValidity() );
        policyDto.setType( policy.getType() );
        policyDto.setBenefitAndLimit( policy.getBenefitAndLimit() );
        policyDto.setPrice( policy.getPrice() );
        policyDto.setIssueDate( policy.getIssueDate() );
        policyDto.setLastDayCoveragePeriod( policy.getLastDayCoveragePeriod() );
        List<Person> list = policy.getPersonList();
        if ( list != null ) {
            policyDto.setPersonList( new ArrayList<Person>( list ) );
        }
        policyDto.setCreatedBy( policy.getCreatedBy() );

        return policyDto;
    }

    @Override
    public Policy toEntity(PolicyDto dto) {
        if ( dto == null ) {
            return null;
        }

        Policy.PolicyBuilder policy = Policy.builder();

        policy.createdBy( policyDtoToPerson( dto ) );
        policy.id( dto.getPolicyId() );
        policy.eligibility( dto.getEligibility() );
        policy.validity( dto.isValidity() );
        policy.type( dto.getType() );
        policy.benefitAndLimit( dto.getBenefitAndLimit() );
        policy.price( dto.getPrice() );
        policy.issueDate( dto.getIssueDate() );
        policy.lastDayCoveragePeriod( dto.getLastDayCoveragePeriod() );
        List<Person> list = dto.getPersonList();
        if ( list != null ) {
            policy.personList( new ArrayList<Person>( list ) );
        }

        return policy.build();
    }

    @Override
    public List<PolicyDto> toDtoList(List<Policy> policyList) {
        if ( policyList == null ) {
            return null;
        }

        List<PolicyDto> list = new ArrayList<PolicyDto>( policyList.size() );
        for ( Policy policy : policyList ) {
            list.add( toDto( policy ) );
        }

        return list;
    }

    private Integer policyCreatedById(Policy policy) {
        if ( policy == null ) {
            return null;
        }
        Person createdBy = policy.getCreatedBy();
        if ( createdBy == null ) {
            return null;
        }
        Integer id = createdBy.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }

    protected Person policyDtoToPerson(PolicyDto policyDto) {
        if ( policyDto == null ) {
            return null;
        }

        Person.PersonBuilder person = Person.builder();

        person.id( policyDto.getPersonId() );

        return person.build();
    }
}
