package ru.ivanov.ggnetwork.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy;
import ru.ivanov.ggnetwork.aop.annotations.ResourceId;
import ru.ivanov.ggnetwork.authorization.Authorizer;
import ru.ivanov.ggnetwork.exceptions.AuthenticationException;
import ru.ivanov.ggnetwork.exceptions.ExpectedAnnotationException;
import ru.ivanov.ggnetwork.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.HashMap;

@Component
@Aspect
public class AuthorizationAspect {
    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Pointcut("within(ru.ivanov.ggnetwork.controllers..*)")
    public void controllerLayer() {}

    @Pointcut("@annotation(ru.ivanov.ggnetwork.aop.annotations.AuthorizedBy)")
    public void methodWithAuthorizedByAnnotation() {}

    @Around("controllerLayer() && methodWithAuthorizedByAnnotation()")
    public Object authorizationProcess(ProceedingJoinPoint pjp) throws Throwable {
        var methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        var method = methodSignature.getMethod();
        var args = pjp.getArgs();
        var params = method.getParameters();
        var resources = new HashMap<String, Integer>();
        var authorizedByAnnotation = method.getAnnotation(AuthorizedBy.class);
        if (authorizedByAnnotation != null) {
            for (int i = 0; i < params.length; i++) {
                var param = params[i];
                var resourceIdAnnotation = param.getAnnotation(ResourceId.class);
                if (resourceIdAnnotation != null) {
                    if (! (args[i] instanceof Integer))
                        throw new ClassCastException("can't cast value " + args[i] + " to type Integer");
                    var arg = (Integer) args[i];
                    var resourceName = "";
                    if (!resourceIdAnnotation.value().isBlank())
                        resourceName = resourceIdAnnotation.value();
                    else
                        resourceName = param.getName();
                    resources.put(resourceName, arg);
                }
            }
            var authorizerType = authorizedByAnnotation.value();
            var authorizer = context.getBean(authorizerType);
            var authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()
            || authentication.getAuthorities().contains("ROLE_ANONYMOUS"))
                throw new AuthenticationException("resource only for authenticated users");
            var userDetails = (UserDetailsImpl) authentication.getPrincipal();
            var user = userDetails.getUser();
            if (!authorizer.checkAuthorize(user, resources))
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("actions on this resource not allowed for you");
        }
        return pjp.proceed(pjp.getArgs());
    }
}
