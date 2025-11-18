package com.portfolio.backend.security;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

/**
 * Simple in-memory store that keeps track of revoked JWT tokens until they
 * naturally expire.
 */
@Component
public class TokenBlacklistService {

    private final Map<String, Instant> revokedTokens = new ConcurrentHashMap<>();

    public void revoke(String token, Instant expiresAt) {
        if (token == null || expiresAt == null) {
            return;
        }
        revokedTokens.put(token, expiresAt);
    }

    public boolean isRevoked(String token) {
        if (token == null) {
            return false;
        }
        cleanup();
        Instant expiry = revokedTokens.get(token);
        return expiry != null && expiry.isAfter(Instant.now());
    }

    private void cleanup() {
        Instant now = Instant.now();
        revokedTokens.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    }
}
