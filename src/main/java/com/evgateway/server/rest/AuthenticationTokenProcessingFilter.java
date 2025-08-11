package com.evgateway.server.rest;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.filter.OncePerRequestFilter;

import com.evgateway.server.pojo.User;

import io.jsonwebtoken.ExpiredJwtException;

@Service
public class AuthenticationTokenProcessingFilter extends OncePerRequestFilter {

	private final UserDetailsService userService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Value("${map-portalurl}")
	private String mapportalurl;

	private final Logger logger = LoggerFactory.getLogger(AuthenticationTokenProcessingFilter.class);

	public AuthenticationTokenProcessingFilter(UserDetailsService userService) {
		this.userService = userService;

	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
	        throws IOException, ServletException {

	    logger.debug("doFilter() is invoked..");

	    HttpServletRequest httpServletRequest = this.getAsHttpRequest(request);
	    String requestURI = httpServletRequest.getRequestURI();
	    String referer = httpServletRequest.getHeader("Referer");

	  

	    logger.info("Request URI: " + requestURI);
	    logger.info("Referer: " + referer);

	    try {
	        if (requestURI.startsWith("/services/map")) {
	            logger.info("Validating request for /services/map with Host: " + referer);

	            if (referer == null || !isValidHost(referer)) {
	                logger.error("Invalid host for /services/map request: " + referer);
	                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid host");
	                return;
	            }

	            logger.info("Host validation passed for /services/map.");
	        } else {
	            // JWT Token handling for other requests
	        	  MDC.put("X-Request-ID", httpServletRequest.getHeader("X-Request-ID"));
	      	    MDC.put("X-Correlation-ID", httpServletRequest.getHeader("X-Correlation-ID"));
	            handleAuthorization(httpServletRequest, response);
	        }

	        chain.doFilter(request, response);
	    } catch (Exception e) {
	        logger.error("An error occurred during filtering: ", e);
	        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
	    }
	}

	private void handleAuthorization(HttpServletRequest request, HttpServletResponse response) {
	    String requestTokenHeader = request.getHeader("Authorization");
	    String jwtToken = null;
	    String username = null;

	    if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
	        try {
	            jwtToken = requestTokenHeader.substring(7);
	            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
	        } catch (IllegalArgumentException e) {
	            logger.error("Unable to get JWT Token", e);
	        } catch (ExpiredJwtException e) {
	            logger.error("JWT Token has expired", e);
	        }
	    } else {
	        logger.debug("JWT Token does not begin with Bearer String");
	    }

	    logger.debug("JWT Token: " + jwtToken + ", Username: " + username);

	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        try {
	            User user = new User();
	            user.setUsername(username);
	            UserDetails userDetails = user;

	            if (jwtTokenUtil.validateToken(jwtToken, userDetails, request)) {
	                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                        userDetails, null, userDetails.getAuthorities());
	                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	                SecurityContextHolder.getContext().setAuthentication(authToken);
	            }
	        } catch (Exception e) {
	            logger.error("Error during token validation", e);
	        }
	    }
	}


	private HttpServletRequest getAsHttpRequest(ServletRequest request) {
		if (!(request instanceof HttpServletRequest)) {
			throw new RuntimeException("Expecting an HTTP request");
		}

		return (HttpServletRequest) request;
	}

	private boolean isValidHost(String referer) {
		// Define your host validation logic here
		// Example: Allow only a specific host or pattern
		return mapportalurl.equals(referer);
	}
}