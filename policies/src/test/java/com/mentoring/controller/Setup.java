package com.mentoring.controller;

import com.mentoring.model.BenefitAndLimit;
import com.mentoring.model.Person;
import com.mentoring.model.Policy;
import com.mentoring.model.Type;

import java.time.LocalDateTime;
import java.util.List;

public class Setup {

    public static Person personBuilder(String name, int age, String nationality, List<Policy> policyList){
        return  Person.builder()
                .id(0)
                .name(name)
                .age(age)
                .nationality(nationality)
                .policyList(policyList)
                .build();
    }

    public static Policy policyBuilder(int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit,
                                       double price, LocalDateTime localDateTime, LocalDateTime localDateTime1, List<Person> person ) {

        return Policy.builder()
                .id(0)
                .eligibility(eligibility)
                .validity(validity)
                .type(type)
                .benefitAndLimit(benefitAndLimit)
                .price(price)
                .issueDate(localDateTime)
                .lastDayCoveragePeriod(localDateTime1)
                .personList(person)
                .createdBy(Person.builder().build())
                .build();
    }
}
