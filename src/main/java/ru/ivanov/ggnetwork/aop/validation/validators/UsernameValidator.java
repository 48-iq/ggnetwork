package ru.ivanov.ggnetwork.aop.validation.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.ivanov.ggnetwork.aop.validation.annotations.UsernameConstraint;

import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {
    @Override
    public boolean isValid(String username,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (username == null || username.isEmpty())
            return false;
        var pattern = Pattern.compile("[a-zA-Z0-9_\\-]{3,32}");
        var matcher = pattern.matcher(username);
        return matcher.matches();
    }
}
