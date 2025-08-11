package com.evgateway.server.services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.messages.Error;
import com.evgateway.server.model.Mail;
import com.evgateway.server.model.MailContent;
import com.evgateway.server.model.SendemailModel;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.ConfigurationSettings;
import com.evgateway.server.pojo.Country;
import com.evgateway.server.pojo.CustomSetting;
import com.evgateway.server.pojo.Hours;
import com.evgateway.server.pojo.Image;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Logo;
import com.evgateway.server.pojo.Minutes;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.Site;
import com.evgateway.server.pojo.SiteTiming;
import com.evgateway.server.pojo.TOUProfile;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.VantivCeditCardKeys;
import com.evgateway.server.pojo.Zone;
import com.evgateway.server.utils.OTP;
import com.evgateway.server.utils.UserTimeZoneConvertion;
import com.evgateway.server.utils.Utils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Autowired
	private UserService userService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${report_service_url}")
	private String reportserviceurl;

	@Value("${EVG-Correlation-ID}")
	private String cooRelationId;
	@Value("${notification_url}")
	private String emailnotificationurl;

	@Autowired
	private StationDao<?, ?> stationDao;

	@Value("${password.encoder.secret}")
	private String passwordEncoder;

	final static Logger LOGGER = LoggerFactory.getLogger(CommonServiceImpl.class);

	@Override
	public List<Zone> getTimeZones() {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getTimeZones() - []");

		return generalDao.findAll(new Zone());
	}

	@Autowired
	private UserTimeZoneConvertion timeZoneConvertion;

	@Override
	public List<Language> getLangs() {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getLangs() - []");

		return generalDao.findAll(new Language());
	}

	@Override
	public List<Map<String, Object>> getDriverProfileGroups(String useruid)
			throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		LOGGER.info("CommonServiceImpl.getDriverProfileGroups() - []");

		User user = this.generalDao.findOneHQLQuery(new User(), "From User where uid='" + useruid + "'");

		Role role = userService.getCurrentRole();

		String hql = "";

		if (role.getRolename().equals(Roles.Admin.toString()))
			hql = "Select dg.groupName,dg.id  From driver_profile_groups dg";

		else if (role.getRolename().equals(Roles.DealerAdmin.toString())) {

			hql = "Select dg.groupName,dg.id  FROM "
					+ " driver_profile_groups dg  WHERE dg.ownerUser IN ( SELECT ownerId FROM owner_in_dealer WHERE dealerId="
					+ user.getId() + ")  ";

		} else if (role.getRolename().equals(Roles.GroupDealerAdmin.toString())) {

			List<Long> dealerorgIds = stationDao.getOrgIdsOfGroupDealerAdmin(user.getId());

			String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
			ids = ids.isEmpty() ? "0" : ids;

			hql = "Select dg.groupName,dg.id  FROM " + " driver_profile_groups dg  WHERE dg.ownerUser IN "
					+ "( SELECT ownerId FROM owner_in_dealer WHERE dealerId  IN (SELECT dealerId FROM dealer_in_org  WHERE orgId IN("
					+ ids + ")))";

		} else if (role.getRolename().equals(Roles.Dealer.toString())) {

			hql = "Select dg.groupName,dg.id  FROM "
					+ " driver_profile_groups dg  WHERE dg.ownerUser IN ( SELECT ownerId FROM owner_in_dealer WHERE dealerId="
					+ user.getId() + ")  ";

		} else if (role.getRolename().equals(Roles.Owner.toString())) {

			hql = "Select dg.groupName,dg.id  FROM  driver_profile_groups dg  WHERE dg.ownerUser = " + user.getId();

		}

		return generalDao.findAliasData(hql);

	}

	@Override
	public List<Minutes> getMinutes() throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getMinutes() - []");

		return generalDao.findAllHQLQuery(new Minutes(), "FROM Minutes m ORDER BY m.id");
	}

	@Override
	public List<Hours> getHours() throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getHours() - []");

		return generalDao.findAllHQLQuery(new Hours(), "FROM Hours hr ORDER BY hr.id");
	}

	@Override
	public List<Map<String, Object>> getDriverUser(String useruid) throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getDriverUser() - []");

		User user = this.generalDao.findOneHQLQuery(new User(), "From User where uid='" + useruid + "'");

		Role role = userService.getCurrentRole();

		String sql = "";

		if (role.getRolename().equals(Roles.Admin.toString())) {
			sql = "SELECT u.userId, u.email,u.orgId FROM  users u, usersinroles ur, role r "
					+ "where u.userId = ur.user_id and u.enabled=1 and ur.role_id=r.id and r.rolename IN('Driver')"
					+ " order by u.userId";

			return generalDao.findAliasData(sql);
		}

		else if (role.getRolename().equals(Roles.GroupDealerAdmin.toString())) {

			String ids = getOrgIdsBasedonGroupId(user.getId());

			sql = "SELECT u.userId , u.email,u.orgId From Users u " + "INNER JOIN address a On a.user_id = u.userId "
					+ "INNER JOIN accounts ac On ac.user_id = u.userId Inner join organization oo on u.orgId =oo.id "
					+ "INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur,"
					+ " Role r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('Driver') and u.orgId In ("
					+ ids + ") order by u.userId";

		} else if (role.getRolename().equals(Roles.DealerAdmin.toString())) {
			sql = "SELECT u.userId, u.email,u.orgId From Users u "
					+ " INNER JOIN address a On a.user_id = u.userId INNER JOIN accounts ac On ac.user_id = u.userId "
					+ " INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, Role r "
					+ " where u.userId =ur.user_id and ur.role_id=r.id AND u.orgId=" + user.getOrgId()
					+ " AND r.rolename IN ('Driver') order by u.userId";
		}
		return generalDao.findAliasData(sql);

		/*
		 * List<String> ls = new ArrayList<String>(2); ls.add(Roles.Driver.toString());
		 * return generalDao.findAllNamedQuery("@HQL_GET_ASSIGN_USERS", "rolename", ls);
		 */

	}

	public String getOrgIdsBasedonGroupId(long userId) {
		List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
						+ userId + ")"));

		String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
		return ids = ids.isEmpty() ? "0" : ids;
	}

	private List<Long> convertToLongList(List<BigDecimal> bigDecimalList) {

		List<Long> longs = bigDecimalList.stream().map(BigDecimal::longValue).collect(Collectors.toList());

		return longs;
	}

	@Override
	public List<VantivCeditCardKeys> getVantivCeditCardKeys() throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getVantivCeditCardKeys() - []");

		return generalDao.findAllHQLQuery(new VantivCeditCardKeys(),
				"FROM VantivCeditCardKeys v ORDER BY v.merchantName");
	}

	// @Override
	// public List<Country> getCountry() {
	// // TODO Auto-generated method stub
	// return commonDao.getCountry();
	// }
	//
	// @Override
	// public List<States> getStates() {
	// // TODO Auto-generated method stub
	// return commonDao.getStates();
	// }
	//
	// @Override
	// public List<ModelConnector> getModelConnector() {
	// // TODO Auto-generated method stub
	// return commonDao.getModelConnector();
	// }

	@Override
	public String getStationImage(Long id) throws UserNotFoundException, IOException {

		LOGGER.info("SuperAdminServiceImpl.getLogoImage() - with [] ");

		String out = "";
		long imId = generalDao.findIdBySqlQuery("SELECT imageId FROM station  WHERE  id=" + id);

		Image logoImages = generalDao.findOneById(new Image(), imId);

		if (logoImages.getUrl() != null)
			try {
				out = getThumbnailFilename(new File(logoImages.getUrl()));
				// map.put("file", file);

			} catch (HttpClientErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (java.rmi.ServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return out;

	}

	public String getThumbnailFilename(File fileUpload) throws IOException {

		BufferedImage inputBuffer = ImageIO.read(new File(fileUpload.getPath()));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		ImageIO.write(inputBuffer, "jpeg", baos);

		String data = DatatypeConverter.printBase64Binary(baos.toByteArray());
		String imageString = "<img src=\"data:image/jpeg;base64, " + data + "\" alt=\"blue square\">";

		return imageString;
	}

//	@Override
//	public List<Country> getCountries() throws UserNotFoundException {
//		// TODO Auto-generated method stub
//		LOGGER.info("CommonServiceImpl.getCountries() - []");
//
//		return generalDao.findAllHQLQuery(new Country(), "FROM Country u ORDER BY u.id");
//	}

	@Override
	public List<Map<String, Object>> getCurrency() throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getCountries() - []");

		return generalDao.findAliasData("Select currencyCode from country_currency ");
	}

	@Override
	public List<Map<String, Object>> getcountryCode() throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("CommonServiceImpl.getCountries() - []");

		return generalDao.findAliasData("Select distinct isdCode from country_currency");
	}

	@Override
	public ByteArrayInputStream getReport(String sessionId, String timeZone)
			throws IOException, UserNotFoundException, NumberFormatException {
		return null;
	}

	@Override
	public List<Country> getCountries() {
		// TODO Auto-generated method stub
		return generalDao.findAll(new Country());
	}

	@Override
	public void generateOTPForPassword(String pid, String did)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException {
		// TODO Auto-generated method stub

		Accounts account = generalDao.findOneHQLQuery(new Accounts(), "FROM Accounts WHERE digitalId='" + did + "'");

		Password password = generalDao.findOneHQLQuery(new Password(), "FROM Password WHERE uid='" + pid + "'");

		if (account == null || password == null)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		if (!(account.getUser().getId() == password.getUser().getId()))
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		User user = account.getUser();
		Profile profile = user.getProfiles().iterator().next();
		Address address = user.getAddress().iterator().next();

		String result = new OTP().otpGenerate();
		profile.setOtp((String) result);
		generalDao.update(profile);
		Organization organization = user.getOrg().iterator().next();

		StringBuilder fullName = new StringBuilder();
		fullName.append(user.getFirstName());
		fullName.append(" ");
		fullName.append(user.getLastName());

		ConfigurationSettings configSettings = generalDao.findOneHQLQuery(new ConfigurationSettings(),
				"From ConfigurationSettings Where orgId = " + user.getOrgId());

		try {

			final Mail mails = new Mail();

			mails.setMailSubject("Welcome to the " + organization.getOrgName() + " Family");
			mails.setMailContent(new MailContent().getRegisterMsg(fullName.toString(), organization.getOrgName(), "",
					result, configSettings.getSupportEmail(), configSettings.getOrgId(),
					configSettings.getPhoneNumber()));
			mails.setMailTo(user.getEmail());
			mails.setMailFrom(configSettings.getEmail());

			SendemailModel model = new SendemailModel();
			model.setEmail(configSettings.getEmail());
			model.setMail(mails);
			model.setPassword(configSettings.getPassword());
			model.setHost(configSettings.getHost());
			model.setPort(configSettings.getPort());
			model.setOrgId(configSettings.getOrgId());
			String serverUrl1 = emailnotificationurl + "/services/sendemail/send";

			HttpHeaders header = new HttpHeaders();
			header.set("EVG-Correlation-ID", cooRelationId);
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SendemailModel> requestEntity3 = new HttpEntity<>(model, header);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);

			System.out.println("postForEntity::" + postForEntity.getStatusCode());
			// emailService.sendEmail(mails, configSettings.getHost(),
			// configSettings.getPort(), configSettings.getEmail(),
			// configSettings.getPassword());
			HashMap<String, String> reqObjMap = new HashMap<>();
			reqObjMap.put("toPhoneNo", address.getPhone());
			reqObjMap.put("smsMessage", "Welcome to the " + organization.getOrgName()
					+ " EV charging network use the One Time Password: " + result + " to activate your account.");
			// smsService.sendSMSToUser(reqObjMap, address.getCountryCode());

		} catch (Exception e) {

		}

	}

	@Override
	public void validateOTPForSetPassword(String pid, String did, String otp)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException {
		// TODO Auto-generated method stub
		validateUserAuthentication(pid, did, otp, true);
	}

	public Map<String, Object> validateUserAuthentication(String pid, String did, String otp, boolean isOtp)
			throws UserNotFoundException, ServerException {

		Map<String, Object> map = new HashMap<String, Object>();

		Accounts account = generalDao.findOneHQLQuery(new Accounts(), "FROM Accounts WHERE digitalId='" + did + "'");

		Password password = generalDao.findOneHQLQuery(new Password(), "FROM Password WHERE uid='" + pid + "'");

		if (account == null || password == null)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		if (!(account.getUser().getId() == password.getUser().getId()))
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		map.put("account", account);
		map.put("password", password);

		if (isOtp) {
			if (otp == null || otp.trim().equals(""))
				throw new ServerException(Error.REG_MAIL_INVALID.toString(),
						Integer.toString(Error.REG_MAIL_INVALID.getCode()));

			User user = account.getUser();
			Profile profile = user.getProfiles().iterator().next();

			if (!profile.getOtp().equals(otp))
				throw new ServerException(Error.REG_MAIL_INVALID.toString(),
						Integer.toString(Error.REG_MAIL_INVALID.getCode()));
		}
		return map;
	}

	@Override
	public void setPasswordForUser(Map<String, Object> passwordData)
			throws UserNotFoundException, InvalidKeyException, NoSuchAlgorithmException, ServerException {
		// TODO Auto-generated method stub
		Map<String, Object> data = validateUserAuthentication(getStringBasedOnObject(passwordData.get("pid")),
				getStringBasedOnObject(passwordData.get("did")), "", false);
		Accounts account = (Accounts) data.get("account");
		Password passwordObject = (Password) data.get("password");
		User user = account.getUser();
		updatePasswordValidations(passwordObject.getPassword(), getStringBasedOnObject(passwordData.get("password")),
				getStringBasedOnObject(passwordData.get("confirmPassword")), user);
		changeUserPassword(user, passwordObject,
				new StandardPasswordEncoder(passwordEncoder).encode(passwordData.get("password").toString()));
	}

	public String getStringBasedOnObject(Object obj) {
		return obj == null ? null : obj.toString();
	}

	@SuppressWarnings("unused")
	private void updatePasswordValidations(String oldPassword, String newpassword, String confPassword,
			User currentuser) throws ServerException, UserNotFoundException {

		if (newpassword == null || newpassword.trim().equals(""))
			throw new ServerException(Error.EDITPROF_NEWPWD_NULL.toString(),
					Integer.toString(Error.EDITPROF_NEWPWD_NULL.getCode()));

		if (confPassword == null || confPassword.trim().equals(""))
			throw new ServerException(Error.REG_CONFPWD_NULL.toString(),
					Integer.toString(Error.REG_CONFPWD_NULL.getCode()));

		if (newpassword != null && !currentuser.getRoles().iterator().next().getRolename().equalsIgnoreCase("Driver")) {

			if (!Pattern.matches(
					"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*\"()_+{}\\[\\]:;,.<>/?-])[A-Za-z\\d~!@#$%^&*()_+\"{}\\[\\]:;,.<>/?-]{12,20}$",
					newpassword)) {
				throw new ServerException(Error.REG_PWD_INVALID.toString(),
						Integer.toString(Error.REG_PWD_INVALID.getCode()));
			}
		}

		if (newpassword != null && confPassword == null)
			throw new ServerException(Error.REG_PWD_NULL.toString(), Integer.toString(Error.REG_PWD_NULL.getCode()));

		if (newpassword == null && confPassword != null)
			throw new ServerException(Error.REG_CONFPWD_NULL.toString(),
					Integer.toString(Error.REG_CONFPWD_NULL.getCode()));

		if (newpassword != null && confPassword != null)
			if (!StringUtils.equals(newpassword, confPassword))
				throw new ServerException(Error.REG_CONFPWD_INVALID.toString(),
						Integer.toString(Error.REG_CONFPWD_INVALID.getCode()));

		if (newpassword != null && oldPassword != null)
			if (!newpassword.equals(confPassword))
				throw new ServerException(Error.REG_NOTSAMEPASSWORD.toString(),
						Integer.toString(Error.REG_NOTSAMEPASSWORD.getCode()));
		if (newpassword != null && confPassword != null)
			if (StringUtils.equals(newpassword, oldPassword))
				throw new ServerException(Error.REG_NOTSAMEPASSWORD1.toString(),
						Integer.toString(Error.REG_NOTSAMEPASSWORD1.getCode()));

	}

	public void changeUserPassword(User user, Password passwordObject, String password)
			throws ServerException, UserNotFoundException {
		LOGGER.info("CommonServiceImpl.changeUserPassword() - with  [" + user.getEmail() + "]");
		passwordObject.setPassword(password);
		passwordObject.setLastChangedDate(new Date());
		passwordObject.setPwdExpired(false);
		generalDao.savOrupdate(passwordObject);
	}

	@Override
	public void getFile(ReportsAndStatsForm filterform) throws IOException, UserNotFoundException, ServerException {
		LOGGER.info("CommonServiceImpl---------- getFile()-- filterform [  " + filterform.getData() + "]-- range [  "
				+ filterform.getRange() + "]");

		try {
			User user = userService.getUserByUID(filterform.getUserId());
			Role role = user.getRoles().iterator().next();

			filterform.setRequestType("Mobile");
			filterform.setUser(user);
			filterform.setUserId(user.getId().toString());
			filterform.setTimeZone("");
			filterform.setRole(role.getRolename());
			filterform.setReportType("Driver_Report");
			String serverUrl1 = reportserviceurl + "/services/analytics/driverreport";
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<ReportsAndStatsForm> requestEntity3 = new HttpEntity<>(filterform, header);
			ResponseEntity<byte[]> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, byte[].class);
//			ByteArrayInputStream targetStream = new ByteArrayInputStream(postForEntity.getBody());
			LOGGER.info("CommonServiceImpl getFile() success response  : {}", postForEntity.getStatusCode());
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("CommonServiceImpl getFile() exception message : {}", e.getMessage());
		}
	}

	@Override
	public CustomSetting getColor(Long orgId) throws UserNotFoundException, ServerException {

		CustomSetting sessting = generalDao.findOneSQLQuery(new CustomSetting(),
				"Select * from CustomSetting where orgId=" + orgId);

		if (sessting == null) {
			sessting = new CustomSetting();
			sessting.setPrimaryColor("#EA6A18");
			sessting.setSecondaryColor("#535252");
			sessting.setOrgId(1);
			sessting = generalDao.save(sessting);
		}

		return sessting;
	}

	@Override
	public List<Map<String, Object>> getLogoImage(Long orgId)
			throws ServerException, UserNotFoundException, FileNotFoundException {
		// TODO Auto-generated method stub

		List<Map<String, Object>> map = new ArrayList<>();

		List<Logo> logoImages = generalDao.findAllHQLQuery(new Logo(),
				"From Logo where createdBy in('Dealer') AND orgId=" + orgId);
		if (logoImages.size() == 0) {
			logoImages = generalDao.findAllHQLQuery(new Logo(),
					"From Logo where createdBy in('Dealer') AND orgId=" + orgId);
		}

		for (Logo logoImage : logoImages) {
			try {

				Map<String, Object> map2 = new HashMap<>();
				map2.put("thumbnail", logoImage.getUrl());
				map2.put("logo_type", logoImage.getLogoType());

				map.add(map2);
				// map.put("file", file);

			} catch (HttpClientErrorException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return map;

	}

	@Override
	public List<Map<String, Object>> getLogoImagebasedonorg(Long orgId) throws UserNotFoundException {
		List<Map<String, Object>> map = new ArrayList<>();

		Organization org = generalDao.findOneHQLQuery(new Organization(), "From Organization where id =" + orgId);

		if (org.isWhiteLabel())
			orgId = org.getId();

		List<Logo> logoImages = generalDao.findAllHQLQuery(new Logo(),
				"From Logo where createdBy in('Dealer') AND orgId=" + orgId);

		for (Logo logoImage : logoImages) {
			try {

				Map<String, Object> map2 = new HashMap<>();
				map2.put("thumbnail", logoImage.getUrl());
				map2.put("logo_type", logoImage.getLogoType());

				map.add(map2);

			} catch (HttpClientErrorException e) {

				e.printStackTrace();
			}

		}

		return map;
	}

	@Override
	public List<Map<String, Object>> getMap() {
		// TODO Auto-generated method stub

		String hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
				+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
				+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
				+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
				+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
				+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  " + " ELSE 'NA' END AS status, "
				+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
				+ "(select top 1 orgid from  dealer_in_org   "
				+ "where dealerId in(select dealerid from owner_in_dealer where ownerId "
				+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
				+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
				+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId ";

		return generalDao.findAliasData(hql);
	}

	@Override
	public List<Map<String, Object>> getMapFilter(String filter)
			throws UserNotFoundException, ServerException, JsonMappingException, JsonProcessingException {

		System.out.println("stationId::" + filter);

		String hql = "";
		String hql2 = "DECLARE @json nvarchar(max); WITH src (n) AS ( select s.siteId,s.uuid,s.siteName ,g.latitude, g.longitude,isNull((Select url from logo_image where"
				+ " createdBy ='Dealer' and logoType='mini' and orgid in (select top 1 orgid from  "
				+ " dealer_in_org where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
				+ " in (select ownerid from owner_in_org where orgid in (select org from site "
				+ " where siteid =s.siteId))))),(select url from logo_image where createdBy ='Dealer' "
				+ " and logoType='mini' and orgid =1 )) as url ,"
				+ " CONCAT(s.streetName,',', s.city,' ,', s.state,' ',s.country,' ',s.postal_code) as siteaddress,"
				+ " (select count(id)from station where siteId=s.siteId) as stationCount,"
				+ " COUNT(p.id) AS totalports, "
				+ " count(case when sn.status = 'Available' or sn.status='Finishing'  then sn.status end) AS available, "
				+ " count(case when sn.status = 'Charging' or sn.status = 'Planned' or sn.status='Preparing' then sn.status end) AS charging, "
				+ " count(case when sn.status = 'Inoperative' or sn.status = 'OUTOFORDER' "
				+ " or sn.status = 'Reserved' or sn.status = 'Blocked'  "
				+ " or sn.status = 'Removed' or  sn.status = 'SuspendedEVSE' or sn.status = 'SuspendedEV' "
				+ " or  sn.status = 'Unknown'  then sn.status end) AS unavailable from site s  "
				+ " inner JOIN  station st ON st.siteId = s.siteId "
				+ " inner join port p on p.station_id=st.id left JOIN statusNotification sn ON p.id = sn.port_id "
				+ " INNER JOIN geoLocation g ON g.id = s.coordinateId "
				+ " where (s.privateAccess=0 AND st.stationAvailStatus!='Disable') AND s.siteVisibilityOnMap=1  AND (s.siteName like N'%"
				+ filter + "%' or " + " CONCAT(s.streetName,',', s.city,' ,', s.state,' ',s.country,' ',s.postal_code) "
				+ " like N'%" + filter + "%' or s.streetName like N'%" + filter + "%' or s.city like N'%" + filter
				+ "%' or s.state like N'%" + filter + "%' ) group by s.uuid,s.siteName ,"
				+ " g.latitude, g.longitude,s.siteId,s.streetName,s.state,s.city,s.country,s.postal_code"
				+ " for json path ) SELECT @json = src.n FROM src SELECT @json as 'station'  ";
		if (!filter.equalsIgnoreCase("null")) {

			List<Map<String, Object>> listSite = generalDao.findAliasData(hql2);
			List<Map<String, Object>> response = new ArrayList<>();

			ObjectMapper objectMapper = new ObjectMapper();

			TypeReference<List<Map<String, Object>>> mapType = new TypeReference<List<Map<String, Object>>>() {
			};

			if (listSite.size() > 0 && listSite.get(0).get("station") != null) {
				response = objectMapper.readValue(listSite.get(0).get("station").toString(), mapType);

			}
			return response;

		}
		hql = "DECLARE @json nvarchar(max); WITH src (n) AS ( select s.siteId, s.uuid,s.siteName ,g.latitude, g.longitude,isNull((Select url from logo_image where"
				+ " createdBy ='Dealer' and logoType='mini' and orgid in (select top 1 orgid from  "
				+ " dealer_in_org where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
				+ " in (select ownerid from owner_in_org where orgid in (select org from site "
				+ " where siteid =s.siteId))))),(select url from logo_image where createdBy ='Dealer' "
				+ " and logoType='mini' and orgid =1 )) as url ,"
				+ " CONCAT(s.streetName,',', s.city,' ,', s.state,' ',' ',s.country,' ',s.postal_code) as siteaddress,"
				+ " (select count(id)from station where siteId=s.siteId) as stationCount,"
				+ " COUNT(p.id) AS totalports, "
				+ " count(case when sn.status = 'Available' or sn.status='Finishing' then sn.status end) AS available, "
				+ " count(case when sn.status = 'Charging' or sn.status = 'Planned' or sn.status='Preparing' then sn.status end) AS charging, "
				+ " count(case when sn.status = 'Inoperative' or sn.status = 'OUTOFORDER' "
				+ " or sn.status = 'Reserved' or sn.status = 'Blocked'  "
				+ " or sn.status = 'Removed' or  sn.status = 'SuspendedEVSE' or sn.status = 'SuspendedEV' "
				+ " or  sn.status = 'Unknown'  then sn.status end) AS unavailable " + "from site s  "
				+ " inner JOIN  station st ON st.siteId = s.siteId "
				+ " inner join port p on p.station_id=st.id left JOIN statusNotification sn ON p.id = sn.port_id "
				+ " INNER JOIN geoLocation g ON g.id = s.coordinateId  WHERE s.privateAccess=0 AND st.stationAvailStatus!='Disable'  AND s.siteVisibilityOnMap=1  group by s.uuid,s.siteName ,"
				+ " g.latitude, g.longitude,s.siteId,s.streetName,s.state,s.city,s.country,s.postal_code"
				+ " for json path ) SELECT @json = src.n FROM src SELECT @json as 'station'  ";

		List<Map<String, Object>> listSite = generalDao.findAliasData(hql);
		List<Map<String, Object>> response = new ArrayList<>();

		ObjectMapper objectMapper = new ObjectMapper();

		TypeReference<List<Map<String, Object>>> mapType = new TypeReference<List<Map<String, Object>>>() {
		};

		if (listSite.size() > 0 && listSite.get(0).get("station") != null) {
			response = objectMapper.readValue(listSite.get(0).get("station").toString(), mapType);

		}
		return response;
	}

	@Override
	public List<Map<String, Object>> getSitebylatlang(double lat, double lng) {

		String sql = "Select top 5 ISNULL(ROUND((" + 111.04500001243 + " * DEGREES(ACOS(COS(RADIANS('" + lat + "'))"
				+ " * COS(RADIANS(g.latitude))  * COS(RADIANS(g.longitude) - RADIANS('" + lng + "')) +SIN(RADIANS('"
				+ lat + "'))" + " * SIN(RADIANS(g.latitude))))),2),'') AS distance  ,s.uuid,s.siteId,s.siteName,"
				+ "(select count(p.id) from port p inner join station st on st.id=p.station_id where st.siteId=s.siteId and st.stationAvailStatus!='Disable' ) as totalports "
				+ " from site s inner join geolocation g on g.id=s.coordinateId  where s.privateAccess=0 AND s.siteVisibilityOnMap=1  AND (ISNULL(ROUND(("
				+ 111.04500001243 + " * DEGREES(ACOS(COS(RADIANS('" + lat + "')) "
				+ " * COS(RADIANS(g.latitude))  * COS(RADIANS(g.longitude) - RADIANS('" + lng + "')) +SIN(RADIANS('"
				+ lat + "'))* SIN(RADIANS(g.latitude))))),2),'') !=0 ) " + "order by distance asc";
		List<Map<String, Object>> sitedistance = generalDao.findAliasData(sql);

		return sitedistance;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getMapFilterByUid(String uid, String userTimeZone, String userstandardTimezone)
			throws JsonMappingException, JsonProcessingException, UserNotFoundException {

		List<Map<String, Object>> listStation = new ArrayList<Map<String, Object>>();
		String query = "DECLARE @json nvarchar(max); WITH src (n) AS "
				+ "(SELECT s.uuid,s.dynamicPriceFlag,s.siteName ,concat(s.spocCountryCode,' ',s.spocPhoneNo) as phone ,g.latitude,g.longitude,"
				+ "CONCAT(s.streetName,',', s.city,' ,', s.state,' ',' ',s.country,' ',s.postal_code) as siteaddress, case when s.publicAccess=1 then 'Public Access' "
				+ " when s.privateAccess=1 then 'Private Access' when s.fleetAccess=1 then 'Fleet Access' "
				+ " end as access," + "(select count(p.id) from port p inner join station stt on p.station_id=stt.id"
				+ " where stt.siteId=s.siteId  AND  stt.stationAvailStatus!='Disable') as totalports, (select count(p.id) from port p inner join station stt on p.station_id=stt.id inner join statusNotification sn on  sn.port_id=p.id where stt.siteId=s.siteId  and (sn.status = 'Available' or sn.status='Removed' )) as availablePorts,"
				+ " (select name,images,displayName from facility f inner join site_in_facility sf "
				+ "on sf.facilityId=f.id where sf.siteId=s.siteId for json path) as facility , "
				+ " json_query(coalesce((select top 1 i.url from image i  inner join site_in_images si on si.imageId=i.id "
				+ "where si.siteId=s.siteId order by i.id desc for json path),'[]')) as siteImage , "
				+ " ( SELECT s.currencyType, sn.status,st.referNo," + " pt.capacity, "
				+ "case when pt.vendingPriceUnit='Hr' then concat(pt.vendingPricePerUnit,' /hr') "
				+ " when pt.vendingPriceUnit='Min' then concat(pt.vendingPricePerUnit,' /min') "
				+ " when pt.vendingPriceUnit='kWh' then concat(pt.vendingPricePerUnit,' /kWh') "
				+ " when pt.vendingPriceUnit='Com' then concat(pt.vendingPricePerUnit1, '/',vendingPriceUnit1,' &',pt.vendingPricePerUnit2, '/',vendingPriceUnit2) else "
				+ " 'kWhTOU' end as portPrice ,IIF(pt.chargerType!='AC','DC',pt.chargerType) as chargerType ,st.stationMode  "
				+ ",(SELECT displayName FROM connectorType where id=pt.standard ) AS connectorType,pt.displayName as connectorId   FROM  "
				+ "port pt inner join station st on s.siteId=st.siteId inner join statusNotification sn on sn.port_id=pt.id WHERE pt.station_id = st.id  AND st.stationAvailStatus!='Disable'   FOR JSON PATH ) AS ports  "
				+ " FROM site s inner join geolocation g on g.id=s.coordinateId WHERE s.uuid='" + uid
				+ "'  AND s.siteVisibilityOnMap=1  AND s.privateAccess=0 for json path ) SELECT @json = src.n FROM src SELECT @json as 'loc'  ";
		List<Map<String, Object>> list = generalDao.findAliasData(query);
		ObjectMapper objectMapper = new ObjectMapper();

		List<Map<String, Object>> loc = new ArrayList<>();

		TypeReference<List<Map<String, Object>>> mapType = new TypeReference<List<Map<String, Object>>>() {
		};

		if (list.size() > 0 && list.get(0).get("loc") != null) {
			loc = objectMapper.readValue(list.get(0).get("loc").toString(), mapType);
			for (Map<String, Object> map : loc) {
				map.put("open24X7", issiteavailable(map.get("uuid").toString(), userTimeZone, userstandardTimezone));
			}

			System.out.println("loc: " + loc);
		}
		if (loc.size() == 0)
			return listStation;

		listStation = loc;

		if (Boolean.valueOf(listStation.get(0).get("dynamicPriceFlag").toString())) {
			try {
				List<Map<String, Object>> ports = (List<Map<String, Object>>) listStation.get(0).get("ports");

				Map<String, Object> portPricedetails = getPortinfoMethod(userTimeZone,
						listStation.get(0).get("uuid").toString());

				String chargerType = portPricedetails.get("chargerType").toString();
				String s = portPricedetails.get("VendingPrice").toString() + "/"
						+ portPricedetails.get("VendingPriceUnit").toString();

				if (s != null && !s.equalsIgnoreCase("0.0/")) {
					List<Map<String, Object>> newportsData = ports.stream().map(p -> {
						if (p.get("chargerType").toString().equalsIgnoreCase(chargerType)
								|| chargerType.equalsIgnoreCase("Both"))
							p.put("portPrice", s);
						return p;
					}).collect(Collectors.toList());

					if (newportsData.size() > 0)
						listStation.get(0).put("ports", newportsData);

				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("site info exception uuid : {}  message : {}", uid, e.getMessage());
			}

		}

		return listStation;

	}

	@Transactional(readOnly = true)
	public Map<String, Object> getPortinfoMethod(String userTimeZone, String uid) throws UserNotFoundException {
		Map<String, Object> portPrice = new HashMap<>();
		;
		// standardPortPrice(id);
		Site site = generalDao.findOneSQLQuery(new Site(), "select * from site s where s.uuid='" + uid + "'");
		if (site.isDynamicPriceFlag()) {

			List<TOUProfile> touProfileList = generalDao.findAllSQLQuery(new TOUProfile(),
					"select * from tou_profile where dynamicProfileId in "
							+ "(select sdt.dynamicProfileId from site_dynamicTariff sdt where sdt.siteId="
							+ site.getId() + ")");

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

	@Transactional(readOnly = true)
	public boolean issiteavailable(String uid, String userTimeZone, String standardTimeZone)
			throws UserNotFoundException {

		Site site = generalDao.findOneSQLQuery(new Site(), "Select * from site where uuid='" + uid + "'");
		if (site.isOpen24X7())
			return true;

		int timeopen = 0;
		Map<String, Object> userTimeMap = timeZoneConvertion.getUserCoordinates(userTimeZone);

		Long hours = Long.valueOf(userTimeMap.get("Hours").toString());
		Long minutes = Long.valueOf(userTimeMap.get("Minutes").toString());

		try {

			List<SiteTiming> list = new ArrayList<SiteTiming>();

			try {
				list = generalDao.findAllHQLQuery(new SiteTiming(), "FROM SiteTiming WHERE siteId=" + site.getId());
			} catch (UserNotFoundException e) {

				e.printStackTrace();
			}
			if (list.size() > 0) {
				for (SiteTiming siteTiming : list) {

					Calendar cal = Calendar.getInstance();
					Date d1 = new Date();
					try {
						SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");

						sdff.format(d1);
						d1 = sdff.parse(Utils.convertTime(siteTiming.getOpeningTime()));

						// remove next line if you're always using the current time.
						cal.setTime(d1);
						cal.add(Calendar.HOUR, hours.intValue());
						cal.add(Calendar.MINUTE, minutes.intValue());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Date oneHourBack = cal.getTime();

					final Integer daysDifference = (oneHourBack.getDate() == 31 ? 1
							: oneHourBack.getDate() - d1.getDate()) * -1;

					long[] days = generalDao
							.findAllByIdSQLQuery(
									"select weekdaysid from site_timing_days where sitetimingid=" + siteTiming.getId())
							.stream()
							.mapToLong(
									l -> (Long.parseLong(l.toString()) - daysDifference) == 0 ? 7
											: (Long.parseLong(l.toString()) - daysDifference) >= 8 ? 1
													: (Long.parseLong(l.toString()) - daysDifference))
							.sorted().toArray();

					Set<Long> daysSet = Arrays.stream(days).boxed().collect(Collectors.toSet());

					siteTiming.setDay(daysSet);
					siteTiming.setClosingTime(Utils.subOrAddTimeToString(siteTiming.getClosingTime(), hours, minutes));
					siteTiming.setOpeningTime(Utils.subOrAddTimeToString(siteTiming.getOpeningTime(), hours, minutes));

					List<Map<String, Object>> dateTimeData = generalDao.findAliasData("select format(dateAdd(HOUR, "
							+ hours + ",DateAdd(MINUTE, " + minutes
							+ ", getUTCDate())),'HH:mm') as time, DATENAME(MONTH,getUTCDate() at time Zone 'UTC' at time zone '"
							+ standardTimeZone + "') as month, "
							+ " LEFT(DATENAME(WEEKDAY, GETUTCDATE() at time Zone 'UTC' at time zone '"
							+ standardTimeZone
							+ "'), 3) AS day,DATEPART(WEEKDAY, GETUTCDATE() at time Zone 'UTC' at time zone '"
							+ standardTimeZone + "' ) AS WeekdayNumber ");
					System.out.println("OpeningTime::" + siteTiming.getOpeningTime());
					System.out.println("ClosingTime" + siteTiming.getClosingTime());
					for (Long day : siteTiming.getDay()) {
						if (Long.parseLong(dateTimeData.get(0).get("WeekdayNumber").toString()) == day) {

							String quesry = "select iif((format((GETUTCDATE() at time Zone 'UTC' at time zone '"
									+ standardTimeZone + "'),'yyyy-MM-dd HH:mm:ss')" + " between dATEAdD(hour,"
									+ siteTiming.getOpeningTime().substring(0, 2) + ",dateAdd(MINUTE,"
									+ siteTiming.getOpeningTime().substring(3, 5) + "," + " format(DATEADD(DAY, (" + day
									+ " - DATEPART(WEEKDAY, GETUTCDATE())) % 7, "
									+ " CAST(GETUTCDATE() AS DATE)),'yyyy-MM-dd HH:mm:ss'))) AND " + " dATEAdD(hour,"
									+ siteTiming.getClosingTime().substring(0, 2) + ",dateAdd(MINUTE,"
									+ siteTiming.getClosingTime().substring(3, 5) + ", format(DATEADD(DAY," + " (" + day
									+ " - DATEPART(WEEKDAY, GETUTCDATE())) % 7, CAST(GETUTCDATE() AS DATE)),'yyyy-MM-dd HH:mm:ss')))),'i','k') as timing ";

							List<Map<String, Object>> dateTimeData1 = generalDao.findAliasData(quesry);
							if (dateTimeData1.get(0).get("timing").equals("i")) {
								timeopen = timeopen + 1;
								System.out.println("Site open");

							} else {
								System.out.println("Site close");

							}
						}
					}

				}

				if (timeopen > 0)
					return true;
				else
					return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public List<SiteTiming> getSiteTimingBySiteId(long siteid, String userTimeZone) throws UserNotFoundException {

		Map<String, Object> userTimeMap = timeZoneConvertion.getUserCoordinates(userTimeZone);

		Long hours = Long.valueOf(userTimeMap.get("Hours").toString());
		Long minutes = Long.valueOf(userTimeMap.get("Minutes").toString());

		try {

			List<SiteTiming> list = new ArrayList<SiteTiming>();

			try {
				list = generalDao.findAllHQLQuery(new SiteTiming(), "FROM SiteTiming WHERE siteId=" + siteid);
			} catch (UserNotFoundException e) {

				e.printStackTrace();
			}
			if (list.size() > 0) {
				for (SiteTiming siteTiming : list) {

					Calendar cal = Calendar.getInstance();
					Date d1 = new Date();
					try {
						SimpleDateFormat sdff = new SimpleDateFormat("HH:mm");

						sdff.format(d1);
						d1 = sdff.parse(Utils.convertTime(siteTiming.getOpeningTime()));

						// remove next line if you're always using the current time.
						cal.setTime(d1);
						cal.add(Calendar.HOUR, hours.intValue());
						cal.add(Calendar.MINUTE, minutes.intValue());

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					Date oneHourBack = cal.getTime();

					final Integer daysDifference = (oneHourBack.getDate() == 31 ? -1
							: oneHourBack.getDate() - d1.getDate());

					long[] days = generalDao
							.findAllByIdSQLQuery(
									"select weekdaysid from site_timing_days where sitetimingid=" + siteTiming.getId())
							.stream()
							.mapToLong(
									l -> (Long.parseLong(l.toString()) + daysDifference) == 0 ? 7
											: (Long.parseLong(l.toString()) + daysDifference) >= 8 ? 1
													: (Long.parseLong(l.toString()) + daysDifference))
							.sorted().toArray();

					Set<Long> daysSet = Arrays.stream(days).boxed().collect(Collectors.toSet());

					siteTiming.setDay(daysSet);
					siteTiming.setClosingTime(Utils.subOrAddTimeToString(siteTiming.getClosingTime(), hours, minutes));
					siteTiming.setOpeningTime(Utils.subOrAddTimeToString(siteTiming.getOpeningTime(), hours, minutes));

				}
			}

			return list.size() > 0 ? list : null;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}