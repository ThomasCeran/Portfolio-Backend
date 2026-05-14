package com.portfolio.backend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class RecaptchaServiceImplTest {

    @Test
    void shouldReturnFalseWhenSecretOrTokenMissing() {
        RecaptchaServiceImpl serviceNoSecret = new RecaptchaServiceImpl("", new RestTemplate());
        assertFalse(serviceNoSecret.isTokenValid("token", "127.0.0.1"));

        RecaptchaServiceImpl serviceWithSecret = new RecaptchaServiceImpl("secret", new RestTemplate());
        assertFalse(serviceWithSecret.isTokenValid("", "127.0.0.1"));
    }

    @Test
    void shouldValidateTokenWithRemoteCall() {
        RestTemplate restTemplate = new RestTemplate();
        RecaptchaServiceImpl service = new RecaptchaServiceImpl("secret", restTemplate);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        String body = "{\"success\":true}";
        server.expect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        assertTrue(service.isTokenValid("token-value", "203.0.113.10"));
        server.verify();
    }

    @Test
    void shouldReturnFalseWhenGoogleRejectsToken() {
        RestTemplate restTemplate = new RestTemplate();
        RecaptchaServiceImpl service = new RecaptchaServiceImpl("secret", restTemplate);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        String body = "{\"success\":false}";
        server.expect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        assertFalse(service.isTokenValid("token-value", "203.0.113.10"));
        server.verify();
    }

    @Test
    void shouldReturnFalseWhenGoogleVerificationFails() {
        RestTemplate restTemplate = new RestTemplate();
        RecaptchaServiceImpl service = new RecaptchaServiceImpl("secret", restTemplate);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        server.expect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
                .andRespond(withServerError());

        assertFalse(service.isTokenValid("token-value", "203.0.113.10"));
        server.verify();
    }
}
