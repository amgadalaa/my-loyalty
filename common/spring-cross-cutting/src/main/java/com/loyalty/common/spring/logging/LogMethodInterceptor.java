package com.loyalty.common.spring.logging;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogMethodInterceptor {

	private Logger log = LoggerFactory.getLogger(LogMethodInterceptor.class);

	@Around("@within(Loggable)")
	public Object logServiceAccess(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Received request for method: {}, with args {} ", joinPoint.getSignature(), joinPoint.getArgs());

		Object output = joinPoint.proceed();

		log.info("Resposne from method is: {}", output);
		return output;
	}

}
