package com.example.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Locale;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.email.company-name}")
    private String companyName;

    /**
     * Отправляет простое текстовое письмо
     */
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            logger.info("Sending simple email to: {}", to);

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);

            logger.info("Simple email successfully sent to: {}", to);
        } catch (Exception e) {
            logger.error("Failed to send simple email to: {}", to, e);
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }

    /**
     * Отправляет HTML письмо с использованием шаблона
     */
    public void sendHtmlEmail(String to, String subject, String templateName, Context context) {
        try {
            logger.info("Sending HTML email to: {}, template: {}", to, templateName);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String htmlContent = templateEngine.process(templateName, context);

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);

            logger.info("HTML email successfully sent to: {}", to);
        } catch (MessagingException e) {
            logger.error("Failed to send HTML email to: {}", to, e);
            throw new RuntimeException("Failed to send HTML email: " + e.getMessage(), e);
        }
    }

    /**
     * Отправляет письмо о создании аккаунта
     */
    public void sendAccountCreatedEmail(String to, String username) {
        logger.info("Sending account creation email to: {}, username: {}", to, username);

        Context context = new Context(Locale.getDefault());
        context.setVariable("username", username != null ? username : "Пользователь");
        context.setVariable("companyName", companyName);
        context.setVariable("email", to);

        sendHtmlEmail(
                to,
                "Добро пожаловать в " + companyName + "!",
                "account-created",
                context
        );
    }

    /**
     * Отправляет письмо об удалении аккаунта
     */
    public void sendAccountDeletedEmail(String to, String username) {
        logger.info("Sending account deletion email to: {}, username: {}", to, username);

        Context context = new Context(Locale.getDefault());
        context.setVariable("username", username != null ? username : "Пользователь");
        context.setVariable("companyName", companyName);
        context.setVariable("email", to);

        sendHtmlEmail(
                to,
                "Ваш аккаунт в " + companyName + " удален",
                "account-deleted",
                context
        );
    }

    /**
     * Отправляет тестовое письмо
     */
    public void sendTestEmail(String to) {
        sendSimpleEmail(
                to,
                "Тестовое письмо от " + companyName,
                "Это тестовое письмо, отправленное из Notification Service."
        );
    }
}