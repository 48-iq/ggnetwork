package ru.ivanov.ggnetwork.aop.validation.validators;


import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ivanov.ggnetwork.aop.validation.annotations.NewGroupTitleConstraint;
import ru.ivanov.ggnetwork.repositories.GroupRepository;

public class NewGroupTitleValidator implements ConstraintValidator<NewGroupTitleConstraint, String> {
    @Autowired
    private GroupRepository groupRepository;
    @Override
    public boolean isValid(String title, ConstraintValidatorContext constraintValidatorContext) {
        return !groupRepository.existsByTitle(title);
    }
}
