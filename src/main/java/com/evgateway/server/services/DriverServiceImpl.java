package com.evgateway.server.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.DataTableDao;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.GridkeyDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.enums.Range;
import com.evgateway.server.enums.Reports;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.FilterForm;
import com.evgateway.server.form.ReportsAndStatsForm;
import com.evgateway.server.form.VehicleForm;
import com.evgateway.server.messages.Error;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.mobile.model.Response;
import com.evgateway.server.model.AppConfigSettingResponse;
import com.evgateway.server.model.Mail;
import com.evgateway.server.model.SendemailModel;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.Accounts;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.AppConfigSetting;
import com.evgateway.server.pojo.AutoReload;
import com.evgateway.server.pojo.ConfigurationSettings;
import com.evgateway.server.pojo.Credentials;
import com.evgateway.server.pojo.DealerInOrg;
import com.evgateway.server.pojo.GridKeyRequests;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Notification;
import com.evgateway.server.pojo.Organization;
import com.evgateway.server.pojo.OwnerInOrg;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.PreferredNotification;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.RecurringAmount;
import com.evgateway.server.pojo.ReleaseNote;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.UserFavStation;
import com.evgateway.server.pojo.Vehicles;
import com.evgateway.server.pojo.WorldPayCreditCard;
import com.evgateway.server.utils.TimeConverstion;
import com.evgateway.server.utils.UserTimeZoneConvertion;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DriverServiceImpl implements DriverService {

	final static Logger LOGGER = LoggerFactory.getLogger(DriverServiceImpl.class);

	@Value("${password.encoder.secret}")
	private String passwordEncoder;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Autowired
	private StationDao stationDao;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private DataTableDao<?, ?> dataTableDao;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${report_service_url}")
	private String reportserviceurl;

	@Value("${EVG-Correlation-ID}")
	private String EVGCorrelationID;

	@Value("${notification_url}")
	private String emailnotificationurl;

	@Autowired
	private UserService userService;

	@Autowired
	private UserTimeZoneConvertion timeZoneConvertion;

	@Autowired
	private UserDao<?, ?> userDao;

	@Autowired
	private GridkeyDao gridkeyDao;

	@Value("${mobileAPIsURL}")
	private String mobileAPIsURL;

	private List<Long> convertToLongList(List<BigDecimal> bigDecimalList) {

		List<Long> longs = bigDecimalList.stream().map(BigDecimal::longValue).collect(Collectors.toList());

		return longs;
	}

	public List<Long> getSiteIdBasedOnOrg(Long orgId) throws UserNotFoundException {

		LOGGER.info("StationDaoImpl.getStationIdBasedOnRole() - id: [" + orgId + "]");

		List<Long> longList = new ArrayList<>();

		OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
				"select Top 1 * from owner_in_org where orgId =" + orgId);
		DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
				"select Top 1 * from dealer_in_org where orgId =" + orgId);
		System.out.println("dealerorg---->" + dealer);

		if (owner != null) {
			List<Long> ownerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_org WHERE ownerId IN "
							+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId =" + orgId + "))"));

			longList.addAll(ownerSite);
			System.out.println("siteid based om owner" + longList);
		}

		if (dealer != null) {
			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
							+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId =" + orgId + "))"));

			longList.addAll(dealerSite);
			System.out.println("siteid based om dealer" + longList);
		}

		return longList;

	}

	@Override
	public List<Map<String, Object>> getDashboardReports(String useruid, int id, int period)
			throws UserNotFoundException {

//		User u = userDao.getCurrentUser();
		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		LOGGER.info("DriverServiceImpl.getDashboardReports() -for [] ");

		Accounts userAccount = u.getAccount().iterator().next();

		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();

		switch (id) {

		case 1:

			String sql = "SELECT COUNT(s.id) AS transactions,  "
					+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0) AS kWh, "
					+ " coalesce(Round(SUM(s.sessionElapsedInMin),2),0) AS duration, "
					+ " coalesce(Round(SUM(at.amtDebit),2),0)  AS charges , "
					+ " (SELECT acc.accountBalance FROM accounts acc WHERE acc.user_id = " + u.getId()
					+ ") AS balance, " + " (Select count(id) from station) AS totalStation "
					+ " From session s INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
					+ " INNER JOIN accounts a ON a.id = at.account_id "
					+ " INNER JOIN USERS u ON u.userId = a.user_id WHERE u.userId=" + u.getId();

			if (u.getOrgId() != 1) {
				List<Long> longList = getSiteIdBasedOnOrg(u.getOrgId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = (ids.isEmpty() ? "0" : ids);
				sql = sql.replace("from station", " from station where siteId in(" + ids + ")");
			}

			finalData.addAll(generalDao.findAliasData(sql));

			break;
		case 2:

			String hql3 = "Select top 1 st.id, st.stationName,st.referNo,s.sessionElapsedInMin, s.finalCostInSlcCurrency,s.kilowattHoursUsed "
					+ " from session s inner join port p on s.port_id = p.id "
					+ " INNER JOIN station st ON p.station_id = st.id "
					+ " INNER JOIN account_transaction ats on ats.id = s.accountTransaction_id where ats.account_id = "
					+ userAccount.getId() + " order BY s.startTimeStamp desc";

			finalData.addAll(generalDao.findAliasData(hql3));

			break;
		}

		return finalData;
	}

	@Override
	public List<Map<String, Object>> getChartReports(String useruid, int id, int period) throws UserNotFoundException {

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
//		User user = userDao.getCurrentUser();

		LOGGER.info("DriverServiceImpl.getChartReports() -for [] with " + period);

		String hql = null;

		switch (id) {

		case 1:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ "And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) AND u.UserId = "
						+ user.getId()
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ "AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time]," + "COUNT(s.id) [value] FROM session s "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) "
						+ "AND GETUTCDATE() AND u.UserId = " + user.getId()
						+ "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time]," + "COUNT(s.id) [value] FROM session s "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE() "
						+ "AND u.UserId = " + user.getId() + "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period + "AND u.UserId = " + user.getId()
						+ "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			break;

		case 2:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ "And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) AND u.UserId = "
						+ user.getId()
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ "AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) "
						+ "AND GETUTCDATE() AND u.UserId = " + user.getId()
						+ "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE() AND u.UserId = "
						+ user.getId() + "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND u.UserId = "
						+ user.getId() + "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0) as [value] FROM session s  "
						+ "INNER JOIN account_transaction at ON s.accountTransaction_id = at.id "
						+ "INNER JOIN accounts ac ON at.account_id = ac.id  "
						+ "INNER JOIN Users u ON ac.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period + "AND u.UserId = " + user.getId()
						+ "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			break;

		default:
			break;

		}

		List<Map<String, Object>> data = generalDao.findAliasData(hql);

		boolean nullFlag = true;
		for (Map<String, Object> map : data) {
			BigDecimal bigDecimalCurrency = new BigDecimal(map.get("value").toString());
			// Assigning the converted value of bg to d
			Double dob = bigDecimalCurrency.doubleValue();
			if (dob != 0)
				nullFlag = false;
		}

		if (nullFlag)
			return null;

		return data;

	}

	@Override
	public ByteArrayInputStream getPDFReports(String useruid, int period, String type)
			throws IOException, UserNotFoundException {

		LOGGER.info("ChargingActivityServiceImpl.getPDFReports() -  [" + period + "]");

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
//		User user = userDao.getCurrentUser();

		String hql = "";

		if (period == 30) {
			hql = "SELECT st.siteName ,stn.stationName, stn.referNo,s.startTimeStamp, s.endTimeStamp, ct.displayName [portType], "
					+ "s.customerId, p.displayName, "
					+ "u.firstName+' '+u.lastname [customerName], Round(s.kilowattHoursUsed,2) [kwh], s.cost, s.transactionFee, "
					+ "s.finalCostInSlcCurrency [spent], u.email, s.sessionElapsedInMin [duration], at.transactionId "
					+ "FROM session s  " + "INNER JOIN account_transaction at ON at.id = s.accountTransaction_id "
					+ "INNER JOIN accounts ac ON ac.id = at.account_id " + "INNER JOIN Users u ON u.UserId= ac.user_id "
					+ "INNER JOIN port p ON p.id = s.port_id " + "INNER JOIN connectorType ct ON ct.id = p.standard "
					+ "INNER JOIN station stn ON stn.id = p.station_id "
					+ "INNER JOIN site st ON st.siteId = stn.siteId "
					+ "WHERE MONTH(s.startTimeStamp) = MONTH(GETUTCDATE()) AND YEAR(s.startTimeStamp)=YEAR(GETUTCDATE())"
					+ "  AND u.UserId =  " + user.getId() + "ORDER BY s.startTimeStamp DESC";
		} else if (period == 365) {
			hql = "SELECT st.siteName ,stn.stationName, stn.referNo,s.startTimeStamp, s.endTimeStamp, ct.displayName [portType], "
					+ "s.customerId, p.displayName, "
					+ "u.firstName+' '+u.lastname [customerName], Round(s.kilowattHoursUsed,2) [kwh], s.cost, s.transactionFee, "
					+ "s.finalCostInSlcCurrency [spent], u.email, s.sessionElapsedInMin [duration], at.transactionId "
					+ "FROM session s  " + "INNER JOIN account_transaction at ON at.id = s.accountTransaction_id "
					+ "INNER JOIN accounts ac ON ac.id = at.account_id " + "INNER JOIN Users u ON u.UserId= ac.user_id "
					+ "INNER JOIN port p ON p.id = s.port_id " + "INNER JOIN connectorType ct ON ct.id = p.standard "
					+ "INNER JOIN station stn ON stn.id = p.station_id "
					+ "INNER JOIN site st ON st.siteId = stn.siteId "
					+ "WHERE YEAR(s.startTimeStamp)=YEAR(GETUTCDATE()) " + "   AND u.UserId =  " + user.getId()
					+ "ORDER BY s.startTimeStamp DESC";

		} else if (period == 7) {
			hql = "SELECT st.siteName ,stn.stationName, stn.referNo,s.startTimeStamp, s.endTimeStamp, ct.displayName [portType], "
					+ "s.customerId, p.displayName, "
					+ "u.firstName+' '+u.lastname [customerName], Round(s.kilowattHoursUsed,2) [kwh], s.cost, s.transactionFee, "
					+ "s.finalCostInSlcCurrency [spent], u.email, s.sessionElapsedInMin [duration], at.transactionId "
					+ "FROM session s  " + "INNER JOIN account_transaction at ON at.id = s.accountTransaction_id "
					+ "INNER JOIN accounts ac ON ac.id = at.account_id " + "INNER JOIN Users u ON u.UserId= ac.user_id "
					+ "INNER JOIN port p ON p.id = s.port_id " + "INNER JOIN connectorType ct ON ct.id = p.standard "
					+ "INNER JOIN station stn ON stn.id = p.station_id "
					+ "INNER JOIN site st ON st.siteId = stn.siteId "
					+ "WHERE  s.startTimeStamp BETWEEN DATEADD(DAY, 2- DATEPART(WEEKDAY, GETUTCDATE()), CAST(GETUTCDATE() AS DATE)) AND  "
					+ "DATEADD(DAY, 8 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(GETUTCDATE() AS DATE)) "
					+ "   AND u.UserId =  " + user.getId() + "ORDER BY s.startTimeStamp DESC";

		} else if (period == 1) {
			hql = "SELECT st.siteName ,stn.stationName, stn.referNo,s.startTimeStamp, s.endTimeStamp, ct.displayName [portType], "
					+ "s.customerId, p.displayName, "
					+ "u.firstName+' '+u.lastname [customerName], Round(s.kilowattHoursUsed,2) [kwh], s.cost, s.transactionFee, "
					+ "s.finalCostInSlcCurrency [spent], u.email, s.sessionElapsedInMin [duration], at.transactionId "
					+ "FROM session s  " + "INNER JOIN account_transaction at ON at.id = s.accountTransaction_id "
					+ "INNER JOIN accounts ac ON ac.id = at.account_id " + "INNER JOIN Users u ON u.UserId= ac.user_id "
					+ "INNER JOIN port p ON p.id = s.port_id " + "INNER JOIN connectorType ct ON ct.id = p.standard "
					+ "INNER JOIN station stn ON stn.id = p.station_id "
					+ "INNER JOIN site st ON st.siteId = stn.siteId "
					+ "WHERE DAY(s.startTimeStamp)=DAY(GETUTCDATE()) AND YEAR(s.startTimeStamp)=YEAR(GETUTCDATE()) AND MONTH(s.startTimeStamp)=MONTH(GETUTCDATE()) "
					+ "   AND u.UserId =  " + user.getId() + "ORDER BY s.startTimeStamp DESC";

		}

		List<Map<String, Object>> list = generalDao.findAliasData(hql);

		String[] sessionTblHeaderNames = null;
		String[] sessionTblKeys = null;

		if (type.equals("pdf")) {

			sessionTblHeaderNames = new String[] { "User Name", "Station ID", "Station Name", "Port Id", "Port Type",
					"Start Time", "End Time", "Duration\n(HH:MM:SS)", "Energy \n Usage(kWh)", "Transaction Id",
					"Cost", };

			sessionTblKeys = new String[] { "email", "referNo", "stationName", "displayName", "portType",
					"startTimeStamp", "endTimeStamp", "duration", "kwh", "transactionId", "cost" };

		} else if (type.equals("xls")) {

			sessionTblHeaderNames = new String[] { "SITE NAME", "Station ID", "STATION NAME", "CHARGE START-TIME",
					"CHARGE END-TIME", "DURATION", "CONNECTOR TYPE", "CUSTOMER ID", "PORT ID", "CUSTOMER NAME", "SPENT",
					"kWh USED", "COST", "TRANSACTION FEE" };

			sessionTblKeys = new String[] { "siteName", "referNo", "stationName", "startTimeStamp", "endTimeStamp",
					"duration", "portType", "customerId", "displayName", "customerName", "spent", "kwh", "cost",
					"transactionFee" };

		}

		String summaryTblKeys[] = { "generatedBy", "totCost", "totCount", "totDuration", "totKwh", "reportTime" };

		String summaryTblheadings[] = { "Report Generated By", "Total Transactions Cost", "Total Transactions Count",
				"Total Charging Time utilized (HH:MM:SS)", "Total Energy (Kilowatt) Consumption",
				"Date and time of the Report being generated" };

		List<Map<String, Object>> totSummaryList = new ArrayList<>();

		Map<String, Object> totSummaryMap = new HashMap<String, Object>();

		if (list != null) {

			double totKwh = 0;
			double totCost = 0;
			double totDurationMins = 0;
			for (Map<String, Object> map : list) {
				totCost = totCost + (Double) map.get("cost");
				String dur = String.valueOf(map.get("duration"));
				totDurationMins = totDurationMins + Double.parseDouble(dur);
				// totDurationMins = totDurationMins + (Double) map.get("duration");
				totKwh = totKwh + Math.floor(Double.valueOf(map.get("kwh").toString()) * 10000.0) / 10000.0;
			}
			totSummaryMap.put("generatedBy", user.getFirstName() + " " + user.getLastName());
			totSummaryMap.put("totCost", "$" + Math.round(totCost));
			totSummaryMap.put("totKwh", Math.floor(totKwh * 10000.0) / 10000.0 + "(kWh)");
			totSummaryMap.put("totDuration", TimeConverstion.convertHHMMSS(totDurationMins));
			totSummaryMap.put("reportTime", new Date());
			totSummaryMap.put("totCount", list.size());

			totSummaryList.add(totSummaryMap);
		}

		ByteArrayInputStream byteInput = null;

//		if (type.equals("xls"))
//			byteInput = excelGenerationService.generateExcel(list, sessionTblKeys, sessionTblHeaderNames, "fileName",
//					"Driver-ChargingActivity");
//		else if (type.equals("pdf")) {
//			byteInput = pdfGenerationService.generateChargingActivityPDF(totSummaryList, summaryTblheadings,
//					summaryTblKeys, list, sessionTblKeys, sessionTblHeaderNames, "fileName", "userRole",
//					user.getOrgId(), true);
//		}

		return byteInput;
	}

	@Override
	public List<Map<String, Object>> getRecentlyUseStations(String useruid) throws UserNotFoundException {

		List<Map<String, Object>> findAllSQLQuery = new ArrayList<Map<String, Object>>();
		try {
			User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
//			User u = userDao.getCurrentUser();

			LOGGER.info("DriverServiceImpl.getRecentlyUseStations() -  [" + u.getUsername() + "]");

			Long accountId = u.getAccount().iterator().next().getId();

			String hql = "Select distinct st.id From station st Inner join port p on p.station_id = st.id Inner join session se on se.port_id = p.id Inner join account_transaction ac "
					+ "on ac.id = se.accountTransaction_id WHERE ac.account_id = " + accountId + "";
			List<String> stationId = (List<String>) generalDao.findAllByIdSQLQuery(hql);
			System.out.println("stationId : " + stationId);
			String station = stationId.toString().replace("[", "").replace("]", "");

			if (station.isEmpty() || station == null)
				station = "0";

			String query = "Select top 10 * From Station st where st.id in (" + station + ")";

			findAllSQLQuery = generalDao.findAliasData(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return findAllSQLQuery;
	}

	@Override
	public List<Map<String, Object>> getFavoriteStations(String useruid) throws UserNotFoundException {

		User u = getUserByUID(useruid);
//		User u = userDao.getCurrentUser();
		LOGGER.info("DriverServiceImpl.getFavoriteStations() -  [" + u.getUsername() + "]");

		String hql = "Select * From station Where id IN (Select stationId From user_fav_station Where userId = "
				+ u.getId() + ")";

		return generalDao.findAliasData(hql);
	}

	@Override
	public List<Map<String, Object>> addFavoriteStations(String useruid, long stationId)
			throws UserNotFoundException, ServerException {

		LOGGER.info("DriverServiceImpl.addFavoriteStations() - station  Id [" + stationId + "]");
		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
//		User u = userDao.getCurrentUser();

		String query = "INSERT INTO user_fav_station (stationId, userId) VALUES (" + stationId + "," + u.getId() + ")";

		try {
			generalDao.queryExecute(query);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new ServerException(Error.ADD_FAV_STATIONS.toString(),
					Integer.toString(Error.ADD_FAV_STATIONS.getCode()));
		}

		return getFavoriteStations(useruid);
	}

	@Override
	public List<Map<String, Object>> getStation(String useruid, String filter) throws UserNotFoundException {
		// TODO Auto-generated method stub

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		// User user = userDao.getCurrentUser();

		String hql = "";
		String hql2 = "SELECT s.referNo, s.uid,s.id , s.stationName, g.latitude, g.longitude,st.siteName ,count(s.id) as stationCount,"
				+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
				+ "(select top 1 orgid from  dealer_in_org   "
				+ "where dealerId in(select dealerid from owner_in_dealer where ownerId "
				+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
				+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url , "
				+ " s.stationAddress From site st left join station s on s.siteId = st.siteId"
				+ " INNER JOIN geoLocation g ON g.id = s.coordinateId "
				+ " WHERE s.visibilityOnMap = 'true' AND (s.referNo LIKE '%" + filter + "%' or s.stationAddress like '%"
				+ filter + "%'  or s.stationName LIKE '%" + filter
				+ "%' )group by  s.uid,  st.siteId,st.siteName, g.latitude, g.longitude,st.uuid,s.referNo ,s.id,s.siteId,s.stationName,s.stationAddress,s.stationTimeStamp,s.stationAvailStatus ";
		if (user.getOrgId() != 1) {
			List<Long> longList = stationDao.getSiteIdBasedOnOrg(user.getOrgId());
			String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
			ids = (ids.isEmpty() ? "0" : ids);
			hql2 = hql2.replace("s.visibilityOnMap = 'true'",
					"s.visibilityOnMap = 'true' and s.siteId in(" + ids + ")");
		}
		if (!filter.equalsIgnoreCase("null")) {
			return generalDao.findAliasData(hql2);
		}
		hql = "SELECT s.referNo, s.id ,s.uid, s.stationName, g.latitude,st.siteName ,count(s.id) as stationCount,"
				+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
				+ "(select top 1 orgid from  dealer_in_org   "
				+ "where dealerId in(select dealerid from owner_in_dealer where ownerId "
				+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
				+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url ,"
				+ " g.longitude, s.stationAddress From site st left join station s on s.siteId = st.siteId INNER JOIN geoLocation g ON g.id = s.coordinateId WHERE s.visibilityOnMap = 'true'"
				+ " group by s.uid,  st.siteId,st.siteName, g.latitude, g.longitude,st.uuid,s.referNo ,s.id,s.siteId,s.stationName,s.stationAddress,s.stationTimeStamp,s.stationAvailStatus ";
		if (user.getOrgId() != 1) {
			List<Long> longList = stationDao.getSiteIdBasedOnOrg(user.getOrgId());
			String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
			ids = (ids.isEmpty() ? "0" : ids);
			hql = hql.replace("s.visibilityOnMap = 'true'", "s.visibilityOnMap = 'true' and s.siteId in(" + ids + ")");
		}
		return generalDao.findAliasData(hql);
	}

	@Override
	public void addGridKeyRequest(String useruid, long count)
			throws ServerException, UserNotFoundException, IOException {
		// TODO Auto-generated method stub

		LOGGER.info("DriverServiceImpl.addGridKeyRequest() - No. of [" + count + "]");

		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		Accounts account = u.getAccount().iterator().next();
		

		AppConfigSetting config = generalDao.findOneSQLQuery(new AppConfigSetting(),
				"select * from app_config_setting where  currencyCode='"
						+ account.getCurrencyType() + "' and orgId=" + u.getOrgId());
		if(config==null)
			config=generalDao.findOneSQLQuery(new AppConfigSetting(),
					"select * from app_config_setting where  currencyCode='USD' and orgId=1");

		if (count > config.getRFIDLimitPerOrder())
			throw new ServerException(com.evgateway.server.messages.Error.MAX_GRIDKEY_ERROR.toString(),
					Integer.toString(Error.MAX_GRIDKEY_ERROR.getCode()));

		if (gridkeyDao.getCountByUserId(u.getId()) > 0)
			throw new ServerException(com.evgateway.server.messages.Error.YOU_ALREADY_REQ_GRIDKEY.toString(),
					Integer.toString(Error.YOU_ALREADY_REQ_GRIDKEY.getCode()));

		if (account.getAccountBalance() >= (count * config.getRFIDPrice())) {

			Address add = u.getAddress().iterator().next();
			String orderId = String.valueOf((long) (Math.random() * 100000000000000L));
			String status = "Inprogress";
			GridKeyRequests grkey = new GridKeyRequests();
			grkey.setCount(count);
			grkey.setDateGenerated(new Date());
			grkey.setOrderId(orderId);
			grkey.setStatus(status);
			grkey.setUserId(u.getId());
			grkey.setFirstName(u.getFirstName());
			grkey.setRfidCount((int) count);
			grkey.setOrgId(u.getOrgId());
			grkey.setLastName(u.getLastName());
			grkey.setMobile(add.getPhone());
			grkey.setRfidType("RFID");
			grkey.setEmail(u.getEmail());
			String address = add.getAddressLine1() + " " + add.getAddressLine2() + " " + add.getCity() + ", "
					+ add.getState() + "-" + add.getZipCode();

			grkey.setAddress(address);

			gridkeyDao.addGridKeyRequest(grkey);
			AccountTransactions transaction = new AccountTransactions();
			Double amount = (double) (count * config.getRFIDPrice());
			transaction.setAccount(account);
			transaction.setCreateTimeStamp(new Date());
			transaction.setAmtDebit(amount);
			transaction.setStatus("SUCCESS");
			transaction.setComment("RFID activation charge amount");
			transaction.setCurrentBalance(account.getAccountBalance() - amount);
			generalDao.save(transaction);
			account.setAccountBalance(account.getAccountBalance() - amount);
			generalDao.update(account);

			notificationService.addNotification("RFID Request", 00000, status, u);
			constructAcknowledgmentEmail(count, u);

		} else
			throw new ServerException(com.evgateway.server.messages.Error.LOWFUNDS.toString(),
					Integer.toString(Error.LOWFUNDS.getCode()));

	}

	public Organization getOrganizationById(Long orgId) throws UserNotFoundException {

		LOGGER.info("StationServiceImpl.getOrganizationById() - name: [" + orgId + "]");

		return generalDao.findOneHQLQuery(new Organization(),
				"From Organization Where id =" + orgId + " ORDER BY orgName ASC");
	}

	private void constructAcknowledgmentEmail(long count, User user) throws IOException, UserNotFoundException {

		Organization org = getOrganizationById(user.getOrgId());

		ConfigurationSettings config = generalDao.findOneHQLQuery(new ConfigurationSettings(),
				"from ConfigurationSettings Where orgId =" + org.getId());

		String orgName = org.getOrgName();
		Address address = user.getAddress().iterator().next();
		List<String> list = new ArrayList<String>();
		Mail email = new Mail();
		list.add(address.getAddressLine1());
		list.add(address.getCity());
		list.add(address.getCountry());
		String list1 = Arrays.toString(list.toArray()).replace("[", "").replace("]", "").isEmpty() ? "-"
				: Arrays.toString(list.toArray()).replace("[", "").replace("]", "");
		String message = "";
		if (orgName.equalsIgnoreCase("Everon")) {
			message = "Hola administrador  ";
			message = message + " Un usuario Sr./Sra. " + user.getUsername() + " Requerido para " + count
					+ " Tarjetas Gridkey de " + list1;
			email.setMailSubject("Confirmación de solicitud de Gridkey");

		} else {
			message = "Hello Administrator  ";
			message = message + " A user Mr./Mrs. " + user.getUsername() + " requested for " + count
					+ " RFID cards from " + list1;
			email.setMailSubject("RFID Request Acknowledgment");
		}

		list.add(address.getAddressLine1());
		list.add(address.getCity());
		list.add(address.getCountry());

		email.setMailContent(message + " ");

		email.setMailTo(user.getEmail());
		email.setMailFrom(config.getEmail());

		SendemailModel model = new SendemailModel();
		model.setEmail(config.getEmail());
		model.setMail(email);
		model.setPassword(config.getPassword());
		model.setHost(config.getHost());
		model.setPort(config.getPort());
		model.setOrgId(config.getOrgId());
		String serverUrl1 = emailnotificationurl + "/services/sendemail/send";

		HttpHeaders header = new HttpHeaders();
		header.set("EVG-Correlation-ID", EVGCorrelationID);
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SendemailModel> requestEntity3 = new HttpEntity<>(model, header);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, String.class);

		System.out.println("postForEntity::" + postForEntity.getStatusCode());

		// emailService.sendEmail(email, config.getHost(), config.getPort(),
		// config.getEmail(), config.getPassword());

	}

	@Override
	public PagedResult<Map<String, String>> getTransactionByAccount(String useruid, int page, int size)
			throws UserNotFoundException {

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		Accounts account = user.getAccount().iterator().next();

		LOGGER.info("DriverServiceImpl.getTransactionByAccount() -  [" + account.getAccountName() + "],[" + page + "],["
				+ size + "]");

		Sort sort = Sort.by(Sort.Direction.DESC, "createTimeStamp");
		Pageable pageable = PageRequest.of(page, size, sort);

		String query = "WITH CTE AS (SELECT at.id,at.createTimeStamp as createdDate,at.currentBalance,at.status,at.comment"
				+ " AS comments, s.sessionId as uuid,  isNull(at.paymentMode,'Wallet') as paymentMode, ISNULL(at.currencyType,'USD')  AS currencySym, "
				+ "ISNULL(CONVERT(VARCHAR, (s.id)),'-') as sessionId, ISNULL(s.customerId,'-') as customerId, "
				+ "ISNULL((SELECT at.transactionId ),'-' ) As transactionIdforDriver , CASE WHEN at.amtDebit > 0  "
				+ "THEN ROUND(CAST(at.amtDebit AS NUMERIC(10, 3)), 2, 1)WHEN at.amtCredit > 0 THEN  "
				+ "ROUND(CAST(at.amtCredit AS NUMERIC(10, 3)), 2, 1) ELSE 0 END AS amount,CASE WHEN at.amtDebit > 0 THEN "
				+ " 'Debit' WHEN at.amtCredit > 0 THEN 'Credit' ELSE 'Debit' END AS amountType,at.createTimeStamp   "
				+ "FROM account_transaction at LEFT JOIN Session s ON  s.accountTransaction_id = at.id WHERE at.account_Id = "
				+ account.getId() + ")"
				+ "SELECT id,currentBalance,paymentMode,uuid,currencySym,sessionId,customerId, transactionIdforDriver ,  "
				+ "status,comments,amount,amountType,createTimeStamp as createdDate, SUM(CASE WHEN amountType = 'Credit' AND "
				+ "(status != 'FAILED' and comments != 'FAILED' ) THEN amount  WHEN amountType = 'Debit' AND (status != 'FAILED' "
				+ " and ( paymentMode!='Credit Card')  "
				+ ") THEN -amount ELSE 0 END)  OVER (ORDER BY id ) AS balance FROM CTE   ORDER BY id  DESC";
		List<Map<String, String>> sql = generalDao.findAliasDataforac(query);

		List<Map<String, String>> page1 = null;

		page1 = getPage(sql, page, size);

		PagedResult<Map<String, String>> pagedResult = new PagedResult<Map<String, String>>(
				new PageImpl<Map<String, String>>(page1, pageable, sql.size()));

		return pagedResult;
	}

	public List<Map<String, String>> getPage(List<Map<String, String>> list, int pageNumber, int pageSize) {
		int startIndex = (pageNumber) * pageSize;
		if (startIndex >= list.size()) {
			return Collections.emptyList();
		}

		int endIndex = Math.min(startIndex + pageSize, list.size());
		return list.subList(startIndex, endIndex);
	}

	@Override
	public List<Map<String, Object>> getStationInfo(String uid, String useruid)
			throws UserNotFoundException, JsonMappingException, JsonProcessingException, ServerException {

		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		List<Map<String, Object>> listStation = new ArrayList<Map<String, Object>>();
		String query = "DECLARE @json nvarchar(max);  WITH src (n) AS (  SELECT st.referNo,st.id, (select currencyType from site where siteId=st.siteId) as currencyType,"
				+ " ISNULL(st.stationAddress,'-' ) AS  stationAddress,st.stationMode, " + "    st.uid, "
				+ "    st.chagerType, " + " (CASE WHEN EXISTS  (SELECT * FROM user_fav_station "
				+ " WHERE stationId=st.id AND userId='" + u.getId() + "' ) THEN 1 ELSE 0 END ) AS favFlag, "
				+ "(SELECT pt.vendingPriceUnit,pt.vendingPricePerUnit, pt.vendingPriceUnit1,pt.vendingPricePerUnit1, pt.vendingPriceUnit2,pt.vendingPricePerUnit2,     pt.displayName,pt.status,(SELECT displayName FROM connectorType where id=pt.standard ) AS connectorType   FROM "
				+ " port pt  WHERE  pt.station_id = st.id " + "   FOR JSON PATH ) AS ports  FROM "
				+ "    station st WHERE st.uid='" + uid + "'"
				+ "   FOR JSON PATH) SELECT @json = src.n FROM src SELECT @json as 'loc' ";

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

		return listStation;

	}

	@Override
	public String setFavouriteStationToUser(String uid, String userUID)
			throws ServerException, UserNotFoundException, IOException {

		long count = 0;

		String favourite = "";
		long userId = generalDao.findIdBySqlQuery("select userId from Users where uid='" + userUID + "'");
		long stationid = generalDao.findIdBySqlQuery("select id from station where uid='" + uid + "'");

		count = generalDao.countSQL(
				"SELECT * FROM user_fav_station WHERE stationId='" + stationid + "' AND userId='" + userId + "'");
		if (count > 0) {
			generalDao.deleteSqlQuiries(
					"DELETE FROM user_fav_station where stationId='" + stationid + "' AND userId='" + userId + "'");
			favourite = "Station removed from  favourite list";
		} else {
			generalDao.save(new UserFavStation(stationid, userId));
			favourite = "Station added to favourite list";
		}

		return favourite;

	}

	@Override
	public List<Map<String, Object>> getRecentUsedStationByUser(String userUID)
			throws ServerException, UserNotFoundException, IOException {

		long userId = generalDao.findIdBySqlQuery("select userId from Users where uid='" + userUID + "'");

		String query = "SELECT DISTINCT uid,referNo,stationName,stationAddress,endTimeStamp, favFlag "
				+ " FROM (SELECT st.referNo,(CASE WHEN EXISTS    " + " (SELECT * FROM user_fav_station WHERE userid="
				+ userId + " and stationId=st.id) THEN 1 "
				+ "    ELSE 0 END ) AS favFlag,st.uid,st.stationName,ISNULL(st.stationAddress ,'-') AS "
				+ " stationAddress, ss.endTimeStamp , ROW_NUMBER() OVER"
				+ " (PARTITION BY st.referNo ORDER BY ss.endTimeStamp DESC ) AS rn "
				+ "	 FROM session ss INNER JOIN port pt ON ss.port_id = pt.id    "
				+ " INNER JOIN station st ON pt.station_id = st.id  " + "  WHERE ss.userId =" + userId
				+ ") AS subquery  WHERE rn = 1  " + " ORDER BY endTimeStamp DESC  ";

		List<Map<String, Object>> sql = generalDao.findAliasData(query);
		return sql;
	}

	@Override
	public List<Map<String, Object>> getFavouriteStationByUser(String userUID)
			throws ServerException, UserNotFoundException, IOException {
		long userId = generalDao.findIdBySqlQuery("select userId from Users where uid='" + userUID + "'");

		String query = "SELECT st.uid,st.referNO,st.stationName,"
				+ "ISNULL(st.stationAddress ,'-') AS stationAddress FROM station st"
				+ " WHERE  st.id IN (SELECT stationId from user_fav_station WHERE userId='" + userId + "') ";

		List<Map<String, Object>> sql = generalDao.findAliasData(query);

		return sql;
	}

	@Override
	public List<Map<String, Object>> getRFIDCardsByUId(String uid)
			throws ServerException, UserNotFoundException, IOException {

		String query = "SELECT cred.* FROM creadential cred  INNER JOIN accounts at ON cred.account_id = at.id "
				+ "INNER JOIN users u ON at.user_id = u.UserId WHERE u.uid = '" + uid
				+ "'   AND NOT( cred.rfidHex IS NULL  OR  cred.rfid IS NULL);";

		return generalDao.findAliasData(query);
	}

//	@Override
//	public void addRfid(GridKeyReqForm gridKeyRequests) throws UserNotFoundException, ServerException {
//
//		LOGGER.info("DriverServiceImpl.addRfid() - with []");
//
//		User user = userService.getUser(gridKeyRequests.getUserId());
//		User currentUser = userService.getCurrentUser();
//
//		long accountId = user.getAccount().iterator().next().getId();
//		Accounts account = generalDao.findOneById(new Accounts(), accountId);
//		AppConfigSetting appconf = generalDao.findOneSQLQuery(new AppConfigSetting(),
//				"Select * From app_config_setting where orgId=" + user.getOrgId() + " AND currencyCode='"
//						+ account.getCurrencyType() + "'");
//		if (appconf == null) {
//			appconf = generalDao.findOneSQLQuery(new AppConfigSetting(),
//					"Select * From app_config_setting where orgId=1 AND currencyCode='" + account.getCurrencyType()
//							+ "'");
//		}
//		if (gridkeyDao.getCountByUserId(user.getId()) + gridKeyRequests.getNoofcards() > 5)
//			throw new ServerException(com.evgateway.server.messages.Error.MAX_GRIDKEY_ERROR.toString(),
//					Integer.toString(Error.MAX_GRIDKEY_ERROR.getCode()));
//
//		if (gridkeyDao.getCountByUserId(user.getId()) > 0)
//			throw new ServerException(com.evgateway.server.messages.Error.YOU_ALREADY_REQ_GRIDKEY.toString(),
//					Integer.toString(Error.YOU_ALREADY_REQ_GRIDKEY.getCode()));
//		String roleName = user.getRoles().iterator().next().getRolename();
//		Role role = null;
//
//		role = userService.getRole(roleName);
//		if (account.getAccountBalance() < (gridKeyRequests.getNoofcards() * 5)
//				&& role.getRolename().equals(Roles.Driver.toString())) {
//			throw new ServerException(com.evgateway.server.messages.Error.LOWFUNDS.toString(),
//
//					Integer.toString(Error.LOWFUNDS.getCode()));
//
//		}
//		long first14 = (long) (Math.random() * 100000000000000L);
//		String orderId = String.valueOf(first14);
//		Date date = Calendar.getInstance().getTime();
//		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//		String strDate = dateFormat.format(date);
//		LOGGER.info("Current UTC Time Date while requesting gridkey : " + strDate);
//		String status = "Inprogress";
//
//		GridKeyRequests gridKey = new GridKeyRequests();
//		Address add = user.getAddress().iterator().next();
//
//		String query = "select isNull(addressLine1,'') as addressLine1,isNull(addressLine2,'') as addressLine2,"
//				+ "isnull(city,'') as city,isnull(state,'') as state,isnull(zipCode,'') as zipCode  from address where user_Id="
//				+ user.getId();
//
//		List<Map<String, Object>> addresss = generalDao.findAliasData(query);
//
//		String address = addresss.get(0).get("addressLine1") + " " + addresss.get(0).get("addressLine2") + " "
//				+ addresss.get(0).get("city") + " " + addresss.get(0).get("state") + "-"
//				+ addresss.get(0).get("zipCode");
//
//		gridKey.setAddress(address);
//		gridKey.setCount(gridKeyRequests.getNoofcards());
//		gridKey.setDateGenerated(new Date());
//		gridKey.setEmail(user.getEmail());
//		gridKey.setFirstName(user.getFirstName());
//		gridKey.setLastName(user.getLastName());
//		gridKey.setMobile(add.getPhone());
//		gridKey.setOrderId(orderId);
//		gridKey.setOrgId(user.getOrgId());
//		gridKey.setRfidCount(gridKeyRequests.getNoofcards());
//		gridKey.setStatus(status);
//		gridKey.setUserId(user.getId());
//		gridKey.setCreatedBy(currentUser.getUsername());
//		gridKey.setCreationDate(new Date());
//		gridKey.setRfidType(gridKeyRequests.getRfidType());
//
//		if (role.getRolename().equals(Roles.Driver.toString())) {
//			AccountTransactions accountTransactions = new AccountTransactions();
//			accountTransactions.setAccount(account);
//			accountTransactions.setStatus("SUCCESS");
//			accountTransactions.setCurrencyType(account.getCurrencyType());
//			if (gridKeyRequests.getRfidType().equals("External-RFID")) {
//				gridKey.setRfidType("External-RFID");
//				accountTransactions.setComment("RFID Type is External-RFID");
//				accountTransactions.setAmtDebit(0.0);
//				accountTransactions.setCurrentBalance(account.getAccountBalance());
//			} else {
//				gridKey.setRfidType("RFID");
//				accountTransactions.setComment("RFID activation charge amount");
//				accountTransactions.setAmtDebit(gridKeyRequests.getNoofcards() * appconf.getRFIDPrice());
//				accountTransactions.setCurrentBalance(
//						account.getAccountBalance() - gridKeyRequests.getNoofcards() * appconf.getRFIDPrice());
//				account.setAccountBalance(
//						account.getAccountBalance() - gridKeyRequests.getNoofcards() * appconf.getRFIDPrice());
//			}
//			accountTransactions.setPaymentMode("Wallet");
//			accountTransactions.setCreatedBy(currentUser.getUsername());
//			accountTransactions.setCreateTimeStamp(new Date());
//			generalDao.save(accountTransactions);
//		}
//		generalDao.save(gridKey);
//		generalDao.update(account);
//
//		addNotification("RFID Request", 0001, status, user.getId());
//
//	}

	@Override
	public PagedResult<List<Map<String, Object>>> getData(String useruid, FilterForm filter, int page, int size)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		LOGGER.info("ChargingActivity  -  []");

		vaidationForFilter(filter);

		String query = getDataQuery(useruid, filter);

		Sort sort = Sort.by(Sort.Direction.DESC, "s.id");

		Pageable pageable = PageRequest.of(page, size, sort);

		return dataTableDao.getDataAlias(pageable, query);

	}

	private String getDataQuery(String useruid, FilterForm filter)
			throws ServerException, ParseException, UserNotFoundException {

		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> userTimeMap = timeZoneConvertion.getUserCoordinates(filter.getUserTimeZone());
		Calendar calendar = Calendar.getInstance();
		String startTime = "GETUTCDATE()";
		String endTime = "GETUTCDATE()";
		String hours = userTimeMap.get("Hours").toString();
		String minutes = userTimeMap.get("Minutes").toString();
		String timeZone = userTimeMap.get("timeZoneFormat").toString();

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
//		User user = userDao.getCurrentUser();

		String filterResults = "";
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, 2);

		if (filter.getRange().equals("1")) {
			filter.setEndDate(outputFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, -1);
			endTime = "GETUTCDATE()-1";
			filter.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filter.getRange().equals("7")) {
			filter.setEndDate(outputFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, -7);
			endTime = "GETUTCDATE()-7";
			filter.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filter.getRange().equals("14")) {
			filter.setEndDate(outputFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, -14);
			endTime = "GETUTCDATE()-14";
			filter.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filter.getRange().equals("30")) {
			filter.setEndDate(outputFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, -30);
			endTime = "GETUTCDATE()-30";
			filter.setStartDate(outputFormat.format(calendar.getTime()));
		} else if (filter.getRange().equals("365")) {
			filter.setEndDate(outputFormat.format(calendar.getTime()));
			calendar.add(Calendar.DATE, -365);
			endTime = "GETUTCDATE()-365";
			filter.setStartDate(outputFormat.format(calendar.getTime()));
		}

		if (filter.getRange().equals("custom"))
			filterResults = " AND s.startTimeStamp BETWEEN  '" + filter.getStartDate() + "' AND '" + filter.getEndDate()
					+ "' ";
		else if (!filter.getRange().equalsIgnoreCase("All"))
			filterResults = " AND s.startTimeStamp BETWEEN " + endTime + " AND " + startTime + " ";

		String query = "SELECT s.id AS " + " sessionId ,s.sessionId as uuid,(Convert(VARCHAR(20),DATEADD(HOUR," + hours
				+ ",DATEADD(MINUTE," + minutes + ",convert(VARCHAR(10),s.startTimeStamp, 10) + ' ' +  "
				+ " CONVERT(VARCHAR(8), s.startTimeStamp, 108))),20)+' '+'" + timeZone
				+ "') AS startTime , s.sessionElapsedInMin AS chargeTime, s.startTimeStamp as sDate, "
				+ "  ROUND(s.kilowattHoursUsed,4) AS energyUsed,s.inaccurateTxn, s.finalCostInSlcCurrency AS totalCost, s.reasonForTer as endedBy, p.displayName AS  "
				+ "  conductorName, st.referNo AS stationId, sit.siteName,IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socStartVal),  'NA') as starting,  "
				+ "IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socEndVal),  'NA') as final, IIF(ISNULL(s.emailId,'Guest User')='','Guest User',s.emailId) "
				+ "as users ,ISNULL(s.currencyType,'USD') as currencySym FROM station st INNER JOIN port p ON p.station_id = st.id  "
				+ "INNER JOIN session s ON s.port_id = p.id INNER JOIN site sit ON sit.siteId = st.siteId  AND st.preProduction = 0  "
				+ "WHERE s.userId=" + user.getId() + filterResults;

		return query;

	}

	@Override
	public ReleaseNote getcurrtentVersion() throws UserNotFoundException {
		ReleaseNote releaseNote = generalDao.findOneSQLQuery(new ReleaseNote(),
				"Select * from releaseNote where iscurrent=1 ");

		return releaseNote;
	}

	public void vaidationForFilter(FilterForm filter) throws ServerException, ParseException {

		if (filter.getRange().equals("custom")) {
			if (filter.getStartDate() == null || filter.getStartDate() == "")
				throw new ServerException(Error.START_DATE.toString(), Integer.toString(Error.START_DATE.getCode()));

			if (filter.getEndDate() == null || filter.getEndDate() == "")
				throw new ServerException(Error.END_DATE.toString(), Integer.toString(Error.END_DATE.getCode()));

			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

			// Get the two dates to be compared
			Date startDate = outputFormat.parse(filter.getStartDate());
			Date endDate = outputFormat.parse(filter.getEndDate());

//			if (!(startDate.compareTo(endDate) < 0)) {
//				throw new ServerException(Error.START_END_DATECOMPARE.toString(),
//						Integer.toString(Error.START_END_DATECOMPARE.getCode()));
//			}

		}

	}

	@Override
	public void setAutoReloadToUser(String useruid)
			throws ServerException, UserNotFoundException, IOException, ParseException {
		User u = getUserByUID(useruid);
		String updateQuery = "UPDATE accounts SET autoReload=0 WHERE  user_id=" + u.getId();
		generalDao.updateSqlQuery(updateQuery);
		generalDao.deleteSqlQuiries("DELETE FROM autoReload WHERE userId='" + u.getId() + "'");
	}

//	@Override
//	public PagedResult<Map<String, String>> getVehicles(long id, int page, int size, String search)
//			throws ServerException, UserNotFoundException, IOException, ParseException {
//		String query = "SELECT v.id,v.vehicleType,v.connectorType,vm.model_name AS model,vr.brand_name AS make ,vr.id AS make_id ,vm.id AS model_id ,v.uid  FROM vehicles v "
//				+ "INNER JOIN vehicle_models vm ON v.model=vm.id "
//				+ "INNER JOIN vehicle_brands vr ON vr.id=v.make  WHERE   v.account_id=" + id;
//		if (search != null && !search.trim().isEmpty())
//			query = query + " AND ( v.make='" + search + "' OR v.year='" + search + "' OR v.connectorType='" + search
//					+ "' OR v.vehicleType='" + search + "' OR model='" + search + "' ) ";
//		Sort sort = Sort.by(Sort.Direction.DESC, "id");
//		Pageable pageable = PageRequest.of(page, size, sort);
//		return dataTableDao.getDataAlias(pageable, query);
//	}

	@Override
	public PagedResult<Map<String, String>> getVehicles(long id, int page, int size, String search)
			throws ServerException, UserNotFoundException, IOException, ParseException {
		String query = "Select vb.brand_name , vm.model_name, vm.vehicle_type, v.year, v.uid,"
				+ "v.capacitykWh as capacitykWh, "
				+ "(SELECT  STRING_AGG(connector_type, ',') from vehicle_yearmodels where vehicle_model_id =vm.id and year=v.year) AS connector_type  "
				+ "from vehicles v  " + "inner join vehicle_brands vb on vb.brand_name=v.make  "
				+ "inner join vehicle_models vm on vm.model_name=v.model  " + "where account_id =  " + id;

		System.out.println("search" + search);

		if (search != null && !search.equals("undefined") && !search.trim().isEmpty())
			query = query + " AND ( vb.brand_name like'%" + search + "%' OR v.year like '%" + search + "%' Or "
					+ " vm.vehicle_type like '%" + search + "%' OR vm.model_name like '%" + search + "%' ) ";
		Sort sort = Sort.by(Sort.Direction.DESC, "v.id");
		Pageable pageable = PageRequest.of(page, size, sort);
		return dataTableDao.getDataAlias(pageable, query);
	}

	public Vehicles pathVehileValues(VehicleForm vehicleForm, Vehicles vechile) {

		vechile.setCapacitykWh(vehicleForm.getCapacitykWh());

		vechile.setMake(vehicleForm.getMake());
		vechile.setModel(vehicleForm.getModel());
		vechile.setVehicleType(vehicleForm.getVehicle_type());
		vechile.setYear(vehicleForm.getYear());
		return vechile;
	}

	@Override
	public void addVechile(VehicleForm vehicleForm)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		System.out.println("vehicleForm:" + vehicleForm);
		addVehicleValidations(vehicleForm);

		Accounts act = generalDao.findOneSQLQuery(new Accounts(),
				"Select * from accounts where id=" + vehicleForm.getAccountId());
		Vehicles vechile = generalDao.findOneHQLQuery(new Vehicles(),
				"FROM Vehicles WHERE uid='" + vehicleForm.getUid() + "'");
		if (vechile == null) {
			vechile = new Vehicles();
			vechile.setAccount(act);
			vechile.setUid(UUID.randomUUID().toString());
			vechile = pathVehileValues(vehicleForm, vechile);
			generalDao.save(vechile);
		} else {
			vechile = pathVehileValues(vehicleForm, vechile);
			generalDao.update(vechile);
		}

	}

	private void addVehicleValidations(VehicleForm vehicleForm) throws ServerException {

		// Validate the make field
		if (vehicleForm.getMake() == null) {
			throw new ServerException(Error.Make_Null.toString(), Integer.toString(Error.Make_Null.getCode()));
		}
		if (vehicleForm.getMake().trim().isEmpty()) {
			throw new ServerException(Error.Make_Empty.toString(), Integer.toString(Error.Make_Empty.getCode()));
		}

		// Validate the model field
		if (vehicleForm.getModel() == null) {
			throw new ServerException(Error.Model_Null.toString(), Integer.toString(Error.Model_Null.getCode()));
		}
		if (vehicleForm.getModel().trim().isEmpty()) {
			throw new ServerException(Error.Model_Empty.toString(), Integer.toString(Error.Model_Empty.getCode()));
		}

//		// Validate the connectorType field
//		if (vehicleForm.getConnectorType() == null) {
//			throw new ServerException(Error.ConnectorType_Null.toString(),
//					Integer.toString(Error.ConnectorType_Null.getCode()));
//		}
//		if (vehicleForm.getConnectorType().trim().isEmpty()) {
//			throw new ServerException(Error.ConnectorType_Empty.toString(),
//					Integer.toString(Error.ConnectorType_Empty.getCode()));
//		}
//
//		// Validate the vehicleType field
//		if (vehicleForm.getVehicleType() == null) {
//			throw new ServerException(Error.VehicleType_Null.toString(),
//					Integer.toString(Error.VehicleType_Null.getCode()));
//		}
//		if (vehicleForm.getVehicleType().trim().isEmpty()) {
//			throw new ServerException(Error.VehicleType_Empty.toString(),
//					Integer.toString(Error.VehicleType_Empty.getCode()));
//		}

		if (vehicleForm.getYear() == null) {
			throw new ServerException(Error.Year_Null.toString(), Integer.toString(Error.Year_Null.getCode()));
		}

		if (vehicleForm.getYear().trim().isEmpty()) {
			throw new ServerException(Error.Year_Empty.toString(), Integer.toString(Error.Year_Empty.getCode()));
		}

//		// Validate the description field
//		if (vehicleForm.getDescription() == null) {
//			throw new ServerException(Error.Description_Null.toString(),
//					Integer.toString(Error.Description_Null.getCode()));
//		}
//		if (vehicleForm.getDescription().trim().isBlank()) {
//			throw new ServerException(Error.Description_Empty.toString(),
//					Integer.toString(Error.Description_Empty.getCode()));
//		}

//		// Validate the vin field
//		if (vehicleForm.getVin() == null) {
//			throw new ServerException(Error.VIN_Null.toString(), Integer.toString(Error.VIN_Null.getCode()));
//		}
//		if (vehicleForm.getVin().trim().isEmpty()) {
//			throw new ServerException(Error.VIN_Empty.toString(), Integer.toString(Error.VIN_Null.getCode()));
//		}

		if (vehicleForm.getCapacitykWh() == 0) {
			throw new ServerException(Error.STATION_CAPACITY_NULL.toString(),
					Integer.toString(Error.STATION_CAPACITY_NULL.getCode()));
		}

	}

	@Override
	public List<Map<String, String>> getVehicleBrands()
			throws ServerException, UserNotFoundException, IOException, ParseException {

		return generalDao.findAliasDataforac("SELECT id,brand_name FROM vehicle_brands order by brand_name  ");

	}

	@Override
	public List<Map<String, String>> getVehicleMake(String id)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		return generalDao.findAliasDataforac(
				"SELECT *  FROM vehicle_models WHERE vehicle_brand_id =(SELECT id FROM vehicle_brands WHERE brand_name='"
						+ id + "') order by model_name ");
	}

	@Override
	public List<Map<String, Object>> getVehicleYearBasedOnMakeId(String makeID)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		return generalDao.findAliasData(
				"SELECT distinct year  FROM vehicle_yearmodels WHERE vehicle_model_id=(SELECT id FROM vehicle_models WHERE model_name='"
						+ makeID + "') order by year ");
	}

	@Override
	public List<Map<String, Object>> getVehicleCapacityBasedOnyear(String makeID, String year, String useruid)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		return generalDao.findAliasData("SELECT distinct battery_capacity  " + "FROM vehicle_yearmodels WHERE year='"
				+ year + "' and vehicle_model_id=(SELECT id FROM vehicle_models WHERE model_name='" + makeID
				+ "') order by battery_capacity ");
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Map<String, Object>> getUserCards(String userUID)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = mobileAPIsURL + "/api/v3/payment/paymentIntent/" + "cards?uuid=" + userUID;
			ResponseEntity<Response> response = restTemplate.getForEntity(url, Response.class);
			if (response.getStatusCode().equals(HttpStatus.OK))
				map = (List<Map<String, Object>>) response.getBody().getData();

		} catch (Exception e) {
			LOGGER.info("DriverserviceImpl.getUserCards : {}", e.getMessage());
		}
		return map;
	}

	@Override
	public List<Map<String, Object>> getCardDetails(String userUID) throws UserNotFoundException {
		// TODO Auto-generated method stub

		User user = getUserByUID(userUID);

		LOGGER.debug("PaymentServiceImpl.getCardDetails() - by  user [" + user.getId() + "]");

		return generalDao.findAliasData(
				"Select id, accountId, LOWER(cardtype) as cardType,cardId, cardNo,expiryMonth,expiryYear,defaultCard, createdDate ,customerId  from worldPay_creditCard Where flag=1 and accountId="
						+ user.getAccount().iterator().next().getId());
	}

	@Override
	public Vehicles getVehicleBasedOnUId(String UID)
			throws ServerException, UserNotFoundException, IOException, ParseException {

		return generalDao.findOneHQLQuery(new Vehicles(), "FROM Vehicles WHERE uid='" + UID + "'");
	}

	@Override
	public void deleteVehicleBasedOnUId(String UID) {

		generalDao.deleteSqlQuiries("DELETE FROM vehicles where uid='" + UID + "'");
	}

	@Override
	public AutoReload updateAutoReload(AutoReload form) throws UserNotFoundException {

		form.setModifiedDate(new Date());
		return generalDao.update(form);
	}

	@Override
	public AutoReload getAutoReload(String userUID) throws UserNotFoundException {
		User user = getUserByUID(userUID);
		return generalDao.findOneHQLQuery(new AutoReload(), "FROM AutoReload WHERE userId='" + user.getId() + "'");
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
	public List<Map<String, Object>> getChargingActivityInfoBySessionUId(String sessionId, String timeZone) {
		LOGGER.info("ChargingActivityServiceImpl.getInfoBySessionId() -  [" + sessionId + "] TimeZone" + timeZone);
		Map<String, Object> userTimeZoneMap = timeZoneConvertion.getUserCoordinates(timeZone);
		String hours = userTimeZoneMap.get("Hours").toString();
		String minutes = userTimeZoneMap.get("Minutes").toString();

		String timeZoneFormate = userTimeZoneMap.get("timeZoneFormat").toString();

		String query = "Select st.referNo,s.txnType,p.amperage ,p.capacity as chargerCapacity, st.stationName, s.stationMode, st.stationType, s.chargerType, s.id, s.sessionId,"
				+ " (Convert(VARCHAR(20),DATEADD(HOUR," + hours + ",DATEADD(MINUTE," + minutes
				+ ",convert(VARCHAR(10),s.startTimeStamp, 10) + ' ' + CONVERT(VARCHAR(8), s.startTimeStamp, 108))),20)+' '+'"
				+ timeZoneFormate + "') as statTimestamp,(Convert(VARCHAR(20),DATEADD(HOUR," + hours
				+ ",DATEADD(MINUTE," + minutes
				+ ",convert(VARCHAR(10),Isnull(s.endTimeStamp,GETUTCDATE()), 10) + ' ' + CONVERT(VARCHAR(8), Isnull(s.endTimeStamp,GETUTCDATE()), 108))),20)+' '+'"
				+ timeZoneFormate + "') as endTimeStamp, s.startTimeStamp as sDate, s.endTimeStamp as eDate, "
				+ "s.kilowattHoursUsed as totalKilowats,isNULL(s.emailId, '-') as customerName, "
				+ "DATENAME(weekday,s.startTimestamp)as weekDay,s.paymentMode,s.sessionElapsedInMin as duration, "
				+ "s.finalCostInSlcCurrency  as spent,s.port_id as portId,"
				+ "ROUND(CAST(s.processingFee AS numeric(10,3)),2,1) as processingFee,ROUND(CAST(s.userProcessingFee AS numeric(10,3)),2,1) as userProcessingFee, "
				+ "ISNull(ROUND(Cast(s.transactionFee  as numeric(10,3)),2,1),0) as transactionsFee, ISNULL(ROUND(cast(s.cost  as numeric(10,3)),2,1),0)as cost,"
				+ "s.masterList as masterList," + "isnull(s.EmailID,'') as emailId, si.siteName,p.displayName,"
				+ "IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socStartVal),  'NA') as socStartVal, IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socEndVal),  'NA') as socEndVal,"
				+ " s.saleTaxPerc as saleTaxPerc ,s.saleTaxVal as saleTaxVal,s.plunInTime,s.plugInTimeCost,s.usedGraceTime,"
				+ "s.idTagProfileName , s.driverGroupName ,s.postPrice,s.postPriceLimit,s.postPortPrice,s.chargingDuration, "
				+ "isNull(s.portPrice,0) as portPrice,  s.vendingPricePerUnit2,s.vendingPriceUnit2,s.combinationBilling,ROUND(cast(s.combinationCost  as numeric(10,3)) , 2, 1) as combinationCost, "
				+ "s.costOfSmeEnergy as totalInActCost,s.durationMinsofSmeEnergy as totalInActTimeMinutes,  s.portPriceUnit as vendingPriceUnit,"
				+ "ISNULL((select c.phone from creadential c where c.phone=s.customerId union select c.rfidHex "
				+ " from creadential c where c.rfidHex=s.customerId union select c.rfidHex from creadential c where "
				+ " c.rfId=s.customerId ), ISNULL(s.customerId,'0')) as customerId,s.dynamicPricing, s.currencyType , ISNULL(s.rewardType,'') as rewardType ,s.rewardValue,"
				+ " s.plugInDuration,s.plugInStartTime,s.plugInEndTime,s.IdleStartTime,s.IdleEndTime, s.connectedTimePrice,"
				+ " isNull(case when s.connectedTimeUnits='otf' then 'One Time Fee' else s.connectedTimeUnits end ,'min') as connectedTimeUnits  "
				+ " from session s inner join port p on p.id=s.port_id inner join station st on p.station_id = st.id left join site si on st.siteId = si.siteId "
				+ " where s.sessionId='" + sessionId + "'";

		return generalDao.findAliasData(query);
	}

	@Override
	public List<Map<String, Object>> addBalanceToUser(String cardId,String customerId, double amount, String useruid,String token) throws Exception {
		Response<?> response = null;
		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapsecond = new HashMap<String, Object>();
		try {
			User user = getUserByUID(useruid);
			List<Map<String, Object>> data = generalDao
					.findAliasData("SELECT TOP 1 * FROM worldPay_creditCard WHERE customerId='"+customerId+"'");
			data.get(0).put("amount", amount);
			data.get(0).put("orgId", user.getOrgId());
			data.get(0).put("uuid", user.getUid());
			data.get(0).put("cardId", cardId);
			data.get(0).put("customerId", customerId);
			data.get(0).put("currencyCode", data.get(0).get("currencyType"));
			response = sendDataViaPOST(data.get(0),token);
			LOGGER.info("DriverserviceImpl.addBalanceToUser->response : {}", response);
			if (response.getStatus_code() == 401) {
				mapsecond.put("message", response.getStatus_message() == null ? "Payment intent create failed"
						: response.getStatus_message());
				map.add(mapsecond);
				return map;
			}
			if (response.getStatus_code() != 200)
				throw new ServerException(Error.ADD_Balance_Failed.toString(),
						Integer.toString(Error.ADD_Balance_Failed.getCode()));
		} catch (Exception e) {
			LOGGER.info("DriverserviceImpl.addBalanceToUser : {}", e.getMessage());

			throw new ServerException(Error.ADD_Balance_Failed.toString(),
					Integer.toString(Error.ADD_Balance_Failed.getCode()));
		}

		mapsecond.put("message",
				response.getStatus_message() == null ? "Payment intent create failed" : response.getStatus_message());
		map.add(mapsecond);
		return map;

	}

	public <T> Response<?> sendDataViaPOST(T entity,String token) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + token);
		headers.set("EVG-Correlation-ID", EVGCorrelationID);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		return restTemplate.postForObject(mobileAPIsURL + "/api/v3/stripe/paymentIntent/rechargeWallet", requestEntity,
				Response.class);
	}

	public <T> Response<?> sendDataViaPOSTWithAuth(T entity) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(mobileAPIsURL);
		headers.set("EVG-Correlation-ID", EVGCorrelationID);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		return restTemplate.postForObject(mobileAPIsURL + "/api/v3/payment/paymentIntent/rechargeWallet", requestEntity,
				Response.class);
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
	public Boolean searchStationForPayasGo(String stationName) throws UserNotFoundException, ServerException {

		if (stationName == null || stationName.trim().equals(""))
			throw new ServerException(Error.STATION_ID_NULL.toString(),
					Integer.toString(Error.STATION_ID_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z0-9.,_#-]{3,40}", stationName))
			throw new ServerException(Error.STATION_ID_INVALID.toString(),
					Integer.toString(Error.STATION_ID_INVALID.getCode()));
		stationName = stationName.replace("'", "");

		long count = 0;
		count = generalDao.countSQL(
				"SELECT * FROM STATION WHERE referNO='" + stationName + "' OR stationName='" + stationName + "'");
		if (count > 0)
			return true;

		return false;
	}

	@Override
	public String setDefulatCard(String cardId, String customerId, boolean defaultCard, long id, String header,
			String useruid) throws Exception {

		String msg = "Default Card Updated SuccessFully";
		try {
			User user = getUserByUID(useruid);
			Map<String, Object> form = new HashMap<String, Object>();
			form.put("cardId", cardId);
			form.put("customerId", customerId);
			form.put("defaultCard", defaultCard);
			form.put("uuid", user.getUid());
			form.put("id", id);
			sendDataViaPUTWithAuth(form, header);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info("default card exception details : {}", e.getMessage());
			msg = "Default Card Updation UnsuccessFully";
		}
		return msg;
	}

	public <T> void sendDataViaPUT(T entity) throws Exception {
		System.out.println("defaultCard");

		String serverUrl1 = mobileAPIsURL + "/api/v3/stripe/defaultCard";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		System.out.println("serverUrl1" + serverUrl1);
		restTemplate.put(serverUrl1, requestEntity, Response.class);
		// restTemplate.put(mobileAPIsURL + "/api/v3/stripe/defaultCard", requestEntity,
		// Response.class);
	}

	public <T> void sendDataViaPUTWithAuth(T entity, String token) throws Exception {
		System.out.println("defaultCard");

		String serverUrl1 = mobileAPIsURL + "/api/v3/stripe/defaultCard";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);
		headers.set("EVG-Correlation-ID", EVGCorrelationID);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		System.out.println("serverUrl1" + serverUrl1);
		restTemplate.put(serverUrl1, requestEntity, Response.class);
		// restTemplate.put(mobileAPIsURL + "/api/v3/stripe/defaultCard", requestEntity,
		// Response.class);
	}

	private void getChargingSessionDetailsValidation(ReportsAndStatsForm filter)
			throws ServerException, ParseException {
		if (filter.getRange().equals("custom")) {
			if (filter.getStartDate() == null || filter.getStartDate() == "")
				throw new ServerException(Error.START_DATE.toString(), Integer.toString(Error.START_DATE.getCode()));

			if (filter.getEndDate() == null || filter.getEndDate() == "")
				throw new ServerException(Error.END_DATE.toString(), Integer.toString(Error.END_DATE.getCode()));

			SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

			// Get the two dates to be compared
			Date startDate = outputFormat.parse(filter.getStartDate());
			Date endDate = outputFormat.parse(filter.getEndDate());
//
//			if (!(startDate.compareTo(endDate) < 0)) {
//				throw new ServerException(Error.START_END_DATECOMPARE.toString(),
//						Integer.toString(Error.START_END_DATECOMPARE.getCode()));
//			}

		}

	}

	@Override
	public PagedResult<List<Map<String, Object>>> getChagringSessionDetails(ReportsAndStatsForm filter, int page,
			int size, String useruid) throws ParseException, UserNotFoundException, ServerException {

		LOGGER.info("DriverServiceImpl.getTotalDataForReportAnalytics()- with []");

		getChargingSessionDetailsValidation(filter);

		Sort sort = Sort.by(Sort.Direction.DESC, " s.startTimeStamp");

		Pageable pageable = PageRequest.of(page, size, sort);

		String sql = getChagringSessionDetailsQuery(filter, useruid);

		return dataTableDao.getDataAlias(pageable, sql);
	}

	public ReportsAndStatsForm getRange(ReportsAndStatsForm reportsAndStatsForm) {

		String startDate = null;
		String endDate = null;

		if (reportsAndStatsForm.getRange().equals(Range.yesterday.toString())) {

			String sDate = "SELECT CAST(DATEADD(day, -1,GETUTCDATE()) as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(GETUTCDATE() as DATE)";
			endDate = generalDao.getSingleString(eDate);
			reportsAndStatsForm.setLastDate(startDate);

		} else if (reportsAndStatsForm.getRange().equals(Range.today.toString())) {

			String sDate = "SELECT CAST(GETUTCDATE() as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(DATEADD(day, 1,GETUTCDATE())as DATE)";
			endDate = generalDao.getSingleString(eDate);
			reportsAndStatsForm.setLastDate(startDate);
		} else if (reportsAndStatsForm.getRange().equals(Range.this_week.toString())) {

			String sDate = "SELECT  DATEADD(DAY, 1 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(GETUTCDATE() AS DATE))";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(DATEADD(day, 1,GETUTCDATE()) as DATE)";
			endDate = generalDao.getSingleString(eDate);
			String lDate = "select DATEADD(DAY, 7 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(DATEADD(WEEK,0,GETUTCDATE())AS DATE))";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.last_week.toString())) {

			String sDate = "SELECT DATEADD(DAY, 1 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(DATEADD(WEEK,-1,GETUTCDATE()) AS DATE))";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT DATEADD(DAY, 8 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(DATEADD(WEEK,-1,GETUTCDATE())AS DATE))";
			endDate = generalDao.getSingleString(eDate);
			String lDate = "select DATEADD(DAY, 7 - DATEPART(WEEKDAY, GETUTCDATE()), CAST(DATEADD(WEEK,-1,GETUTCDATE())AS DATE))";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.this_month.toString())) {

			String sDate = "SELECT  CAST(DateAdd(day,-day(GETUTCDATE()-1),GETUTCDATE()) as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(DateAdd(day,1,GETUTCDATE()) as DATE)";
			endDate = generalDao.getSingleString(eDate);
			String lDate = "SELECT CAST(DATEADD(day, 0,EOMONTH(DATEADD(MONTH,0,GETDATE()))) as DATE)";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.last_month.toString())) {

			String sDate = "SELECT CAST(DATEADD(month, DATEDIFF(month, -1, getdate()) - 2, 0) as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(DATEADD(day, 1,EOMONTH(DATEADD(MONTH,-1,GETDATE()))) as DATE)";
			endDate = generalDao.getSingleString(eDate);
			String lDate = "SELECT CAST(DATEADD(day, 0,EOMONTH(DATEADD(MONTH,-1,GETDATE()))) as DATE)";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.current_year.toString())) {

			String sDate = "SELECT CAST(DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0) as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = null;
			if (reportsAndStatsForm.getReportType().equals(Reports.COLORADO_Report.toString())) {
				eDate = "SELECT cast(DATEADD(DAY,-DAY(GETDATE()-1),GETDATE()) AS date)";
			} else {
				eDate = "SELECT CAST(DateAdd(day,1,GETUTCDATE()) as DATE)";
			}
			endDate = generalDao.getSingleString(eDate);
			String lDate = "select CAST(DATEADD(yy, DATEDIFF(yy, 0, GETDATE()) + 1, -1) as DATE)";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.last_year.toString())) {

			String sDate = "SELECT  CAST(DATEADD(yy,-1,DATEADD(yy,DATEDIFF(yy,0,GETDATE()),0)) as DATE)";
			startDate = generalDao.getSingleString(sDate);
			String eDate = "SELECT CAST(DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), 0) as DATE)";
			endDate = generalDao.getSingleString(eDate);
			String lDate = "select Cast(DATEADD(yy, DATEDIFF(yy, 0, GETDATE()), -1) as DATE)";
			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else if (reportsAndStatsForm.getRange().equals(Range.yearToDate.toString())) {

			String sDate = "SELECT cast(DateAdd(day,0,'" + reportsAndStatsForm.getStartDate() + "') as Date)";
			startDate = generalDao.getSingleString(sDate);

			String eDate = "SELECT cast(DateAdd(day,1,'" + reportsAndStatsForm.getEndDate() + "') as Date)";
			endDate = generalDao.getSingleString(eDate);

			String lDate = "SELECT cast(DateAdd(day,0,'" + reportsAndStatsForm.getEndDate() + "') as Date)";

			reportsAndStatsForm.setLastDate(generalDao.getSingleString(lDate));

		} else {

			String sDate = "SELECT cast(DateAdd(day,0,'" + reportsAndStatsForm.getStartDate() + "') as Date)";
			startDate = generalDao.getSingleString(sDate);

			String eDate = "SELECT cast(DateAdd(day,1,'" + reportsAndStatsForm.getEndDate() + "') as Date)";
			endDate = generalDao.getSingleString(eDate);

			String lDate = "SELECT cast(DateAdd(day,0,'" + reportsAndStatsForm.getEndDate() + "') as Date)";
			String lastDate = generalDao.getSingleString(lDate);

			reportsAndStatsForm.setLastDate(lastDate);

		}
		reportsAndStatsForm.setStartDate(startDate);
		reportsAndStatsForm.setEndDate(endDate);

		return reportsAndStatsForm;
	}

	private String getChagringSessionDetailsQuery(ReportsAndStatsForm filter, String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		User user = getUserByUID(useruid);

		Map<String, Object> userTimeMap = timeZoneConvertion.getUserCoordinates(filter.getUserTimeZone());
		String hours = userTimeMap.get("Hours").toString();
		String minutes = userTimeMap.get("Minutes").toString();
		String timeZone = userTimeMap.get("timeZoneFormat").toString();

		String startFromDate = "";

		if (!filter.getRange().equalsIgnoreCase("All")) {
			filter = getRange(filter);
			startFromDate = "  AND  dateadd(HOUR," + hours + ", DATEADD(MINUTE," + minutes
					+ ", s.startTimeStamp))  BETWEEN '" + filter.getStartDate() + "' AND '" + filter.getEndDate()
					+ "' ";
		}

		String query = "SELECT s.id AS sessionId ,s.sessionId as uuid," + " (Convert(VARCHAR(20),DATEADD(HOUR," + hours
				+ ",DATEADD(MINUTE," + minutes + ",convert(VARCHAR(10),s.startTimeStamp, 10) + ' ' +  "
				+ " CONVERT(VARCHAR(8), s.startTimeStamp, 108))),20)+' '+'" + timeZone
				+ "') AS startTime, s.sessionElapsedInMin AS chargeTime, s.startTimeStamp as sDate, "
				+ "  s.kilowattHoursUsed AS energyUsed,s.inaccurateTxn, s.finalCostInSlcCurrency AS totalCost, s.reasonForTer as endedBy , p.displayName AS  "
				+ "  conductorName, st.referNo AS stationId, sit.siteName,IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socStartVal),  'NA') as starting,  "
				+ "IIF( s.chargerType!= 'AC' , CONVERT (VARCHAR(8), s.socEndVal),  'NA') as final, IIF(ISNULL(s.emailId,'Guest User')='','Guest User',s.emailId) "
				+ "as users,ISNULL(s.currencyType,'USD') as currencySym FROM station st INNER JOIN port p ON p.station_id = st.id  "
				+ "INNER JOIN session s ON s.port_id = p.id INNER JOIN site sit ON sit.siteId = st.siteId  AND st.preProduction = 0  "
				+ "WHERE s.userId=" + user.getId() + startFromDate;

		return query;

	}

	@Override
	public ByteArrayInputStream getFile(ReportsAndStatsForm filterform, String useruid, String userstandardTimezone)
			throws IOException, UserNotFoundException, ServerException {
		LOGGER.info("DriverServiceImpl---------- getFile()-- filterform [  " + filterform.getData() + "]-- range [  "
				+ filterform + "]");
		User user = getUserByUID(useruid);

		filterform.setRequestType("Portal");
		filterform.setUser(user);
		filterform.setTimeZone("");
		filterform.setRole("Driver");
		filterform.setUserStandardTimeZone(userstandardTimezone);
		filterform.setReportType("Driver_Report");
		String serverUrl1 = reportserviceurl + "/services/analytics/driverreport";
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<ReportsAndStatsForm> requestEntity3 = new HttpEntity<>(filterform, header);
		ResponseEntity<byte[]> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, byte[].class);
		ByteArrayInputStream targetStream = new ByteArrayInputStream(postForEntity.getBody());
		return targetStream;
	}

	@Override
	public ByteArrayInputStream getSessionFile(String sessionId, String timeZone, String timeZonestatndard)
			throws IOException, UserNotFoundException {
		String serverUrl1 = reportserviceurl + "/services/analytics/chargingactivityreport/" + sessionId + "/"
				+ timeZone + "/" + timeZonestatndard;

		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<?> requestEntity3 = new HttpEntity<>(header);

		ResponseEntity<byte[]> postForEntity = restTemplate.postForEntity(serverUrl1, requestEntity3, byte[].class);

		ByteArrayInputStream targetStream = new ByteArrayInputStream(postForEntity.getBody());

		return targetStream;
	}

	public boolean isEmailExist(String userEmail) throws UserNotFoundException {
		String queryForUserEmailCount = "Select email from Users u where email='" + userEmail + "' OR username='"
				+ userEmail + "'";

		System.out.println(generalDao.countSQL(queryForUserEmailCount));

		return generalDao.countSQL(queryForUserEmailCount) == 0 ? false : true;
	}

	@Override
	public void updateUser(String id, Map<String, Object> user) throws UserNotFoundException, ServerException {

		User currentUser = getUserByUID(id);
		if (user.get("firstName") == null || user.get("firstName").toString().equals(""))
			throw new ServerException(Error.REG_FIRSTNAME_NULL.toString(),
					Integer.toString(Error.REG_FIRSTNAME_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z -]{3,20}", user.get("firstName").toString()))
			throw new ServerException(Error.REG_FIRSTNAME_INVALID.toString(),
					Integer.toString(Error.REG_FIRSTNAME_INVALID.getCode()));

		if (user.get("lastName") != null && !user.get("lastName").toString().equals("")) {

			if (!Pattern.matches("[a-z A-Z -]{3,20}", user.get("lastName").toString()))
				throw new ServerException(Error.REG_LASTNAME_INVALID.toString(),
						Integer.toString(Error.REG_LASTNAME_INVALID.getCode()));
		}
		if (user.get("email") == null || user.get("email").toString().equals(""))
			throw new ServerException(Error.REG_MAIL_NULL.toString(), Integer.toString(Error.REG_MAIL_NULL.getCode()));
		else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[A-Za-z]{2,}$", user.get("email").toString())
				|| user.get("email").toString().length() > 80)
			throw new ServerException(Error.REG_MAIL_INVALID.toString(),
					Integer.toString(Error.REG_MAIL_INVALID.getCode()));

		if (isEmailExist(user.get("email").toString()) && !user.get("email").toString().equals(currentUser.getEmail()))
			throw new ServerException(Error.DUPPLICATE_EMAILID.toString(),
					Integer.toString(Error.DUPPLICATE_EMAILID.getCode()));

		currentUser.setFirstName((String) user.get("firstName"));
		currentUser.setLastName((String) user.get("lastName"));
		currentUser.setEmail((String) user.get("email"));
		currentUser.setModifiedDate(new Date());
		currentUser.setLastModifiedBy((String) user.get("username"));

		generalDao.merge(currentUser);
	}

//	@Override
//	public List<Map<String, Object>> getTOUDataBasedOnStationUID(String stationUID)
//			throws NumberFormatException, UserNotFoundException {
//		
//
//		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
//
//		String hql = "SELECT  " + "(CASE " + "    WHEN st.flat!=0 THEN convert(VARCHAR(10),st.flat, 10) "
//				+ "    WHEN st.time!=0 THEN  " + "     CASE "
//				+ "        WHEN st.timeStepSize=3600 THEN convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Hours',10) "
//				+ "        WHEN st.timeStepSize=600 THEN  convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Minutes',10) "
//				+ "	     ELSE convert(VARCHAR(10),st.time, 10)+ '/ '+ convert(VARCHAR(10),'Seconds',10) END "
//				+ "	 WHEN st.energy!=0 THEN convert(VARCHAR(10),st.energy, 10)+'/kwh' " + "    ELSE 'NA' "
//				+ "END) AS price , " + "(CASE  "
//				+ "    WHEN st.tax1_percentage!=0 THEN convert(VARCHAR(10),st.tax1, 10)+ ' : '+ convert(VARCHAR(10),st.tax1_percentage,10)+'%' "
//				+ "    ELSE 'NA' " + " END ) AS tax1 , " + " (CASE  "
//				+ "    WHEN st.tax2_percentage!=0 THEN convert(VARCHAR(10),st.tax2, 10)+ ' : '+ convert(VARCHAR(10),st.tax2_percentage,10)+'%' "
//				+ "    ELSE 'NA' " + " END ) AS tax2, " + "st.rateRider , " + "st.type   ,st.tariff_id"
//				+ " FROM touprofile_for_mobile st   "
//				+ " INNER JOIN station_in_tariff sit ON  sit.tariffId=st.tariff_id "
//				+ " INNER JOIN station stn ON stn.id=sit.stationId  WHERE  stn.uid ='" + stationUID + "'";
//
//		map = generalDao.findAliasData(hql);
//
////		map.stream().filter(map2 -> "TOU".equals(map2.get("type"))).forEach(map2 -> {
////			map2.put("tariff", getTariffbyid(Long.valueOf(map2.get("tariff_id").toString())));
////			map2.remove("tariff_id");
////		});
//
//		return map;
//	}

	@Override
	public List<Map<String, Object>> getTOUDataBasedOnStationUID(String stationUID)
			throws NumberFormatException, UserNotFoundException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();

		String hql = "SELECT  " + "(CASE " + "    WHEN st.flat!=0 THEN convert(VARCHAR(10),st.flat, 10)+' Per Session' "
				+ "    WHEN st.time!=0 THEN  " + "     CASE "
				+ "        WHEN st.timeStepSize=3600 THEN convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Hours',10) "
				+ "        WHEN st.timeStepSize=60 THEN  convert(VARCHAR(10),st.time, 10)+ '/'+ convert(VARCHAR(10),'Minutes',10) "
				+ "	     ELSE convert(VARCHAR(10),st.time, 10)+ '/ '+ convert(VARCHAR(10),'Seconds',10) END "
				+ "	 WHEN st.energy!=0 THEN convert(VARCHAR(10),st.energy, 10)+'/kwh' " + "    ELSE 'NA' "
				+ "END) AS cost ,'Rate Rider   ('+convert(VARCHAR(10),st.rateRider,10) +'%) & '+ "
				+ "convert(VARCHAR(255),(CASE  "
				+ "    WHEN st.tax1_percentage>0 THEN CONCAT(st.tax1, ' : ', st.tax1_percentage,'%')  "
				+ "    ELSE ' ' " + " END ),25) +' & '+ " + " convert(VARCHAR(255),(CASE  "
				+ "    WHEN st.tax2_percentage>0 THEN convert(VARCHAR(10),st.tax2, 10)+ ' : '+ convert(VARCHAR(10),st.tax2_percentage,10)+'%' "
				+ "    ELSE ' ' " + " END ) ,25)  AS taxprice "
				+ " FROM touprofile_for_mobile st    INNER JOIN station_in_tariff sit ON  sit.tariffId=st.tariff_id  INNER JOIN station stn ON stn.id=sit.stationId WHERE  stn.id ="
				+ stationUID + "";

		map = generalDao.findAliasData(hql);

		return map;
	}

	@Override
	public List<Map<String, Object>> getRFIDPriceDetails(String useruid) throws UserNotFoundException, ServerException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		User user = getUserByUID(useruid);
		String hql = "SELECT RFIDPrice AS rfidcost,  "

				+ " RFIDLimitPerOrder ,currencyCode ,currencySymbol,orgId  ,(Select orgName from organization where id="
				+ user.getOrgId() + ") as orgName " + " FROM app_config_setting  where  currencyCode= '"
				+ user.getAccount().iterator().next().getCurrencyType() + "' and orgId=" + user.getOrgId();

		map = generalDao.findAliasData(hql);

		return map;
	}

	@Override
	public List<Map<String, Object>> getPromoCodeReward(String useruid) throws UserNotFoundException, ServerException {

		List<Map<String, Object>> map = new ArrayList<Map<String, Object>>();
		map = generalDao.findAliasData(
				"SELECT amount ,kwh FROM promoCode_reward WHERE userId=" + getUserByUID(useruid).getId());
		if (map.size() == 0)
			map.stream().forEach(m -> {
				m.put("amount", 0);
				m.put("kwh", 0);
			});
		return map;
	}

	@Override
	public PagedResult<List<Map<String, Object>>> getPromoCodeRewardHistory(int page, int size, String useruid)
			throws ParseException, UserNotFoundException, ServerException {

		LOGGER.info("getPromoCodeRewardHistory   -  []");
		long userId = getUserByUID(useruid).getId();
		Sort sort = Sort.by(Sort.Direction.DESC, "id");
		Pageable pageable = PageRequest.of(page, size, sort);

		return dataTableDao.getDataAlias(pageable, "SELECT * FROM promoCodeHistory WHERE userId=" + userId);
	}

	@Override
	public List<Language> getLanguages() {

		return generalDao.findAll(new Language());

	}

	@Override
	public Profile getByUserId(String uid) throws UserNotFoundException {

		User user = generalDao.findOneHQLQuery(new User(), "From User where uid='" + uid + "'");

		return generalDao.findOneHQLQuery(new Profile(), "From Profile Where user.id=" + user.getId());
	}

	@Override
	public void updateUserAddress(String id, Address address) throws UserNotFoundException, ServerException {

		LOGGER.info("AddressServiceImpl.update() - [" + address + "]");

		User user = userService.getUserByUID(id);

		String role = user.getRoles().iterator().next().getRolename();

		updateAdressValidations(address);

		if (!user.getAddress().iterator().next().getPhone().equals(address.getPhone())) {
			if (userService.getUserByPhone(address.getPhone()).size() > 0 && role.equals(Roles.Driver.toString())) {
				throw new ServerException(Error.DUPLICATE_PHONE_NUMBER.toString(),
						Integer.toString(Error.DUPLICATE_PHONE_NUMBER.getCode()));
			}
		}
		address.setUser(user);

		generalDao.merge(address);

	}

	private void updateAdressValidations(Address address) throws ServerException {
		if (address.getAddressLine1() == null || address.getAddressLine1().equals(""))
			throw new ServerException(Error.REG_ADD1_NULL.toString(), Integer.toString(Error.REG_ADD1_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z0-9 .,-]{3,40}", address.getAddressLine1()))
			throw new ServerException(Error.REG_ADD1_LENGTH.toString(),
					Integer.toString(Error.REG_ADD1_LENGTH.getCode()));

		if (address.getAddressLine2() != null && !address.getAddressLine2().isEmpty()) {
			if (!Pattern.matches("[a-zA-Z0-9 .,-]{3,40}", address.getAddressLine2() + ""))
				throw new ServerException(Error.REG_ADD2_LENGTH.toString(),
						Integer.toString(Error.REG_ADD2_LENGTH.getCode()));
		}

		if (address.getCity() == null || address.getCity().equals(""))
			throw new ServerException(Error.REG_CITY_NULL.toString(), Integer.toString(Error.REG_CITY_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{3,20}", address.getCity()))
			throw new ServerException(Error.REG_CITY_INVALID.toString(),
					Integer.toString(Error.REG_CITY_INVALID.getCode()));

		if (address.getState() == null || address.getState().equals(""))
			throw new ServerException(Error.REG_STATE_NULL.toString(),
					Integer.toString(Error.REG_STATE_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{2,20}", address.getState()))
			throw new ServerException(Error.REG_STATE_INVALID.toString(),
					Integer.toString(Error.REG_STATE_INVALID.getCode()));
		int count = StringUtils.countMatches(address.getZipCode(), "-");
		if (address.getZipCode() == null || address.getZipCode().equals(""))
			throw new ServerException(Error.REG_ZIPCODE_NULL.toString(),
					Integer.toString(Error.REG_ZIPCODE_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z0-9 -]{4,12}", address.getZipCode()) || count > 2)
			throw new ServerException(Error.REG_ZIPCODE_INVALID.toString(),
					Integer.toString(Error.REG_ZIPCODE_INVALID.getCode()));

		if (address.getCountry() == null || address.getCountry().equals(""))
			throw new ServerException(Error.REG_COUNTRY_NULL.toString(),
					Integer.toString(Error.REG_COUNTRY_NULL.getCode()));
		else if (!Pattern.matches("[a-zA-Z ]{2,20}", address.getCountry()))
			throw new ServerException(Error.REG_COUNTRY_INVALID.toString(),
					Integer.toString(Error.REG_COUNTRY_INVALID.getCode()));

		if (address.getCountryCode() == null || address.getCountryCode().equals(""))
			throw new ServerException(Error.REG_COUNTRYCODE_NULL.toString(),
					Integer.toString(Error.REG_COUNTRYCODE_NULL.getCode()));
		else if (!Pattern.matches("[0-9 + - ]{1,4}", address.getCountryCode()))
			throw new ServerException(Error.REG_COUNTRYCODE_INVALID.toString(),
					Integer.toString(Error.REG_COUNTRYCODE_INVALID.getCode()));
		else if (address.getPhone() == null || address.getPhone().trim().equals(""))
			throw new ServerException(Error.REG_PHONE_NULL.toString(),
					Integer.toString(Error.REG_PHONE_NULL.getCode()));
		else if (address.getCountryCode().equals("+506") && !Pattern.matches("[0-9]{8}", address.getPhone()))
			throw new ServerException(Error.REG_PHONE_INVALID_CRC.toString(),
					Integer.toString(Error.REG_PHONE_INVALID_CRC.getCode()));
		else if ((address.getCountryCode().equals("+1") || address.getCountryCode().equals("+91")
				|| address.getCountryCode().equals("+972") || address.getCountryCode().equals("+49"))
				&& !Pattern.matches("[0-9]{8,12}", address.getPhone()))
			throw new ServerException(Error.REG_PHONE_INVALID.toString(),
					Integer.toString(Error.REG_PHONE_INVALID.getCode()));

		if (address.getSecondaryPhone() != null && !address.getSecondaryPhone().trim().equals("")) {

			if (!Pattern.matches("[0-9]{10,10}", address.getSecondaryPhone()))
				throw new ServerException(Error.REG_SECPHONE_INVALID.toString(),
						Integer.toString(Error.REG_SECPHONE_INVALID.getCode()));
		}
	}

	@Override
	public Password changeUserPassword1(User user, String password) throws ServerException, UserNotFoundException {

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

	@Override
	public void updatePassword(String id, Map<String, Object> password, boolean check)
			throws UserNotFoundException, ServerException {

		User currentUser = getUserByUID(id);

		System.out.println("password--->" + password);

		updatePasswordValidations((String) password.get("password"), (String) password.get("newPassword"),
				(String) password.get("confrimPassword"), currentUser.getPassword(), check, currentUser);

		changeUserPassword1(currentUser, (String) password.get("newPassword"));

	}

	private void updatePasswordValidations(String oldPassword, String newpassword, String confPassword, String password,
			boolean check, User currentuser) throws ServerException {

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

	@Override
	public void updateProfile(String uid, Profile profile) throws UserNotFoundException, DuplicateUserException {
		System.out.println(uid);
		// try {

		User user = generalDao.findOneHQLQuery(new User(), "From User where uid='" + uid + "'");

		Profile profileToUpdate = generalDao.findOneSQLQuery(new Profile(),
				"Select * from profile where user_id=" + user.getId());

		if (StringUtils.isNotBlank(profile.getStatus()))
			profileToUpdate.setStatus(profile.getStatus());

		profileToUpdate.setUser(user);
		profileToUpdate.setLang(profile.getLang());
		// profileToUpdate.setOtp(otp);

		generalDao.update(profileToUpdate);

//		} catch (UserNotFoundException e) {
//			logger.error("updateProfile() - [" + e + "]");
//		}

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
	public List<Notification> getNotifications(String useruid) throws UserNotFoundException, ServerException {

		User user = getUserByUID(useruid);

		Role role = user.getRoles().iterator().next();

		String hql = "";

		if (role.getRolename().equals(Roles.Admin.toString())) {
			hql = "From Notification t Where t.orgId=1 ORDER BY t.createdDate DESC";
		} else if (role.getRolename().equals(Roles.Driver.toString())) {
			hql = "From Notification t Where t.userId =" + user.getId() + "ORDER BY t.createdDate DESC";
		} else {
			hql = "From Notification t Where t.orgId=" + user.getOrgId() + " ORDER BY t.createdDate DESC";
		}

		return generalDao.findAllWithMaxRecord(new Notification(), hql, 5);

	}

//	public void addNotification(String comment, long stationId, String title, long userId)
//			throws UserNotFoundException, ServerException {
//
//		User user = userService.getCurrentUser();
//
//		Notification noti = new Notification();
//		noti.setComment(comment);
//		noti.setCreatedBy(user.getUsername());
//		noti.setCreatedDate(new Date());
//		noti.setSeen(false);
//		noti.setStationid(stationId);
//		noti.setTitle(title);
//		noti.setUserId(userId);
//		noti.setOrgId(user.getOrgId());
//
//		generalDao.save(noti);
//
//	}

	@Override
	public Accounts getAccountByUser(String uid) throws UserNotFoundException {

		LOGGER.info("AccountsServiceImpl.getAccountByUser() - id: [" + uid + "] ");
		long userId = getUserByUID(uid).getId();
		return generalDao.findOneHQLQuery(new Accounts(), "From Accounts  Where  user.id = " + userId);
	}

	@Override
	public PreferredNotification updateNotification(PreferredNotification preferredNotification, String useruid)
			throws UserNotFoundException, ServerException {

		long userId = getUserByUID(useruid).getId();
		preferredNotification.setUserId(userId);
		preferredNotification = generalDao.update(preferredNotification);

		System.out.println("prefe object 0" + preferredNotification.toString());
		return preferredNotification;
	}

	@Override
	public boolean istwofa(String uid) throws UserNotFoundException {

		User user = generalDao.findOneHQLQuery(new User(), "From User where uid='" + uid + "'");

		long count = generalDao.countSQL("Select * from profile where istwofa=1 and user_id=" + user.getId());
		if (count == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public PreferredNotification getNotification(String useruid) throws UserNotFoundException, ServerException {

		long userId = getUserByUID(useruid).getId();
		PreferredNotification preferredNotification = new PreferredNotification();
		preferredNotification = generalDao.findOneHQLQuery(new PreferredNotification(),
				"FROM PreferredNotification WHERE userId=" + userId);
		if (preferredNotification == null || preferredNotification.getId() == 0) {
			preferredNotification = new PreferredNotification();
			preferredNotification.setUserId(userId);
			preferredNotification = generalDao.save(preferredNotification);
		}
		return preferredNotification;
	}

	@Override
	public PagedResult<Map<String, String>> getGeneratedReport(int page, int size, String useruid)
			throws UserNotFoundException {

		User user = getUserByUID(useruid);

		LOGGER.info("DriverServiceImpl.getGeneratedReport() -  [" + user.getEmail() + "]");

		Sort sort = Sort.by(Sort.Direction.DESC, "creationDate");
		Pageable pageable = PageRequest.of(page, size, sort);

		String query = "SELECT ri.id as reportId, ri.creationDate AS requestedDate ,ri.lastUpdated AS generatedReportDate,ri.reportType AS reportName ,ri.exportType FROM reportrequestinfo ri WHERE ri.userId="
				+ user.getId()
				+ " AND ri.lastUpdated IS NOT NULL AND  (ri.creationDate BETWEEN  GETUTCDATE()-30 AND  GETUTCDATE()+1) and emailFlag=1 order by id desc ";

		List<Map<String, String>> sql = generalDao.findAliasDataforac(query);

		List<Map<String, String>> page1 = null;

		page1 = getPage(sql, page, size);

		PagedResult<Map<String, String>> pagedResult = new PagedResult<Map<String, String>>(
				new PageImpl<Map<String, String>>(page1, pageable, sql.size()));

		return pagedResult;
	}

	@Override
	public ByteArrayInputStream getGeneratedReportsExport(long reportId, String userUID)
			throws IOException, UserNotFoundException, ServerException {

		if (getUserByUID(userUID) == null)
			throw new UserNotFoundException("User not found");

		long count = 0;

		count = generalDao.countSQL("select * from reportrequestinfo where userId=" + getUserByUID(userUID).getId());

		if (count == 0) {
			LOGGER.info("DriverServiceImpl---------- getGeneratedReportsExport() reportId : " + reportId + " userUID : "
					+ userUID);
			throw new UserNotFoundException("User not found");

		}

		try {
			LOGGER.info("DriverServiceImpl---------- getGeneratedReportsExport()-");
			String serverUrl1 = reportserviceurl + "/services/analytics/downloadreport/" + reportId;
			HttpHeaders header = new HttpHeaders();
			header.setContentType(MediaType.APPLICATION_JSON);
//			HttpEntity<ReportsAndStatsForm> requestEntity3 = new HttpEntity<>(header);
			ResponseEntity<byte[]> postForEntity = restTemplate.getForEntity(serverUrl1, byte[].class);
			ByteArrayInputStream targetStream = new ByteArrayInputStream(postForEntity.getBody());
			return targetStream;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isAmountlowbalance(double amount, String useruid) throws UserNotFoundException, ServerException {

		User user = getUserByUID(useruid);

		AppConfigSetting configs = generalDao.findOneSQLQuery(new AppConfigSetting(),
				"select * from app_config_setting where  currencyCode='"
						+ user.getAccount().iterator().next().getCurrencyType() + "' and orgId=" + user.getOrgId());

		RecurringAmount config = generalDao.findOneSQLQuery(new RecurringAmount(),
				"select top 1 * from recurringAmount where currencyCode='"
						+ user.getAccount().iterator().next().getCurrencyType() + "'");

		if (amount > configs.getAutoReloadMaxAmount())
			return false;
		else if (amount < config.getAmount())
			return false;

		return true;
	}

	@Override
	public boolean isAmount(double amount, String useruid) throws UserNotFoundException {

		User user = getUserByUID(useruid);

		AppConfigSetting config = generalDao.findOneSQLQuery(new AppConfigSetting(),
				"select * from app_config_setting where  currencyCode='"
						+ user.getAccount().iterator().next().getCurrencyType() + "' and orgId=" + user.getOrgId());

		if (amount > config.getAddMoneyMaxAmount() || amount < config.getAddMoneyMinAmount())
			return false;

		return true;
	}

	@Override
	public boolean isAmountForautoReload(double amount, String useruid) throws UserNotFoundException, ServerException {
		User user = getUserByUID(useruid);

		AppConfigSetting config = generalDao.findOneSQLQuery(new AppConfigSetting(),
				"select * from app_config_setting where  currencyCode='"
						+ user.getAccount().iterator().next().getCurrencyType() + "' and orgId=" + user.getOrgId());

		if (amount > config.getAutoReloadMaxAmount() || amount < config.getAutoReloadMinAmount())
			return false;

		return true;
	}

	@Override
	public boolean emailPromotional(long userId) throws UserNotFoundException {

		List<Map<String, Object>> emailPromotional = generalDao
				.findAliasData("select emailPromotional from preferredNotification where userId= " + userId);

		if (emailPromotional.size() > 0)
			if (Boolean.valueOf(emailPromotional.get(0).get("emailPromotional").toString()))
				return true;

		return false;
	}

	@Override
	public List<Map<String, Object>> getDynamicProfileInfoBySessionId(String sessionId, String timeZone) {
		// TODO Auto-generated method stub
		LOGGER.info("ChargingActivityServiceImpl.getDynamicProfileInfoBySessionId() -  [" + sessionId + "] TimeZone"
				+ timeZone);
		Map<String, Object> userTimeZoneMap = timeZoneConvertion.getUserCoordinates(timeZone);
		String hours = userTimeZoneMap.get("Hours").toString();
		String minutes = userTimeZoneMap.get("Minutes").toString();
		String timeZoneFormate = userTimeZoneMap.get("timeZoneFormat").toString();

		String query = "Select "
				+ "CAST((CASE   WHEN s.dynamicPricing=1   THEN  sp.portPrice ELSE  0  END ) AS VARCHAR(255)) + '/' + CAST(ISNULL (sp.portPriceUnit,'-') AS VARCHAR(255)) AS  dynamicprice,IIF(s.dynamicPricing=1,'ENABLED','DISABLED' ) AS dynamicPricing ,(case when ISNULL(sp.dynamicProfileName,'') = '' then 'Standard' else sp.dynamicProfileName end) AS dynamicProfileName ,ISNULL (sp.cost,0) AS costofengery  ,"
				+ "(Convert(VARCHAR(20),DATEADD(HOUR," + hours + ",DATEADD(MINUTE," + minutes
				+ ",convert(VARCHAR(10),sp.startTimeStamp, 10) + ' ' + CONVERT(VARCHAR(8), sp.startTimeStamp, 108))),20)+' '+'"
				+ timeZoneFormate + "') as dynamicstartTimeStamp ," + " (Convert(VARCHAR(20),DATEADD(HOUR," + hours
				+ ",DATEADD(MINUTE," + minutes
				+ " ,convert(VARCHAR(10),sp.endTimeStamp, 10) + ' ' + CONVERT(VARCHAR(8), sp.endTimeStamp, 108))),20)+' '+' "
				+ timeZoneFormate + "') as dynamicendTimeStamp ,(sp.sessionDurationInMin) as dsessionDurationInMin ,"
				+ "( SELECT  COUNT( DISTINCT NULLIF(dynamicProfileId,0) ) FROM session_Pricing WHERE sessionUniqueId=s.id  ) AS noofdynamicprofiles,ISNULL (sp.totalEnergyCost,0) AS profilerevenue , "
				+ "  ISNULL (sp.totalKwUsed,0) AS profileKWConsumed "
				+ " from session s  FUll  JOIN   session_Pricing sp  ON   sp.sessionUniqueId=s.id "
				+ " where s.sessionId= '" + sessionId + "'" + " order by sp.startTimeStamp";

		return generalDao.findAliasData(query);
	}

	@Override
	public void updateistwofa(Long userId, boolean isTwofa) {
		int number = 0;
		if (isTwofa == true) {
			number = 1;
		}

		generalDao.excuteUpdateUserData("UPDATE Profile set istwofa=" + number + " where user_id=" + userId);
	}

	@Override
	public Map<String, Object> getStripeKeys(long orgId) {
		String query = "select stripe_CAD_PublicKey,stripe_USD_PublicKey from paymentKeys WHERE orgId=" + orgId + "";
		return generalDao.findAliasData(query).get(0);
	}

	@Override
	public void updateRfidData(long id, String name) throws UserNotFoundException {
		LOGGER.info("UserServiceImpl.updateRfidData() - with  []");

		Credentials credentials = generalDao.findOneSQLQuery(new Credentials(),
				"select * from creadential where id=" + id);

		generalDao.excuteUpdateUserData(
				"update gridkeyStatus set rfidName='" + name + "' where rfidHex='" + credentials.getRfidHex() + "'");

		 credentials.setRfidName(name);
		generalDao.update(credentials);
	}

	@Override
	public Boolean isRFIDNameExists(String rfidName) throws UserNotFoundException, ServerException {

		long count = generalDao.countSQL("select * from creadential where rfidName='" + rfidName + "'");
		count += generalDao.countSQL("select * from gridkeystatus where rfidName='" + rfidName + "'");
		if (count == 0L) {
			return false;
		}
		return true;
	}

	@Override
	public AppConfigSettingResponse getappconfig(String uid) throws UserNotFoundException {

		AppConfigSettingResponse config = new AppConfigSettingResponse();
		User u = userService.getUserByUID(uid);
		Accounts account = u.getAccount().iterator().next();

		AppConfigSetting appConfig = generalDao.findOneSQLQuery(new AppConfigSetting(),
				"select * from app_config_setting where orgId=1 and currencyCode='" + account.getCurrencyType() + "'");

		List<RecurringAmount> recurringAmountList = generalDao.findAllHQLQuery(new RecurringAmount(),
				"FROM RecurringAmount WHERE currencyCode = '" + appConfig.getCurrencyCode() + "' ORDER BY amount ASC");

		config.setOrgId(appConfig.getOrgId());
		config.setAutoReloadMinAmount(appConfig.getAutoReloadMinAmount());
		config.setAutoReloadMaxAmount(appConfig.getAutoReloadMaxAmount());
		config.setAddMoneyMinAmount(appConfig.getAddMoneyMinAmount());
		config.setAddMoneyMaxAmount(appConfig.getAddMoneyMaxAmount());
		config.setAddMoneyOption1(appConfig.getAddMoneyOption1());
		config.setAddMoneyOption2(appConfig.getAddMoneyOption2());
		config.setAddMoneyOption3(appConfig.getAddMoneyOption3());
		config.setLowBalance(appConfig.getLowBalance());
		config.setRecurringAmount(recurringAmountList.get(0).getAmount());
		config.setCurrencyCode(account.getCurrencyType());

		return config;
	}

	@Override
	public void addAutoReload(double amount, double lowBalance, String cardId, String cardNo, String cardType,
			String expiryYear, String expiryMonth, String useruid,String token,String customerId) throws Exception {

		User user = getUserByUID(useruid);
		Accounts account = user.getAccount().iterator().next();

//		List<Map<String, Object>> recurringAmount = generalDao
//				.findAliasData("select * from recurringAmount where  currencyCode='" + account.getCurrencyType() + "'");

		List<Map<String, Object>> data = generalDao
				.findAliasData("SELECT TOP 1 * FROM worldPay_creditCard WHERE accountId="
						+ user.getAccount().iterator().next().getId());
		data.get(0).put("amount", amount);
		data.get(0).put("orgId", user.getOrgId());
		data.get(0).put("uuid", user.getUid());
		data.get(0).put("cardId", cardId);
		data.get(0).put("customerId", customerId);
		data.get(0).put("recurringAmount", lowBalance);
		data.get(0).put("cardNo", cardNo);
		data.get(0).put("expiryMonth", expiryMonth);
		data.get(0).put("expiryYear", expiryYear);
		data.get(0).put("flag", true);

		Response<?> response = sendDataViaPOSTforautoReload(data.get(0),token);
		if (response.getStatus_code() != 200)
			throw new ServerException(Error.ADD_AutoReload_Failed.toString(),
					Integer.toString(Error.ADD_AutoReload_Failed.getCode()));

	}

	public <T> Response<?> sendDataViaPOSTforautoReload(T entity,String token) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer " + token);
		headers.set("EVG-Correlation-ID", EVGCorrelationID);
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<T> requestEntity = new HttpEntity<T>(entity, headers);
		LOGGER.info("DriverserviceImpl.setAutoReload : {}", requestEntity);
		return restTemplate.postForObject(mobileAPIsURL + "/api/v3/stripe/paymentIntent/autoReload", requestEntity,
				Response.class);
	}

}
