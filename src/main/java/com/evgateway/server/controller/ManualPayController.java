package com.evgateway.server.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.OcppRequestForm;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.messages.ResponseMessage;
import com.evgateway.server.services.ManualPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Scope("request")
@Hidden
@RequestMapping(value = "manualPay", produces = { "application/json" })
public class ManualPayController {

	@Autowired
	private ManualPayService manualPayService;

	private static final Logger logger = LoggerFactory.getLogger(ManualPayController.class);

	//@ApiOperation(value = "Get Search Data")
	@RequestMapping(value = "/paymentIntent/create", method = RequestMethod.POST)
	public ResponseEntity<List<Map<String, Object>>> getPaymentIntenent(
			@RequestBody PaymentIntentForm paymentIntentForm)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		logger.info("ManualPayController : ManualPayController ");

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.getPaymentIntenent(paymentIntentForm));
	}

	//@ApiOperation(value = "search station payasyougo")
	@RequestMapping(value = "/station/{stationName}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> searchStationForPayasGo(@PathVariable String stationName)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		logger.info("ManualPayController : searchStationForPayasGo station name : {} ", stationName);
		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.searchStationForPayasGo(stationName));
	}

	@RequestMapping(value = "/paymentIntent/authorize", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> authorizeGuest(@RequestBody PaymentIntentForm paymentIntentForm)
			throws UserNotFoundException, java.text.ParseException, ServerException {
		logger.info("authorizeGuest for stripe API  {} ", paymentIntentForm);
		manualPayService.authorizeGuest(paymentIntentForm);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("User Authorised Successfully"));
	}

	//@ApiOperation(value = "is Email Already exists")
	@RequestMapping(value = "isEmailExist/{email}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isEmailExist(@PathVariable String email)
			throws UserNotFoundException, ServerException {

		logger.debug("ManualPayController.isEmailExist() - email : {}", email);

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.isEmailExist(email));
	}
	
	//@ApiOperation(value = "is RFID Already exists")
	@RequestMapping(value = "isPhoneExist/{phone}", method = RequestMethod.GET)
	public ResponseEntity<Boolean> isPhoneExist(@PathVariable String phone)
			throws UserNotFoundException, ServerException {

		logger.debug("ManualPayController.isphoneExist() - phone [" + phone + "]");

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.isPhoneExist(phone));
	}

	//@ApiOperation(value = "Get charging session details based on sessionId")
	@RequestMapping(value = "/chargingSessionBasedOnSessionId/{sessionId}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> chargingSessionBasedOnSessionIdForGuest(
			@PathVariable String sessionId) throws ServerException, UserNotFoundException {

		logger.info("chargingSessionBasedOnSessionId API :" + sessionId);

		return new ResponseEntity<>(manualPayService.getChargingSessionBasedOnSessionId(sessionId), HttpStatus.OK);
	}
	
	//@ApiOperation(value = "Get charging session details based on userId")
	@RequestMapping(value = "/chargingSession/{id}/{phone}/{token}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> chargingSessionForGuest(@PathVariable Long id,
			@PathVariable String phone, @PathVariable String token) throws ServerException, UserNotFoundException {
		logger.info("chargingSession API token :" + token);

		return new ResponseEntity<>(manualPayService.chargingSession(phone, token, id), HttpStatus.OK);
	}

	@RequestMapping(value = "/portInfo/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getPortInfoById(@PathVariable long id)
			throws ServerException, UserNotFoundException {

		logger.info("getPortInfoById id :  {} ", id);

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.getPortInfoById(id));
	}

	@RequestMapping(value = "/ocpp/request", method = RequestMethod.POST)
	public ResponseEntity<com.evgateway.server.ocpp.ResponseMessage> ocppRequest(
			@RequestBody OcppRequestForm ocppRequest) throws Exception {

		logger.info("ocppRequest ocppRequest :  {} ", ocppRequest);

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.request(ocppRequest));
	}

	//@ApiOperation(value = "Get active transaction details based on userId")
	@RequestMapping(value = "/activeTransaction/{orgId}/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> activeTransaction(@PathVariable Long orgId, @PathVariable Long id,
			@RequestParam("token") String token, @RequestParam("phone") String phone)
			throws ServerException, UserNotFoundException {

		logger.info("invoked activeTransaction id : {}", id);

		return new ResponseEntity<>(manualPayService.activeTransaction(id, orgId, token, phone), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sessionInfo/{orgId}/{id}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getSessionInfoById(@PathVariable long orgId, @PathVariable String id)
			throws UserNotFoundException, JsonMappingException, JsonProcessingException {

		logger.info("invoked getSessionInfoById().....");

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.getSessionInfoById(id));
	}
	//@ApiOperation(value = "info edit By Session Id")
	@GetMapping("/infoedit/{sessionId}")
	public ResponseEntity<List<Map<String, Object>>> getinfoeditBySessionId(@PathVariable String sessionId) throws JsonMappingException, JsonProcessingException, UserNotFoundException {
 
		logger.debug("ChargingActivityController.getChartDataBySessionId() -[" + sessionId + "]");
 
		return ResponseEntity.status(HttpStatus.OK)
				.body(manualPayService.getinfoeditBySessionIds(sessionId));
	}
	
	@RequestMapping(value = "/portprice/{userTimeZone}/{id}", method = RequestMethod.GET)
	public ResponseEntity<Map<String,Object>> getPortinfoMethod(@PathVariable String userTimeZone, @PathVariable String id)
			throws UserNotFoundException {

		logger.info("invoked getStationInfoById().....");

		return ResponseEntity.status(HttpStatus.OK).body(manualPayService.getPortinfoMethod(userTimeZone,id));
	}
}