package com.example.argusclone.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(
            ResourceNotFoundException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(404).body(errorDetails);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(
            DuplicateResourceException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(409).body(errorDetails);
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<?> handleScheduleConflictException(
            ScheduleConflictException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(409).body(errorDetails);
    }

    @ExceptionHandler(TooManyResourcesException.class)
    public ResponseEntity<?> handleTooManyResourcesException(
            TooManyResourcesException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(422).body(errorDetails);
    }

    @ExceptionHandler(ValueExceedsMaximumException.class)
    public ResponseEntity<?> handleValueExceedsMaximumException(
            ValueExceedsMaximumException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(422).body(errorDetails);
    }

    @ExceptionHandler(NegativeValueException.class)
    public ResponseEntity<?> handleNegativeValueException(
            NegativeValueException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(422).body(errorDetails);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(
            HttpClientErrorException exception, WebRequest request
    ) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));

        return ResponseEntity.status(400).body(errorDetails);
    }
}
