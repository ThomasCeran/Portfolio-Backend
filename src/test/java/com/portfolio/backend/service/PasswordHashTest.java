package com.portfolio.backend.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTest {
    @Test
    void printBcryptHash() {
        String rawPassword = "23111999.Toto";
        String hash = new BCryptPasswordEncoder().encode(rawPassword);
        System.out.println("Hash: " + hash);
    }
}