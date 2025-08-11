package com.evgateway.server.services;

import java.util.List;
import java.util.Map;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.OcppRequestForm;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.ocpp.ResponseMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ManualPayService {

	List<Map<String, Object>> getPaymentIntenent(PaymentIntentForm paymentIntentForm);

	List<Map<String, Object>> searchStationForPayasGo(String stationName)
			throws UserNotFoundException, JsonMappingException, JsonProcessingException, ServerException;

	void authorizeGuest(PaymentIntentForm paymentIntentForm) throws ServerException;

	List<Map<String,Object>> getPortInfoById(Long id);

	ResponseMessage request(OcppRequestForm ocppFrom) throws Exception;

	List<Map<String, Object>> activeTransaction(Long id, Long orgId, String token, String phone);

	Boolean isEmailExist(String email) throws UserNotFoundException;

	List<Map<String, Object>> getChargingSessionBasedOnSessionId(String sessionId);

	List<Map<String, Object>> getSessionInfoById(String id) throws JsonMappingException, JsonProcessingException;

	Boolean isPhoneExist(String phone) throws UserNotFoundException;

	List<Map<String, Object>> chargingSession(String phone, String deviceToken, Long stationId);

	List<Map<String, Object>>   getinfoeditBySessionIds(String sessionId)throws UserNotFoundException, JsonMappingException, JsonProcessingException;

	Map<String,Object> getPortinfoMethod(String userTimeZone, String id) throws UserNotFoundException;

}
