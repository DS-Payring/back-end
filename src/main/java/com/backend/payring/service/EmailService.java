package com.backend.payring.service;

import jakarta.mail.MessagingException;

public interface EmailService {
    void sendVerificationCode(String email, String verificationCode) throws MessagingException;
    void sendReminder(String to, String name, String receiverName, String room, Integer amount) throws MessagingException;
}
