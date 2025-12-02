package com.example.argusclone.exceptions;

public class TooManyResourcesException extends RuntimeException {
    public TooManyResourcesException(String message) {
        super(message);
    }
}
