package com.smartlease.smartlease_backend.exception;

import com.sun.jdi.request.DuplicateRequestException;

public class DuplicateResourceException extends RuntimeException{
    public DuplicateResourceException(String message) {
        super(message);
    }
}
