package com.portfolio.backend.service;

/**
 * Minimal Twilio client abstraction to allow mocking in tests.
 */
public interface TwilioClient {

    /**
     * Send an SMS using either a Messaging Service SID (preferred) or a From number.
     *
     * @param to recipient in E.164
     * @param fromNumber from number in E.164 (ignored if messagingServiceSid is provided)
     * @param messagingServiceSid Twilio messaging service SID (optional)
     * @param body sms body
     */
    void sendSms(String to, String fromNumber, String messagingServiceSid, String body);
}
