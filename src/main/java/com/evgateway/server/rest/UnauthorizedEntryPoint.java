package com.evgateway.server.rest;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Service;

import com.evgateway.server.messages.UnAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link AuthenticationEntryPoint} that rejects all requests with an
 * unauthorized error message.
 * 
 * @author Philip W. Sorst <philip@sorst.net>
 */
@Service
public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {

		UnAuthResponse error = new UnAuthResponse(new Date(), 401, "Unauthorized",
				"Unauthorized: Authentication token was either missing or invalid.");

		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(response.getOutputStream(), error);
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
//				"Unauthorized: Authentication token was either missing or invalid.");
	}

}