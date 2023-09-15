package com.mentoring.TravelInsurance.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "persons")
@Builder(toBuilder = true)
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

    public Person(int id, String name, Integer age, String nationality) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.nationality = nationality;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
        return Objects.equals(name, person.name) && Objects.equals(age, person.age) && Objects.equals(nationality, person.nationality);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "Person{ " +
                "id= " + id +
                ", name=' " + name + '\'' +
                ", age= " + age +
                ", nationality=' " + nationality + '\'' +
                '}';
    }
}
