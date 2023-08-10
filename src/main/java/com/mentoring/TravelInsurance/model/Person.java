package com.mentoring.TravelInsurance.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "persons")
@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private int id;

    @NotBlank
    @Size(max = 20, message = "First name should have with maximum length of 200 characters")
    @Column(name = "name")
    private String name;

    @Positive
    @NotNull(message = "Age must not be null")
    @Min(value = 18, message = "Age must be greater than or equal to 18")
    @Max(value = 60, message = "Age must be less than or equal to 60")
    @Column(name = "age")
    private Integer age;

    @Column(name = "nationality")
    private String nationality;

    @OneToMany(cascade = CascadeType.ALL,  orphanRemoval = true, fetch=FetchType.EAGER)
   @JoinColumn(name = "id")
    @JsonIgnore
    private List<Policy> policyList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(nationality, person.nationality) && Objects.equals(policyList, person.policyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, nationality, policyList);
    }
}
