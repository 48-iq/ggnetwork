package ru.ivanov.ggnetwork.aop.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ivanov.ggnetwork.aop.validation.annotations.NewUsernameConstraint;
import ru.ivanov.ggnetwork.repositories.UserRepository;

public class NewUsernameValidator implements ConstraintValidator<NewUsernameConstraint, String> {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByUsername(username);
    }
}
