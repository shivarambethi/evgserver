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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.form.StripeForm;
import com.evgateway.server.messages.ResponseMessage;
import com.evgateway.server.services.PaymentService;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Hidden
@Scope("request")
@RequestMapping(value = "/payment", produces = { "application/json" })

public class PaymentController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	//@ApiOperation(value = "add Card")
	@RequestMapping(value = "/addcard/{useruid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> addCard(@RequestBody StripeForm stripe, @PathVariable String useruid)
			throws Exception {
		logger.info("PaymentController.addCard() - by [" + stripe + "]");
		paymentService.addCard(stripe, useruid);
		return ResponseEntity.ok().body(new ResponseMessage("Card successfully added"));
	}

	@RequestMapping(value = "/addAutoReload/{useruid}", params = { "amount", "lowBalance",
			"cardNo" }, method = RequestMethod.GET)
	public ResponseEntity<?> addAutoReload(@RequestParam("amount") Long amount,
			@RequestParam("lowBalance") Long lowBalance, @RequestParam("cardNo") String cardNo,
			@PathVariable String useruid) throws Exception {
		logger.info("PaymentController.addAutoReload() - []");
		return new ResponseEntity<>(paymentService.addAutoReload(amount, lowBalance, cardNo, useruid), HttpStatus.OK);
	}

	//@ApiOperation(value = "add Card")
	@RequestMapping(value = "/addbalance/{useruid}", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> addBalance(@RequestBody StripeForm stripe, @PathVariable String useruid)
			throws Exception {
		logger.info("PaymentController.addBalance() - by [" + stripe + "]");
		paymentService.addBalance(stripe, useruid);
		return ResponseEntity.ok().body(new ResponseMessage("Balance successfully added"));
	}

	@RequestMapping(value = "/card/{id}/{cardId}/{uuid}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCard(@PathVariable long id,@PathVariable String cardId,@PathVariable String uuid) throws DuplicateUserException, UserNotFoundException, ServerException {
		logger.info("deleteEV API :" + id);
		return new ResponseEntity<>(paymentService.deleteCard(id,cardId,uuid), HttpStatus.OK);
	}
//Payment Intent 
	
	
	@PostMapping("/usercards")
	public ResponseEntity<List<Map<String, Object>>> getUserCards(@RequestBody Map<String, Object> mapData)
			throws UserNotFoundException, ServerException, IOException, ParseException {

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getUserCards(mapData));
	}
	
	
	@PostMapping("/paymentIntent/create")
	public ResponseEntity<List<Map<String, Object>>> getPaymentIntenent(
			@RequestBody PaymentIntentForm paymentIntentForm)
			throws ParseException, UserNotFoundException, ServerException, IOException {

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentIntenent(paymentIntentForm));
	}

	
	@PostMapping("/paymentIntent/cancelAuthorization")
	public ResponseEntity<ResponseMessage> cancelAuthorization(@RequestBody PaymentIntentForm paymentIntentForm)
			throws ParseException, UserNotFoundException, ServerException, IOException {
		paymentService.cancelAuthorization(paymentIntentForm);
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Card added successfully"));
	}

	@PutMapping("/setdefaultcard")
	public ResponseEntity<ResponseMessage> setDefulatCard(@RequestBody Map<String, Object> mapData) throws Exception {

		String msg = paymentService.setDefulatCard(mapData);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
	}

	@PostMapping("/deletecard")
	public ResponseEntity<ResponseMessage> deleteCard(@RequestBody Map<String, Object> mapData)
			throws ParseException, UserNotFoundException, ServerException, IOException {

		System.out.println(mapData);
		paymentService.deleteCard(mapData);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Card deleted successfully"));
	}
	@RequestMapping(value = "/getStripeKey/{uid}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getStripeKeys(@PathVariable String uid)
			throws UserNotFoundException {

		logger.info("getStripeKeys.....");

		return ResponseEntity.status(HttpStatus.OK).body(paymentService.getStripeKeys(uid));
	}
	
}
