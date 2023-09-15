package com.mentoring.TravelInsurance.error.policy;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class PolicyRestExceptionHandler extends ResponseEntityExceptionHandler {
    private  static final Logger logger = LoggerFactory.getLogger(PolicyRestExceptionHandler.class);

    //404
    @ExceptionHandler(value = { EntityNotFoundException.class, ResourceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<PolicyErrorResponse> handleException(PolicyNotFoundException exc) {
        logger.info(exc.getClass().getName());
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setMessage(exc.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    // 400
    @ExceptionHandler( DataIntegrityViolationException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST,
            reason="Data integrity violation")
    public ResponseEntity<PolicyErrorResponse> handleBadRequest(final DataIntegrityViolationException ex) {
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<PolicyErrorResponse> handleConflict(final RuntimeException ex) {
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse,new HttpHeaders(), HttpStatus.CONFLICT);
    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
    /*500*/public ResponseEntity<PolicyErrorResponse> handleInternal(final RuntimeException ex) {
        logger.error("500 Status Code", ex);
        PolicyErrorResponse errorResponse = new PolicyErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
