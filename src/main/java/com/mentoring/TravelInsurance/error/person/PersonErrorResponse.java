package com.mentoring.TravelInsurance.error.person;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonErrorResponse {

    private HttpStatus status;
    private String message;
    private long timestamp;
    private List<String> errors;

}
