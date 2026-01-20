package com.example.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Value("${mail.from:no-reply@example.com}")
    private String fromEmail;

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // Для Kafka событий
    public void sendUserCreatedEmail(String toEmail, String username) {
        String subject = "Добро пожаловать!";
        String text = String.format(
                "Здравствуйте%s! Ваш аккаунт на сайте был успешно создан.",
                username != null ? ", " + username : ""
        );
        sendEmail(toEmail, subject, text);
        logger.info("Отправлено письмо о создании для: {}", toEmail);
    }

    public void sendUserDeletedEmail(String toEmail, String username) {
        String subject = "Ваш аккаунт удален";
        String text = String.format(
                "Здравствуйте%s! Ваш аккаунт был удалён.",
                username != null ? ", " + username : ""
        );
        sendEmail(toEmail, subject, text);
        logger.info("Отправлено письмо об удалении для: {}", toEmail);
    }

    // Для REST API
    public void sendCustomEmail(String toEmail, String subject, String text) {
        sendEmail(toEmail, subject, text);
        logger.info("Отправлено пользовательское письмо для: {}", toEmail);
    }

    private void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            logger.info("Email успешно отправлен на: {}", toEmail);
        } catch (Exception e) {
            logger.error("Ошибка отправки email на {}: {}", toEmail, e.getMessage());
        }
    }
}