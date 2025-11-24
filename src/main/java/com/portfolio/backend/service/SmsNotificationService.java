package com.portfolio.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.portfolio.backend.entity.ContactMessage;
import com.portfolio.backend.service.TwilioClient;

/**
 * Sends an SMS when a new contact message is received.
 * Relies on Twilio credentials provided through environment variables.
 */
@Service
@ConditionalOnProperty(prefix = "notification.sms", name = "enabled", havingValue = "true")
public class SmsNotificationService implements NotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationService.class);

    private final boolean smsEnabled;
    private final String toPhoneNumber;
    private final String fromPhoneNumber;
    private final String messagingServiceSid;
    private final TwilioClient twilioClient;

    public SmsNotificationService(boolean smsEnabled, String toPhoneNumber, String fromPhoneNumber,
            TwilioClient twilioClient) {
        this(smsEnabled, toPhoneNumber, fromPhoneNumber, "", twilioClient);
    }

    public SmsNotificationService(
            @Value("${notification.sms.enabled:false}") boolean smsEnabled,
            @Value("${notification.sms.to-number:}") String toPhoneNumber,
            @Value("${notification.sms.from-number:}") String fromPhoneNumber,
            @Value("${notification.sms.messaging-service-sid:}") String messagingServiceSid,
            TwilioClient twilioClient) {
        this.smsEnabled = smsEnabled;
        this.toPhoneNumber = toPhoneNumber;
        this.fromPhoneNumber = fromPhoneNumber;
        this.messagingServiceSid = messagingServiceSid;
        this.twilioClient = twilioClient;
    }

    @Override
    public void notifyNewContact(ContactMessage message) {
        if (!smsEnabled) {
            return;
        }

        if (!StringUtils.hasText(toPhoneNumber)) {
            LOGGER.warn("SMS notifications enabled but recipient number is missing");
            return;
        }

        if (!StringUtils.hasText(messagingServiceSid) && !StringUtils.hasText(fromPhoneNumber)) {
            LOGGER.warn("SMS notifications enabled but neither messagingServiceSid nor from-number is set");
            return;
        }

        String smsBody = buildSmsBody(message);

        twilioClient.sendSms(toPhoneNumber, fromPhoneNumber, messagingServiceSid, smsBody);
    }

    private String buildSmsBody(ContactMessage message) {
        String trimmedSubject = message.getSubject();
        if (trimmedSubject != null && trimmedSubject.length() > 60) {
            trimmedSubject = trimmedSubject.substring(0, 57) + "...";
        }

        String trimmedContent = message.getMessage();
        if (trimmedContent != null && trimmedContent.length() > 160) {
            trimmedContent = trimmedContent.substring(0, 157) + "...";
        }

        String email = nullToPlaceholder(message.getEmail(), "email?");
        String phone = nullToPlaceholder(message.getPhone(), "tél?");
        String name = nullToPlaceholder(message.getName(), "Inconnu");

        return String.format("Nouveau message: %s\nDe: %s (%s, %s)\nContenu: %s",
                nullToPlaceholder(trimmedSubject, "Sans sujet"),
                name,
                email,
                phone,
                nullToPlaceholder(trimmedContent, "Pas de contenu"));
    }

    private String nullToPlaceholder(String value, String placeholder) {
        return StringUtils.hasText(value) ? value : placeholder;
    }
}
