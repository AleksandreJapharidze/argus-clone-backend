package com.example.argusclone.exceptions;

public class HttpClientErrorException extends RuntimeException {
    public HttpClientErrorException(String message) {
        super(message);
    }
}
