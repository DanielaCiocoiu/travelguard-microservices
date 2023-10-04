package com.mentoring.TravelInsurance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Column(name = "eligibility")
    private int eligibility;

    @Column(name = "validity")
    private boolean validity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = "benefitAndLimit")
    private BenefitAndLimit benefitAndLimit;

    @Positive
    @Column(name = "price")
    private double price;

    @Column(name = "issue_date")
    private LocalDateTime issueDate;

    @Column(name = "last_day_coverage_period")
    private LocalDateTime lastDayCoveragePeriod;

    @OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    @JsonIgnore
    @JsonProperty("Person list: ")
    private List<Person> personList;

    @ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumn(name="person_id", nullable=false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    @JsonProperty("This policy was created for person with:  ")
    private Person createdBy;

    public Policy(int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit, double price, LocalDateTime issueDate, LocalDateTime lastDayCoveragePeriod, List<Person> personList, Person createdBy) {
        this.eligibility = eligibility;
        this.validity = validity;
        this.type = type;
        this.benefitAndLimit = benefitAndLimit;
        this.price = price;
        this.issueDate = issueDate;
        this.lastDayCoveragePeriod = lastDayCoveragePeriod;
        this.personList = personList;
        this.createdBy = createdBy;
    }

    public Policy(Integer id, int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit, double price, LocalDateTime issueDate, LocalDateTime lastDayCoveragePeriod, List<Person> personList, Person createdBy) {
        this.id = id;
        this.eligibility = eligibility;
        this.validity = validity;
        this.type = type;
        this.benefitAndLimit = benefitAndLimit;
        this.price = price;
        this.issueDate = issueDate;
        this.lastDayCoveragePeriod = lastDayCoveragePeriod;
        this.personList = personList;
        this.createdBy = createdBy;
    }
    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    public Policy() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return eligibility == policy.eligibility && validity == policy.validity && Double.compare(policy.price, price) == 0 && Objects.equals(id, policy.id) && type == policy.type && benefitAndLimit == policy.benefitAndLimit && Objects.equals(issueDate, policy.issueDate) && Objects.equals(lastDayCoveragePeriod, policy.lastDayCoveragePeriod) && Objects.equals(personList, policy.personList) && Objects.equals(createdBy, policy.createdBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, eligibility, validity, type, benefitAndLimit, price, issueDate, lastDayCoveragePeriod, personList, createdBy);
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", eligibility=" + eligibility +
                ", validity=" + validity +
                ", type=" + type +
                ", benefitAndLimit=" + benefitAndLimit +
                ", price=" + price +
                ", issueDate=" + issueDate +
                ", lastDayCoveragePeriod=" + lastDayCoveragePeriod +
                ", personList=" + personList +
                ", createdBy=" + createdBy +
                '}';
    }
}
