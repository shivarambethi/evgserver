package com.evgateway.server.controller;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.LoginUser;
import com.evgateway.server.form.UserResigtarion;
import com.evgateway.server.messages.Error;
import com.evgateway.server.messages.ResponseMessage;
import com.evgateway.server.model.Mail;
import com.evgateway.server.model.MailContent;
import com.evgateway.server.model.SendemailModel;
import com.evgateway.server.pojo.ConfigurationSettings;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.PermissionInRevenue;
import com.evgateway.server.pojo.User;
import com.evgateway.server.rest.JwtTokenUtil;
import com.evgateway.server.services.UserService;
import com.evgateway.server.utils.OTP;

import io.swagger.v3.oas.annotations.Hidden;

@RestController
@Scope("request")
@Hidden
@RequestMapping(value = "login/", produces = "application/json")

public class LoginController {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private GeneralDao<?, ?> generalDao;


	@Value("${password.encoder.secret}")
	private String passwordEncoder;
	
	@Value("${EVG-Correlation-ID}")
	private String cooRelationId;
	@Value("${notification_url}")
	private String emailnotificationurl;

	private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@SuppressWarnings("unchecked")
	// @ApiOperation(value = "Login Authentication With Username & Password")
	@RequestMapping(value = "authenticate", method = RequestMethod.POST)
	public ResponseEntity<User> authenticate(@RequestBody LoginUser loginUser, HttpServletRequest request)
			throws Exception {

		logger.debug("LoginController.authenticate() - with [" + loginUser.getEmail() + "]");

//		String plainTextPass = RSAUtil.decrypt(loginUser.getPassword(), privateKey);
//
//		System.out.println("Decrypted String: " + plainTextPass);
//
//		loginUser.setPassword(plainTextPass);

		// loginUser.setPassword(dStr);

		if (loginUser.getUsername() == null || loginUser.getUsername().trim().equals("")) {
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));
		}
		if (userService.getUser(loginUser.getUsername()) == null)
			throw new ServerException(Error.USER_NOT_EXIST.toString(),
					Integer.toString(Error.USER_NOT_EXIST.getCode()));
		if (loginUser.getPassword() == null || loginUser.getPassword().trim().equals("")) {
			throw new ServerException(Error.REG_Password_NULL.toString(),
					Integer.toString(Error.REG_Password_NULL.getCode()));
		}

		User user = (User) userDetailsService.loadUserByUsername(loginUser.getUsername());

		logger.info("user.getPasswords()   : {}", user.getPasswords());
		if (!user.isEnabled()) {
			throw new ServerException(Error.User_Blocked.toString(),
					Integer.toString(Error.User_Blocked.getCode()));

		}
		if (user.getRoles().iterator().next().getRolename().equals(Roles.Driver.toString())) {
			if (user.getPasswords().iterator().next().getPassword() == null
					|| userService.getUserStatus(loginUser.getUsername()) == 0)
				throw new ServerException(Error.MAIL_VERIFICATION_ERROR.toString(),
						Integer.toString(Error.MAIL_VERIFICATION_ERROR.getCode()));
		} else {
			if (user.getPasswords().iterator().next().getPassword() == null
					|| userService.getUserStatus(loginUser.getUsername()) == 0)
				throw new ServerException(Error.SET_PASSWORD.toString(),
						Integer.toString(Error.SET_PASSWORD.getCode()));

		}

		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				loginUser.getUsername(), loginUser.getPassword());

		Authentication authentication = authManager.authenticate(authenticationToken);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpSession session = request.getSession(true);
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				SecurityContextHolder.getContext());

		try {
			user.setToken(jwtTokenUtil.generateToken(user,request));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user.getRoles().iterator().next().getRolename().equals(Roles.Admin.toString())) {
			List<Organization> org = generalDao.findAllSQLQuery(new Organization(),
					"Select * from organization where id=1 ");
			Set<Organization> orgs = new HashSet<>(org);
			user.setOrg(orgs);
		}

		PermissionInRevenue perrmission = new PermissionInRevenue();
		perrmission.setUserId((long) user.getId());
		perrmission.setShowRevenue(true);
		perrmission.setChangePrice(true);
		user.setRevenuePermission(perrmission);
		if (user.getProfiles().iterator().next().isIstwofa()) {

			ConfigurationSettings config = generalDao.findOneSQLQuery(new ConfigurationSettings(),
					"select * from configurationSettings where orgId=" + user.getOrgId());
			String result = new OTP().otpGenerate();
			if (config == null) {
				config = generalDao.findOneSQLQuery(new ConfigurationSettings(),
						"select * from configurationSettings where orgId=1");

			}
			generalDao.excuteUpdateUserData("Update Profile Set otp=" + result + " where user_id=" + user.getId());

			String message = "Hi " + user.getFirstName() + "\r\n The Otp for the authentication is " + result;
			final Mail mail = new Mail();
			mail.setMailTo(user.getEmail());

			mail.setMailSubject("Authentication Email");
			mail.setMailContent(message);
			mail.setMailFrom(config.getEmail());
			
			SendemailModel model = new SendemailModel();
			model.setEmail(config.getEmail());
			model.setMail(mail);
			model.setPassword(config.getPassword());
			model.setHost(config.getHost());
			model.setPort(config.getPort());
			model.setOrgId(config.getOrgId());
			String serverUrl1 = emailnotificationurl + "/services/sendemail/send";

			HttpHeaders header = new HttpHeaders();
			header.set("EVG-Correlation-ID", cooRelationId);
			header.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SendemailModel> requestEntity3 = new HttpEntity<>(model, header);
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);

			System.out.println("postForEntity::" + postForEntity.getStatusCode());

			//emailService.sendEmail(mail, config.getHost(), config.getPort(), config.getEmail(), config.getPassword());
			return ResponseEntity.status(HttpStatus.OK).body(user);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(user);
		}
	
	}

	// @ApiOperation(value = "Validate Otp")

	@RequestMapping(value = "validateOtp", method = RequestMethod.POST)
	public ResponseEntity<Boolean> validateOtp(@RequestBody LoginUser loginUser, HttpServletRequest request)
			throws Exception {

		logger.debug("LoginController.authenticate() - with [" + loginUser.getEmail() + "]");
		if (loginUser.getUsername() == null || loginUser.getUsername().trim().equals("")) {
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));
		}

		if (loginUser.getPassword() == null || loginUser.getPassword().trim().equals("")) {
			throw new ServerException(Error.REG_Password_NULL.toString(),
					Integer.toString(Error.REG_Password_NULL.getCode()));
		}

		User user = (User) this.userDetailsService.loadUserByUsername(loginUser.getUsername());

		if (loginUser.getOtp().equals(user.getProfiles().iterator().next().getOtp())) {
			return ResponseEntity.status(HttpStatus.OK).body(true);
		} else {

			return ResponseEntity.status(HttpStatus.OK).body(false);
		}

	}

	// @ApiOperation(value = "Logout")
	@RequestMapping(value = "logout", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> logout(HttpServletRequest request, HttpServletResponse response) {

		logger.debug("LoginController.logout() - with [" + request + "]");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Logout Successfully"));
		}

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Coudn't Logout Successfully"));
	}

	// @ApiOperation(value = "Registration For New User")
	@RequestMapping(value = "registration", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> registration(@RequestBody UserResigtarion user, HttpServletRequest request)
			throws Exception {

		logger.debug("LoginController.registration() - with [" + user.getEmail() + "]");

		userService.registerUser(user);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Registered successfully"));
	}

	public Organization getOrganizationById(Long orgId) throws UserNotFoundException {

		return generalDao.findOneHQLQuery(new Organization(),
				"From Organization Where id =" + orgId + " ORDER BY orgName ASC");
	}
//
//	//@ApiOperation(value = "Forgot Password with Email Id")
//	@RequestMapping(value = "forgotPassword", method = RequestMethod.POST)
//	public ResponseEntity<ResponseMessage> resetPassword(final HttpServletRequest request,
//			@RequestBody final String userEmail) throws ServerException, UserNotFoundException {
//
//		if (userEmail.isEmpty() || userEmail.trim().equalsIgnoreCase("")) {
//			throw new ServerException(Error.EMAIL_NULL.toString(), Integer.toString(Error.EMAIL_NULL.getCode()));
//
//		} else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$", userEmail)
//				|| userEmail.length() > 80)
//			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
//					Integer.toString(Error.REG_MAIL_INVALID.getCode()));
//		logger.debug("LoginController.resetPassword() - with [" + userEmail + "]");
//
//		final User user = userService.findUserByEmailForPassword(userEmail);
//
//		Organization org = getOrganizationById(user.getOrgId());
//
//		String orgName = "Evgateway";
//
//		if (org.isWhiteLabel())
//			orgName = org.getOrgName();
//
//		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//				"From ConfigurationSettings Where orgName='" + orgName + "'");
//
//		if (config == null)
//			config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
//					"From ConfigurationSettings Where orgId=1");
//
//		final String token = UUID.randomUUID().toString() + ":" + user.getId();
//		userService.createPasswordResetTokenForUser(user, token);
//
//		final String appUrl = config.getPortalLink();
//		String message = "";
//		final String url = appUrl + "password?id=" + user.getUid() + "&resetToken=" + token;
//
//		System.out.println("reset password token  : {}" + url);
//
//		final Mail mail = new Mail();
//		mail.setMailTo(user.getEmail());
//
//		if (org.getOrgName().equalsIgnoreCase("Everon") || org.getOrgName() == "Everon") {
//			message = "Restablecer enlace de contraseña";
//			mail.setMailSubject(org.getOrgName() + "- Restablecer la contraseña");
//			mail.setMailContent(new MailContent().getResetMsg(message, url, org.getOrgName()));
//			mail.setMailFrom(config.getEmail());
//
//			emailService.sendEmail(mail, config.getHost(), config.getPort(), config.getEmail(), config.getPassword());
//
//			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(
//					"Debería recibir un correo electrónico de restablecimiento de contraseña en breve"));
//		} else {
//			message = "Reset password link";
//			mail.setMailSubject(org.getOrgName() + "- Reset Password");
//			mail.setMailContent(new MailContent().getResetMsg(message, url, org.getOrgName()));
//			mail.setMailFrom(config.getEmail());
//
//			emailService.sendEmail(mail, config.getHost(), config.getPort(), config.getEmail(), config.getPassword());
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(new ResponseMessage("You should receive an email to reset your password shortly"));
//		}
//
//	}

	// @ApiOperation(value = "Forgot Password with Email Id")
	@RequestMapping(value = "forgotPassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> resetPassword(final HttpServletRequest request,
			@RequestBody final String userEmail) throws ServerException, UserNotFoundException {

		if (userEmail.isEmpty() || userEmail.trim().equalsIgnoreCase("")) {
			throw new ServerException(Error.EMAIL_NULL.toString(), Integer.toString(Error.EMAIL_NULL.getCode()));

		} else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,}$", userEmail)
				|| userEmail.length() > 80)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));
		logger.debug("LoginController.resetPassword() - with [" + userEmail + "]");

		final User user = userService.findUserByEmailForPassword(userEmail);

		Organization org = generalDao.findOneHQLQuery(new Organization(),
				"From Organization Where id =" + user.getOrgId() + " ORDER BY orgName ASC");

		String orgName = "Evgateway";

		if (org.isWhiteLabel())
			orgName = org.getOrgName();

		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
				"From ConfigurationSettings Where orgName='" + orgName + "'");

		if (config == null)
			config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
					"From ConfigurationSettings Where orgId=1");

		final String token = UUID.randomUUID().toString() + ":" + user.getId();
		userService.createPasswordResetTokenForUser(user, token);

		final String appUrl = config.getDriverPortalLink();
		String message = "";
		final String url = appUrl + "password?id=" + user.getId() + "&resetToken=" + token;
		final Mail mail = new Mail();
		mail.setMailTo(user.getEmail());

		message = "Reset password link";
		mail.setMailSubject(org.getOrgName() + "- Reset Password");
		mail.setMailContent(new MailContent().getResetMsg(message, url, org.getOrgName()));
		mail.setMailFrom(config.getEmail());

		
		SendemailModel model = new SendemailModel();
		model.setEmail(config.getEmail());
		model.setMail(mail);
		model.setPassword(config.getPassword());
		model.setHost(config.getHost());
		model.setPort(config.getPort());
		model.setOrgId(config.getOrgId());
		String serverUrl1 = emailnotificationurl + "/services/sendemail/send";

		HttpHeaders header = new HttpHeaders();
		header.set("EVG-Correlation-ID", cooRelationId);
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SendemailModel> requestEntity3 = new HttpEntity<>(model, header);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);

		//emailService.sendEmail(mail, config.getHost(), config.getPort(), config.getEmail(), config.getPassword());

		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseMessage("You should receive an email to reset your password shortly"));

	}

	// @ApiOperation(value = "Change Password with Email Id")
	@RequestMapping(value = "changePassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> changePassword(@RequestBody Map<String, Object> map)
			throws ServerException, UserNotFoundException {

		logger.info("LoginController.changePassword() - " + map);

		String message = "Invalid account confirmation token";

		final Password passToken = userService.getPasswordResetToken(map.get("resetToken").toString());

		final User user = passToken.getUser();
		if (passToken == null || user.getId() != Long.parseLong(map.get("id").toString())) {
			message = "Invalid account confirmation token";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
		}
		final Calendar cal = Calendar.getInstance();
		System.out.println((passToken.getPasswordVerificationExpiration().getTime() - cal.getTime().getTime()) <= 0);

		if ((passToken.getPasswordVerificationExpiration().getTime() - cal.getTime().getTime()) <= 0) {
			message = "Your registration token has expired. Please register again.";
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage(message));
		}

		userService.updatePassword(user.getUid(), map, false);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Password Changed Successfully"));

	}

	// @ApiOperation(value = "ActivateUser with Email Id")
	@RequestMapping(value = "activateUser", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> activateUser(@RequestBody Map<String, Object> map)
			throws ServerException, UserNotFoundException {

		logger.info("LoginController.activateUser() - with [" + map.get("email").toString() + "]");

		userService.activateUser(map.get("email").toString());

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("User Activated Successfully"));

	}

	// @ApiOperation(value = "setPassword with Email Id")
	@RequestMapping(value = "setPassword", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> setPassword(@RequestBody Map<String, Object> map)
			throws ServerException, UserNotFoundException {

		logger.info("LoginController.activateUser() - with [" + map.get("email").toString() + "]");

		userService.setPassword(map);

		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("Password Set  Successfully"));

	}

	// @ApiOperation(value = "validate token")
	@RequestMapping(value = "isTokenValid", method = RequestMethod.POST)
	public ResponseEntity<Map<String, Boolean>> validateToken(@RequestBody Map<String, String> map)
			throws NumberFormatException, UserNotFoundException, ServerException {

		Map<String, Boolean> isExpired = new HashMap<String, Boolean>();

		boolean tokenExpired = false;
		boolean userpwdExpired = false;

		String token = map.get("token") == null || map.get("token").equals("undefined") || map.get("token").equals("")
				? "gsadfhgaskjdf"
				: map.get("token");
		String userId = map.get("userId") == null || map.get("userId").equals("")
				|| map.get("userId").equals("undefined") ? "0" : map.get("userId");

		logger.info("LoginController.validateToken() - with [" + token + "] [" + userId + "]");

		userpwdExpired = userService.isUsrPwdExprd(Long.valueOf(userId));

		isExpired.put("tokenExpired", tokenExpired);
		isExpired.put("userpwdExpired", userpwdExpired);

		try {
			jwtTokenUtil.isTokenExpired(token);
		} catch (Exception e) {
			logger.info("LoginController.validateToken() - token Expired  [" + userId + "]");
			isExpired.put("tokenExpired", true);
			isExpired.put("userpwdExpired", userpwdExpired);
		}

		return ResponseEntity.status(HttpStatus.OK).body(isExpired);

	}

	@RequestMapping(value = "getColor/{orgId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getColor(@PathVariable Long orgId)
			throws FileNotFoundException, ServerException, UserNotFoundException {
		return ResponseEntity.status(HttpStatus.OK).body(userService.getColor(orgId));

	}

	// @ApiOperation(value = "is user password expired")
	@RequestMapping(value = "isUsrPwdExprd", method = RequestMethod.POST)
	public ResponseEntity<Boolean> isUsrPwdExprd(@RequestBody long userId)
			throws ServerException, UserNotFoundException {

		logger.info("LoginController.isUsrPwdExprd() - with [" + userId + "]");

		return ResponseEntity.status(HttpStatus.OK).body(userService.isUsrPwdExprd(userId));

	}

}
