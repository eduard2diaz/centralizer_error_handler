package com.cloud.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import response.ApiError;
import response.Response;
import java.io.IOException;

@ControllerAdvice
public class RestExceptionHandler {

    private final ErrorProcessorService errorProcessorService;

    public RestExceptionHandler(ErrorProcessorService errorProcessorService) {
        this.errorProcessorService = errorProcessorService;
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<Response> handleIOExistException(IOException ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                ex.getLocalizedMessage(),
                null);

        errorProcessorService.process(apiError);
        return new ResponseEntity<>(Response.fail(apiError), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleGenericException(Exception ex) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                ex.getLocalizedMessage(),
                null
        );
        errorProcessorService.process(apiError);
        return new ResponseEntity<>(Response.fail(apiError), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}