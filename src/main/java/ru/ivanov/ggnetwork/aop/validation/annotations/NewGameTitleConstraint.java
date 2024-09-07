package ru.ivanov.ggnetwork.aop.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ivanov.ggnetwork.aop.validation.validators.NewGameTitleValidator;
import ru.ivanov.ggnetwork.aop.validation.validators.NewUsernameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NewGameTitleValidator.class)
public @interface NewGameTitleConstraint {
    String message() default  "title already exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
