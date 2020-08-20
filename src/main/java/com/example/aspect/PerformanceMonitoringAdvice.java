package com.example.aspect;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.utils.LoggerUtil;

@Component("performanceAdvice")
@Order(3)
@Aspect
public class PerformanceMonitoringAdvice {

	private Logger logger = LoggerUtil.getLoggerObject(PerformanceMonitoringAdvice.class);
	
	@Around("execution(* com.example.service.*.*(..))")
	public Object monitorAspect(ProceedingJoinPoint pjp)throws Throwable{
		long start=0, end=0;
		Object retVal=null;
		
		start=System.currentTimeMillis();
		retVal=pjp.proceed();
		end=System.currentTimeMillis();
		logger.info(pjp.getSignature()+" with the arguments ::"+Arrays.toString(pjp.getArgs())+" has taken::"+(end-start)/1000+" seconds");
		return retVal;
		
	}
	
}