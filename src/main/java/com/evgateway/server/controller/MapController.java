package com.evgateway.server.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.SiteTiming;
import com.evgateway.server.services.CommonService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
@Scope("request")
@RequestMapping(value = "/services/map", produces = { "application/json" })
public class MapController {

	@Autowired
	private CommonService commonService;

	@Value("${map-accesstoken}")
	private String mapaccesstoken;
	private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

	@ApiOperation(value = "Get All filter")
	@GetMapping("/map/filter")
	public ResponseEntity<List<Map<String, Object>>> getMap(@RequestParam String search,
			@RequestParam(required = true) String correlationID)
			throws ServerException, UserNotFoundException, JsonMappingException, JsonProcessingException {

		logger.debug("CommonController.getLangs() -  []");
		if (!correlationID.equals(mapaccesstoken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
		return ResponseEntity.status(HttpStatus.OK).body(commonService.getMapFilter(search));

	}

	@ApiOperation(value = "Get All Sitetiming Based On siteid")
	@RequestMapping(value = "/location", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> getSitebylatlang(@RequestParam double lat,
			@RequestParam double lng, @RequestParam(required = true) String correlationID)
			throws UserNotFoundException {

		logger.debug("StationController.getSitebylatlong() -   [" + lat + "],[" + lng + "]");
		if (!correlationID.equals(mapaccesstoken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
		return ResponseEntity.status(HttpStatus.OK).body(commonService.getSitebylatlang(lat, lng));
	}

	@GetMapping("/map/station/{uid}")
	public ResponseEntity<List<Map<String, Object>>> getMapStation(@PathVariable String uid,
			@RequestParam String userTimeZone, @RequestParam String userStandardTime,
			@RequestParam(required = true) String correlationID)
			throws ServerException, UserNotFoundException, JsonMappingException, JsonProcessingException {

		logger.debug("CommonController.getLangs() -  []");
		if (!correlationID.equals(mapaccesstoken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(commonService.getMapFilterByUid(uid, userTimeZone, userStandardTime));

	}

	@ApiOperation(value = "Get All Sitetiming Based On siteid")
	@RequestMapping(value = "/site/sitetiming/{siteId}", method = RequestMethod.GET)
	public ResponseEntity<List<SiteTiming>> getSiteTimingBySiteId(@PathVariable long siteId,
			@RequestParam String timezone, @RequestParam(required = true) String correlationID)
			throws UserNotFoundException {

		if (!correlationID.equals(mapaccesstoken)) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ArrayList<>());
		}
		logger.debug("StationController.getSiteTimingBySiteId() -   [" + siteId + "]");

		return ResponseEntity.status(HttpStatus.OK).body(commonService.getSiteTimingBySiteId(siteId, timezone));
	}

}
