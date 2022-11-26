package com.example.canya.exceptionHandler;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RestApiException {

    private String errorMessage;
    private org.springframework.http.HttpStatus HttpStatus;

}