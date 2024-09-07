package ru.ivanov.ggnetwork.aop.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ivanov.ggnetwork.aop.validation.annotations.NewGameTitleConstraint;
import ru.ivanov.ggnetwork.repositories.GameRepository;

public class NewGameTitleValidator implements ConstraintValidator<NewGameTitleConstraint, String> {
    @Autowired
    private GameRepository gameRepository;
    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        return !gameRepository.existsByTitle(title);
    }
}
