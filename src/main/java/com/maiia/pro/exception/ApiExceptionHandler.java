package com.maiia.pro.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    /**
     * This method is used to handle any ApiRequestException
     * that will be thrown in the controllers
     *
     * @param e : The instance of exception that will be caught
     * @return  : ResponseEntity object which will b the response for the client
     */
    @ExceptionHandler(ApiRequestException.class)
    public ResponseEntity<Object> handleApiRequestException(ApiRequestException e){
        // Create payload that will be sent inside the response entity
        ApiException apiException = new ApiException(e.getMessage(),
                HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
    }
}