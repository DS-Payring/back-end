package com.backend.payring.service;

public interface EmailService {
    String generateVerificationCode(String email);

    void sendVerificationCode(String email, String verificationCode);
}
