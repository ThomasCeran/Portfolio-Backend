package com.portfolio.backend.service;

/**
 * Minimal Twilio client abstraction to allow mocking in tests.
 */
public interface TwilioClient {

    void sendSms(String to, String from, String body);
}
