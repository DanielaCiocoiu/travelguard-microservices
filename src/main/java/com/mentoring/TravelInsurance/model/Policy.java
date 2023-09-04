package com.mentoring.TravelInsurance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter
@ToString
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

    @OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true, fetch=FetchType.EAGER)
    @JoinColumn(name = "id")
    @JsonIgnore
    private List<Person> personList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return eligibility == policy.eligibility && validity == policy.validity && Double.compare(policy.price, price)
                == 0 && type == policy.type && benefitAndLimit == policy.benefitAndLimit && Objects.equals(personList, policy.personList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eligibility, validity, type, benefitAndLimit, price, personList);
    }
}
