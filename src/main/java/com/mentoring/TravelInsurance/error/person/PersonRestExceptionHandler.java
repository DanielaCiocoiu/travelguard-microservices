package com.mentoring.TravelInsurance.error.person;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PersonRestExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exc) {
        PersonErrorResponse errorResponse = new PersonErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //handler de exceptie generic, pt orice tip de ex
    @ExceptionHandler
    public ResponseEntity<PersonErrorResponse> handleException(Exception exc) {

        PersonErrorResponse errorResponse = new PersonErrorResponse();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
