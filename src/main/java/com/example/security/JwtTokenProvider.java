package com.example.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.example.domain.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenProvider {
	
	@Value("${app.jwtSecret}")
	private String jwtSecret;
	@Value("${app.jwtExpirationInMs}")
	private int jwtExpirationInMs;
	@SuppressWarnings("unused")
	private UserPrincipal userPrincipal;
	
	public String generateJwtToken(Authentication authentication, User user) {
		userPrincipal=(UserPrincipal) authentication.getPrincipal();
		Date expiryDate=new Date(System.currentTimeMillis() + jwtExpirationInMs);
		/*return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
		.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 *10))
		.signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();*/
		return Jwts.builder().setSubject(Long.toString(System.currentTimeMillis() + jwtExpirationInMs))
				.setId(user.getExternalId()).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	
	public Long getUserIdFromJWT(String token) {
		Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		return Long.parseLong(claims.getSubject());
	}

	public boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException ex) {
			//("Invalid JWT signature")
			ex.printStackTrace();
		} catch (MalformedJwtException ex) {
			//("Invalid JWT token")
			ex.printStackTrace();
		} catch (ExpiredJwtException ex) {
			//("Expired JWT token")
			ex.printStackTrace();
		} catch (UnsupportedJwtException ex) {
			//("Unsupported JWT token")
			ex.printStackTrace();
		} catch (IllegalArgumentException ex) {
			//("JWT claims string is empty.")
			ex.printStackTrace();
		}
		return false;
	}

}
