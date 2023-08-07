package com.mentoring.TravelInsurance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Policy policy = (Policy) o;
        return eligibility == policy.eligibility && validity == policy.validity && Double.compare(policy.price, price) == 0 && type == policy.type && benefitAndLimit == policy.benefitAndLimit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eligibility, validity, type, benefitAndLimit, price);
    }
}
