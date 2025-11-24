package com.portfolio.backend.service;

public interface RecaptchaService {

    boolean isTokenValid(String token, String remoteIp);
}
