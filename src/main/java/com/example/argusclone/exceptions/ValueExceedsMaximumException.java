package com.example.argusclone.exceptions;

public class ValueExceedsMaximumException extends RuntimeException {
    public ValueExceedsMaximumException(String message) {
        super(message);
    }
}
