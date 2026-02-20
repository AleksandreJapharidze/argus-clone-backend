package com.example.argusclone.exceptions;

public class OperationNotAllowedYetException extends RuntimeException {
    public OperationNotAllowedYetException(String message) {
        super(message);
    }
}
