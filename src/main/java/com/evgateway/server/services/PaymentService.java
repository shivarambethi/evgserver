package com.evgateway.server.services;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.form.StripeForm;

@Transactional(propagation = Propagation.REQUIRED)
public interface PaymentService {

	void addCard(StripeForm stripe, String useruid) throws UserNotFoundException, ServerException;

	Object addAutoReload(Long amount, Long lowBalance, String cardNo, String useruid)
			throws UserNotFoundException, ParseException, ServerException;

	void addBalance(StripeForm stripe, String useruid) throws UserNotFoundException, ServerException;

	Object deleteCard(long id, String cardId, String uuid) throws UserNotFoundException, ServerException;

	List<Map<String, Object>> getUserCards(Map<String, Object> mapData);

	void deleteCard(Map<String, Object> mapData) throws ServerException;

	String setDefulatCard(Map<String, Object> mapData);

	void cancelAuthorization(PaymentIntentForm paymentIntentForm);

	List<Map<String, Object>> getPaymentIntenent(PaymentIntentForm paymentIntentForm)
			throws ParseException, UserNotFoundException;

	Map<String, Object> getStripeKeys(String uid);

}
