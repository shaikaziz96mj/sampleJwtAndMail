package com.example.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import com.example.constants.MessageConstants;
import com.example.domain.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jwt.SignedJWT;

public class CommonUtils {
	
	private CommonUtils() {
		throw new IllegalStateException("Utility class");
	}

	public static String getUserExternalId(String authToken) {
		String token = authToken;
		if (token == null) {
			return MessageConstants.UNAUTHORIZED;
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return MessageConstants.UNAUTHORIZED;
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			jpe.printStackTrace();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageConstants.UNAUTHORIZED;
	}
	
	public static String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}
	
	public static String getUserExternalId(HttpServletRequest authToken) {
		String token = resolveToken(authToken);
		if (token == null) {
			return MessageConstants.UNAUTHORIZED;
		}
		SignedJWT signedJWT;
		ObjectMapper mapper = new ObjectMapper();
		try {
			signedJWT = SignedJWT.parse(token);
			String object = signedJWT.getPayload().toJSONObject().toJSONString();
			AuthUser user = mapper.readValue(object, AuthUser.class);
			if (System.currentTimeMillis() > user.getSub()) {
				return MessageConstants.UNAUTHORIZED;
			} else {
				return user.getJti();
			}
		} catch (ParseException | JsonProcessingException jpe) {
			jpe.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MessageConstants.UNAUTHORIZED;
	}

	public static boolean isNull(Long value) {
		return (value == null || value == 0);
	}

	public static boolean isNull(String value) {
		return (value == null || value.trim().length() == 0);
	}

	public static boolean isNotNull(Long value) {
		return (value != null && value >= 0);
	}

	public static boolean isNotNull(String value) {
		return (value != null && value.trim().length() > 0);
	}

	public static boolean isNull(BigDecimal value) {
		return (value == null || value.longValue() == 0);
	}

	public static boolean isNull(Integer value) {
		return (value == null || value == 0);
	}

	public static boolean isNull(Double value) {
		return (value == null || value == 0);
	}

	public static boolean isNotNull(Double value) {
		return (value != null && value > 0);
	}

	public static Long getLongValue(String value) {
		Long returnValue = 0L;
		try {
			if (value != null) {
				returnValue = Long.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return returnValue;
	}

	public static Integer getIntegerValue(String value) {
		Integer returnValue = 0;
		try {
			if (value != null) {
				returnValue = Integer.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return returnValue;
	}

	public static Double getDoubleValue(String value) {
		Double returnValue = 0D;
		try {
			if (value != null) {
				returnValue = Double.valueOf(value);
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return returnValue;
	}

	public static Double checkDoubleValue(Double value) {
		Double returnValue = 0D;
		try {
			if (value != null) {
				returnValue = value;
			}
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
		return returnValue;
	}

	public static <T> boolean isEmpty(Collection<T> collection) {
		if ((collection == null) || collection.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

}