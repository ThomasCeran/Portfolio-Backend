package com.portfolio.backend.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.portfolio.backend.entity.ContactMessage;

/**
 * Unit tests for {@link SmsNotificationService}.
 */
class SmsNotificationServiceTest {

    @Test
    void notifyNewContact_sendsSmsWhenEnabledAndNumbersProvided() {
        TwilioClient twilioClient = Mockito.mock(TwilioClient.class);
        SmsNotificationService service = new SmsNotificationService(
                true,
                "+33123456789",
                "+33987654321",
                "",
                twilioClient);

        ContactMessage msg = new ContactMessage();
        msg.setName("John");
        msg.setEmail("john@example.com");
        msg.setSubject("Hello");
        msg.setMessage("Test content");

        service.notifyNewContact(msg);

        verify(twilioClient, times(1)).sendSms(
                eq("+33123456789"),
                eq("+33987654321"),
                eq(""),
                contains("Nouveau message de John"));
    }

    @Test
    void notifyNewContact_doesNothingWhenDisabled() {
        TwilioClient twilioClient = Mockito.mock(TwilioClient.class);
        SmsNotificationService service = new SmsNotificationService(
                false,
                "+33123456789",
                "+33987654321",
                "",
                twilioClient);

        service.notifyNewContact(new ContactMessage());

        verify(twilioClient, never()).sendSms(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void notifyNewContact_doesNothingWhenNumbersMissing() {
        TwilioClient twilioClient = Mockito.mock(TwilioClient.class);
        SmsNotificationService service = new SmsNotificationService(
                true,
                "",
                "",
                "",
                twilioClient);

        service.notifyNewContact(new ContactMessage());

        verify(twilioClient, never()).sendSms(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void notifyNewContact_usesMessagingServiceSidWhenProvided() {
        TwilioClient twilioClient = Mockito.mock(TwilioClient.class);
        SmsNotificationService service = new SmsNotificationService(
                true,
                "+33123456789",
                "",
                "MGxxxx",
                twilioClient);

        ContactMessage msg = new ContactMessage();
        msg.setName("Jane");
        msg.setEmail("jane@example.com");
        msg.setSubject("Hello");
        msg.setMessage("Test with MSID");

        service.notifyNewContact(msg);

        verify(twilioClient, times(1)).sendSms(
                eq("+33123456789"),
                eq(""),
                eq("MGxxxx"),
                contains("Jane"));
    }
}
