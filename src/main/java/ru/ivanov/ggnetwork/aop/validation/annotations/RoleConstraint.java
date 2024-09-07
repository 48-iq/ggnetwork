package ru.ivanov.ggnetwork.aop.validation.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import ru.ivanov.ggnetwork.aop.validation.validators.RoleValidator;
import ru.ivanov.ggnetwork.entities.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface RoleConstraint {
    String message() default  "role should be ROLE_USER or ROLE_ADMIN";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}
