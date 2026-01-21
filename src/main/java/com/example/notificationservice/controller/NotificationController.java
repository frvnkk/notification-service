package com.example.notificationservice.controller;

import com.example.notificationservice.model.EmailRequest;
import com.example.notificationservice.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        notificationService.sendCustomEmail(
                emailRequest.getToEmail(),
                emailRequest.getSubject(),
                emailRequest.getText()
        );
        return ResponseEntity.ok("Email отправлен успешно");
    }

    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(
            @RequestParam String email,
            @RequestParam(required = false) String username) {
        notificationService.sendWelcomeEmail(email, username);
        return ResponseEntity.ok("Приветственное письмо отправлено");
    }

    @PostMapping("/goodbye")
    public ResponseEntity<String> sendGoodbyeEmail(
            @RequestParam String email,
            @RequestParam(required = false) String username) {
        notificationService.sendGoodbyeEmail(email, username);
        return ResponseEntity.ok("Письмо об удалении отправлено");
    }

    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(@RequestParam String email) {
        notificationService.sendCustomEmail(email, "Test Email", "This is a test email");
        return ResponseEntity.ok("Тестовое письмо отправлено");
    }
}