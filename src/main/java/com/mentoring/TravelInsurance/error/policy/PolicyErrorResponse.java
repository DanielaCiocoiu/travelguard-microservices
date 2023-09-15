package com.mentoring.TravelInsurance.error.policy;


import org.springframework.http.HttpStatus;


public class PolicyErrorResponse {

    private HttpStatus status;
    private String message;

    public PolicyErrorResponse() {
    }

    public PolicyErrorResponse(HttpStatus status, String message ) {
        super();
        this.status = status;
        this.message = message;

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
        return "PolicyErrorResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                '}';
    }
}
