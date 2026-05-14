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
public class TelegramNotificationServiceImpl implements TelegramNotificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramNotificationServiceImpl.class);
    private static final String SEND_MESSAGE_URL = "https://api.telegram.org/bot%s/sendMessage";

    private final boolean notificationsEnabled;
    private final String botToken;
    private final String chatId;
    private final RestTemplate restTemplate;

    @Autowired
    public TelegramNotificationServiceImpl(
            @Value("${telegram.notifications-enabled:${TELEGRAM_NOTIFICATIONS_ENABLED:false}}") boolean notificationsEnabled,
            @Value("${telegram.bot-token:${TELEGRAM_BOT_TOKEN:}}") String botToken,
            @Value("${telegram.chat-id:${TELEGRAM_CHAT_ID:}}") String chatId,
            RestTemplateBuilder restTemplateBuilder) {
        this(notificationsEnabled, botToken, chatId, restTemplateBuilder
                .connectTimeout(Duration.ofSeconds(3))
                .readTimeout(Duration.ofSeconds(3))
                .build());
    }

    TelegramNotificationServiceImpl(boolean notificationsEnabled, String botToken, String chatId,
            RestTemplate restTemplate) {
        this.notificationsEnabled = notificationsEnabled;
        this.botToken = botToken;
        this.chatId = chatId;
        this.restTemplate = restTemplate;
    }

    @Override
    public void notifyNewContact(ContactMessage message) {
        if (!notificationsEnabled) {
            return;
        }

        if (!StringUtils.hasText(botToken) || !StringUtils.hasText(chatId)) {
            LOGGER.warn("Telegram notifications enabled but bot token or chat ID is missing");
            return;
        }

        try {
            Map<String, String> payload = Map.of(
                    "chat_id", chatId,
                    "text", buildNotificationText(message));
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.postForEntity(
                    SEND_MESSAGE_URL.formatted(botToken),
                    new HttpEntity<>(payload, headers),
                    Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                LOGGER.warn("Telegram notification failed with HTTP status {}", response.getStatusCode());
            }
        } catch (Exception ex) {
            LOGGER.warn("Telegram notification failed: {}", ex.getClass().getSimpleName());
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
