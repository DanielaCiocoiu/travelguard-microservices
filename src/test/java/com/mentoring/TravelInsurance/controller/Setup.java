package com.mentoring.TravelInsurance.controller;

import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Person;
import com.mentoring.TravelInsurance.model.Policy;
import com.mentoring.TravelInsurance.model.Type;

import java.util.List;

public class Setup {

    public static Person personBuilder(String name, int age){
        return  Person.builder()
                .id(0)
                .name(name)
                .age(age)
                .policy(List.of(policyBuilder(1, true, Type.MISSIONARY_INSURANCE, BenefitAndLimit.LAISURE_ACTIVITIES, 150.0)))
                .build();
    }

    public static Policy policyBuilder(int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit, double price ) {
        return Policy.builder()
                .id(0)
                .eligibility(eligibility)
                .validity(validity)
                .type(type)
                .benefitAndLimit(benefitAndLimit)
                .price(price)
                .build();
    }
}
