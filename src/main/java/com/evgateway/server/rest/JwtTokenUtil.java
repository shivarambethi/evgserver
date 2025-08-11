package com.evgateway.server.rest;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.evgateway.server.dao.GeneralDao;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private final static Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);

	private static final long serialVersionUID = -2550185165626007488L;
	public static final long JWT_TOKEN_VALIDITY = 12 * 60 * 60;

	public static final long JWT_TOKEN_VALIDITY_APP = 24 * 60 * 60;

	@Value("${jwt.secret}")
	private String secret;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	// retrieve username from jwt token
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	// retrieve expiration date from jwt token
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	// for retrieveing any information from token we will need the secret key
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	// check if the token has expired
	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	// generate token for user
	public String generateToken(UserDetails userDetails, HttpServletRequest request) {
		logger.debug("generateToken() is invoked..: " + userDetails);
		//Map<String, Object> claims = new HashMap<>();
		
		Map<String, Object> claims = new HashMap<>();
		claims.put("ip", request.getRemoteAddr());
		claims.put("User-Agent", request.getHeader("User-Agent"));
		return doGenerateToken(claims, userDetails.getUsername());
	}

	
	private String doGenerateToken(Map<String, Object> claims, String subject) {

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	// validate token
	public Boolean validateToken(String token, UserDetails userDetails, HttpServletRequest request) {
		logger.debug("validateToken() is invoked..: " + userDetails);
		final String username = getUsernameFromToken(token);

		if (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isIpValidate(token, request))
			return true;
		else
			return false;

	}

	public Boolean isIpValidate(String token, HttpServletRequest request) {
		final Claims claims = getAllClaimsFromToken(token);
		String ip = claims.get("ip", String.class);
		String userAgent = claims.get("User-Agent", String.class);
		
		return (request.getRemoteAddr().equals(ip) && request.getHeader("User-Agent").equals(userAgent));

	}
	public String generateTokenForApp(UserDetails userDetails) {
		logger.debug("generateToken() is invoked..: " + userDetails);
		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder().setClaims(claims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY_APP * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	

}
