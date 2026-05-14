package com.portfolio.backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.portfolio.backend.entity.ContactMessage;

import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotificationServiceImpl.class);

    private final boolean notificationsEnabled;
    private final String mailHost;
    private final String mailUsername;
    private final String mailPassword;
    private final String mailFrom;
    private final String mailTo;
    private final JavaMailSender mailSender;

    public EmailNotificationServiceImpl(
            @Value("${email.notifications-enabled:${EMAIL_NOTIFICATIONS_ENABLED:false}}") boolean notificationsEnabled,
            @Value("${spring.mail.host:${MAIL_HOST:}}") String mailHost,
            @Value("${spring.mail.username:${MAIL_USERNAME:}}") String mailUsername,
            @Value("${spring.mail.password:${MAIL_PASSWORD:}}") String mailPassword,
            @Value("${mail.from:${MAIL_FROM:}}") String mailFrom,
            @Value("${mail.to:${MAIL_TO:}}") String mailTo,
            JavaMailSender mailSender) {
        this.notificationsEnabled = notificationsEnabled;
        this.mailHost = mailHost;
        this.mailUsername = mailUsername;
        this.mailPassword = mailPassword;
        this.mailFrom = mailFrom;
        this.mailTo = mailTo;
        this.mailSender = mailSender;
    }

    @Override
    public void notifyNewContact(ContactMessage message) {
        if (!notificationsEnabled) {
            return;
        }

        if (!hasRequiredConfiguration()) {
            LOGGER.warn("Email notifications enabled but mail configuration is incomplete");
            return;
        }

        try {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom(mailFrom);
            email.setTo(mailTo);
            email.setSubject("New portfolio contact message from " + valueOrPlaceholder(message.getName(), "Unknown"));
            email.setText(buildEmailBody(message));

            if (isValidEmail(message.getEmail())) {
                email.setReplyTo(message.getEmail());
            }

            mailSender.send(email);
        } catch (MailException ex) {
            LOGGER.warn("Email notification failed: {}", ex.getClass().getSimpleName());
        } catch (Exception ex) {
            LOGGER.warn("Email notification failed: {}", ex.getClass().getSimpleName());
        }
    }

    private boolean hasRequiredConfiguration() {
        return StringUtils.hasText(mailHost)
                && StringUtils.hasText(mailUsername)
                && StringUtils.hasText(mailPassword)
                && StringUtils.hasText(mailFrom)
                && StringUtils.hasText(mailTo);
    }

    private String buildEmailBody(ContactMessage message) {
        StringBuilder body = new StringBuilder()
                .append("New portfolio contact message\n\n")
                .append("Name: ").append(valueOrPlaceholder(message.getName(), "Unknown")).append('\n')
                .append("Email: ").append(valueOrPlaceholder(message.getEmail(), "No email")).append('\n');
        appendOptionalLine(body, "Phone", message.getPhone());
        appendOptionalLine(body, "Subject", message.getSubject());
        body.append('\n')
                .append("Message:\n")
                .append(valueOrPlaceholder(message.getMessage(), "No message"));
        return body.toString();
    }

    private void appendOptionalLine(StringBuilder body, String label, String value) {
        if (StringUtils.hasText(value)) {
            body.append(label).append(": ").append(value).append('\n');
        }
    }

    private boolean isValidEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return false;
        }

        try {
            InternetAddress address = new InternetAddress(email);
            address.validate();
            return true;
        } catch (AddressException ex) {
            return false;
        }
    }

    private String valueOrPlaceholder(String value, String placeholder) {
        return StringUtils.hasText(value) ? value : placeholder;
    }
}
