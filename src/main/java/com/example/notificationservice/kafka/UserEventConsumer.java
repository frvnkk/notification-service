package com.example.notificationservice.kafka;

import com.example.notificationservice.event.UserEvent;
import com.example.notificationservice.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Обработка событий пользователей из Kafka",
            description = """
            Получает события о создании/удалении пользователей из топика Kafka 'user-events' 
            и отправляет соответствующие email уведомления.
            
            ### Обрабатываемые события:
            - USER_CREATED: отправляет приветственное письмо
            - USER_DELETED: отправляет письмо об удалении аккаунта
            
            ### Формат сообщения:
                       {
              "eventType": "USER_CREATED",
              "email": "user@example.com",
              "userId": 1,
              "username": "Иван",
              "timestamp": "2024-01-21T10:30:00"
            }
                        """
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Событие успешно обработано"),
            @ApiResponse(responseCode = "400", description = "Неверный формат JSON сообщения"),
            @ApiResponse(responseCode = "422", description = "Неизвестный тип события"),
            @ApiResponse(responseCode = "500", description = "Ошибка отправки email или обработки")
    })
    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void listen(String message) {
        logger.info("Получено сообщение из Kafka: {}", message);

        try {
            UserEvent event = objectMapper.readValue(message, UserEvent.class);
            logger.info("Обработка события: {}", event.getEventType());

            if ("USER_CREATED".equals(event.getEventType())) {
                emailService.sendUserCreatedEmail(event.getEmail(), event.getUsername());
                logger.info("Отправлено приветственное письмо на: {}", event.getEmail());

            } else if ("USER_DELETED".equals(event.getEventType())) {
                emailService.sendUserDeletedEmail(event.getEmail(), event.getUsername());
                logger.info("Отправлено письмо об удалении на: {}", event.getEmail());

            } else {
                logger.warn("Неизвестный тип события: {}", event.getEventType());
            }

        } catch (Exception e) {
            logger.error("Ошибка обработки сообщения из Kafka: {}", e.getMessage(), e);
        }
    }
}