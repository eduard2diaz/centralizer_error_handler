package com.cloud.centralized_error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import response.ApiError;
import response.Response;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<Response> handleIOExistException(IOException ex) {
        ApiError apiError = new ApiError.Builder()
                .setMessage(ex.getMessage())
                .setException(ex)
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError.Builder()
                .setMessage(ex.getMessage())
                .setException(ex)
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .build();
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Response> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(Response.fail(apiError), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}