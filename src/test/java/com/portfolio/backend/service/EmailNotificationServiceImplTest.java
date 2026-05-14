package com.portfolio.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import com.portfolio.backend.entity.ContactMessage;

class EmailNotificationServiceImplTest {

    @Test
    void notifyNewContact_sendsEmailWhenConfigured() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailNotificationServiceImpl service = configuredService(mailSender);

        service.notifyNewContact(contactMessage(true));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        SimpleMailMessage email = captor.getValue();
        assertEquals("portfolio@example.com", email.getFrom());
        assertEquals("admin@example.com", email.getTo()[0]);
        assertEquals("jane@example.com", email.getReplyTo());
        assertEquals("New portfolio contact message from Jane", email.getSubject());
        assertTrue(email.getText().contains("Name: Jane"));
        assertTrue(email.getText().contains("Email: jane@example.com"));
        assertTrue(email.getText().contains("Phone: +33123456789"));
        assertTrue(email.getText().contains("Subject: Project"));
        assertTrue(email.getText().contains("Message:\nHello from the contact form"));
    }

    @Test
    void notifyNewContact_skipsWhenDisabled() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailNotificationServiceImpl service = new EmailNotificationServiceImpl(
                false,
                "smtp.example.com",
                "portfolio@example.com",
                "password",
                "portfolio@example.com",
                "admin@example.com",
                mailSender);

        service.notifyNewContact(contactMessage(true));

        verify(mailSender, never()).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void notifyNewContact_skipsWhenRequiredConfigMissing() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailNotificationServiceImpl service = new EmailNotificationServiceImpl(
                true,
                "",
                "portfolio@example.com",
                "password",
                "portfolio@example.com",
                "admin@example.com",
                mailSender);

        service.notifyNewContact(contactMessage(true));

        verify(mailSender, never()).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void notifyNewContact_doesNotThrowWhenEmailFails() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        doThrow(new MailSendException("SMTP unavailable")).when(mailSender).send(Mockito.any(SimpleMailMessage.class));
        EmailNotificationServiceImpl service = configuredService(mailSender);

        service.notifyNewContact(contactMessage(true));

        verify(mailSender).send(Mockito.any(SimpleMailMessage.class));
    }

    @Test
    void notifyNewContact_omitsPhoneAndSubjectWhenMissing() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailNotificationServiceImpl service = configuredService(mailSender);

        service.notifyNewContact(contactMessage(false));

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());

        String text = captor.getValue().getText();
        assertTrue(text.contains("Name: Jane"));
        assertTrue(text.contains("Email: jane@example.com"));
        assertTrue(text.contains("Message:\nHello from the contact form"));
        assertTrue(!text.contains("Phone:"));
        assertTrue(!text.contains("Subject:"));
    }

    @Test
    void notifyNewContact_skipsReplyToWhenSenderEmailInvalid() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailNotificationServiceImpl service = configuredService(mailSender);
        ContactMessage message = contactMessage(true);
        message.setEmail("invalid-email");

        service.notifyNewContact(message);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(captor.capture());
        assertNull(captor.getValue().getReplyTo());
    }

    private EmailNotificationServiceImpl configuredService(JavaMailSender mailSender) {
        return new EmailNotificationServiceImpl(
                true,
                "smtp.example.com",
                "portfolio@example.com",
                "password",
                "portfolio@example.com",
                "admin@example.com",
                mailSender);
    }

    private ContactMessage contactMessage(boolean includeOptionalFields) {
        ContactMessage msg = new ContactMessage();
        msg.setName("Jane");
        msg.setEmail("jane@example.com");
        if (includeOptionalFields) {
            msg.setPhone("+33123456789");
            msg.setSubject("Project");
        }
        msg.setMessage("Hello from the contact form");
        return msg;
    }
}
