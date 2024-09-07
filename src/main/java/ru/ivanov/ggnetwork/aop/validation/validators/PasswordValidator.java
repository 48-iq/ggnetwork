package ru.ivanov.ggnetwork.aop.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ivanov.ggnetwork.aop.validation.annotations.PasswordConstraint;

import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {
    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        var pattern = Pattern.compile(".{8,64}");
        var matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
