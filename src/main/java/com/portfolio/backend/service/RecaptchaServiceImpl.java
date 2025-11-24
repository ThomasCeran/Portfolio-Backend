package com.portfolio.backend.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RecaptchaServiceImpl implements RecaptchaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecaptchaServiceImpl.class);
    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    private final String secret;
    private final RestTemplate restTemplate = new RestTemplate();

    public RecaptchaServiceImpl(@Value("${recaptcha.secret:}") String secret) {
        this.secret = secret;
    }

    @Override
    public boolean isTokenValid(String token, String remoteIp) {
        if (secret == null || secret.isBlank() || token == null || token.isBlank()) {
            return false;
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", secret);
        params.add("response", token);
        if (remoteIp != null && !remoteIp.isBlank()) {
            params.add("remoteip", remoteIp);
        }

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, params, Map.class);
            Object success = response.getBody() != null ? response.getBody().get("success") : null;
            return Boolean.TRUE.equals(success);
        } catch (Exception ex) {
            LOGGER.warn("Recaptcha verification failed: {}", ex.getMessage());
            return false;
        }
    }
}
