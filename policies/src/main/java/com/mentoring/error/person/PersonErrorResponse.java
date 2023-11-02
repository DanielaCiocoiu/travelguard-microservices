package com.mentoring.error.person;


import org.springframework.http.HttpStatus;


public class PersonErrorResponse {

    private HttpStatus status;
    private String message;

    public PersonErrorResponse(HttpStatus status, String message) {

        this.status = status;
        this.message = message;
    }

    public PersonErrorResponse() {
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PersonErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
