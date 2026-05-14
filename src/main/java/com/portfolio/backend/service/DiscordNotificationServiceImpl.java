package com.portfolio.backend.service;

import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.portfolio.backend.entity.ContactMessage;

@Service
public class DiscordNotificationServiceImpl implements DiscordNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordNotificationServiceImpl.class);

    private final boolean notificationsEnabled;
    private final String webhookUrl;
    private final RestTemplate restTemplate;

    @Autowired
    public DiscordNotificationServiceImpl(
            @Value("${discord.notifications-enabled:${DISCORD_NOTIFICATIONS_ENABLED:false}}") boolean notificationsEnabled,
            @Value("${discord.webhook-url:${DISCORD_WEBHOOK_URL:}}") String webhookUrl,
            RestTemplateBuilder restTemplateBuilder) {
        this(notificationsEnabled, webhookUrl, restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(3))
                .build());
    }

    DiscordNotificationServiceImpl(boolean notificationsEnabled, String webhookUrl, RestTemplate restTemplate) {
        this.notificationsEnabled = notificationsEnabled;
        this.webhookUrl = webhookUrl;
        this.restTemplate = restTemplate;
    }

    @Override
    public void notifyNewContact(ContactMessage message) {
        if (!notificationsEnabled) {
            return;
        }

        if (!StringUtils.hasText(webhookUrl)) {
            LOGGER.warn("Discord notifications enabled but webhook URL is missing");
            return;
        }

        try {
            Map<String, String> payload = Map.of("content", buildNotificationText(message));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    webhookUrl,
                    new HttpEntity<>(payload, headers),
                    String.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.warn("Discord notification failed with HTTP status {}", response.getStatusCode());
            }
        } catch (Exception ex) {
            LOGGER.warn("Discord notification failed: {}", ex.getClass().getSimpleName());
        }
    }

    private String buildNotificationText(ContactMessage message) {
        StringBuilder text = new StringBuilder()
                .append("New portfolio contact message\n\n")
                .append("Name: ").append(valueOrPlaceholder(message.getName(), "Unknown")).append('\n')
                .append("Email: ").append(valueOrPlaceholder(message.getEmail(), "No email")).append('\n');
        appendOptionalLine(text, "Phone", message.getPhone());
        text.append("Subject: ").append(valueOrPlaceholder(message.getSubject(), "No subject")).append('\n')
                .append("Message:\n")
                .append(valueOrPlaceholder(message.getMessage(), "No message"));
        return text.toString();
    }

    private void appendOptionalLine(StringBuilder text, String label, String value) {
        if (StringUtils.hasText(value)) {
            text.append(label).append(": ").append(value).append('\n');
        }
    }

    private String valueOrPlaceholder(String value, String placeholder) {
        return StringUtils.hasText(value) ? value : placeholder;
    }
}
