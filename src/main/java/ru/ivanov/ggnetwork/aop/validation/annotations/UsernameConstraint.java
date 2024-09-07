package ru.ivanov.ggnetwork.aop.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ivanov.ggnetwork.aop.validation.validators.UsernameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= UsernameValidator.class)
public @interface UsernameConstraint {
    String message() default  "username size should be >= 3 and <= 32 and should contains only 0-9 a-z A-Z _ - characters";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
