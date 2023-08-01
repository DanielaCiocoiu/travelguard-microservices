package com.mentoring.TravelInsurance.error.policy;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PolicyRestExceptionHandler {


    @ExceptionHandler
    public ResponseEntity<PolicyErrorResponse> handleException(PolicyNotFoundException exc) {
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    //handler de exceptie generic, pt orice tip de ex
    @ExceptionHandler
    public ResponseEntity<PolicyErrorResponse> handleException(Exception exc) {

        PolicyErrorResponse errorResponse = new PolicyErrorResponse();

        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
