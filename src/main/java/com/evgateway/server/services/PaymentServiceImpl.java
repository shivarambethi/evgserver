package com.evgateway.server.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.form.StripeForm;
import com.evgateway.server.messages.Error;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.AutoReload;
import com.evgateway.server.pojo.CardDelHistory;
import com.evgateway.server.pojo.OCPPActiveTransaction;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.WorldPayCreditCard;
import com.evgateway.server.utils.Response;
import com.evgateway.server.utils.StatusCodes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PaymentServiceImpl implements PaymentService {
	final static Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Value("${mobileAPIsURL}")
	private String mobileserviceurl;
	@Value("${EVG-Correlation-ID}")
	private String cooRelationId;
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Override
	public void addCard(StripeForm stripe, String useruid) throws UserNotFoundException, ServerException {
		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Organization org = generalDao.findOneSQLQuery(new Organization(),
				"select * from organization where id=" + user.getOrgId());
		String serverUrl1 = mobileserviceurl + "/api/v3/stripe/card";
		stripe.setOrgId(user.getOrgId());
		stripe.setOrgName(org.getOrgName());
		stripe.setAutoReloadFlag(false);
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Authorization", "Bearer " + stripe.getBearerToken());
		header.set("EVG-Correlation-ID", cooRelationId);

		System.out.println("header" + header);

		HttpEntity<StripeForm> requestEntity3 = new HttpEntity<>(stripe, header);
		System.out.println("requestEntity3" + requestEntity3);

		ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);
		System.out.println("BOdy :" + postForEntity.getBody());

	}

	@Override
	public Object addAutoReload(Long amount, Long lowBalance, String cardNo, String useruid)
			throws UserNotFoundException, ParseException, ServerException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(sdf.format(new Date()));
		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Accounts accounts = user.getAccount().iterator().next();

		long userId = user.getId();
		String query1 = "FROM WorldPayCreditCard w WHERE w.accountId = " + accounts.getId() + " and w.cardNo = '"
				+ cardNo + "'";
		WorldPayCreditCard cards = generalDao.findOneHQLQuery(new WorldPayCreditCard(), query1);
		String query = "From AutoReload a where a.userId=" + userId + " ";
		AutoReload autoReload = generalDao.findOneHQLQuery(new AutoReload(), query);
		if (autoReload == null) {
			autoReload = new AutoReload();
			autoReload.setAccountId(accounts.getId());
			autoReload.setAmount(amount);
			autoReload.setCardNo(cardNo);
			autoReload.setLowBalance(lowBalance);
			autoReload.setUserId(userId);
			autoReload.setFlag(true);
			autoReload.setCardId(cards.getCardId());
			autoReload.setCurrencyType(accounts.getCurrencyType());
			autoReload.setPaymentId("");
			autoReload.setPaymentType("Stripe");
			autoReload.setCreatedDate(date);
			if (isCardExpired(cards)) {
				generalDao.save(autoReload);
				accounts.setAutoReload(true);
				generalDao.update(accounts);

			} else {
				throw new ServerException(Error.Card_ExPired.toString(),
						Integer.toString(Error.Card_ExPired.getCode()));
			}
		} else {
			autoReload.setAccountId(accounts.getId());
			autoReload.setAmount(amount);
			autoReload.setCardNo(cardNo);
			autoReload.setLowBalance(lowBalance);
			autoReload.setUserId(userId);
			autoReload.setFlag(true);
			autoReload.setCardId(cards.getCardId());
			autoReload.setCurrencyType(accounts.getCurrencyType());
			autoReload.setPaymentType("Stripe");
			autoReload.setModifiedDate(date);

			if (isCardExpired(cards)) {
				generalDao.update(autoReload);
				accounts.setAutoReload(true);
				generalDao.update(accounts);

			} else {
				throw new ServerException(Error.Card_ExPired.toString(),
						Integer.toString(Error.Card_ExPired.getCode()));
			}

		}
		return null;

	}

	public boolean isCardExpired(WorldPayCreditCard cards) {
		LocalDateTime now = LocalDateTime.now();
		if (Integer.parseInt(cards.getExpiryYear().substring(cards.getExpiryYear().length() - 2)) > Integer
				.parseInt(String.valueOf(now.getYear()).substring(2))) {
			return true;
		} else if (Integer.parseInt(cards.getExpiryYear().substring(cards.getExpiryYear().length() - 2)) == Integer
				.parseInt(String.valueOf(now.getYear()).substring(2))) {
			if (Integer.parseInt(cards.getExpiryMonth()) >= now.getMonth().getValue()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public void addBalance(StripeForm stripe, String useruid) throws UserNotFoundException, ServerException {
		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Organization org = generalDao.findOneSQLQuery(new Organization(),
				"select * from organization where id=" + user.getOrgId());
		String serverUrl1 = mobileserviceurl + "/api/v3/stripe/addAmountWithExistingCard";
		stripe.setOrgId(user.getOrgId());
		stripe.setOrgName(org.getOrgName());
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Authorization", "Bearer " + stripe.getBearerToken());
		header.set("EVG-Correlation-ID", cooRelationId);

		System.out.println("header" + header);

		HttpEntity<StripeForm> requestEntity3 = new HttpEntity<>(stripe, header);
		System.out.println("requestEntity3" + requestEntity3);

		ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);
		System.out.println("BOdy :" + postForEntity.getBody());

	}

	@Override
	public Object deleteCard(long id, String cardId, String uuid) throws UserNotFoundException, ServerException {

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + uuid + "'");
		Accounts acc = generalDao.findOneSQLQuery(new Accounts(),
				"Select * from accounts where user_id = " + user.getId() + "");
		String query1 = "FROM AutoReload WHERE cardId = '" + cardId + "' and accountId = " + acc.getId()
				+ " and flag = 1";
		AutoReload autoReloadData = generalDao.findOneHQLQuery(new AutoReload(), query1);
		if (autoReloadData == null) {
			String query = "FROM WorldPayCreditCard w WHERE w.accountId = " + acc.getId();
			List<WorldPayCreditCard> cardsList = generalDao.findAllHQLQuery(new WorldPayCreditCard(), query);
			System.out.println(cardsList.size());
			if (cardsList.size() == 3) {

				throw new ServerException(Error.One_Card.toString(), Integer.toString(Error.One_Card.getCode()));

			} else {
				String query2 = "FROM WorldPayCreditCard w WHERE  w.cardId = '" + cardId + "' and w.accountId = "
						+ acc.getId();
				WorldPayCreditCard cardsData = generalDao.findOneHQLQuery(new WorldPayCreditCard(), query2);

				if (cardsData == null) {
					throw new ServerException(Error.Card_NoT_exist.toString(),
							Integer.toString(Error.Card_NoT_exist.getCode()));
				} else {
					if (acc.getAccountBalance() < 0)
						throw new ServerException(Error.Negative_balance.toString(),
								Integer.toString(Error.Negative_balance.getCode()));
					List<OCPPActiveTransaction> activeTransactionList = new ArrayList<>();
					String queryActive = "Select * From OCPP_ActiveTransaction oc where oc.userId = "
							+ acc.getUser().getId() + " and oc.status ='Charging' order by oc.id desc";
					activeTransactionList = generalDao.findAllSQLQuery(new OCPPActiveTransaction(), queryActive);
					if (activeTransactionList.size() > 0)
						throw new ServerException(Error.Session_Going_On.toString(),
								Integer.toString(Error.Session_Going_On.getCode()));

					CardDelHistory cardDel = new CardDelHistory();
					cardDel.setAccountId(cardsData.getAccountId());
					cardDel.setCardNo(cardsData.getCardNo());
					cardDel.setCardType(cardsData.getCardType());
					cardDel.setCreatedDate(new Date());
					cardDel.setExpiryMonth(cardsData.getExpiryMonth());
					cardDel.setExpiryYear(cardsData.getExpiryYear());
					cardDel.setPaymentAccountId(cardsData.getCardId());
					cardDel.setPaymentType("Stripe");
					generalDao.save(cardDel);

					String queryForDel = "Delete from autoReload where cardId = '" + cardId + "' and accountId = "
							+ acc.getId() + "";
					generalDao.queryExecute(queryForDel);
					generalDao.delete(cardsData);
					return new Response<WorldPayCreditCard>(null, StatusCodes.SUCCESS, "Removed Succesfully",
							new Date());
				}

			}
		} else {
			throw new ServerException(Error.AutoReload_Exist.toString(),
					Integer.toString(Error.AutoReload_Exist.getCode()));
		}
	}

	@Override
	public List<Map<String, Object>> getUserCards(Map<String, Object> mapData) {
		// TODO Auto-generated method stub

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {

			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.set("Authorization", "Bearer " + mapData.get("token"));
			header.set("EVG-Correlation-ID", cooRelationId);

			HttpEntity<String> entity = new HttpEntity<>(header);

			String url = mobileserviceurl + "/api/v3/stripe/paymentIntent/cards?uuid=" + mapData.get("uid");
			ResponseEntity<Response> response = restTemplate.exchange(url, HttpMethod.GET, entity, Response.class);

			if (response.getStatusCode().equals(HttpStatus.OK))
				map = (List<Map<String, Object>>) response.getBody().getData();
			LOGGER.info("DriverserviceImpl.getUserCards : {}", map);
		} catch (Exception e) {
			LOGGER.info("DriverserviceImpl.getUserCards error : {}", e.getMessage());
		}

		return map == null ? new ArrayList<Map<String, Object>>() : map;
	}

	@Override
	public List<Map<String, Object>> getPaymentIntenent(PaymentIntentForm paymentIntentForm)
			throws ParseException, UserNotFoundException {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		LOGGER.info("ManualPayServiceImpl : getPaymentIntenent : {}" + paymentIntentForm);
		Response<?> response = null;
		User user = getUserByUID(paymentIntentForm.getUuid());
		Map<String, Object> mapsecond = new HashMap<String, Object>();

		try {
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.set("Authorization", "Bearer " + paymentIntentForm.getBearerToken());
			header.set("EVG-Correlation-ID", cooRelationId);
			paymentIntentForm.setTransactionType("Card");
			paymentIntentForm.setAmount(1);
			paymentIntentForm.setCurrencyCode(user.getAccount().iterator().next().getCurrencyType());
			paymentIntentForm.setEmail(user.getEmail());
			paymentIntentForm.setCountryCode(user.getAddress().iterator().next().getCountryCode());
			paymentIntentForm.setPhone(user.getAddress().iterator().next().getPhone());
			paymentIntentForm.setOrgId(user.getOrgId());
			HttpEntity<PaymentIntentForm> requestEntity3 = new HttpEntity<>(paymentIntentForm, header);
			System.out.println("requestEntity3" + requestEntity3);

			response = restTemplate.postForObject(mobileserviceurl + "/api/v3/stripe/paymentIntent/create",
					requestEntity3, Response.class);
			System.out.println("response" + response);

			if (response.getStatus_code() == 200 && response.data != null) {

				LOGGER.info("ManualPayServiceImpl : getPaymentIntenent : Success  {}" + response);
				ObjectMapper mapper = new ObjectMapper();
				mapsecond = mapper.convertValue(response.getData(), new TypeReference<Map<String, Object>>() {
				});

			} else
				LOGGER.info("ManualPayServiceImpl : getPaymentIntenent : failed  {}" + response);
		} catch (Exception e) {
			// TODO Auto-generated catch block

			LOGGER.info(" getPaymentIntenent  exception message : {} ", e.getMessage());
			e.printStackTrace();
		}

		map.add(mapsecond);
		mapsecond.put("message",
				response.getStatus_message() == null ? "Payment intent create failed" : response.getStatus_message());
		return map;

	}

	public User getUserByUID(String uid) throws UsernameNotFoundException {

		LOGGER.info("UserServiceImpl.getUserByUID() - [" + uid + "]");
		User userDetails = null;
		try {
			userDetails = generalDao.findOneHQLQuery(new User(), "From User where uid='" + uid + "'");

			LOGGER.info("user object {}", userDetails.toString());
			return userDetails;
		} catch (UserNotFoundException e) {
			UserServiceImpl.LOGGER.info("Invalid User Details : " + uid);
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public void cancelAuthorization(PaymentIntentForm paymentIntentForm) {
		User user = getUserByUID(paymentIntentForm.getUuid());

		try {
			paymentIntentForm.setDeviceType("Portal");
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.set("Authorization", "Bearer " + paymentIntentForm.getBearerToken());
			header.set("EVG-Correlation-ID", cooRelationId);
			paymentIntentForm.setCurrencyCode(user.getAccount().iterator().next().getCurrencyType());
			HttpEntity<PaymentIntentForm> requestEntity3 = new HttpEntity<>(paymentIntentForm, header);
			System.out.println("requestEntity3" + requestEntity3);

			restTemplate.postForObject(mobileserviceurl + "/api/v3/stripe/paymentIntent/cancelAuthorization",
					requestEntity3, Response.class);

		} catch (Exception e) {

			LOGGER.info(" getPaymentIntenent  exception message : {} ", e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public String setDefulatCard(Map<String, Object> mapData) {
		// TODO Auto-generated method stub
		try {
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			header.set("Authorization", "Bearer " + mapData.get("token"));
			header.set("EVG-Correlation-ID", cooRelationId);

			HttpEntity<Map<String, Object>> requestEntity3 = new HttpEntity<>(mapData, header);
			System.out.println("requestEntity3" + requestEntity3);

			restTemplate.exchange(mobileserviceurl + "/api/v3/stripe/paymentIntent/defaultCard", HttpMethod.PUT,
					requestEntity3, Response.class);

		} catch (Exception e) {

			LOGGER.info(" getPaymentIntenent  exception message : {} ", e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void deleteCard(Map<String, Object> mapData) throws ServerException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Authorization", "Bearer " + mapData.get("token"));
		header.set("EVG-Correlation-ID", cooRelationId);

		HttpEntity<Map<String, Object>> requestEntity3 = new HttpEntity<>(header);
		System.out.println("deleteCardrequestEntity3" + requestEntity3);
		ResponseEntity<Response> response = null;
		response = restTemplate.exchange(mobileserviceurl + "/api/v3/stripe/paymentIntent/card?uuid="
				+ mapData.get("uuid") + "&cardId=" + mapData.get("cardId"), HttpMethod.DELETE, requestEntity3,
				Response.class);

		if (response.getBody().getStatus_code() != 200) {
			throw new ServerException(Error.Delete_Card_Failed.toString(),
					Integer.toString(Error.Delete_Card_Failed.getCode()));
		}

	}

	@Override
	public Map<String, Object> getStripeKeys(String uid) {
		User user = getUserByUID(uid);
		String query = "";
		if (user.getAccount().iterator().next().getCurrencyType().equalsIgnoreCase("USD"))
			query = "select stripe_USD_PublicKey from paymentKeys";
		else
			query = "select stripe_CAD_PublicKey as stripe_USD_PublicKey from paymentKeys";

		return generalDao.findAliasData(query).get(0);
	}

}
