package com.example.utils;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LoggerUtil {

	private LoggerUtil() {
		//0-param private constructor 
	}
	
	public static Logger getLoggerObject(Class<?> clazz) {
		return Logger.getLogger(clazz);
	}
	static{
		PropertyConfigurator.configure("src/main/java/com/example/common/log4j.properties");
	}
	
}