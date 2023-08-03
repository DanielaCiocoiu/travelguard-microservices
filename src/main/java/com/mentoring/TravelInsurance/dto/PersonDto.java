package com.mentoring.TravelInsurance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private int id;
    private String name;
    private Integer age;
    private String nationality;
    private List<PolicyDto> policy;
}
