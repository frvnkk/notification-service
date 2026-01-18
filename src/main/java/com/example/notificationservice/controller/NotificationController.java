package com.example.notificationservice.controller;

import com.example.notificationservice.dto.UserEvent;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private NotificationService notificationService;

    /**
     * Проверка работоспособности сервиса
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        logger.info("Health check requested");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "notification-service");
        response.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(response);
    }

    /**
     * Отправка кастомного email
     */
    @PostMapping("/email/send")
    public ResponseEntity<Map<String, Object>> sendEmail(
            @RequestBody Map<String, String> request) {

        logger.info("Send email request received: {}", request);

        try {
            String email = request.get("email");
            String subject = request.get("subject");
            String message = request.get("message");

            // Валидация входных данных
            if (email == null || email.trim().isEmpty()) {
                return buildErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }

            if (subject == null || subject.trim().isEmpty()) {
                return buildErrorResponse("Subject is required", HttpStatus.BAD_REQUEST);
            }

            if (message == null || message.trim().isEmpty()) {
                return buildErrorResponse("Message is required", HttpStatus.BAD_REQUEST);
            }

            // Отправка email
            notificationService.sendCustomEmail(email, subject, message);

            // Успешный ответ
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Email sent successfully");
            response.put("email", email);

            logger.info("Email sent successfully to: {}", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to send email", e);
            return buildErrorResponse("Failed to send email: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Эмуляция события создания пользователя
     */
    @PostMapping("/user/created")
    public ResponseEntity<Map<String, Object>> simulateUserCreated(
            @RequestBody Map<String, String> request) {

        logger.info("Simulate user created request: {}", request);

        try {
            String email = request.get("email");
            String username = request.get("username");

            if (email == null || email.trim().isEmpty()) {
                return buildErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }

            UserEvent event = new UserEvent("CREATED", email, username);
            notificationService.processUserEvent(event);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User created notification sent");
            response.put("email", email);
            response.put("username", username);

            logger.
                    info("User created notification sent to: {}", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to simulate user created event", e);
            return buildErrorResponse("Failed to send notification: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Эмуляция события удаления пользователя
     */
    @PostMapping("/user/deleted")
    public ResponseEntity<Map<String, Object>> simulateUserDeleted(
            @RequestBody Map<String, String> request) {

        logger.info("Simulate user deleted request: {}", request);

        try {
            String email = request.get("email");
            String username = request.get("username");

            if (email == null || email.trim().isEmpty()) {
                return buildErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }

            UserEvent event = new UserEvent("DELETED", email, username);
            notificationService.processUserEvent(event);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User deleted notification sent");
            response.put("email", email);
            response.put("username", username);

            logger.info("User deleted notification sent to: {}", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to simulate user deleted event", e);
            return buildErrorResponse("Failed to send notification: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Отправка тестового email
     */
    @PostMapping("/test")
    public ResponseEntity<Map<String, Object>> sendTestEmail(
            @RequestBody Map<String, String> request) {

        logger.info("Send test email request: {}", request);

        try {
            String email = request.get("email");

            if (email == null || email.trim().isEmpty()) {
                return buildErrorResponse("Email is required", HttpStatus.BAD_REQUEST);
            }

            notificationService.sendTestEmail(email);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Test email sent successfully");
            response.put("email", email);

            logger.info("Test email sent to: {}", email);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Failed to send test email", e);
            return buildErrorResponse("Failed to send test email: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Вспомогательный метод для создания ответа с ошибкой
     */
    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("error", message);
        errorResponse.put("status", status.value());
        errorResponse.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.status(status).body(errorResponse);
    }
}