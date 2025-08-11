package com.evgateway.server.controller;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.form.SiteTimingform;
import com.evgateway.server.messages.ResponseMessage;
import com.evgateway.server.pojo.Country;
import com.evgateway.server.pojo.CustomSetting;
import com.evgateway.server.pojo.Hours;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.SiteTiming;
import com.evgateway.server.pojo.VantivCeditCardKeys;
import com.evgateway.server.pojo.Zone;
import com.evgateway.server.services.CommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
@Scope("request")
@RequestMapping(value = "/common", produces = { "application/json" })

public class CommonController {

	@Autowired
	private CommonService commonService;

	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	
	@GetMapping("/langs")
	public ResponseEntity<List<Language>> getLangs() throws ServerException, UserNotFoundException {

		logger.debug("CommonController.getLangs() -  []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getLangs());

	}

	
	@GetMapping("/zone")
	public ResponseEntity<List<Zone>> getTimeZones() throws UserNotFoundException {

		logger.debug("CommonController.getTimeZones() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getTimeZones());

	}

	//@ApiOperation(value = "Get All Driver Group")
	@GetMapping("/drivergrp/{useruid}")
	public ResponseEntity<List<Map<String, Object>>> getDriverGroups(@PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.debug("CommonController.getDriverGroups() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getDriverProfileGroups(useruid));

	}

	//@ApiOperation(value = "Get vantivCeditCardKeys")
	@GetMapping("/vantivCeditCardKeys")
	public ResponseEntity<List<VantivCeditCardKeys>> getVantivCeditCardKeys() throws UserNotFoundException {

		logger.debug("CommonController.getVantivCeditCardKeys() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getVantivCeditCardKeys());

	}

	//@ApiOperation(value = "Get All List of  Minutes")
	@GetMapping("/minutes")
	public ResponseEntity<Object> getMinutes() throws UserNotFoundException {

		logger.debug("CommonController.getMinutes() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getMinutes());
	}

	//@ApiOperation(value = "Get All List of Hours")
	@GetMapping("/hours")
	public ResponseEntity<List<Hours>> getHours() throws UserNotFoundException {

		logger.debug("CommonController.getHours() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getHours());

	}

	//@ApiOperation(value = "Get All List of Driver User")
	@GetMapping("/driver/{uesruid}")
	public ResponseEntity<List<Map<String, Object>>> getDriverUser(@PathVariable String useruid)
			throws UserNotFoundException, ServerException {

		logger.debug("CommonController.getDriverUser() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getDriverUser(useruid));

	}

	@GetMapping("/station/image/{id}")
	public ResponseEntity<String> getImage(@PathVariable Long id)
			throws ServerException, UserNotFoundException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getStationImage(id));

	}

	//@ApiOperation(value = "Get All Security Questions")
	@GetMapping("/getcountries")
	public ResponseEntity<List<Country>> getCountries() throws UserNotFoundException {

		logger.debug("CommonController.getCountries() - []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getCountries());

	}

	//@ApiOperation(value = "Get All currency")
	@GetMapping("/currency")
	public ResponseEntity<List<Map<String, Object>>> getCurrency() throws ServerException, UserNotFoundException {

		logger.debug("CommonController.getCurrency() -  []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getCurrency());

	}

	//@ApiOperation(value = "Get All currency")
	@GetMapping("/countryCode")
	public ResponseEntity<List<Map<String, Object>>> getcountryCode() throws ServerException, UserNotFoundException {

		logger.debug("CommonController.getCurrency() -  []");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getcountryCode());

	}

	//@ApiOperation(value = "Export Report")
	@PostMapping("/downloadPDF/{sessionId}/{timeZone}")
	public ResponseEntity<InputStreamResource> getReport(@PathVariable String sessionId, @PathVariable String timeZone)
			throws Exception {

		logger.info("CommonController.getReport() - by []");

		ByteArrayInputStream in = commonService.getReport(sessionId, timeZone);

		HttpHeaders headers = new HttpHeaders();
		String name = "Reports";
		// headers.setContentType(new MediaType(primaryType, subType));
		headers.add("Content-Disposition", "attachment; filename=\"" + name + "\"");

		return ResponseEntity.ok().headers(headers).body(new InputStreamResource(in));

	}

	//@ApiOperation(value = "set password generate otp")
	@PostMapping("/otp/generate/{pid}/{did}")
	public ResponseEntity<ResponseMessage> generateOTPForPassword(@PathVariable String pid, @PathVariable String did)
			throws Exception {
		logger.info("CommonController.getReport() - by []");
		commonService.generateOTPForPassword(pid, did);
		return ResponseEntity.ok().body(new ResponseMessage("Vehicle Added Successfully"));
	}

	//@ApiOperation(value = "validate otp")
	@PostMapping("/otp/validate/{pid}/{did}/{otp}")
	public ResponseEntity<ResponseMessage> validateOTPForSetPassword(@PathVariable String pid, @PathVariable String did,
			String otp) throws Exception {
		logger.info("CommonController.validateOTPForSetPassword() - by []");
		commonService.validateOTPForSetPassword(pid, did, otp);
		return ResponseEntity.ok().body(new ResponseMessage("Vehicle Added Successfully"));
	}

	//@ApiOperation(value = "set password")
	@PostMapping("/password/set")
	public ResponseEntity<ResponseMessage> setPassword(@RequestBody Map<String, Object> passwordData) throws Exception {
		logger.info("DriverController.addVechile() - by []");
		commonService.setPasswordForUser(passwordData);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Vehicle Added Successfully"));
	}

	//@ApiOperation("Export Report And Stats")
	@PostMapping({ "/chargingactivity/report" })
	public ResponseEntity<ResponseMessage> downloadFile(@RequestBody ReportsAndStatsForm reportsAndStatsForm)
			throws IOException, ServerException, UserNotFoundException, ParseException {
		logger.info("CommonController.downloadFile() - basedOn [" + reportsAndStatsForm.getBasedOn() + "]  data ["
				+ reportsAndStatsForm.getData() + "] type : [" + reportsAndStatsForm.getData() + "] rangeValue,range ["
				+ reportsAndStatsForm.getRange() + "]");
		commonService.getFile(reportsAndStatsForm);
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseMessage("Report sent through email successfully."));
	}

	@GetMapping("/noauth/logo/{orgId}")
	public ResponseEntity<List<Map<String, Object>>> getLogoImagebasedonorg(@PathVariable Long orgId)
			throws FileNotFoundException, ServerException, UserNotFoundException {

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getLogoImagebasedonorg(orgId));

	}

	@GetMapping("/noauth/getColor/{orgId}")
	public ResponseEntity<CustomSetting> getColor(@PathVariable Long orgId)
			throws FileNotFoundException, ServerException, UserNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(commonService.getColor(orgId));

	}

	@GetMapping("/noauth/logo/upload/{orgId}")
	public ResponseEntity<List<Map<String, Object>>> getLogoImage(@PathVariable Long orgId)
			throws FileNotFoundException, ServerException, UserNotFoundException {

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getLogoImage(orgId));

	}

	

}
