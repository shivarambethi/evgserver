package com.evgateway.server.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.cenum.Status;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.UserResigtarion;
import com.evgateway.server.messages.Error;
import com.evgateway.server.model.Mail;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.ConfigurationSettings;
import com.evgateway.server.pojo.Country;
import com.evgateway.server.pojo.Credentials;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Logo;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@SuppressWarnings("deprecation")
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private GeneralDao<?, ?> generalDao;


	@Value("${password.encoder.secret}")
	private String passwordEncoder;

	final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public List<Address> getUserByPhone(String phoneNumber) throws UserNotFoundException {

		String query = "From Address where phone = '" + phoneNumber + "' OR secondaryPhone='" + phoneNumber + "'";

		return generalDao.findAllHQLQuery(new Address(), query);

	}

//	public User getCurrentUser() throws UserNotFoundException, ServerException {
//		UserServiceImpl.LOGGER.info("UserServiceImpl.getCurrentUser() - []");
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null && auth.getPrincipal() instanceof UserDetails) {
//			User u = (User) auth.getPrincipal();
//			if (!u.getRoles().iterator().next().getRolename().equals(Roles.Driver.toString()))
//				throw new ServerException(Error.USER_NOT_EXIST.toString(),
//						Integer.toString(Error.USER_NOT_EXIST.getCode()));
//			PermissionInRevenue perrmission = generalDao.findOneSQLQuery(new PermissionInRevenue(),
//					"Select * from permission_in_revenue where userId=" + u.getId());
//			if (perrmission == null) {
//				perrmission = new PermissionInRevenue();
//				perrmission.setUserId((long) u.getId());
//				if (!u.getRoles().iterator().next().getRolename().equals(Roles.Manufacturer.toString())) {
//					perrmission.setShowRevenue(true);
//					perrmission.setChangePrice(true);
//				} else {
//					perrmission.setShowRevenue(false);
//					perrmission.setChangePrice(false);
//
//				}
//				if (u.getRoles().iterator().next().getRolename().equals(Roles.Owner.toString())
//						|| u.getRoles().iterator().next().getRolename().equals(Roles.SiteHost.toString())) {
//					perrmission = generalDao.save(perrmission);
//				}
//
//			} else {
//				if (u.getRoles().iterator().next().getRolename().equals(Roles.Manufacturer.toString())) {
//					perrmission.setShowRevenue(false);
//					perrmission.setChangePrice(false);
//
//				}
//			}
//			if (u.getRoles().iterator().next().getRolename().equals(Roles.Admin.toString())) {
//				List<Organization> org = generalDao.findAllSQLQuery(new Organization(),
//						"Select * from organization where id=1 ");
//				Set<Organization> orgs = new HashSet<>(org);
//				u.setOrg(orgs);
//			}
//			u.setRevenuePermission(perrmission);
//			System.out.println("currenet---->" + u.getRevenuePermission().isShowRevenue());
//			return u;
//		}
//		return null;
//	}

	@Override
	public Role getCurrentRole() {

		LOGGER.info("UserServiceImpl.getCurrentRole() - []");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			for (GrantedAuthority role : auth.getAuthorities())
				return (Role) role;
		else
			return null;
		return null;
	}

	@Override
	public User getUser(long id) throws UserNotFoundException, ServerException {

		LOGGER.info("UserServiceImpl.getUser() - [" + id + "]");

		User userObject = generalDao.findOneById(new User(), id);

		if (userObject == null) {
			throw new UserNotFoundException("User id [" + id + "] not found");
		} else {
			if (!userObject.getRoles().iterator().next().getRolename().equals(Roles.Driver.toString()))
				throw new ServerException(Error.USER_NOT_EXIST.toString(),
						Integer.toString(Error.USER_NOT_EXIST.getCode()));

			return userObject;
		}
	}

	@Override
	public Role getRole(String roleName) throws UserNotFoundException {

		LOGGER.info("UserServiceImpl.getRole() - [" + roleName + "]");

		return generalDao.findOneHQLQuery(new Role(), "from Role where rolename ='" + roleName + "'");
	}

	@Override
	public User getUser(String username) throws UserNotFoundException, ServerException {

		LOGGER.info("UserServiceImpl.getUser() - [" + username + "]");

		String sql = "SELECT u.* from Users u  INNER JOIN address a On a.user_id = u.UserId \r\n"
				+ "INNER JOIN usersinroles uir ON uir.\"user_id\"=u.userId\r\n"
				+ "INNER JOIN role r ON r.id=uir.role_id\r\n" + "WHERE  r.rolename LIKE '%Driver%' AND  (u.username = '"
				+ username + "' OR a.phone='" + username + "' Or u.email ='" + username + "')";

		User u = generalDao.findOneSQLQuery(new User(), sql);

		return u;
	}

	@Override
	public String generatePassword() {

		long code = (long) ((Math.random() * 9 * Math.pow(10, 15)) + Math.pow(10, 15));
		String unique_password = "";
		for (long i = code; i != 0; i /= 100)// a loop extracting 2 digits from the code
		{
			long digit = i % 100;// extracting two digits
			if (digit <= 90)
				digit = digit + 32;
			// converting those two digits(ascii value) to its character value
			char ch = (char) digit;
			// adding 32 so that our least value be a valid character
			unique_password = ch + unique_password;// adding the character to the string
		}
		return unique_password;

	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		LOGGER.info("UserServiceImpl.loadUserByUsername() - [" + username + "]");
		UserDetails userDetails = null;
		try {
			userDetails = (UserDetails) getUser(username);

			return userDetails;
		} catch (UserNotFoundException | ServerException e) {

			LOGGER.info("Invalid User Details : " + username);
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public Map<String, Object> getOrgData(long orgId, String referNo) {
		Map<String, Object> map = new HashMap<>();
		List<Map<String, Object>> list = new ArrayList<>();
		try {
			String query = "select address,email,host,legacykey,logoName,orgId,orgName,password,phoneNumber,port,portalLink,"
					+ "protocol,isnull(serverKey,'') as serverKey from configurationSettings where orgId = '" + orgId
					+ "'";
			ConfigurationSettings config = generalDao.findOneSQLQuery(new ConfigurationSettings(),
					"Select * from configurationSettings where orgId=1");
			list = generalDao.getMapData(query);
			if (list.size() > 0) {
				map = list.get(0);
			} else {
				map.put("orgName", "EvGateway");
				map.put("orgId", "1");
				map.put("legacykey", config.getLegacykey());
				map.put("serverKey", config.getServerKey());
				map.put("email", config.getEmail());
				map.put("host", config.getHost());
				map.put("port", config.getPort());
				map.put("password", config.getPassword());
				map.put("address", "5251 California Ave,STE 150,Irvine,CA-92617.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	@Override
	public Long getUserStatus(String username) throws UserNotFoundException, ServerException {

		LOGGER.info("UserServiceImpl.getUser() - [" + username + "]");

		User user = getUser(username);

		return generalDao.countSQL("Select u.userId from Users u INNER JOIN address a On a.user_id = u.UserId"
				+ " INNER JOIN profile p On p.user_id = u.UserId WHERE u.UserId = " + user.getId()
				+ " AND p.status='ACTIVE'");
	}

	public boolean getDriverphone(final String phoneNumber) {
		String query = "SELECT u.* From Users u INNER JOIN address a On a.user_id = u.userId ,  Usersinroles ur, Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Driver') and a.phone='"
				+ phoneNumber + "'";
		List<Map<String, Object>> phone = generalDao.findAliasData(query);
		if (phone.size() > 0)
			return true;
		else
			return false;
	}

	private boolean isOrganizationExist(String orgName) {

		String queryForOrgNameCount = "Select orgName from organization u where orgName='" + orgName + "'";
		return generalDao.getSingleRecord(queryForOrgNameCount).isEmpty() == true ? false : true;
	}

	@Override
	public void registerUser(UserResigtarion userForm) throws ServerException, UserNotFoundException {

		LOGGER.info("UserServiceImpl.registerUser() - [" + userForm.getEmail() + "]");

		Address address = userForm.getAddress().iterator().next();

		registerUserValidations(userForm, address);

		Password pwd = new Password(new StandardPasswordEncoder(passwordEncoder).encode(userForm.getPassword()));

		if (getDriverphone(address.getPhone())) {
			throw new ServerException(Error.DUPLICATE_PHONE_NUMBER.toString(),
					Integer.toString(Error.DUPLICATE_PHONE_NUMBER.getCode()));
		}

		Role role = null;
		try {
			role = getRole("Driver");
		} catch (UserNotFoundException e) {
			throw new ServerException(Error.REG_ROLENAME_NONKNOW.toString(),
					Integer.toString(Error.REG_ROLENAME_NONKNOW.getCode()));
		}

		Set<Role> roles = new HashSet<Role>();
		roles.add(role);

		if ((getUser(userForm.getEmail())) != null)
			throw new ServerException(Error.DUPLICATE_USER.toString(),
					Integer.toString(Error.DUPLICATE_USER.getCode()));

		User user = new User(userForm.getFirstName(), userForm.getLastName(), userForm.getEmail(), userForm.getEmail(),
				true, roles, userForm.getOrgId());

		user.setCreatedBy("Portal-SignUp");

		user.setUid(UUID.randomUUID().toString());
		user = generalDao.save(user);

		pwd.setUser(user);
		pwd.setPwdExpired(false);
	
		pwd.setUid(UUID.randomUUID().toString());
		generalDao.save(pwd);

		address.setUser(user);
		address.setUid(UUID.randomUUID().toString());
		generalDao.save(address);

		Set<User> users = new HashSet<User>();
		users.add(user);

		if (userForm.getCountryId() == 0)
			userForm.setCountryId(1);

		Country country = generalDao.findOneById(new Country(), userForm.getCountryId());

		Profile profile = new Profile();
		Language l = generalDao.findOneById(new Language(), 1);
		profile.setLang(l);
		profile.setZone(null);
		profile.setStatus(Status.INACTIVE.toString());
		
		profile.setCountry(country);
		profile.setUser(user);
		profile.setIstwofa(false);
		profile.setUid(UUID.randomUUID().toString());
		generalDao.save(profile);

		String digitalId = String.format("%.4s", user.getFirstName()).toUpperCase() + address.getPhone();
		Accounts ac = new Accounts();
		ac.setAccountName(user.getFirstName() + " " + user.getLastName());
		ac.setActiveAccount(true);
		ac.setNotificationFlag(false);
		ac.setCreationDate(new Date());
		ac.setUid(UUID.randomUUID().toString());
		ac.setCurrencySymbol(country.getCurrencySymbol());
		ac.setCurrencyType(country.getCurrencyCode());
		ac.setUser(user);
		ac.setDigitalId(digitalId);
		generalDao.save(ac);

		Credentials c = new Credentials();
		c.setPhone(address.getPhone());
		c.setId(user.getId());
		c.setAccount(ac);
		c.setUid(UUID.randomUUID().toString());
		generalDao.save(c);

		com.evgateway.server.pojo.PreferredNotification notification = new com.evgateway.server.pojo.PreferredNotification();
		notification.setRfidStatus(true);
		notification.setSessionEnds(true);
		notification.setStationAvailability(true);
		notification.setEmail(false);
		notification.setSms(false);
		notification.setNotification(true);
		notification.setUserId(user.getId());
		notification.setOrgId(user.getOrgId());
		generalDao.save(notification);

		generalDao.update(user);

		try {

			Organization orgDetails = generalDao.findOneSQLQuery(new Organization(),
					"Select * from organization Where id =" + userForm.getOrgId());

			long orgId = 1;

			if (orgDetails.isWhiteLabel())
				orgId = orgDetails.getId();

			ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
					"From ConfigurationSettings Where orgId =" + orgId);



			String fullName = user.getFirstName() + " " + user.getLastName();


			
		
			final Mail mails = new Mail();
			mails.setMailSubject("Bc Hydro" + " - One Time Password(OTP)");
			mails.setMailContent("");
			mails.setMailTo(user.getEmail());
			mails.setMailFrom(config.getEmail());

			Map<String, Object> tamplateData = new HashMap<>();
			tamplateData.put("portalUrl", config.getDriverPortalLink());
			String logo = getLogo(config.getOrgId(), "main");
			tamplateData.put("logo", logo);
			tamplateData.put("whiteLabelName", config.getOrgName());
			tamplateData.put("fullName", fullName);
			tamplateData.put("supportEmail", config.getSupportEmail() );
			tamplateData.put("Address", config.getAddress());
			tamplateData.put("Email",user.getEmail() );
			tamplateData.put("templateName", "BcHydro_Register.ftl");
			//emailService.sendEmailWithTemplate(mails, tamplateData, config);

		} catch (Exception e) {
			throw new ServerException(Error.REG_MAIL_ERROR.toString(),
					Integer.toString(Error.REG_MAIL_ERROR.getCode()));
		}

	}

	private void registerUserValidations(UserResigtarion userForm, Address address) throws ServerException {

		if (userForm.getFirstName() == null || userForm.getFirstName() == ""
				|| userForm.getFirstName().trim().equals(""))
			throw new ServerException(Error.REG_FIRSTNAME_NULL.toString(),
					Integer.toString(Error.REG_FIRSTNAME_NULL.getCode()));
		else if (!Pattern.matches("^[a-zA-Z ]{3,20}$", userForm.getFirstName()))
			throw new ServerException(Error.REG_FIRSTNAME_INVALID.toString(),
					Integer.toString(Error.REG_FIRSTNAME_INVALID.getCode()));

		if (userForm.getLastName() == null || userForm.getLastName() == "" || userForm.getLastName().trim().equals(""))
			throw new ServerException(Error.REG_LASTNAME_NULL.toString(),
					Integer.toString(Error.REG_LASTNAME_NULL.getCode()));
		else if (!Pattern.matches("^[a-zA-Z ]{3,20}$", userForm.getLastName()))
			throw new ServerException(Error.REG_LASTNAME_INVALID.toString(),
					Integer.toString(Error.REG_LASTNAME_INVALID.getCode()));

		if (userForm.getEmail() == null || userForm.getEmail().trim().equals(""))
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));
		/*
		 * else if (!Pattern.matches(
		 * "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$", userForm.getEmail()) ||
		 * userForm.getEmail().length() > 40) throw new
		 * ServerException(Error.REG_MAIL_INVALID.toString(),
		 * Integer.toString(Error.REG_MAIL_INVALID.getCode()));
		 */
		else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$", userForm.getEmail())
				|| userForm.getEmail().length() > 80)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		if (userForm.getPassword() == null || userForm.getPassword().trim().equals(""))
			throw new ServerException(Error.REG_PWD_NULL.toString(), Integer.toString(Error.REG_PWD_NULL.getCode()));
		else if (userForm.getPassword().length() < 6 || userForm.getPassword().length() > 15)
			throw new ServerException(Error.Password_INVALID.toString(),
					Integer.toString(Error.Password_INVALID.getCode()));

		if (userForm.getConfrimPassword() == null || userForm.getConfrimPassword().trim().equals(""))
			throw new ServerException(Error.REG_CONFPWD_NULL.toString(),
					Integer.toString(Error.REG_CONFPWD_NULL.getCode()));
		else if (!userForm.getConfrimPassword().equals(userForm.getPassword()))
			throw new ServerException(Error.REG_CONFPWD_INVALID.toString(),
					Integer.toString(Error.REG_CONFPWD_INVALID.getCode()));

		if (!userForm.getPassword().equals(userForm.getConfrimPassword()))
			throw new ServerException(Error.REG_NOTSAMEPASSWORD.toString(),
					Integer.toString(Error.REG_NOTSAMEPASSWORD.getCode()));

		if (address.getCountryCode() == null || address.getCountryCode().trim().equals(""))
			throw new ServerException(Error.REG_COUNTRYCODE_NULL.toString(),
					Integer.toString(Error.REG_COUNTRYCODE_NULL.getCode()));

		if (address.getPhone() == null || address.getPhone().trim().equals(""))
			throw new ServerException(Error.REG_PHONE_NULL.toString(),
					Integer.toString(Error.REG_PHONE_NULL.getCode()));
		else if (address.getCountryCode().equals("+506") && !Pattern.matches("[0-9]{8}", address.getPhone()))
			throw new ServerException(Error.REG_PHONE_INVALID_CRC.toString(),
					Integer.toString(Error.REG_PHONE_INVALID_CRC.getCode()));
		else if ((address.getCountryCode().equals("+1") || address.getCountryCode().equals("+91")
				|| address.getCountryCode().equals("+52") || address.getCountryCode().equals("+972")
				|| address.getCountryCode().equals("+49")) && !Pattern.matches("[0-9]{8,12}", address.getPhone()))
			throw new ServerException(Error.REG_PHONE_INVALID.toString(),
					Integer.toString(Error.REG_PHONE_INVALID.getCode()));

		if (userForm.getGender() == null)
			throw new ServerException(Error.REG_GENDER_NULL.toString(),
					Integer.toString(Error.REG_GENDER_NULL.getCode()));

		if (address.getAddressLine1() == null || address.getAddressLine1().trim().equals(""))
			throw new ServerException(Error.REG_ADD1_NULL.toString(), Integer.toString(Error.REG_ADD1_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z0-9 .,-]{3,40}", address.getAddressLine1()))
			throw new ServerException(Error.REG_ADD1_LENGTH.toString(),
					Integer.toString(Error.REG_ADD1_LENGTH.getCode()));

		if (address.getAddressLine2() != null) {
			if (!Pattern.matches("[a-zA-Z0-9 .,-]{3,40}", address.getAddressLine2() + "")
					|| address.getAddressLine2().trim().equals(""))
				throw new ServerException(Error.REG_ADD2_LENGTH.toString(),
						Integer.toString(Error.REG_ADD2_LENGTH.getCode()));
		}

		if (address.getCity() == null || address.getCity().trim().equals(""))
			throw new ServerException(Error.REG_CITY_NULL.toString(), Integer.toString(Error.REG_CITY_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{2,20}", address.getCity()))
			throw new ServerException(Error.REG_CITY_INVALID.toString(),
					Integer.toString(Error.REG_CITY_INVALID.getCode()));

		if (address.getState() == null || address.getState().trim().equals(""))
			throw new ServerException(Error.REG_STATE_NULL.toString(),
					Integer.toString(Error.REG_STATE_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{2,20}", address.getState()))
			throw new ServerException(Error.REG_STATE_INVALID.toString(),
					Integer.toString(Error.REG_STATE_INVALID.getCode()));

		if (address.getCountry() == null || address.getCountry().trim().equals(""))
			throw new ServerException(Error.REG_COUNTRY_NULL.toString(),
					Integer.toString(Error.REG_COUNTRY_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{2,20}", address.getCountry()))
			throw new ServerException(Error.REG_COUNTRY_INVALID.toString(),
					Integer.toString(Error.REG_COUNTRY_INVALID.getCode()));

		if (address.getZipCode() == null || address.getZipCode().trim().equals(""))
			throw new ServerException(Error.REG_ZIPCODE_NULL.toString(),
					Integer.toString(Error.REG_ZIPCODE_NULL.getCode()));
		int count = StringUtils.countMatches(address.getZipCode(), "-");

		if (address.getZipCode() != null && address.getZipCode() != "") {
			if (!Pattern.matches("[a-zA-Z0-9 -]{3,12}", address.getZipCode()) || count > 2)
				throw new ServerException(Error.REG_ZIPCODE_INVALID.toString(),
						Integer.toString(Error.REG_ZIPCODE_INVALID.getCode()));
		}

	}

	@Override
	public String getLogo(Long orgId, String type) throws UserNotFoundException {
		Logo logo = generalDao.findOneSQLQuery(new Logo(),
				"select * from logo_image where orgId=1 and logoType='" + type + "'");
		return logo.getUrl();
	}

	@Override
	public User findUserByEmailForPassword(String userEmail) throws ServerException, UserNotFoundException {

		LOGGER.info("UserServiceImpl.findUserByEmail() - [" + userEmail + "]");

		String query = "Select * from Users u INNER JOIN address a On a.user_id = u.UserId "
				+ " INNER JOIN Usersinroles ur On ur.user_id = u.UserId INNER JOIN Role r On r.id = ur.role_id WHERE "
				+ "(u.username = '" + userEmail + "' OR a.phone='" + userEmail + "' Or u.email ='" + userEmail
				+ "')  and r.rolename in  ('Driver')";

		User user = generalDao.findOneSQLQuery(new User(), query);

		if (user == null || user.getId() == 0)
			throw new ServerException(Error.INVALID_EMAIL_ID.toString(),
					Integer.toString(Error.INVALID_EMAIL_ID.getCode()));

		return user;
	}

	@Override
	public void createPasswordResetTokenForUser(User user, String token) throws UserNotFoundException {

		LOGGER.info("UserServiceImpl.createPasswordResetTokenForUser() - with  []");

		Password password = generalDao.findOneHQLQuery(new Password(), "FROM Password where user_id =" + user.getId());

		if (password != null) {
			password.setPasswordVerificationToken(token);
			password.setPasswordVerificationExpiration(password.calculateExpiryDate(24 * 60));

			generalDao.savOrupdate(password);
		}
	}

	@Override
	public Password getPasswordResetToken(String token) throws ServerException, UserNotFoundException {

		LOGGER.info("UserServiceImpl.getPasswordResetToken() - with  [" + token + "]");

		Password passwordObject = null;
		List<Password> tempList = generalDao.findAllHQLQuery(new Password(),
				"From Password Where passwordVerificationToken ='" + token + "'");

		if (tempList != null && tempList.size() > 0)
			passwordObject = (Password) tempList.get(0);
		if (passwordObject == null) {
			throw new ServerException(Error.PASSWORD_NOT_FOUND.toString(),
					Integer.toString(Error.PASSWORD_NOT_FOUND.getCode()));
		} else {
			return passwordObject;
		}

	}

	@Override
	public User getUserByUID(String uid) throws UsernameNotFoundException {

		LOGGER.info("UserServiceImpl.getUserByUID() - [" + uid + "]");
		User userDetails = null;
		try {
			userDetails = this.generalDao.findOneHQLQuery(new User(), "From User where uid='" + uid + "'");
//			PermissionInRevenue perrmission = this.generalDao.findOneSQLQuery(new PermissionInRevenue(),
//					"Select * from permission_in_revenue where userId=" + userDetails.getId());
//			if (perrmission == null) {
//				perrmission = new PermissionInRevenue();
//				perrmission.setUserId((long) userDetails.getId());
//				if (!userDetails.getRoles().iterator().next().getRolename().equals(Roles.Manufacturer.toString())) {
//					perrmission.setShowRevenue(true);
//					perrmission.setChangePrice(true);
//				} else {
//					perrmission.setShowRevenue(false);
//					perrmission.setChangePrice(false);
//
//				}
//				if (userDetails.getRoles().iterator().next().getRolename().equals(Roles.Owner.toString())
//						|| userDetails.getRoles().iterator().next().getRolename().equals(Roles.SiteHost.toString())) {
//					perrmission = generalDao.save(perrmission);
//				}
//
//			} else {
//				if (userDetails.getRoles().iterator().next().getRolename().equals(Roles.Manufacturer.toString())) {
//					perrmission.setShowRevenue(false);
//					perrmission.setChangePrice(false);
//
//				}
//			}
//			userDetails.setRevenuePermission(perrmission);

			LOGGER.info("user object {}", userDetails.toString());
			return userDetails;
		} catch (UserNotFoundException e) {
			UserServiceImpl.LOGGER.info("Invalid User Details : " + uid);
			throw new UsernameNotFoundException(e.getMessage());
		}
	}

	@Override
	public void updatePassword(String id, Map<String, Object> password, boolean check)
			throws UserNotFoundException, ServerException {

		User currentUser = getUserByUID(id);

		updatePasswordValidations((String) password.get("password"), (String) password.get("newPassword"),
				(String) password.get("confrimPassword"), currentUser.getPassword(), check, currentUser);

		changeUserPassword(currentUser, (String) password.get("newPassword"));

	}

	@Override
	public Password changeUserPassword(User user, String password) throws ServerException, UserNotFoundException {

		LOGGER.info("UserServiceImpl.changeUserPassword() - with  [" + user.getEmail() + "]");

		Password passwordObject = generalDao
				.findAllHQLQuery(new Password(), "From Password where user_id = " + user.getId()).get(0);

		password = new StandardPasswordEncoder(passwordEncoder).encode(password);
		passwordObject.setPassword(password);
		passwordObject.setLastChangedDate(new Date());
		passwordObject.setPwdExpired(false);
		generalDao.savOrupdate(passwordObject);

		return passwordObject;

	}

	@SuppressWarnings("unused")
	private void updatePasswordValidations(String oldPassword, String newpassword, String confPassword, String password,
			boolean check, User currentuser) throws ServerException, UserNotFoundException {

		if (check) {
			if (oldPassword == null)
				throw new ServerException(Error.OLD_PASSWORD_NULL.toString(),
						Integer.toString(Error.OLD_PASSWORD_NULL.getCode()));
			else if (oldPassword.trim().equals(""))
				throw new ServerException(Error.OLD_PASSWORD_NULL.toString(),
						Integer.toString(Error.OLD_PASSWORD_NULL.getCode()));
			else if (!(new StandardPasswordEncoder(passwordEncoder).matches(oldPassword, password)))
				throw new ServerException(Error.OLD_PASSWORD_MISMATCH.toString(),
						Integer.toString(Error.OLD_PASSWORD_MISMATCH.getCode()));
		}

		if (newpassword == null || newpassword.trim().equals(""))
			throw new ServerException(Error.EDITPROF_NEWPWD_NULL.toString(),
					Integer.toString(Error.EDITPROF_NEWPWD_NULL.getCode()));

		if (confPassword == null || confPassword.trim().equals(""))
			throw new ServerException(Error.REG_CONFPWD_NULL.toString(),
					Integer.toString(Error.REG_CONFPWD_NULL.getCode()));

		if (newpassword != null && !currentuser.getRoles().iterator().next().getRolename().equalsIgnoreCase("Driver")) {

			if ((new StandardPasswordEncoder(passwordEncoder).matches(newpassword, password))) {
				throw new ServerException(Error.OLD_PASSWORD_MISMATCHForReset.toString(),
						Integer.toString(Error.OLD_PASSWORD_MISMATCHForReset.getCode()));
			}

			if (!Pattern.matches(
					"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*\"()_+{}\\[\\]:;,.<>/?-])[A-Za-z\\d~!@#$%^&*()_+\"{}\\[\\]:;,.<>/?-]{12,20}$",
					newpassword)) {
				throw new ServerException(Error.REG_PWD_INVALID.toString(),
						Integer.toString(Error.REG_PWD_INVALID.getCode()));
			}
		}
		if (newpassword != null && currentuser.getRoles().iterator().next().getRolename().equalsIgnoreCase("Driver")) {
			if ((new StandardPasswordEncoder(passwordEncoder).matches(newpassword, password))) {
				throw new ServerException(Error.OLD_PASSWORD_MISMATCHForReset.toString(),
						Integer.toString(Error.OLD_PASSWORD_MISMATCHForReset.getCode()));
			}
			if (newpassword.length() > 15 || newpassword.length() < 6) {
				throw new ServerException(Error.REG_PWD_INVALID_Driver.toString(),
						Integer.toString(Error.REG_PWD_INVALID_Driver.getCode()));
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

	private void updateUserValidations(Map<String, Object> user) throws ServerException {

		User currentUser = new User();
		currentUser.setFirstName((String) user.get("firstName"));
		currentUser.setLastName((String) user.get("lastName"));
		currentUser.setEmail((String) user.get("email"));
		currentUser.setUsername((String) user.get("email"));

		if (currentUser.getFirstName() == null || currentUser.getFirstName() == "")
			throw new ServerException(Error.REG_FIRSTNAME_NULL.toString(),
					Integer.toString(Error.REG_FIRSTNAME_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z0-9 -]{3,20}", currentUser.getFirstName()))
			throw new ServerException(Error.REG_FIRSTNAME_INVALID.toString(),
					Integer.toString(Error.REG_FIRSTNAME_INVALID.getCode()));

		if (currentUser.getLastName() == null || currentUser.getLastName() == "")
			throw new ServerException(Error.REG_LASTNAME_NULL.toString(),
					Integer.toString(Error.REG_LASTNAME_NULL.getCode()));
		else if (!Pattern.matches("[a-z A-Z0-9 -]{3,20}", currentUser.getLastName()))
			throw new ServerException(Error.REG_LASTNAME_INVALID.toString(),
					Integer.toString(Error.REG_LASTNAME_INVALID.getCode()));

		if (currentUser.getEmail() == null || currentUser.getEmail() == "")
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));
		else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,6}$", currentUser.getEmail())
				|| currentUser.getEmail().length() > 80)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

	}

	@Override
	public void activateUser(String email) throws ServerException, UserNotFoundException {
		// addSiteHostUserValidations
		User user = findUserByEmail(email);

		if (user != null) {
			Profile profile = user.getProfiles().iterator().next();
			profile.setStatus(Status.ACTIVE.toString());
			generalDao.update(profile);
			return;
		}

	}

	@Override
	public void setPassword(Map<String, Object> map) throws ServerException, UserNotFoundException {

		String email = (String) map.get("email");
		String password = (String) map.get("password");

		if (password == null || password.equals(""))
			throw new ServerException(Error.REG_PWD_NULL.toString(), Integer.toString(Error.REG_PWD_NULL.getCode()));

		if (email == null || email.equals(""))
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));

		User user = findUserByEmail(email);

		if (!Pattern.matches(
				"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~!@#$%^&*\"()_+{}\\[\\]:;,.<>/?-])[A-Za-z\\d~!@#$%^&*()_+\"{}\\[\\]:;,.<>/?-]{12,20}$",
				password))
			throw new ServerException(Error.REG_PWD_INVALID.toString(),
					Integer.toString(Error.REG_PWD_INVALID.getCode()));

		if (user != null) {
			Profile profile = user.getProfiles().iterator().next();
			profile.setStatus(Status.ACTIVE.toString());
			generalDao.update(profile);
			Password pwd = user.getPasswords().iterator().next();
			pwd.setPassword(new StandardPasswordEncoder(passwordEncoder).encode(password));
			pwd.setPwdExpired(false);
			generalDao.save(pwd);
			generalDao.update(user);
			return;
		}

	}

	@Override
	public boolean isUsrPwdExprd(long userId) throws UserNotFoundException, ServerException {

		User user = getUser(userId);
		

		return user.getPasswords().iterator().next().isPwdExpired();
	}

	@Override
	public Map<String, Object> getColor(Long orgId) {
		String query = "select cs.primaryColor,cs.secondaryColor from customsetting cs where orgId=" + orgId;
		List<Map<String, Object>> list = generalDao.findAliasData(query);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public User findUserByEmail(String userEmail) throws ServerException, UserNotFoundException {

		LOGGER.info("UserServiceImpl.findUserByEmail() - [" + userEmail + "]");

		String query = "Select * from Users u  " + " INNER JOIN Usersinroles ur On ur.user_id = u.UserId WHERE "
				+ "(u.username = '" + userEmail + "' Or u.email ='" + userEmail + "')  and ur.role_id =3";

		User user = generalDao.findOneSQLQuery(new User(), query);


		if (user == null)
			throw new ServerException(Error.NO_RECORDFOUND.toString(),
					Integer.toString(Error.NO_RECORDFOUND.getCode()));
		return user;
	}

}