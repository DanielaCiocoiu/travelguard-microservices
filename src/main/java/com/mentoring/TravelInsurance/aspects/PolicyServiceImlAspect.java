package com.mentoring.TravelInsurance.aspects;

import com.mentoring.TravelInsurance.controller.PolicyController;
import org.aspectj.lang.JoinPoint;
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
public class PolicyServiceImlAspect {
    private static final Logger logger = LoggerFactory.getLogger(PolicyController.class);

    //Advice - when intercept logic
    @Before("execution(* com.mentoring.TravelInsurance.service.policy.PolicyServiceIml.*(..))")
    public void logBeforeMethod() {
        logger.info("Method started at: " + Timestamp.from(Instant.now()));
    }

    @After("execution(* com.mentoring.TravelInsurance.service.policy.PolicyServiceIml.*(..))")
    public void logAfterMethod() {
        logger.info("Method ended at: " + Timestamp.from(Instant.now()));
    }

    @AfterReturning(pointcut = "execution(* com.mentoring.TravelInsurance.service.policy.PolicyServiceIml.findById(..))")
    public void afterReturningMethod(JoinPoint joinPoint) {
        logger.info("Target method: " + joinPoint.getSignature() + " has successfully executed returning ");
    }

    @AfterThrowing(pointcut = "execution(* com.mentoring.TravelInsurance.service.policy.PolicyServiceIml.findById(..))",
            throwing = "throwedException")
    public void afterThrowingMethod(JoinPoint joinPoint, Exception throwedException) {
        logger.info("Target method: " + joinPoint.getSignature() + " didn`t executed returning " + throwedException);
    }
}
