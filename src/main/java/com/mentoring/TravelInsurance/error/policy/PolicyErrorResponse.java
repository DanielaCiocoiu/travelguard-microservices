package com.mentoring.TravelInsurance.error.policy;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyErrorResponse {

    private HttpStatus status;
    private String message;
    private long timestamp;
    private List<String> errors;

    public PolicyErrorResponse(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
