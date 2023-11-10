package com.mentoring.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mentoring.model.BenefitAndLimit;
import com.mentoring.model.Person;
import com.mentoring.model.Type;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;


public class PolicyDto {

    private Integer policyId;
    private int eligibility;
    private boolean validity;
    private Type type;
    private BenefitAndLimit benefitAndLimit;
    private double price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime issueDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastDayCoveragePeriod;
    @JsonSerialize
    @JsonIgnore
    private List<Person> personList;

    @JsonProperty("This policy was created for person with: ")
    private Person createdBy;
    @JsonIgnore
    private  Integer personId;

    public PolicyDto() {
    }

    public PolicyDto(Integer policyId, int eligibility, boolean validity, Type type,
                     BenefitAndLimit benefitAndLimit, double price, LocalDateTime issueDate, LocalDateTime lastDayCoveragePeriod, List<Person> personList, Person createdBy, Integer personId) {

        this.policyId = policyId;
        this.eligibility = eligibility;
        this.validity = validity;
        this.type = type;
        this.benefitAndLimit = benefitAndLimit;
        this.price = price;
        this.issueDate = issueDate;
        this.lastDayCoveragePeriod = lastDayCoveragePeriod;
        this.personList = personList;
        this.createdBy = createdBy;
        this.personId = personId;
    }

    public PolicyDto(int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit, double price, LocalDateTime issueDate, LocalDateTime lastDayCoveragePeriod, List<Person> personList, Person createdBy, Integer personId) {
        this.eligibility = eligibility;
        this.validity = validity;
        this.type = type;
        this.benefitAndLimit = benefitAndLimit;
        this.price = price;
        this.issueDate = issueDate;
        this.lastDayCoveragePeriod = lastDayCoveragePeriod;
        this.personList = personList;
        this.createdBy = createdBy;
        this.personId = personId;
    }

    public Integer getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
    }

    public int getEligibility() {
        return eligibility;
    }

    public void setEligibility(int eligibility) {
        this.eligibility = eligibility;
    }

    public boolean isValidity() {
        return validity;
    }

    public void setValidity(boolean validity) {
        this.validity = validity;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BenefitAndLimit getBenefitAndLimit() {
        return benefitAndLimit;
    }

    public void setBenefitAndLimit(BenefitAndLimit benefitAndLimit) {
        this.benefitAndLimit = benefitAndLimit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDateTime issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateTime getLastDayCoveragePeriod() {
        return lastDayCoveragePeriod;
    }

    public void setLastDayCoveragePeriod(LocalDateTime lastDayCoveragePeriod) {
        this.lastDayCoveragePeriod = lastDayCoveragePeriod;
    }

    public List<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(List<Person> personList) {
        this.personList = personList;
    }

    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

}
