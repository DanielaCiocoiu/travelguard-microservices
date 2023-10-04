package com.mentoring.TravelInsurance.error.policy;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}