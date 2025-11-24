package com.portfolio.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Component
@ConditionalOnClass({Twilio.class, Message.class})
@ConditionalOnProperty(prefix = "notification.sms", name = "enabled", havingValue = "true")
public class TwilioClientImpl implements TwilioClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioClientImpl.class);

    private final boolean initialized;

    public TwilioClientImpl(
            @Value("${notification.sms.account-sid:}") String accountSid,
            @Value("${notification.sms.auth-token:}") String authToken) {
        if (!StringUtils.hasText(accountSid) || !StringUtils.hasText(authToken)) {
            LOGGER.warn("Twilio credentials are missing; SMS notifications will be skipped");
            this.initialized = false;
        } else {
            Twilio.init(accountSid, authToken);
            this.initialized = true;
        }
    }

    @Override
    public void sendSms(String to, String from, String body) {
        if (!initialized) {
            LOGGER.warn("Twilio client not initialized; skipping SMS");
            return;
        }
        try {
            Message.creator(new PhoneNumber(to), new PhoneNumber(from), body).create();
        } catch (ApiException ex) {
            LOGGER.error("Failed to send SMS: {}", ex.getMessage());
        }
    }
}
