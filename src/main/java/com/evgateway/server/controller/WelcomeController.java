package com.evgateway.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.ResponseMessage;

import io.swagger.v3.oas.annotations.Hidden;

@Controller
@Hidden

public class WelcomeController {

	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<ResponseMessage> welcome() throws UserNotFoundException {
		String msg = "Welcome To EVG-Driver-SERVER";

		// logger.info("WelcomeController.welcome() - msg [" + msg + "]");

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
	}

}
