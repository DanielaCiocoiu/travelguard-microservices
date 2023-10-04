package com.mentoring.TravelInsurance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "persons")
@Builder(toBuilder = true)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Integer id;

    @Size(max = 20, message = "First name should have with maximum length of 200 characters")
    @Column(name = "name")
    private String name;

    @Positive
    @Min(value = 18, message = "Age must be greater than or equal to 18")
    @Max(value = 60, message = "Age must be less than or equal to 60")
    @Column(name = "age")
    private Integer age;

    @Column(name = "nationality")
    private String nationality;

    //kind of compromised solution  for a particular usage when we need to fetch the associated collection for most of our use cases., avoid LazyInitializationException
    @OneToMany(cascade = CascadeType.PERSIST, fetch=FetchType.EAGER, orphanRemoval=true,mappedBy="createdBy")
    private List<Policy> policyList;

    public Person(String name, Integer age, String nationality, List<Policy> policyList) {
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.policyList = policyList;
    }

    public Person(Integer id, String name, Integer age, String nationality, List<Policy> policyList) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
        this.policyList = policyList;
    }

    public Person() {
    }

    public void addPolicy(Policy policy) {
        policyList.add(policy);
        policy.setCreatedBy(this);
    }

    public void removePolicy(Policy policy) {
        policyList.remove(policy);
        policy.setCreatedBy(null);
    }
    public List<Policy> getPolicyList() {
        return policyList;
    }

    public void setPolicyList(List<Policy> policyList) {
        this.policyList = policyList;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) && Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(nationality, person.nationality) && Objects.equals(policyList, person.policyList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, age, nationality, policyList);
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", nationality='" + nationality + '\'' +
                ", policyList=" + policyList +
                '}';
    }
}
