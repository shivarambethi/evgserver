package com.evgateway.server.services;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.OcppRequestForm;
import com.evgateway.server.form.PaymentIntentForm;
import com.evgateway.server.messages.Error;
import com.evgateway.server.mobile.model.Response;
import com.evgateway.server.ocpp.RemoteStart;
import com.evgateway.server.ocpp.RemoteStop;
import com.evgateway.server.ocpp.ResponseMessage;
import com.evgateway.server.pojo.Logo;
import com.evgateway.server.pojo.OCPPActiveTransaction;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.Site;
import com.evgateway.server.pojo.TOUProfile;
import com.evgateway.server.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ManualPayServiceImpl implements ManualPayService {

	final static Logger LOGGER = LoggerFactory.getLogger(ManualPayServiceImpl.class);

	@Value("${mobileAPIsURL}")
	private String mobileAPIsURL;

	@Value("${ocpp.url}")
	private String ocppurl;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getPaymentIntenent(PaymentIntentForm paymentIntentForm) {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		LOGGER.info("ManualPayServiceImpl : getPaymentIntenent : {}" + paymentIntentForm);
		Response<?> response = null;

		Map<String, Object> mapsecond = new HashMap<String, Object>();
		try {

			response = sendDataViaPOST(paymentIntentForm, "/api/v3/guest/stripe/paymentIntent/create");
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
		return map;

	}

	@Override
	public List<Map<String, Object>> searchStationForPayasGo(String stationName)
			throws UserNotFoundException, JsonMappingException, JsonProcessingException, ServerException {
		// TODO Auto-generated method stub

		if (stationName != null && stationName.contains("'"))
			stationName = stationName.replace("'", "");

		List<Map<String, Object>> listStation = new ArrayList<Map<String, Object>>();

		String query = "DECLARE @json nvarchar(max);  WITH src (n) AS (  SELECT st.siteId ,st.id ,"
				+ "si.publicAccess as 'site.publicAccess', si.privateAccess as 'site.privateAccess',si.fleetAccess as 'site.fleetAccess',"
				+ "    st.referNo,st.evseType ,st.powerSharing, st.stationMode,"
				+ "   ISNULL((select CONCAT(l.streetName,',', l.city,', ', l.state,' ,' ,l.country,',' ,l.postal_code)   from location l where l.id =st.locationId) ,'-') AS stationAddress,"
				+ "    st.uid,\r\n" + "    st.stationName,\r\n" + "    st.chagerType,\r\n"
				+ "    (        SELECT  pt.id,   pt.displayName,ISNULL(sn.status,'') AS  status,\r\n"
				+ "	 (SELECT displayName FROM connectorType where id=pt.standard ) AS connectorType   FROM\r\n"
				+ "            port pt  inner join  statusNotification sn on sn.port_id=pt.id " + "        WHERE\r\n"
				+ "            pt.station_id = st.id\r\n" + "        FOR JSON PATH\r\n"
				+ "    ) AS ports,(SELECT name  FROM facility where id IN (SELECT facilityId FROM site_in_facility WHERE siteId=st.siteid) FOR JSON PATH ) AS facilities FROM\r\n"
				+ "    station st Inner join site si on si.siteid=st.siteid WHERE st.referNO LIKE '%" + stationName
				+ "%'   FOR JSON PATH) SELECT @json = src.n FROM src SELECT @json as 'loc'";

		List<Map<String, Object>> list = generalDao.findAliasData(query);
		ObjectMapper objectMapper = new ObjectMapper();

		List<Map<String, Object>> loc = new ArrayList<>();

		TypeReference<List<Map<String, Object>>> mapType = new TypeReference<List<Map<String, Object>>>() {
		};

		if (list.size() > 0 && list.get(0).get("loc") != null)
			loc = objectMapper.readValue(list.get(0).get("loc").toString(), mapType);

		if (loc.size() == 0)
			return listStation;

		listStation = loc;

		long siteId = Long.valueOf(listStation.get(0).get("siteId").toString());
		listStation.get(0).remove("siteId");

		listStation.add(getLogoImagebasedonorg(1L, 1L));
		listStation.add(getCurrency(siteId));
		// listStation.addAll(getFacilitiesBasedONSiteId(siteId));

		List<Map<String, Object>> price = new ArrayList<Map<String, Object>>();
		price = getTOUDataBasedOnStationUID(listStation.get(0).get("id").toString());

		if (price.size() != 0)
			listStation.add(price.get(0));

		String portstatus1 = "Removed";
		String portstatus2 = "SuspendedEVSE";
		String portstatus3 = "Inoperative";
		String portstatus4 = "Available";
		String portstatus5 = "Charging";
		String portstatus6 = "Blocked";
		String portstatus7 = "Planned";
		String portstatus8 = "SuspendedEV";
		String portstatus9 = "Finishing";
		String portstatus10 = "Preparing";
		String portStatus11 = "Reserved";

		String statusasone = "";
		List<String> portstatus = new ArrayList<>();
		Map<String, Object> pp = new HashMap<>();
		if (listStation.get(0).get("powerSharing").toString().equals("N")) {
			for (Map<String, Object> map : listStation) {
				List<Map<String, Object>> portdata = (List<Map<String, Object>>) map.get("ports");

				if (portdata != null && portdata.size() > 0) {
					for (Map<String, Object> map2 : portdata) {

						portstatus.add(map2.get("status").toString());

					}

				}
			}

			if (portstatus.contains(portstatus5)) {
				statusasone = portstatus5;
			} else if (portstatus.contains(portstatus9)) {
				statusasone = portstatus9;
			} else if (portstatus.contains(portstatus10)) {
				statusasone = portstatus10;
			} else if (portstatus.contains(portstatus10)) {
				statusasone = portstatus10;
			} else if (portstatus.contains(portStatus11)) {
				statusasone = portStatus11;
			} else if (portstatus.contains(portstatus4)) {
				statusasone = portstatus4;
			} else if (portstatus.contains(portstatus1)) {
				statusasone = portstatus1;
			} else if (portstatus.contains(portstatus2)) {
				statusasone = portstatus2;
			} else if (portstatus.contains(portstatus6)) {
				statusasone = portstatus6;
			} else if (portstatus.contains(portstatus7)) {
				statusasone = portstatus7;
			} else if (portstatus.contains(portstatus8)) {
				statusasone = portstatus8;
			} else if (portstatus.contains(portstatus3)) {
				statusasone = portstatus3;
			}
		}
		pp.put("portStatus", statusasone);
		System.out.println("statusasone" + statusasone);

		if (pp.size() != 0)
			listStation.add(pp);

		return listStation;

	}

	public List<Map<String, Object>> getFacilitiesBasedONSiteId(long siteId) {
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		map = generalDao.findAliasData(
				"SELECT name  FROM facility where id IN (SELECT facilityId FROM site_in_facility WHERE siteId=" + siteId
						+ ")");
		if (map.size() != 0)
			return map;
		return new ArrayList<Map<String, Object>>();
	}

	public List<Map<String, Object>> getTOUDataBasedOnStationUID(String stationUID)
			throws NumberFormatException, UserNotFoundException {
		// TODO Auto-generated method stub

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		String hql = "SELECT \r\n" + "(CASE\r\n"
				+ "    WHEN st.flat!=0 THEN convert(VARCHAR(10),st.flat, 10)+' Per Session'\r\n"
				+ "    WHEN st.time!=0 THEN \r\n" + "     CASE\r\n"
				+ "        WHEN st.timeStepSize=3600 THEN convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Hours',10)\r\n"
				+ "        WHEN st.timeStepSize=60 THEN  convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Minutes',10)\r\n"
				+ "	     ELSE convert(VARCHAR(10),st.time, 10)+ '/ '+ convert(VARCHAR(10),'Seconds',10) END\r\n"
				+ "	 WHEN st.energy!=0 THEN convert(VARCHAR(10),st.energy, 10)+'/kwh'\r\n" + "    ELSE 'NA'\r\n"
				+ "END) AS cost ,'Rate Rider   ('+convert(VARCHAR(10),st.rateRider,10) +'%) & '+\r\n"
				+ "convert(VARCHAR(255),(CASE \r\n"
				+ "    WHEN st.tax1_percentage>0 THEN CONCAT(st.tax1, ' : ', st.tax1_percentage,'%') \r\n"
				+ "    ELSE ' '\r\n" + " END ),25) +' & '+\r\n" + " convert(VARCHAR(255),(CASE \r\n"
				+ "    WHEN st.tax2_percentage>0 THEN convert(VARCHAR(10),st.tax2, 10)+ ' : '+ convert(VARCHAR(10),st.tax2_percentage,10)+'%'\r\n"
				+ "    ELSE ' '\r\n" + " END ) ,25)  AS taxprice\r\n"
				+ " FROM touprofile_for_mobile st    INNER JOIN station_in_tariff sit ON  sit.tariffId=st.tariff_id  INNER JOIN station stn ON stn.id=sit.stationId WHERE  stn.id ="
				+ stationUID + "";

		map = generalDao.findAliasData(hql);

		return map;
	}

	public Map<String, Object> getLogoImagebasedonorg(Long orgId, Long id) throws UserNotFoundException {
		Map<String, Object> map2 = new HashMap<>();

		Organization org = generalDao.findOneHQLQuery(new Organization(), "From Organization where id =" + orgId);

		if (org.isWhiteLabel())
			orgId = org.getId();

		List<Logo> logoImages = generalDao.findAllHQLQuery(new Logo(),
				"From Logo where createdBy in('Dealer') AND  logoType = 'mini' AND orgId=" + orgId);

		Logo dealerLogo = generalDao.findOneSQLQuery(new Logo(),
				"SELECT * FROM logo_image WHERE createdBy='Dealer' AND logoType = 'main' AND orgId IN "
						+ "(SELECT orgId  from dealer_in_org WHERE dealerId IN "
						+ "(Select Top 1 dealerId FROM owner_in_dealer WHERE ownerId IN "
						+ "(SELECT TOP 1 ownerId FROM owner_in_org WHERE orgId IN "
						+ "( SELECT org from site where siteid in (Select siteid from station where id = " + id
						+ ")))))");

		if (dealerLogo == null)
			dealerLogo = generalDao.findOneSQLQuery(new Logo(),
					"SELECT * FROM logo_image WHERE createdBy='Dealer' AND logoType = 'main' AND orgId =1");

		Logo ownerLogo = generalDao.findOneSQLQuery(new Logo(),
				"SELECT * FROM logo_image WHERE createdBy='Owner' AND logoType = 'main'  " + "AND orgId IN  "
						+ "(SELECT orgId FROM owner_in_org WHERE orgId IN  "
						+ "( SELECT org from site where siteid in (Select siteid from station where id =" + id
						+ ") ))");

		try {

			map2.put("thumbnail", logoImages.get(0).getUrl());
			map2.put("logo_type", logoImages.get(0).getLogoType());
			map2.put("operatedBy", dealerLogo.getUrl());
			if (ownerLogo != null)
				map2.put("ownedBy", ownerLogo.getUrl());
			else
				map2.put("ownedBy", dealerLogo.getUrl());

		} catch (HttpClientErrorException e) {
			LOGGER.info(" getLogoImagebasedonorg  exception message  : {} ", e.getMessage());
			e.printStackTrace();
		}

		return map2;
	}

	public <T> Response<?> sendDataViaPOST(T entity, String url) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		return restTemplate.postForObject(mobileAPIsURL + url, requestEntity, Response.class);
	}

	public Map<String, Object> getCurrency(long siteId) {
		// TODO Auto-generated method stub
		String sql = "select c.countryCode,c.currencyCode,c.isdCode,c.currencySymbol from country_currency c inner join site s on s.currencyType = currencyCode where s.siteId ='"
				+ siteId + "'";

		return generalDao.findAliasData(sql).get(0);
	}

	@Override
	public List<Map<String, Object>> getPortInfoById(Long id) {

		List<Map<String, Object>> portArray = new ArrayList<Map<String, Object>>();
		// TODO Auto-generated method stub
		LOGGER.info("ManualPayServiceImpl.getPortInfoById() - id [" + id + "] ");

		try {

			portArray = generalDao.findAliasData(
					"SELECT (SELECT displayName FROM connectorType WHERE id=standard) AS standardDisplayName , id,\r\n"
							+ "displayName,\r\n" + "status,\r\n" + "capacity,\r\n" + "chargerType \r\n"
							+ "  FROM  port   where id=" + id);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info(" getPortInfoById  exception message  : {} ", e.getMessage());
			return portArray;
		}

		return portArray;
	}

	@Override
	public void authorizeGuest(PaymentIntentForm paymentIntentForm) throws ServerException {
		LOGGER.info("ManualPayServiceImpl : authorizeGuest : {}", paymentIntentForm);
		Response<?> response = null;

		try {

			response = sendDataViaPOST(paymentIntentForm, "/api/v3/guest/stripe/paymentIntent/authorize");
			if (response.getStatus_code() == 200) {
				LOGGER.info("ManualPayServiceImpl : authorizeGuest : Success  {}", response);
			} else {
				LOGGER.info("ManualPayServiceImpl : authorizeGuest : Failed  {}", response);
				throw new ServerException(Error.USER_AUTHORIZATION_Failed.toString(),
						Integer.toString(Error.USER_AUTHORIZATION_Failed.getCode()));
			}

		} catch (Exception e) {
			LOGGER.info(" authorizeGuest  exception message : {} ", e.getMessage());
			throw new ServerException(Error.USER_AUTHORIZATION_Failed.toString(),
					Integer.toString(Error.USER_AUTHORIZATION_Failed.getCode()));

		}

	}

	public <T> ResponseMessage sendDataViaPOST(T entity) throws Exception {
		System.out.println("entity:" + entity);
		String url = ocppurl;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);

		ResponseMessage foo = restTemplate.postForObject(url + "/ocpp/request", requestEntity, ResponseMessage.class);

		if (foo != null)
			return foo;

		return new ResponseMessage();

	}

	@Override
	public Boolean isEmailExist(String email) throws UserNotFoundException {
		long count = generalDao
				.countSQL("select u.email from users u INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur,"
						+ " Role r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('Driver') and email='"
						+ email + "'");
		if (count != 0)
			return true;
		else
			return false;
	}

	@Override
	public Boolean isPhoneExist(String phone) throws UserNotFoundException {
		long count = generalDao.countSQL(
				"select a.phone from address a INNER JOIN profile p On p.user_id = a.user_id, Usersinroles ur,"
						+ " Role r where a.user_id =ur.user_id and ur.role_id=r.id and r.rolename in('Driver') and phone='"
						+ phone + "'");
		if (count != 0)
			return true;
		else
			return false;
	}

	@Override
	public List<Map<String, Object>> getSessionInfoById(String id)
			throws JsonMappingException, JsonProcessingException {
		// TODO Auto-generated method stub

		String sql = "SELECT  sp.cost_info,sp.transactionId,sp.metervaluesCount, s.id, GETUTCDATE() AS currentDate,s.kilowattHoursUsed, s.startTimeStamp ,s.sessionId ,s.endTimeStamp,\r\n"
				+ " s.sessionElapsedInMin,s.cost as cost, s.finalCostInSlcCurrency as finalcost ,st.referNo, st.stationName, st.chagerType, p.capacity\r\n"
				+ " , ct.displayName AS connectorType, p.displayName, ISNULL((SELECT CONCAT(streetName,' ',city,' ',state,' ',country,' ',postal_code )   FROM location WHERE id =st.locationId),'-' ) AS  stationAddress, o.orgName,s.sessionStatus   \r\n"
				+ " FROM session s  INNER Join port p ON p.id =s.port_id  \r\n"
				+ " INNER JOIN connectorType ct ON ct.id=p.standard \r\n"
				+ " INNER JOIN station st ON st.id = p.station_id  \r\n"
				+ " INNER JOIN site sit ON sit.siteId = st.siteId \r\n"
				+ "INNER JOIN session_pricings sp ON sp.sessionId=s.sessionId\r\n"
				+ "INNER JOIN organization o ON o.id = sit.org  WHERE s.sessionId ='" + id + "' ";

		List<Map<String, Object>> findAliasData = generalDao.findAliasData(sql);
		ObjectMapper objectMapper = new ObjectMapper();

		if (findAliasData.size() > 0 && findAliasData.get(0).get("cost_info") != null) {
			JsonNode costInfoJson = objectMapper.readTree(findAliasData.get(0).get("cost_info").toString());
			for (JsonNode element : costInfoJson) {

				Map<String, Object> elementMap = objectMapper.convertValue(element, Map.class);
				findAliasData.get(0).put("cost_info", elementMap);
			}

		}

		if (findAliasData.size() > 0)
			return findAliasData;

		return new ArrayList<Map<String, Object>>();
	}

	@Override
	public List<Map<String, Object>> chargingSession(String phone, String deviceToken, Long stationId) {

		// TODO Auto-generated method stub
		LOGGER.info("ManualPayServiceImpl.chargingSession() - phone [" + phone + "] ");

		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();

		try {

			List<Map<String, Object>> preAuths = generalDao
					.findAliasData("Select TOP 1 * From userPayment p where p.phone='" + phone + "' AND p.uid='"
							+ deviceToken + "' order by p.id desc");

			if (preAuths.size() > 0) {
				Map<String, Object> chargingSession = new HashMap<String, Object>();

				List<OCPPActiveTransaction> ocppActives = generalDao.findAllSQLQuery(new OCPPActiveTransaction(),
						"Select * from ocpp_activeTransaction where rfid='" + phone + "' And stationId=" + stationId);

				if (ocppActives.size() > 0) {

					for (OCPPActiveTransaction ocppActive : ocppActives) {

						Map<String, Object> session = generalDao.findAliasData(
								"Select * From session s where s.sessionId = '" + ocppActive.getSessionId() + "'")
								.get(0);

						Map<String, Object> port = generalDao
								.findAliasData("Select * FROM Port Where id =" + ocppActive.getConnectorId()).get(0);

						Map<String, Object> stn = generalDao
								.findAliasData(" select s.* ,CONCAT(l.streetName,',', l.city,', ', l.state,' ,' ,l.country,',' ,l.postal_code ) "
										+ "   as stationAddressofloc from station s "
										+ " left JOIN location l ON  l.id =s.locationId Where s.id =" + ocppActive.getStationId()).get(0);
						
						Map<String, Object> statusNotificationMap = generalDao
								.findAliasData("SELECT  * FROM statusNotification WHERE port_id=" + port.get("id"))
								.get(0);

						Map<String, Object> station = new HashMap<String, Object>();
						station.put("stationId", stn.get("id"));
						station.put("referNo", stn.get("referNo"));
						station.put("stationName", stn.get("stationName"));
						station.put("stationAddress", stn.get("stationAddressofloc"));
						station.put("stationType", stn.get("stationType"));

						Map<String, Object> statusNot = new HashMap<String, Object>();
						statusNot.put("id", statusNotificationMap.get("id"));
						statusNot.put("status", statusNotificationMap.get("status"));
						statusNot.put("stationId", stn.get("id"));
						chargingSession.put("sessionId", ocppActive.getSessionId());

						if (session != null) {

							chargingSession.put("sessionId", session.get("sessionId"));
							chargingSession.put("duration", session.get("sessionElapsedInMin"));
							chargingSession.put("kwh", session.get("kilowattHoursUsed"));
							chargingSession.put("spent", session.get("finalCostInSlcCurrency"));
							chargingSession.put("miles",
									Double.valueOf(session.get("kilowattHoursUsed").toString()) * 0);
							chargingSession.put("cost", session.get("cost"));
							chargingSession.put("transactionFee", session.get("transactionFee"));
							chargingSession.put("portId", port.get("id"));
							chargingSession.put("phone", session.get("customerId"));
							chargingSession.put("sessionId", session.get("sessionId"));
							chargingSession.put("soc", session.get("socEndVal"));
							chargingSession.put("connectorId", port.get("connector_id"));
							chargingSession.put("sessionStatus", session.get("sessionstatus"));
							String chargerType = "DC";
							if (port.get("chargerType").toString().equalsIgnoreCase("AC")) {
								chargerType = "AC";
							}
							chargingSession.put("chargerType", chargerType);
							finalData.add(chargingSession);
							finalData.add(station);
							finalData.add(statusNot);
							return finalData;
						} else {
							chargingSession.put("sessionId", ocppActive.getSessionId());
							chargingSession.put("duration", 0);
							chargingSession.put("kwh", 0);
							chargingSession.put("spent", 0);
							chargingSession.put("miles", 0);
							chargingSession.put("cost", 0);
							chargingSession.put("transactionFee", 0);
							chargingSession.put("portId", ocppActive.getConnectorId());
							chargingSession.put("phone", phone);
							String chargerType = "DC";
							if (port.get("chargerType").toString().equalsIgnoreCase("AC")) {
								chargerType = "AC";
							}
							chargingSession.put("chargerType", chargerType);
							chargingSession.put("connectorId", port.get("connector_id"));

							finalData.add(chargingSession);
							finalData.add(station);
							finalData.add(statusNot);

							return finalData;
						}
					}

				}
				return finalData;
			} else {

				Map<String, Object> chargingSession = new HashMap<String, Object>();

				List<OCPPActiveTransaction> ocppActives = generalDao.findAllSQLQuery(new OCPPActiveTransaction(),
						"Select * from ocpp_activeTransaction where rfid='" + phone + "' And stationId=" + stationId);

				if (ocppActives.size() > 0) {

					for (OCPPActiveTransaction ocppActive : ocppActives) {

						Map<String, Object> session = generalDao.findAliasData(
								"Select * From session s where s.sessionId = '" + ocppActive.getSessionId() + "'")
								.get(0);

						Map<String, Object> port = generalDao
								.findAliasData("Select * FROM Port Where id =" + ocppActive.getConnectorId()).get(0);

						Map<String, Object> stn = generalDao
								.findAliasData(" select s.* ,CONCAT(l.streetName,',', l.city,', ', l.state,' ,' ,l.country,',' ,l.postal_code ) "
										+ "   as stationAddressofloc from station s "
										+ " left JOIN location l ON  l.id =s.locationId Where s.id =" + ocppActive.getStationId()).get(0);
						Map<String, Object> statusNotificationMap = generalDao
								.findAliasData("SELECT  * FROM statusNotification WHERE port_id=" + port.get("id"))
								.get(0);

						Map<String, Object> station = new HashMap<String, Object>();
						station.put("stationId", stn.get("id"));
						station.put("referNo", stn.get("referNo"));
						station.put("stationName", stn.get("stationName"));
						station.put("stationAddress", stn.get("stationAddressofloc"));
						station.put("stationType", stn.get("stationType"));

						Map<String, Object> statusNot = new HashMap<String, Object>();
						statusNot.put("id", statusNotificationMap.get("id"));
						statusNot.put("status", statusNotificationMap.get("status"));
						statusNot.put("stationId", stn.get("id"));
						chargingSession.put("sessionId", ocppActive.getSessionId());

						if (session != null) {

							chargingSession.put("sessionId", session.get("sessionId"));
							chargingSession.put("duration", session.get("sessionElapsedInMin"));
							chargingSession.put("kwh", session.get("kilowattHoursUsed"));
							chargingSession.put("spent", session.get("finalCostInSlcCurrency"));
							chargingSession.put("miles",
									Double.valueOf(session.get("kilowattHoursUsed").toString()) * 0);
							chargingSession.put("cost", session.get("cost"));
							chargingSession.put("transactionFee", session.get("transactionFee"));
							chargingSession.put("portId", port.get("id"));
							chargingSession.put("phone", session.get("customerId"));
							chargingSession.put("sessionId", session.get("sessionId"));
							chargingSession.put("soc", session.get("socEndVal"));
							chargingSession.put("connectorId", port.get("connector_id"));
							chargingSession.put("sessionStatus", session.get("sessionstatus"));
							String chargerType = "DC";
							if (port.get("chargerType").toString().equalsIgnoreCase("AC")) {
								chargerType = "AC";
							}
							chargingSession.put("chargerType", chargerType);
							finalData.add(chargingSession);
							finalData.add(station);
							finalData.add(statusNot);
							return finalData;
						} else {
							chargingSession.put("sessionId", ocppActive.getSessionId());
							chargingSession.put("duration", 0);
							chargingSession.put("kwh", 0);
							chargingSession.put("spent", 0);
							chargingSession.put("miles", 0);
							chargingSession.put("cost", 0);
							chargingSession.put("transactionFee", 0);
							chargingSession.put("portId", ocppActive.getConnectorId());
							chargingSession.put("phone", phone);
							String chargerType = "DC";
							if (port.get("chargerType").toString().equalsIgnoreCase("AC")) {
								chargerType = "AC";
							}
							chargingSession.put("chargerType", chargerType);
							chargingSession.put("connectorId", port.get("connector_id"));

							finalData.add(chargingSession);
							finalData.add(station);
							finalData.add(statusNot);

						}
					}

				}
				return finalData;

			}

		} catch (Exception e) {
			// TODO: handle exception
			return finalData;
		}

	}

	@Override
	public List<Map<String, Object>> getChargingSessionBasedOnSessionId(String sessionId) {
		// TODO Auto-generated method stub

		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();
		try {

			Map<String, Object> chargingSession = new HashMap<String, Object>();

			String sessionDataQuery = "SELECT * From session s where s.sessionId = '" + sessionId + "'";
			List<Map<String, Object>> session = generalDao.findAliasData(sessionDataQuery);

			if (session == null) {
				String startTransactionDataQuery = "SELECT * FROM ocpp_startTransaction u where u.sessionId = '"
						+ sessionId + "'";

				List<Map<String, Object>> startTransactionData = generalDao.findAliasData(startTransactionDataQuery);

				List<Map<String, Object>> portsData = generalDao
						.findAliasData("SELECT * FROM port where id=" + startTransactionData.get(0).get("connectorId"));
				String stationDataQuery = "SELECT * FROM station st where st.id = "
						+ startTransactionData.get(0).get("stationId");

				List<Map<String, Object>> stationData = generalDao.findAliasData(stationDataQuery);

				Map<String, Object> station = new HashMap<String, Object>();

				List<Map<String, Object>> statusData = generalDao
						.findAliasData("SELECT * FROM statusNotification WHERE port_id=" + portsData.get(0).get("id"));

				Map<String, Object> statusNot = new HashMap<String, Object>();
				statusNot.put("id", statusData.get(0).get("id"));
				statusNot.put("stationId", stationData.get(0).get("id"));
				statusNot.put("status", statusData.get(0).get("status"));

				station.put("stationId", stationData.get(0).get("id"));
				station.put("referNo", stationData.get(0).get("referNo"));
				station.put("stationName", stationData.get(0).get("stationName"));
				station.put("stationAddress", stationData.get(0).get("stationAddress"));
				station.put("stationType", stationData.get(0).get("stationType"));

				chargingSession.put("sessionId", startTransactionData.get(0).get("sessionId"));
				chargingSession.put("duration", 0);
				chargingSession.put("kwh", 0);
				chargingSession.put("spent", 0);
				chargingSession.put("miles", 0);
				chargingSession.put("cost", 0);
				chargingSession.put("transactionFee", 0);
				chargingSession.put("portId", portsData.get(0).get("id"));
				chargingSession.put("phone", startTransactionData.get(0).get("idTag"));
				chargingSession.put("sessionId", startTransactionData.get(0).get("sessionId"));

				String chargerType = "DC";
				if (portsData.get(0).get("chargerType").toString().equalsIgnoreCase("AC")) {
					chargerType = "AC";
				}
				chargingSession.put("chargerType", chargerType);
				chargingSession.put("connectorId", portsData.get(0).get("connector_id"));

				finalData.add(chargingSession);
				finalData.add(station);
				finalData.add(statusNot);
				return finalData;

			} else {

				List<Map<String, Object>> portsData = generalDao
						.findAliasData("SELECT * FROM port where id=" + session.get(0).get("port_id"));

				Long stationId = Long.valueOf(portsData.get(0).get("station_id").toString());
				List<Map<String, Object>> stationData = generalDao
						.findAliasData("SELECT * FROM station WHERE id=" + stationId);

				Map<String, Object> station = new HashMap<String, Object>();

				station.put("stationId", stationData.get(0).get("id"));

				station.put("stationId", stationData.get(0).get("id"));
				station.put("referNo", stationData.get(0).get("referNo"));
				station.put("stationName", stationData.get(0).get("stationName"));
				station.put("stationAddress", stationData.get(0).get("stationAddress"));
				station.put("stationType", stationData.get(0).get("stationType"));

				List<Map<String, Object>> statusData = generalDao
						.findAliasData("SELECT * FROM statusNotification WHERE port_id=" + portsData.get(0).get("id"));

				Map<String, Object> statusNot = new HashMap<String, Object>();
				statusNot.put("id", statusData.get(0).get("id"));
				statusNot.put("stationId", stationData.get(0).get("id"));
				statusNot.put("status", statusData.get(0).get("status"));

				chargingSession.put("sessionId", session.get(0).get("sessionId"));
				chargingSession.put("duration", session.get(0).get("sessionElapsedInMin"));
				chargingSession.put("kwh", session.get(0).get("kilowattHoursUsed"));
				chargingSession.put("spent", session.get(0).get("finalCostInSlcCurrency"));
				chargingSession.put("miles", Double.valueOf(session.get(0).get("kilowattHoursUsed").toString()) * 0);
				chargingSession.put("cost", session.get(0).get("cost"));
				chargingSession.put("transactionFee", session.get(0).get("transactionFee"));
				chargingSession.put("portId", portsData.get(0).get("id"));
				chargingSession.put("phone", session.get(0).get("customerId"));
				chargingSession.put("sessionId", session.get(0).get("sessionId"));
				chargingSession.put("soc", session.get(0).get("socEndVal"));
				chargingSession.put("connectorId", portsData.get(0).get("connector_id"));
				chargingSession.put("sessionStatus", session.get(0).get("sessionstatus"));

				String chargerType = "DC";
				if (portsData.get(0).get("chargerType").toString().equalsIgnoreCase("AC")) {
					chargerType = "AC";
				}
				chargingSession.put("chargerType", chargerType);
				finalData.add(chargingSession);
				finalData.add(station);
				finalData.add(statusNot);
				return finalData;

			}

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("getChargingSessionBasedOnSessionId occured : {} ", e.getMessage());
			return finalData;
		}

	}

	@Override
	public ResponseMessage request(OcppRequestForm ocppFrom) throws Exception {
//		long connectorId = generalDao.findIdBySqlQuery("select id from port where station_id=" + ocppFrom.getStationId()
//				+ " and connector_id= " + ocppFrom.getConnectorId());
		LOGGER.info("OCPP Request : {}", ocppFrom);
		List<Map<String, Object>> stn = generalDao
				.findAliasData("select * from station where id=" + ocppFrom.getStationId());
		boolean poweraSharing = true;
		long connectorId = ocppFrom.getConnectorId();
		if (stn.get(0).get("powerSharing").equals("N")) {
			List<Map<String, Object>> ocppactive = generalDao
					.findAliasData(" select * from ocpp_activeTransaction where stationid=" + ocppFrom.getStationId());
			poweraSharing = false;
			if (ocppFrom.getReqType().equalsIgnoreCase("RemoteStop")
					|| ocppFrom.getReqType().equalsIgnoreCase("RemoteStopTransaction"))
				connectorId = Long.parseLong(ocppactive.get(0).get("connectorId").toString());
		}

		ResponseMessage response = null;
		if (ocppFrom.getReqType().equalsIgnoreCase("RemoteStart")
				|| ocppFrom.getReqType().equalsIgnoreCase("RemoteStartTransaction")) {
			RemoteStart remotestart = new RemoteStart(ocppFrom.getStationId(), ocppFrom.getConnectorId(),
					ocppFrom.getIdTag(), ocppFrom.getOrgId());
			remotestart.setPowerSharing(poweraSharing);
			response = sendDataViaPOST(remotestart);
		} else if (ocppFrom.getReqType().equalsIgnoreCase("RemoteStop")
				|| ocppFrom.getReqType().equalsIgnoreCase("RemoteStopTransaction")) {
			RemoteStop remoteStop = new RemoteStop(ocppFrom.getStationId(), connectorId);
			remoteStop.setPowerSharing(poweraSharing);
			response = sendDataViaPOST(remoteStop);
		}
		LOGGER.info("OCPP Response : {}", response);
		return response;
	}

	@Override
	public List<Map<String, Object>> activeTransaction(Long id, Long orgId, String token, String phone) {
		// TODO Auto-generated method stub
		LOGGER.info("ManualPayServiceImpl.activeTransaction() - id :{} token  : {} phone : {} ", id, token, phone);

		List<Map<String, Object>> portList = new ArrayList<>();

		try {

			List<Map<String, Object>> preAuths = generalDao
					.findAliasData("Select TOP 1 * From userPayment p where p.phone='" + phone + "' AND p.uid='" + token
							+ "' order by p.id desc");

			if (preAuths.size() > 0) {

				List<Map<String, Object>> ports = generalDao.findAliasData("Select * From port Where station_id=" + id);

				if (ports.size() > 0) {

					for (Map<String, Object> port : ports) {
						Map<String, Object> portsMap = new HashMap<String, Object>();
						Map<String, Object> activeMap = new HashMap<String, Object>();
						List<OCPPActiveTransaction> ocppActives = generalDao.findAllSQLQuery(
								new OCPPActiveTransaction(),
								"Select * from ocpp_activeTransaction where connectorId=" + port.get("id"));

						if (ocppActives.size() > 0) {
							for (OCPPActiveTransaction ocppAct : ocppActives) {
								if (ocppAct.getRfId().equalsIgnoreCase(phone)) {
									activeMap.put("id", ocppAct.getId());
									activeMap.put("status", ocppAct.getStatus());
								} else {
									activeMap.put("status", "Port Not Available");
								}
								activeMap.put("flag", true);
							}
						} else {
							activeMap.put("id", 0);
							activeMap.put("status", "");
							activeMap.put("flag", false);
						}

						portsMap.put("id", port.get("id"));
						portsMap.put("displayName", port.get("displayName"));
						portsMap.put("standard", port.get("standard"));
						portsMap.put("statusNotification", generalDao.findAliasData(
								"SELECT id, (case WHEN status ='Planned' THEN 'Preparing' else  status END) as status  FROM statusNotification WHERE port_id ="
										+ port.get("id"))
								.get(0));
						portsMap.put("ocppActiveTransaction", activeMap);
						portsMap.put("reservationId", 0);
						portList.add(portsMap);

					}

				}

				return portList;

			}

		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("ManualPayServiceImpl.activeTransaction() -exception message : {}", e.getMessage());
			return portList;
		}

		return portList;
	}
	@Override
	public List<Map<String, Object>> getinfoeditBySessionIds(String sessionId)
			throws JsonMappingException, JsonProcessingException {

		List<Map<String, Object>> session = generalDao
				.findAliasData("select * from session_pricings where sessionId='" + sessionId + "'");

		ObjectMapper objectMapper = new ObjectMapper();

		List<Map<String, Object>> infovalue = new ArrayList<>();
		if (session.get(0).get("cost_info") != null) {
			JsonNode costInfoJson = objectMapper.readTree(session.get(0).get("cost_info").toString());
			Map<String, Object> sessonid = new HashMap<>();
			for (JsonNode element : costInfoJson) {

				Map<String, Object> elementMap = objectMapper.convertValue(element, Map.class);
				infovalue.add(elementMap);
			}
			sessonid.put("sessionId", session.get(0).get("sessionId").toString());
			infovalue.add(sessonid);
			System.out.println("infovalue:" + infovalue);
		}

		return infovalue;
	}
	
	@Override
	public Map<String, Object> getPortinfoMethod(String userTimeZone, String id) throws UserNotFoundException {
		Map<String, Object> portPrice = new HashMap<>();
		;
		// standardPortPrice(id);
		Site site = generalDao.findOneSQLQuery(new Site(), "select p.* from station st "
				+ "inner join site p on p.siteId = st.siteId where st.referNo in ('" + id + "')");
		if (site.isDynamicPriceFlag()) {

			List<TOUProfile> touProfileList = generalDao.findAllSQLQuery(new TOUProfile(),
					"select * from tou_profile where dynamicProfileId in (select sdt.dynamicProfileId from site_dynamicTariff sdt "
							+ "inner join station st on st.siteId = sdt.siteId where (st.referNo in ('" + id
							+ "') OR st.stationName = '" + id + "'))");

			Map<String, Object> userTimeMap = null;
			String hours = null;
			String minutes = null;
			try {
				userTimeMap = Utils.getUserCoordinates(userTimeZone);
				hours = userTimeMap.get("Hours").toString();
				minutes = userTimeMap.get("Minutes").toString();
			} catch (Exception e) {

				e.printStackTrace();
			}

			double standardPrice = touProfileList.get(0).getStandardPrice();
			String standardPriceUnit = touProfileList.get(0).getStandardPriceUnit();

			List<Map<String, Object>> dateTimeData = generalDao.findAliasData("select format(dateAdd(HOUR, " + hours
					+ ",DateAdd(MINUTE, " + minutes
					+ ", getUTCDate())),'HH:mm') as time, DATENAME(MONTH,getUTCDate()) as month,  DATENAME(WEEKDAY,getUTCDate()) as day ");

			String time = dateTimeData.get(0).get("time").toString();
			String month = dateTimeData.get(0).get("month").toString();
			String day = dateTimeData.get(0).get("day").toString();

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

			LocalTime input = LocalTime.parse(time, formatter);

			for (TOUProfile touProfile : touProfileList) {

				String startT = generalDao.getSingleRecord("select format(dateAdd(HOUR, " + hours + ",DateAdd(MINUTE, "
						+ minutes + ", '" + touProfile.getStartTime() + "')),'HH:mm')");

				String endT = generalDao.getSingleRecord("select format(dateAdd(HOUR, " + hours + ",DateAdd(MINUTE, "
						+ minutes + ", '" + touProfile.getEndTime() + "')),'HH:mm')");
				LocalTime startTime = LocalTime.parse(startT, formatter);
				LocalTime endTime = LocalTime.parse(endT, formatter);

				if ((!input.isBefore(startTime) && !input.isAfter(endTime))
						&& month.equalsIgnoreCase(touProfile.getMonth()) && day.equalsIgnoreCase(touProfile.getDay())) {
					standardPrice = touProfile.getPrice();
					standardPriceUnit = touProfile.getPriceUnit();
				}

			}

			portPrice.put("VendingPrice", standardPrice);
			portPrice.put("VendingPriceUnit", standardPriceUnit);

		}

		return portPrice;
	}
}
