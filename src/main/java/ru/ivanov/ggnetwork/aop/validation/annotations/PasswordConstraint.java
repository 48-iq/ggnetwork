package ru.ivanov.ggnetwork.aop.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ivanov.ggnetwork.aop.validation.validators.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= PasswordValidator.class)
public @interface PasswordConstraint {
    String message() default  "password length should be >= 8";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
