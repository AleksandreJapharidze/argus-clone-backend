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
        return ResponseEntity.status(404).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<?> handleDuplicateResourceException(
            DuplicateResourceException exception, WebRequest request
    ) {
        return ResponseEntity.status(409).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(ScheduleConflictException.class)
    public ResponseEntity<?> handleScheduleConflictException(
            ScheduleConflictException exception, WebRequest request
    ) {
        return ResponseEntity.status(409).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(TooManyResourcesException.class)
    public ResponseEntity<?> handleTooManyResourcesException(
            TooManyResourcesException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(ValueExceedsMaximumException.class)
    public ResponseEntity<?> handleValueExceedsMaximumException(
            ValueExceedsMaximumException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(NegativeValueException.class)
    public ResponseEntity<?> handleNegativeValueException(
            NegativeValueException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(OperationNotAllowedYetException.class)
    public ResponseEntity<?> handleOperationNotAllowedYetException(
            OperationNotAllowedYetException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(PrerequisitesNotMetException.class)
    public ResponseEntity<?> handlePrerequisitesNotMetException(
            PrerequisitesNotMetException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?> handleValidationException(
            ValidationException exception, WebRequest request
    ) {
        return ResponseEntity.status(422).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(JwtGenerationException.class)
    public ResponseEntity<?> handleJwtGenerationException(
            JwtGenerationException exception, WebRequest request
    ) {
        return ResponseEntity.status(401).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(AlreadyPresentException.class)
    public ResponseEntity<?> handleAlreadyPresentException(
            AlreadyPresentException exception, WebRequest request
    ) {
        return ResponseEntity.status(400).body(getErrorDetails(exception, request));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgumentException(
            IllegalArgumentException exception, WebRequest request
    ) {
        return ResponseEntity.status(400).body(getErrorDetails(exception, request));
    }

    private Map<String, Object> getErrorDetails(Exception exception, WebRequest request) {
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("message", exception.getMessage());
        errorDetails.put("path", request.getDescription(false));
        return errorDetails;
    }
}
