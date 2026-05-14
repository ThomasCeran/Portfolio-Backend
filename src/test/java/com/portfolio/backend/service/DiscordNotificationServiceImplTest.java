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

class DiscordNotificationServiceImplTest {

    private static final String WEBHOOK_URL = "https://discord.com/api/webhooks/test-webhook";

    @Test
    void notifyNewContact_sendsExpectedRequestWhenConfigured() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        DiscordNotificationServiceImpl service = new DiscordNotificationServiceImpl(
                true,
                WEBHOOK_URL,
                restTemplate);

        server.expect(once(), requestTo(WEBHOOK_URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").value(org.hamcrest.Matchers.allOf(
                        org.hamcrest.Matchers.containsString("New portfolio contact message"),
                        org.hamcrest.Matchers.containsString("Name: Jane"),
                        org.hamcrest.Matchers.containsString("Email: jane@example.com"),
                        org.hamcrest.Matchers.containsString("Phone: +33123456789"),
                        org.hamcrest.Matchers.containsString("Subject: Project"),
                        org.hamcrest.Matchers.containsString("Message:\nHello from the contact form"))))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_skipsWhenDisabled() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        DiscordNotificationServiceImpl service = new DiscordNotificationServiceImpl(
                false,
                WEBHOOK_URL,
                restTemplate);

        server.expect(never(), requestTo(WEBHOOK_URL));

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_skipsWhenWebhookUrlMissing() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        DiscordNotificationServiceImpl service = new DiscordNotificationServiceImpl(
                true,
                "",
                restTemplate);

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    @Test
    void notifyNewContact_doesNotThrowWhenDiscordFails() {
        RestTemplate restTemplate = new RestTemplate();
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);
        DiscordNotificationServiceImpl service = new DiscordNotificationServiceImpl(
                true,
                WEBHOOK_URL,
                restTemplate);

        server.expect(once(), requestTo(WEBHOOK_URL))
                .andRespond(withServerError());

        service.notifyNewContact(contactMessage());

        server.verify();
    }

    private ContactMessage contactMessage() {
        ContactMessage msg = new ContactMessage();
        msg.setName("Jane");
        msg.setEmail("jane@example.com");
        msg.setPhone("+33123456789");
        msg.setSubject("Project");
        msg.setMessage("Hello from the contact form");
        return msg;
    }
}
