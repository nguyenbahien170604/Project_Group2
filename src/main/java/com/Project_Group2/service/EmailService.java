package com.Project_Group2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") // Email gửi đi từ cấu hình
    private String senderEmail;

    public void sendEmail(String fromEmail, String to, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom(senderEmail); // Gửi từ hệ thống
        email.setReplyTo(fromEmail); // Khi admin nhấn "Reply", nó sẽ gửi đến email người dùng
        mailSender.send(email);
    }
}
