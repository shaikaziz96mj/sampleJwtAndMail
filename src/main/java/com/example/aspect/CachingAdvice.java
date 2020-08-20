package com.example.aspect;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.utils.LoggerUtil;

@Component("cacheAdvice")
@Order(1)
@Aspect
public class CachingAdvice {

	private Logger logger = LoggerUtil.getLoggerObject(CachingAdvice.class);

	private Map<String, Object> cacheMap = new HashMap<String, Object>();

	@Around("execution(* com.example.service.*.*(..))")
	public Object cacheAdvice(ProceedingJoinPoint pjp) throws Throwable {
		Object retVal = null;
		String key = null;

		key = pjp.getSignature() + Arrays.toString(pjp.getArgs());
		logger.debug("CachingAdvice(key):"+key);
		if (!cacheMap.containsKey(key)) {
			retVal = pjp.proceed();
			cacheMap.put(key, retVal);
			logger.info("Response from service class(CachingAdvice)");
		} else {
			retVal = cacheMap.get(key);
			logger.info("Response from cache(CachingAdvice)");
		}
		return retVal;
	}

}