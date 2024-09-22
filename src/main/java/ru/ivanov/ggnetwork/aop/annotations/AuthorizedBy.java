package ru.ivanov.ggnetwork.aop.annotations;

import ru.ivanov.ggnetwork.authorization.Authorizer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthorizedBy {
    Class<? extends Authorizer> value();
}
