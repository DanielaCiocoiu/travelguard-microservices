package com.mentoring.TravelInsurance.error.person;

public class PersonWrongAgeException extends RuntimeException {

    public PersonWrongAgeException(String s) {
        super("Wrong age");
    }
}
