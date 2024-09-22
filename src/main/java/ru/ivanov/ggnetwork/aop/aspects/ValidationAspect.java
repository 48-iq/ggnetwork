package ru.ivanov.ggnetwork.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Validator;
import ru.ivanov.ggnetwork.aop.annotations.UseValidator;
import ru.ivanov.ggnetwork.aop.annotations.ValidatedBy;
import ru.ivanov.ggnetwork.exceptions.ExpectedAnnotationException;

@Aspect
@Component
public class ValidationAspect {

    @Pointcut("within(ru.ivanov.ggnetwork.controllers..*)")
    public void controllerLayer(){}

    private ApplicationContext context;

    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    @Around("controllerLayer()")
    public Object validationProcess(ProceedingJoinPoint pjp) throws Throwable {
        var methodSignature = (MethodSignature) pjp.getStaticPart().getSignature();
        var method = methodSignature.getMethod();
        var args = pjp.getArgs();
        var params = method.getParameters();
        for (int i = 0; i < params.length; i++) {
            var param = params[i];
            var useValidatorAnnotation = param.getAnnotation(UseValidator.class);
            if (useValidatorAnnotation != null) {
                var type = param.getType();
                var validatedByAnnotation = type.getAnnotation(ValidatedBy.class);
                if (validatedByAnnotation == null)
                    throw new ExpectedAnnotationException("annotation validated by expected in class " + type.getName());
                var validatorType = validatedByAnnotation.value();
                var validatorBean = (Validator) context.getBean(validatorType);
                var target = args[i];
                var errors = new BeanPropertyBindingResult(target, type.getName());
                validatorBean.validate(target, errors);
                if (errors.hasErrors()) {
                    return ResponseEntity.badRequest().body(errors.getAllErrors().stream()
                            .map(e -> e.getDefaultMessage()).toList()
                    );
                }
            }
        }
        return pjp.proceed(pjp.getArgs());
    }
}
