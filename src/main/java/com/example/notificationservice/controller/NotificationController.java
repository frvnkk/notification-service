package com.example.notificationservice.controller;

import com.example.notificationservice.model.EmailRequest;
import com.example.notificationservice.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notification Management",
        description = "API для отправки email уведомлений и управления уведомлениями")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Отправить произвольный email",
            description = "Отправляет email с пользовательской темой и текстом")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email успешно отправлен",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Ошибка отправки email",
                    content = @Content)
    })
    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @Parameter(description = "Данные для отправки email", required = true)
            @Valid @RequestBody EmailRequest emailRequest) {

        emailService.sendCustomEmail(
                emailRequest.getToEmail(),
                emailRequest.getSubject(),
                emailRequest.getText()
        );
        return ResponseEntity.ok("Email успешно отправлен");
    }

    @Operation(summary = "Отправить приветственное письмо",
            description = "Отправляет стандартное приветственное письмо при создании пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Приветственное письмо отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный email адрес")
    })
    @PostMapping("/welcome")
    public ResponseEntity<String> sendWelcomeEmail(
            @Parameter(description = "Email получателя", required = true, example = "user@example.com")
            @RequestParam String email,
            @Parameter(description = "Имя пользователя (опционально)", example = "Иван")
            @RequestParam(required = false) String username) {

        emailService.sendUserCreatedEmail(email, username);
        return ResponseEntity.ok("Приветственное письмо отправлено");
    }

    @Operation(summary = "Отправить письмо об удалении",
            description = "Отправляет стандартное письмо об удалении аккаунта")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Письмо об удалении отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный email адрес")
    })
    @PostMapping("/goodbye")
    public ResponseEntity<String> sendGoodbyeEmail(
            @Parameter(description = "Email получателя", required = true, example = "user@example.com")
            @RequestParam String email,
            @Parameter(description = "Имя пользователя (опционально)", example = "Иван")
            @RequestParam(required = false) String username) {

        emailService.sendUserDeletedEmail(email, username);
        return ResponseEntity.ok("Письмо об удалении отправлено");
    }

    @Operation(summary = "Отправить тестовое письмо",
            description = "Отправляет тестовое письмо для проверки работы сервиса")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Тестовое письмо отправлено"),
            @ApiResponse(responseCode = "400", description = "Неверный email адрес")
    })
    @PostMapping("/test")
    public ResponseEntity<String> sendTestEmail(
            @Parameter(description = "Email получателя", required = true, example = "test@example.com")
            @RequestParam String email) {

        emailService.sendCustomEmail(email, "Тестовое письмо",
                "Это тестовое письмо отправлено через Notification Service API");
        return ResponseEntity.ok("Тестовое письмо отправлено");
    }

    @Operation(summary = "Проверить статус сервиса",
            description = "Возвращает статус работы notification service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Сервис работает")
    })
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Notification Service is running");
    }

    @Operation(summary = "Получить информацию о Kafka",
            description = "Возвращает информацию о подключении к Kafka")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация получена")
    })
    @GetMapping("/kafka-info")
    public ResponseEntity<String> getKafkaInfo() {
        return ResponseEntity.ok("Listening to Kafka topic: user-events");
    }
}