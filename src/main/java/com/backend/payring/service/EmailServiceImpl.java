package com.backend.payring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl {
    private final JavaMailSender mailSender;

    public String generateVerificationCode(String email) {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public void sendVerificationCode(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Payring 이메일 인증 번호");
        message.setText("인증 코드: " + verificationCode);
        mailSender.send(message);
    }
}
