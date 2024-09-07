package ru.ivanov.ggnetwork.aop.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ivanov.ggnetwork.aop.validation.validators.EmailValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface EmailConstraint {
    String message() default  "incorrect email";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
