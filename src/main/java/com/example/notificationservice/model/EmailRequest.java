package com.example.notificationservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Модель запроса для отправки email")
public class EmailRequest {

    @Schema(description = "Email адрес получателя",
            example = "recipient@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email получателя обязателен")
    @Email(message = "Неверный формат email")
    private String toEmail;

    @Schema(description = "Тема письма",
            example = "Важная информация",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Тема письма обязательна")
    private String subject;

    @Schema(description = "Текст письма",
            example = "Уважаемый пользователь, это тестовое письмо.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Текст письма обязателен")
    private String text;

    // Геттеры и сеттеры
    public String getToEmail() { return toEmail; }
    public void setToEmail(String toEmail) { this.toEmail = toEmail; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}