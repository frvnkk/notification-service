package com.example.notificationservice.kafka;

import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class UserEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(UserEventConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final EmailService emailService;

    public UserEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void listen(String message) {
        logger.info("Получено сообщение из Kafka: {}", message);

        try {
            // 1. Парсим JSON сообщение
            JsonNode jsonNode = objectMapper.readTree(message);

            // 2. Извлекаем данные из JSON
            String eventType = jsonNode.get("eventType").asText();
            String email = jsonNode.get("email").asText();
            String username = jsonNode.has("username") ?
                    jsonNode.get("username").asText() : null;

            logger.info("Событие: {}, Email: {}, Username: {}",
                    eventType, email, username);

            // 3. Обрабатываем событие
            if ("USER_CREATED".equals(eventType)) {
                emailService.sendUserCreatedEmail(email, username);
                logger.info("Отправлено письмо о создании аккаунта для: {}", email);
            } else if ("USER_DELETED".equals(eventType)) {
                emailService.sendUserDeletedEmail(email, username);
                logger.info("Отправлено письмо об удалении аккаунта для: {}", email);
            } else {
                logger.warn("Неизвестный тип события: {}", eventType);
            }

        } catch (Exception e) {
            logger.error("Ошибка обработки сообщения из Kafka: {}", e.getMessage());
        }
    }
}