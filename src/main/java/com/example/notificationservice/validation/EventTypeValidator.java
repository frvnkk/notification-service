package com.example.notificationservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EventTypeValidator implements ConstraintValidator<ValidEventType, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        try {
            // Проверяем, что значение соответствует одному из EventType
            return "USER_CREATED".equals(value) || "USER_DELETED".equals(value);
        } catch (Exception e) {
            return false;
        }
    }
}