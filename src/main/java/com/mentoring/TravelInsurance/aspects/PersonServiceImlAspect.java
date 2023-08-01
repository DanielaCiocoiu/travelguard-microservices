package com.mentoring.TravelInsurance.aspects;

import com.mentoring.TravelInsurance.controller.PersonController;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;

@Aspect
@Component
@EnableAspectJAutoProxy
public class PersonServiceImlAspect {
    private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

    //Advice - when intercept logic
    @Before("execution(* com.mentoring.TravelInsurance.service.person.PersonServiceIml.*(..))")
    public void logBeforeMethod() {
        logger.info("Method started at: " + Timestamp.from(Instant.now()));
    }

    @After("execution(* com.mentoring.TravelInsurance.service.person.PersonServiceIml.*(..))")
    public void logAfterMethod() {
        logger.info("Method ended at: " + Timestamp.from(Instant.now()));
    }

    @AfterReturning(pointcut = "execution(* com.mentoring.TravelInsurance.service.person.PersonServiceIml.findById(..))")
    public void afterReturningMethod(JoinPoint joinPoint) {
        logger.info("Target method: " + joinPoint.getSignature() + " has successfully executed returning ");
    }

    @AfterThrowing(pointcut = "execution(* com.mentoring.TravelInsurance.service.person.PersonServiceIml.findById(..))",
            throwing = "throwedException")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception throwedException) {
        logger.info("Target method: " + joinPoint.getSignature() + " didn`t executed returning " + throwedException);
    }

    //intercept save() method with pointcut defined
    @Pointcut("execution(* com.mentoring.TravelInsurance.service.person.PersonServiceIml.save(..))")
    public void methodAddEmployeeInCompanyService() {
    }

    @Around(value = "methodAddEmployeeInCompanyService()")
    public Object aroundAdviceMethod(ProceedingJoinPoint proceedingJoinPoint) {
        final String targetClass = proceedingJoinPoint.getTarget().getClass().getSimpleName();
        final String targetMethod = proceedingJoinPoint.getSignature().toShortString();
        logger.info("Before aroundAdviceMethod method: " + targetClass + "." + targetMethod);
        Object result = new Object();
        try {
            result = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            logger.info(throwable.getMessage());
        }
        final String arg = proceedingJoinPoint.getArgs()[0].toString();
        logger.info("After aroundAdviceMethod method:  executed successfully with  " + arg + " argument");
        return result;
    }
}
