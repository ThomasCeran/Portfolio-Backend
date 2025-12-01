package com.portfolio.backend.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

class RecaptchaServiceImplTest {

    @Test
    void shouldReturnFalseWhenSecretOrTokenMissing() {
        RecaptchaServiceImpl serviceNoSecret = new RecaptchaServiceImpl("");
        assertFalse(serviceNoSecret.isTokenValid("token", "127.0.0.1"));

        RecaptchaServiceImpl serviceWithSecret = new RecaptchaServiceImpl("secret");
        assertFalse(serviceWithSecret.isTokenValid("", "127.0.0.1"));
    }

    @Test
    void shouldValidateTokenWithRemoteCall() throws Exception {
        RecaptchaServiceImpl service = new RecaptchaServiceImpl("secret");
        RestTemplate restTemplate = extractRestTemplate(service);
        MockRestServiceServer server = MockRestServiceServer.createServer(restTemplate);

        String body = "{\"success\":true}";
        server.expect(requestTo("https://www.google.com/recaptcha/api/siteverify"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        assertTrue(service.isTokenValid("token-value", "203.0.113.10"));
        server.verify();
    }

    private RestTemplate extractRestTemplate(RecaptchaServiceImpl service) throws Exception {
        Field field = RecaptchaServiceImpl.class.getDeclaredField("restTemplate");
        field.setAccessible(true);
        return (RestTemplate) field.get(service);
    }
}
