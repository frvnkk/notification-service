package com.example.notificationservice.controller;

import com.example.notificationservice.dto.UserEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;


    @PostMapping("/kafka/user-created")
    public ResponseEntity<Map<String, Object>> sendKafkaUserCreated(
            @RequestBody Map<String, String> request) {

        logger.info("Test: Sending user created event to Kafka");

        String email = request.get("email");
        String username = request.get("username");

        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email is required");
            return ResponseEntity.badRequest().body(error);
        }

        UserEvent event = new UserEvent("CREATED", email, username);

        try {
            kafkaTemplate.send("user-events-topic", event);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Kafka event sent successfully");
            response.put("event", event);

            logger.info("Test: Kafka event sent: {}", event);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Test: Failed to send Kafka event", e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to send Kafka event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }


    @PostMapping("/kafka/user-deleted")
    public ResponseEntity<Map<String, Object>> sendKafkaUserDeleted(
            @RequestBody Map<String, String> request) {

        logger.info("Test: Sending user deleted event to Kafka");

        String email = request.get("email");
        String username = request.get("username");

        if (email == null || email.trim().isEmpty()) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Email is required");
            return ResponseEntity.badRequest().body(error);
        }

        UserEvent event = new UserEvent("DELETED", email, username);

        try {
            kafkaTemplate.send("user-events-topic", event);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Kafka event sent successfully");
            response.put("event", event);

            logger.info("Test: Kafka event sent: {}", event);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Test: Failed to send Kafka event", e);

            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "Failed to send Kafka event: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }


    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getServiceInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "notification-service");
        info.put("version", "1.0.0");
        info.put("description", "Microservice for sending email notifications");
        info.put("kafka-topic", "user-events-topic");
        info.put("endpoints", Map.of(
                "health", "GET /api/v1/notifications/health",
                "send-email", "POST /api/v1/notifications/email/send",
                "user-created", "POST /api/v1/notifications/user/created",
                "user-deleted", "POST /api/v1/notifications/user/deleted",
                "test-kafka-create", "POST /api/v1/test/kafka/user-created",
                "test-kafka-delete", "POST /api/v1/test/kafka/user-deleted",
                "test-info", "GET /api/v1/test/info"
        ));

        return ResponseEntity.ok(info);
    }
}