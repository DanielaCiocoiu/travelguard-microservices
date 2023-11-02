package com.mentoring.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mentoring.model.Policy;

import java.util.List;


public class PersonDto {

    private Integer id;
    private String name;
    private Integer age;
    private String nationality;
    @JsonSerialize
    @JsonProperty("Policies list for each person: ")
    private List<Policy> policyList;

    public PersonDto(Integer id, String name, Integer age, String nationality, List<Policy> policyList) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.policyList = policyList;
    }


    public PersonDto(String name, Integer age, String nationality, List<Policy> policyList) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.policyList = policyList;
    }

    public PersonDto() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Policy> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<Policy> policyList) {
        this.policyList = policyList;
    }

   }
