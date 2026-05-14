package com.portfolio.backend.service;

import static org.springframework.test.web.client.ExpectedCount.never;
import static org.springframework.test.web.client.ExpectedCount.once;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import com.portfolio.backend.entity.ContactMessage;

class TelegramNotificationServiceImplTest {

    @Test
    void notifyNewContact_sendsExpectedRequestWhenConfigured() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        TelegramNotificationServiceImpl service = new TelegramNotificationServiceImpl(
                true,
                "bot-token",
                "123456",
                restTemplate);

        server.expect(once(), requestTo("https://api.telegram.org/botbot-token/sendMessage"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.chat_id").value("123456"))
                .andExpect(jsonPath("$.text").value(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.containsString("New portfolio contact message"),
                        org.hamcrest.Matchers.containsString("Name: Jane"),
                        org.hamcrest.Matchers.containsString("Email: jane@example.com"),
                        org.hamcrest.Matchers.containsString("Subject: Project"),
                        org.hamcrest.Matchers.containsString("Message:\nHello from the contact form"))))
                .andExpect(jsonPath("$.parse_mode").doesNotExist())
                .andRespond(withSuccess("{\"ok\":true}", MediaType.APPLICATION_JSON));

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_skipsWhenDisabled() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        TelegramNotificationServiceImpl service = new TelegramNotificationServiceImpl(
                false,
                "bot-token",
                "123456",
                restTemplate);

        server.expect(never(), requestTo("https://api.telegram.org/botbot-token/sendMessage"));

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_skipsWhenTokenMissing() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        TelegramNotificationServiceImpl service = new TelegramNotificationServiceImpl(
                true,
                "",
                "123456",
                restTemplate);

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_skipsWhenChatIdMissing() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        TelegramNotificationServiceImpl service = new TelegramNotificationServiceImpl(
                true,
                "bot-token",
                "",
                restTemplate);

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_doesNotThrowWhenTelegramFails() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        TelegramNotificationServiceImpl service = new TelegramNotificationServiceImpl(
                true,
                "bot-token",
                "123456",
                restTemplate);

        server.expect(once(), requestTo("https://api.telegram.org/botbot-token/sendMessage"))
                .andRespond(withServerError());

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    private ContactMessage contactMessage() {
        ContactMessage msg = new ContactMessage();
        msg.setName("Jane");
        msg.setEmail("jane@example.com");
        msg.setSubject("Project");
        msg.setMessage("Hello from the contact form");
        return msg;
    }
}
