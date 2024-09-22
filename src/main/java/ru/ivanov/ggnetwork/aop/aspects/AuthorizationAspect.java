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
import ru.ivanov.ggnetwork.aop.annotations.EntityId;
import ru.ivanov.ggnetwork.authorization.Authorizer;
import ru.ivanov.ggnetwork.exceptions.ExpectedAnnotationException;
import ru.ivanov.ggnetwork.security.UserDetailsImpl;

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
        for (int i = 0; i < params.length; i++) {
            var param = params[i];
            var entityIdAnnotation = param.getAnnotation(EntityId.class);
            if (entityIdAnnotation != null) {
                var authorizedByAnnotation = method.getAnnotation(AuthorizedBy.class);
                if (authorizedByAnnotation == null)
                    throw new ExpectedAnnotationException("AuthorizedBy annotation expected");
                var authorizerType = authorizedByAnnotation.value();
                var authorizerBean = (Authorizer) context.getBean(authorizerType);
                var authentication = SecurityContextHolder.getContext().getAuthentication();
                if (    authentication == null ||
                        !authentication.isAuthenticated() ||
                        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ANONYMOUS")))
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("only for authenticated users");
                var userDetails = (UserDetailsImpl) authentication.getPrincipal();
                var user = userDetails.getUser();
                var entityId = (Integer) args[i];
                if (!authorizerBean.checkAuthorize(user, entityId)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("action is not allowed");
                }
            }
        }
        return pjp.proceed(pjp.getArgs());
    }
}
