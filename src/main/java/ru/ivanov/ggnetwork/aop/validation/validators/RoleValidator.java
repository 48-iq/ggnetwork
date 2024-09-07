package ru.ivanov.ggnetwork.aop.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ivanov.ggnetwork.aop.validation.annotations.RoleConstraint;
import ru.ivanov.ggnetwork.entities.Role;

import java.util.Arrays;

public class RoleValidator implements ConstraintValidator<RoleConstraint, String> {
    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(Role.values()).anyMatch(r -> r.name().equals(role));
    }
}
