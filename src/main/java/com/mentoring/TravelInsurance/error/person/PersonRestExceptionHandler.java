package com.mentoring.TravelInsurance.error.person;

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
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class PersonRestExceptionHandler extends ResponseEntityExceptionHandler {

    private  static final Logger logger = LoggerFactory.getLogger(PersonRestExceptionHandler.class);

    //404
    @ExceptionHandler(value = { EntityNotFoundException.class, ResourceNotFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException exc) {
        logger.info(exc.getClass().getName());
        PersonErrorResponse errorResponse = new PersonErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.NOT_FOUND.value()));
        errorResponse.setMessage(exc.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    // 400
    @ExceptionHandler( DataIntegrityViolationException.class)
    @ResponseStatus(value=HttpStatus.BAD_REQUEST,
            reason="Data integrity violation")
    public ResponseEntity<PersonErrorResponse> handleBadRequest(final DataIntegrityViolationException ex) {
        PersonErrorResponse errorResponse = new PersonErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse,new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
    // 409
    @ExceptionHandler({ InvalidDataAccessApiUsageException.class, DataAccessException.class })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<PersonErrorResponse> handleConflict(final RuntimeException ex) {
        PersonErrorResponse errorResponse = new PersonErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse,new HttpHeaders(), HttpStatus.CONFLICT);
    }

    // 500
    @ExceptionHandler({ NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class })
   public ResponseEntity<PersonErrorResponse> handleInternal(final RuntimeException ex) {
        logger.error("500 Status Code", ex);
        PersonErrorResponse errorResponse = new PersonErrorResponse();
        errorResponse.setStatus(HttpStatus.valueOf(HttpStatus.CONFLICT.value()));
        errorResponse.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
