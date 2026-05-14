package com.portfolio.backend.exception;

public class RecaptchaVerificationException extends RuntimeException {

    public RecaptchaVerificationException(String message) {
        super(message);
    }
}
