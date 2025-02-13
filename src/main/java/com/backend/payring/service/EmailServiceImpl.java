package com.backend.payring.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public String generateVerificationCode() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    public void sendVerificationCode(String email, String verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Payring 이메일 인증 번호");

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String htmlContent = templateEngine.process("code", context);

        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendReminder(String to, String name, String receiverName, String room, Integer amount) throws MessagingException {
        // Thymeleaf 템플릿을 통해 이메일 내용 생성
        Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("room", room);
        context.setVariable("amount", amount);
        context.setVariable("receiverName", receiverName);

        String emailContent = templateEngine.process("reminder.html", context);

        // 이메일 보내기
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(to);
        helper.setSubject("정산 안내");
        helper.setText(emailContent, true);

        mailSender.send(message);
    }
}
