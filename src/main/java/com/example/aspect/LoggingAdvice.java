package com.example.aspect;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.utils.LoggerUtil;

@Component("logAdvice")
@Order(2)
@Aspect
public class LoggingAdvice {

	private Logger logger = LoggerUtil.getLoggerObject(LoggingAdvice.class);

	@Around("execution(* com.example.service.*.*(..))")
	public Object loggingAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = null;
		
		logger.debug("Entering into method::" + pjp.getSignature() + "  with arguments::" + Arrays.toString(pjp.getArgs()));
		retVal = pjp.proceed();
		logger.debug("Exiting from method::" + pjp.getSignature() + "  with arguments::" + Arrays.toString(pjp.getArgs()));

		return retVal;
	}

}