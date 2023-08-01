package com.mentoring.TravelInsurance.dto;

import com.mentoring.TravelInsurance.model.BenefitAndLimit;
import com.mentoring.TravelInsurance.model.Type;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDto {

    private int id;
    private int eligibility;
    private boolean validity;
    private Type type;
    private BenefitAndLimit benefitAndLimit;
    private double price;
}
