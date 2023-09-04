package com.mentoring.TravelInsurance.error.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class PolicyRestExceptionHandler {
    private  static final Logger logger = LoggerFactory.getLogger(PolicyRestExceptionHandler.class);
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<PolicyErrorResponse> handleException(PolicyNotFoundException exc) {
        logger.info(exc.getClass().getName());
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        errorResponse.setErrors(List.of());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<PolicyErrorResponse> handleException(Exception exc) {
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setMessage(exc.getMessage());
        errorResponse.setTimestamp(System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
