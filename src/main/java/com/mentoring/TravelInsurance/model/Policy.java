package com.mentoring.TravelInsurance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.util.List;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

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

    @OneToMany(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER, orphanRemoval=true)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private List<Person> personList;

    public Policy(int id, int eligibility, boolean validity, Type type, BenefitAndLimit benefitAndLimit, double price, List<Person> personList) {
        this.id = id;
        this.eligibility = eligibility;
        this.validity = validity;
        this.type = type;
        this.benefitAndLimit = benefitAndLimit;
        this.price = price;
        this.personList = personList;
    }

    public Policy() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return eligibility == policy.eligibility && validity == policy.validity && Double.compare(policy.price, price) == 0 && type == policy.type && benefitAndLimit == policy.benefitAndLimit && Objects.equals(personList, policy.personList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eligibility, validity, type, benefitAndLimit, price, personList);
    }

    @Override
    public String toString() {
        return "Policy {" +
                "id= " + id +
                ", eligibility= " + eligibility +
                ", validity= " + validity +
                ", type= " + type +
                ", benefitAndLimit= " + benefitAndLimit +
                ", price= " + price +
                ", personList= " + personList +
                '}';
    }
}
