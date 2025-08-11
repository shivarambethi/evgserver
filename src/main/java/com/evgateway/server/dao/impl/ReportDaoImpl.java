package com.evgateway.server.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.ReportDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.DealerInOrg;
import com.evgateway.server.pojo.OwnerInOrg;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@SuppressWarnings("deprecation")
@Repository
public class ReportDaoImpl implements ReportDao {

	final static Logger LOGGER = LoggerFactory.getLogger(ReportDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private UserDao<?, ?> userDao;

	@Autowired
	private StationDao<?, ?> stationDao;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getDashboardReports(int id, int period, String type, String useruid)
			throws NumberFormatException, UserNotFoundException {

		LOGGER.info("ReportDaoImpl.getDashboardReports() -  [" + type + "] with " + period);

		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Set<Role> roles = u.getRoles();
		String roleName = "";
		if (roles != null)
			for (Role role : roles) {
				roleName = role.getRolename();
				break;
			}

		String hql = null;
		String sql = null;
		String hql1 = null;

		List<Map<String, Object>> finalData = new ArrayList<Map<String, Object>>();

		switch (id) {

		case 1:

			if (roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString())) {

				hql = "SELECT Count(s.id) as stations , (SELECT COUNT(siteid) FROM site ) AS sites,  "
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s WHERE s.preProduction = 0";

				if (!type.equals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = "SELECT Count(s.id) as stations , (SELECT COUNT(siteid) FROM site WHERE siteId IN (" + ids
							+ ")) AS sites, "
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
							+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
							+ " FROM station s  Where s.preProduction = 0 AND  s.siteId IN (" + ids + ")";
				}

			} else if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

				List<Long> longList = (!type.equals("All")) ? stationDao.getSiteIdBasedOnOrg(Long.parseLong(type))
						: stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = "SELECT Count(s.id) as stations , (SELECT COUNT(siteid) FROM site WHERE siteId IN (" + ids
						+ ")) AS sites, "
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s  Where s.preProduction = 0 AND  s.siteId IN (" + ids + ")";

			} else if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {

				hql = "SELECT COUNT (s.id) as stations ,COUNT(Distinct(s.siteid)) AS sites, "
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
						+ " WHERE s.preProduction = 0 AND um.userId= " + u.getId();

			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = "SELECT Count(s.id) as stations , (SELECT COUNT(siteid) FROM site WHERE siteId IN (" + ids
						+ ")) AS sites,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s  Where s.preProduction = 0 AND s.siteId IN (" + ids + ")";

				if (!type.equals("All")) {
					DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
							"select Top 1 * from dealer_in_org where orgId =" + type);
					System.out.println("dealerorg---->" + dealer);
					if (dealer != null) {
						List<Long> longLists = stationDao.getSiteIdBasedOnRole(Roles.DealerAdmin.toString(),
								dealer.getDealerId());

						String idss = Arrays.toString(longLists.toArray()).replace("[", "").replace("]", "");

						ids = idss.isEmpty() ? "0" : idss;

					}

					OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
							"select Top 1 * from owner_in_org where orgId =" + type);
					System.out.println("ownerorg---->" + owner);
					if (owner != null) {
						List<Long> longLists = stationDao.getSiteIdBasedOnRole(Roles.Owner.toString(),
								owner.getOwnerId());
						System.out.println("list of ids based on owner" + longLists);
						String idss = Arrays.toString(longLists.toArray()).replace("[", "").replace("]", "");

						ids = idss.isEmpty() ? "0" : idss;

					}
					hql = "SELECT Count(s.id) as stations , (SELECT COUNT(siteid) FROM site WHERE siteId IN (" + ids
							+ ")) AS sites,"
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
							+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
							+ " FROM station s Where s.preProduction = 0 AND s.siteId IN (" + ids + ")";

				}
			}

			System.out.println("site station count: " + hql);

			break;

		case 2:

			if (roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString())) {
				hql = "SELECT COUNT(p.id) AS ports,"
						+ "count(case when sn.status = 'Available' then sn.status end) AS available,"
						+ "count(case when sn.status = 'Charging' then sn.status end) AS charging,"
						+ "count(case when sn.status = 'Inoperative' then sn.status end) AS inoperative,"
						+ "count(case when sn.status = 'OUTOFORDER' then sn.status end) AS outoforder,"
						+ "count(case when sn.status = 'Reserved' then sn.status end) AS reserved, "
						+ "count(case when sn.status = 'Blocked' then sn.status end) AS blocked, "
						+ "count(case when sn.status = 'Planned' then sn.status end) AS planned, "
						+ "count(case when sn.status = 'Removed' then sn.status end) AS removed, "
						+ "count(case when sn.status = 'SuspendedEVSE' then sn.status end) AS suspendedEVSE, "
						+ "count(case when sn.status = 'SuspendedEV' then sn.status end) AS suspendedEV, "
						+ "count(case when sn.status = 'Unknown' then sn.status end) AS unknown "
						+ "FROM  port p  INNER JOIN  station st ON st.id = p.station_id "
						+ "INNER JOIN statusNotification sn ON p.id = sn.port_id  And  st.preProduction = 0 WHERE";

				if (type.equals("All")) {
					hql = hql.replace("WHERE", "");
				} else {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") ");
				}
			} else if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {

				hql = "SELECT COUNT(p.id) AS ports,"
						+ "count(case when sn.status = 'Available' then sn.status end) AS available,"
						+ "count(case when sn.status = 'Charging' then sn.status end) AS charging,"
						+ "count(case when sn.status = 'Inoperative' then sn.status end) AS inoperative,"
						+ "count(case when sn.status = 'OUTOFORDER' then sn.status end) AS outoforder,"
						+ "count(case when sn.status = 'Reserved' then sn.status end) AS reserved, "
						+ "count(case when sn.status = 'Blocked' then sn.status end) AS blocked, "
						+ "count(case when sn.status = 'Planned' then sn.status end) AS planned, "
						+ "count(case when sn.status = 'Removed' then sn.status end) AS removed, "
						+ "count(case when sn.status = 'SuspendedEVSE' then sn.status end) AS suspendedEVSE, "
						+ "count(case when sn.status = 'SuspendedEV' then sn.status end) AS suspendedEV, "
						+ "count(case when sn.status = 'Unknown' then sn.status end) AS unknown "
						+ " FROM  port p  INNER JOIN  station st ON st.id = p.station_id "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = st.manufacturerId"
						+ " INNER JOIN statusNotification sn ON p.id = sn.port_id  "
						+ " WHERE st.preProduction = 0 AND um.userId= " + u.getId();

			} else {
				hql = "SELECT COUNT(p.id) AS ports,"
						+ "count(case when sn.status = 'Available' then sn.status end) AS available,"
						+ "count(case when sn.status = 'Charging' then sn.status end) AS charging,"
						+ "count(case when sn.status = 'Inoperative' then sn.status end) AS inoperative,"
						+ "count(case when sn.status = 'OUTOFORDER' then sn.status end) AS outoforder,"
						+ "count(case when sn.status = 'Reserved' then sn.status end) AS reserved, "
						+ "count(case when sn.status = 'Blocked' then sn.status end) AS blocked, "
						+ "count(case when sn.status = 'Planned' then sn.status end) AS planned, "
						+ "count(case when sn.status = 'Removed' then sn.status end) AS removed, "
						+ "count(case when sn.status = 'SuspendedEVSE' then sn.status end) AS suspendedEVSE, "
						+ "count(case when sn.status = 'SuspendedEV' then sn.status end) AS suspendedEV, "
						+ "count(case when sn.status = 'Unknown' then sn.status end) AS unknown "
						+ "FROM  port p  INNER JOIN  station st ON st.id = p.station_id "
						+ "INNER JOIN statusNotification sn ON p.id = sn.port_id  And  st.preProduction = 0 WHERE";

				if (type.equals("All")) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") ");
				} else {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") ");
				}
			}

			break;

		case 3:

			if (period == 1) {
				hql = "select COUNT(s.id) AS transactions ,isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue,"
						+ " isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh,"
						+ " Count(DISTINCT(s.userid)) as uniqueDriver"
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) "
						+ " AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

				sql = "SELECT count(u.UserId) AS 'drivres',"
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId end) as active,"
						+ " count(case when p.status = 'INACTIVE' then u.UserId end) as new   "
						+ " from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id "
						+ " INNER JOIN role r ON ur.role_id = r.id " + "INNER JOIN accounts ac ON ac.user_id = u.UserId"
						+ " INNER JOIN profile p ON p.user_id = u.UserId"
						+ " WHERE r.rolename='Driver' AND DATEPART(month, u.creationDate) = DATEPART(month, GETUTCDATE()) AND  "
						+ " DATEPART(day, u.creationDate) = DATEPART(day, GETUTCDATE()) AND YEAR(u.creationDate) = YEAR(GETUTCDATE())";

				hql1 = "select ((isNULL(SUM(s.kilowattHoursUsed),0)* 1.5)/ 907.185) AS co2, ((ISNULL(SUM(s.kilowattHoursUsed),0)*4)/20) AS gallons "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) "
						+ " AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

			} else if (period == 7) {
				hql = "select COUNT(s.id) AS transactions ,isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue,"
						+ " Count(DISTINCT(s.userid)) as uniqueDriver,"
						+ " isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh"
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE DATEPART(week, s.startTimeStamp) = DATEPART(week, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

				sql = "SELECT count(u.UserId) AS 'drivres',"
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId end) as active,"
						+ " count(case when p.status = 'INACTIVE' then u.UserId end) as new   "
						+ " from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id "
						+ " INNER JOIN role r ON ur.role_id = r.id " + "INNER JOIN accounts ac ON ac.user_id = u.UserId"
						+ " INNER JOIN profile p ON p.user_id = u.UserId"
						+ " WHERE r.rolename='Driver' AND DATEPART(week, u.creationDate) = DATEPART(week, GETUTCDATE()) AND YEAR(u.creationDate) = YEAR(GETUTCDATE())";

				hql1 = "select ((isNULL(SUM(s.kilowattHoursUsed),0)* 1.5)/ 907.185) AS co2, ((ISNULL(SUM(s.kilowattHoursUsed),0)*4)/20) AS gallons "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId   And  st.preProduction = 0 "
						+ " WHERE DATEPART(week, s.startTimeStamp) = DATEPART(week, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

			} else if (period == 30) {

				hql = "select COUNT(s.id) AS transactions ,isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue,"
						+ " Count(DISTINCT(s.userid)) as uniqueDriver,"
						+ " isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh"
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

				sql = "SELECT count(u.UserId) AS 'drivres',"
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId end) as active,"
						+ " count(case when p.status = 'INACTIVE' then u.UserId end) as new   "
						+ " from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id "
						+ " INNER JOIN role r ON ur.role_id = r.id " + "INNER JOIN accounts ac ON ac.user_id = u.UserId"
						+ " INNER JOIN profile p ON p.user_id = u.UserId"
						+ " WHERE r.rolename='Driver' AND DATEPART(month, u.creationDate) = DATEPART(month, GETUTCDATE()) AND YEAR(u.creationDate) = YEAR(GETUTCDATE())";

				hql1 = "select((isNULL(SUM(s.kilowattHoursUsed),0)* 1.5)/ 907.185) AS co2, ((ISNULL(SUM(s.kilowattHoursUsed),0)*4)/20) AS gallons "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

			} else if (period == 365) {

				hql = "select COUNT(s.id) AS transactions ,isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue,"
						+ " Count(DISTINCT(s.userid)) as uniqueDriver,"
						+ " isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh"
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";

				sql = "SELECT count(u.UserId) AS 'drivres',"
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId end) as active,"
						+ " count(case when p.status = 'INACTIVE' then u.UserId end) as new   "
						+ " from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id "
						+ " INNER JOIN role r ON ur.role_id = r.id " + "INNER JOIN accounts ac ON ac.user_id = u.UserId"
						+ " INNER JOIN profile p ON p.user_id = u.UserId"
						+ " WHERE r.rolename='Driver'AND YEAR(u.creationDate) = YEAR(GETUTCDATE())";

				hql1 = "select ((isNULL(SUM(s.kilowattHoursUsed),0)* 1.5)/ 907.185) AS co2, ((ISNULL(SUM(s.kilowattHoursUsed),0)*4)/20) AS gallons "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())";
			} else {

				hql = "select COUNT(s.id) AS transactions ,isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue,"
						+ " Count(DISTINCT(s.userid)) as uniqueDriver,"
						+ " isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh"
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id  And  st.preProduction = 0 "
						+ " inner JOIN  site si ON st.siteId = si.siteId " + " WHERE YEAR(s.startTimeStamp) = "
						+ period;

				sql = "SELECT count(u.UserId) AS 'drivres',"
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId end) as active,"
						+ " count(case when p.status = 'INACTIVE' then u.UserId end) as new   "
						+ " from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id "
						+ " INNER JOIN role r ON ur.role_id = r.id " + "INNER JOIN accounts ac ON ac.user_id = u.UserId"
						+ " INNER JOIN profile p ON p.user_id = u.UserId"
						+ " WHERE r.rolename='Driver'AND YEAR(u.creationDate) = " + period;

				hql1 = "select ((isNULL(SUM(s.kilowattHoursUsed),0)* 1.5)/ 907.185) AS co2, ((ISNULL(SUM(s.kilowattHoursUsed),0)*4)/20) AS gallons "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  And  st.preProduction = 0 WHERE YEAR(s.startTimeStamp) = "
						+ period;
			}

			if (type.equals("All")) {
				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
							"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
									+ u.getId() + ")"));
					String orgids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
					orgids = orgids.isEmpty() ? "0" : orgids;

					List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;

					hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND ");

					sql = sql.replace("WHERE", "WHERE u.orgId IN (" + orgids + ") AND ");

					hql1 = hql1.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND");

					finalData.addAll(generalDao.findAliasData(hql));

					finalData.addAll(generalDao.findAliasData(sql));

					finalData.addAll(generalDao.findAliasData(hql1));

				}

				if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
						|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND ");

					sql = sql.replace("WHERE", "WHERE u.orgId =" + u.getOrgId() + " AND ");

					hql1 = hql1.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND");

					finalData.addAll(generalDao.findAliasData(hql));

					finalData.addAll(generalDao.findAliasData(sql));

					finalData.addAll(generalDao.findAliasData(hql1));

				} else {

					finalData.addAll(generalDao.findAliasData(hql));

					finalData.addAll(generalDao.findAliasData(sql));

					finalData.addAll(generalDao.findAliasData(hql1));
				}
			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;

				hql = hql.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND ");

				sql = sql.replace("WHERE", "WHERE u.orgId =" + type + " AND ");

				hql1 = hql1.replace("WHERE", "WHERE st.siteId In(" + ids + ") AND");

				finalData.addAll(generalDao.findAliasData(hql));
				finalData.addAll(generalDao.findAliasData(sql));
				finalData.addAll(generalDao.findAliasData(hql1));

			}

			return finalData;

		case 4:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time]," + "COUNT(s.id) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time]," + "COUNT(s.id) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "COUNT(s.id) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			if (type.equals("All")) {
				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;

					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				} else if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
						|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				}
			} else {
				System.out.println("roleName---->" + roleName);

				List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");
			}
			List<Map<String, Object>> data = generalDao.findAliasData(hql);

			boolean nullFlag = true;
			for (Map<String, Object> map : data) {
				if (Double.parseDouble(map.get("value").toString()) != 0)
					nullFlag = false;
			}

			if (nullFlag)
				return null;

			return data;

		case 5:
			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], "
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], "
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "isNULL(ROUND(SUM(s.finalCostInSlcCurrency),2),0) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			if (type.equals("All")) {

				if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
						|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				}
			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");
			}

			boolean nullFlag2 = true;
			for (Map<String, Object> map : generalDao.findAliasData(hql)) {
				if (Double.parseDouble(map.get("value").toString()) != 0)
					nullFlag2 = false;
			}

			if (nullFlag2)
				return null;

			return generalDao.findAliasData(hql);

		case 6:

			if (type != null) {

				if (type.equals("Revenue")) {
					hql = "SELECT TOP 5  si.name, si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue "
							+ " from session s inner join port p on p.id=s.port_id "
							+ " inner JOIN  station st on p.station_id = st.id "
							+ " inner JOIN  site si ON st.siteId = si.siteId  "
							+ "	 WHERE s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ "   , GETUTCDATE()) AND GETUTCDATE()  AND st.preProduction = 0  GROUP BY si.name,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";

				} else if (type.equals("Energy Usage")) {
					hql = "SELECT TOP 5  si.name, si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh "
							+ " from session s inner join port p on p.id=s.port_id "
							+ " inner JOIN  station st on p.station_id = st.id "
							+ " inner JOIN  site si ON st.siteId = si.siteId  "
							+ "	 WHERE s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ "   , GETUTCDATE()) AND GETUTCDATE()  AND st.preProduction = 0  GROUP BY si.name,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";

				} else if (type.equals("Transaction")) {
					hql = "SELECT TOP 5  si.name, si.city, si.country, si.streetName, si.streetNo, si.postal_code, COUNT(s.id) AS transactions "
							+ " from session s inner join port p on p.id=s.port_id "
							+ " inner JOIN  station st on p.station_id = st.id "
							+ " inner JOIN  site si ON st.siteId = si.siteId  "
							+ "	 WHERE s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ "   , GETUTCDATE()) AND GETUTCDATE()  AND st.preProduction = 0  GROUP BY si.name,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";

				} else if (type.equals("Drivers")) {
					hql = "SELECT TOP 5  si.name, si.city, si.country, si.streetName, si.streetNo, si.postal_code,COUNT(Distinct(s.userId)) AS drivers  "
							+ " from session s inner join port p on p.id=s.port_id "
							+ " inner JOIN  station st on p.station_id = st.id "
							+ " inner JOIN  site si ON st.siteId = si.siteId  "
							+ "	 WHERE s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ "   , GETUTCDATE()) AND GETUTCDATE()  AND st.preProduction = 0  GROUP BY si.name,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";

				}

				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());

					hql = hql.replace("WHERE", "WHERE si.siteId IN (:siteId) AND ");
					finalData = getCurrentSession().createSQLQuery(hql).setParameterList("siteId", longList)
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

				} else if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| roleName.equalsIgnoreCase(Roles.Driver.toString())) {
					hql = hql.replace("WHERE", "WHERE si.siteId IN (:siteId) AND ");
					finalData = getCurrentSession().createSQLQuery(hql)
							.setParameterList("siteId", stationDao.getSiteIdBasedOnRole(roleName, u.getId()))
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
				} else {

					finalData = getCurrentSession().createSQLQuery(hql)
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

				}

				if (finalData.size() == 0) {
					hql = "Select TOP 5 si.siteName,si.city,si.country,si.streetName,si.streetNo,si.postal_code, 0 as	energyUsage,0 as revenue,0 AS transactions "

							+ " from site si inner join station st on si.siteId = st.siteId ";

					if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
							|| !(roleName.equalsIgnoreCase(Roles.Admin.toString()))
							|| !(roleName.equalsIgnoreCase(Roles.Admin.toString()))
							|| roleName.equalsIgnoreCase(Roles.Driver.toString()))) {
						hql += " WHERE si.siteId IN (:siteId) GROUP BY si.siteName,si.city,	si.country, si.streetName, si.streetNo, si.postal_code ORDER BY si.siteName ASC";

						finalData = getCurrentSession().createSQLQuery(hql)
								.setParameterList("siteId", stationDao.getSiteIdBasedOnRole(roleName, u.getId()))
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();
					} else {
						hql += " GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY si.siteName ASC";

						finalData = getCurrentSession().createSQLQuery(hql)
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

					}
				}
			}

			return finalData;

		case 7:

			String current = null, max = null, min = null;

			if (period == 1) {

				current = "SELECT CAST(GETUTCDATE() AS DATE) [year],  "
						+ "isNULL(ROUND(SUM(s.kilowattHoursUsed),2),0)  [value]   "
						+ "from session s inner join port p on p.id=s.port_id   "
						+ "inner JOIN  station st on p.station_id = st.id  "
						+ "inner JOIN  site si ON st.siteId = si.siteId "
						+ "WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) "
						+ " AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  AND st.preProduction = 0  "
						+ "ORDER BY [value] ";

				min = "SELECT TOP 1 CAST(s.startTimeStamp AS DATE) [year],  "
						+ "isNULL(ROUND(SUM(s.kilowattHoursUsed),2),0)  [value]   "
						+ "from session s inner join port p on p.id=s.port_id  "
						+ "inner JOIN  station st on p.station_id = st.id  "
						+ "inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY CAST(s.startTimeStamp AS DATE) " + "ORDER BY [value] ";

				max = "SELECT TOP 1 CAST(s.startTimeStamp AS DATE) [year],  "
						+ "isNULL(ROUND(SUM(s.kilowattHoursUsed),2),0)  [value]   "
						+ "from session s inner join port p on p.id=s.port_id  "
						+ "inner JOIN  station st on p.station_id = st.id  "
						+ "inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY CAST(s.startTimeStamp AS DATE) " + "ORDER BY [value]  DESC";

			} else if (period == 7) {
				current = "SELECT DATENAME(Month, GETUTCDATE())+' '+DATENAME(Year, GETUTCDATE()) [year], "
						+ " isNULL(coalesce(Round(SUM(s.kilowattHoursUsed),2),0),0)  [value]  "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ " WHERE DATEPART(week, s.startTimeStamp) = DATEPART(week, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) ORDER BY [value] ";

				min = "SELECT TOP 1  DATEPART(week,s.startTimeStamp ) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ " GROUP BY DATEPART(week,s.startTimeStamp ), DATENAME(YEAR, s.startTimeStamp)"
						+ " ORDER BY [value] ";

				max = "SELECT TOP 1  DATEPART(week,s.startTimeStamp ) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ " GROUP BY DATEPART(week,s.startTimeStamp ), DATENAME(YEAR, s.startTimeStamp)"
						+ " ORDER BY [value]  DESC";

			} else if (period == 30) {
				current = "SELECT DATENAME(Month, GETUTCDATE())+' '+DATENAME(Year, s.startTimeStamp) [year],  "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]   "
						+ " from session s inner join port p on p.id=s.port_id  "
						+ " inner JOIN  station st on p.station_id = st.id  "
						+ " inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND  "
						+ " YEAR(s.startTimeStamp) =  YEAR(GETUTCDATE())  AND st.preProduction = 0  "
						+ " GROUP BY DATENAME(Year, s.startTimeStamp)" + " ORDER BY [value]";

				min = "SELECT  TOP 1 DATENAME(Month, s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ " GROUP BY DATENAME(Month, s.startTimeStamp),DATENAME(YEAR, s.startTimeStamp)"
						+ " ORDER BY [value] ";

				max = "SELECT  TOP 1 DATENAME(Month, s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ " from session s inner join port p on p.id=s.port_id "
						+ " inner JOIN  station st on p.station_id = st.id "
						+ " inner JOIN  site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ " GROUP BY DATENAME(Month, s.startTimeStamp),DATENAME(YEAR, s.startTimeStamp)"
						+ " ORDER BY [value] DESC";

			} else if (period == 365) {
				current = "SELECT DATEPART(Year, GETUTCDATE()) [year], "
						+ "coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ "from session s inner join port p on p.id=s.port_id  "
						+ "inner JOIN  station st on p.station_id = st.id  "
						+ "inner JOIN  site si ON st.siteId = si.siteId   AND st.preProduction = 0   "
						+ "WHERE  YEAR(s.startTimeStamp) =  YEAR(GETUTCDATE())  " + "ORDER BY [value] ";

				min = "SELECT TOP 1 YEAR(s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  " + "FROM session s"
						+ " INNER JOIN port p ON p.id = s.port_id " + " INNER JOIN station st on p.station_id = st.id"
						+ " INNER JOIN site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY YEAR(s.startTimeStamp)" + " ORDER BY  [value]";

				max = " SELECT TOP 1 YEAR(s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  " + "FROM session s"
						+ " INNER JOIN port p ON p.id = s.port_id " + " INNER JOIN station st on p.station_id = st.id"
						+ " INNER JOIN site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY YEAR(s.startTimeStamp)" + " ORDER BY  [value] DESC";

			} else {

				current = "SELECT " + period + "[year], " + "coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  "
						+ "from session s inner join port p on p.id=s.port_id  "
						+ "inner JOIN  station st on p.station_id = st.id  "
						+ "inner JOIN  site si ON st.siteId = si.siteId   AND st.preProduction = 0   "
						+ "WHERE  YEAR(s.startTimeStamp) =" + period + " ORDER BY [value] ";

				min = "SELECT TOP 1 YEAR(s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  " + "FROM session s"
						+ " INNER JOIN port p ON p.id = s.port_id " + " INNER JOIN station st on p.station_id = st.id"
						+ " INNER JOIN site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY YEAR(s.startTimeStamp)" + " ORDER BY  [value]";

				max = " SELECT TOP 1 YEAR(s.startTimeStamp) [year], "
						+ " coalesce(Round(SUM(s.kilowattHoursUsed),2),0)  [value]  " + "FROM session s"
						+ " INNER JOIN port p ON p.id = s.port_id " + " INNER JOIN station st on p.station_id = st.id"
						+ " INNER JOIN site si ON st.siteId = si.siteId  AND st.preProduction = 0  "
						+ "GROUP BY YEAR(s.startTimeStamp)" + " ORDER BY  [value] DESC";

			}

			if (type.equals("All")) {
				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					current = current.replace("WHERE", "WHERE si.siteId In (" + ids + ") AND ");

					max = max.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
					min = min.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
					finalData.addAll(generalDao.findAliasData(min));
					finalData.addAll(generalDao.findAliasData(current));
					finalData.addAll(generalDao.findAliasData(max));

				} else if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
						|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					current = current.replace("WHERE", "WHERE si.siteId In (" + ids + ") AND ");

					max = max.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
					min = min.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
					finalData.addAll(generalDao.findAliasData(min));
					finalData.addAll(generalDao.findAliasData(current));
					finalData.addAll(generalDao.findAliasData(max));

				} else {
					finalData.addAll(generalDao.findAliasData(min));
					finalData.addAll(generalDao.findAliasData(current));
					finalData.addAll(generalDao.findAliasData(max));

				}
			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
				System.out.println("longList=====" + longList);
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				current = current.replace("WHERE", "WHERE si.siteId In (" + ids + ") AND ");
				max = max.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
				min = min.replace("GROUP BY", "WHERE si.siteId In (" + ids + ") GROUP BY ");
				System.out.println("current" + current);
				System.out.println("max" + max);
				System.out.println("min" + min);

				finalData.addAll(generalDao.findAliasData(min));
				finalData.addAll(generalDao.findAliasData(current));
				finalData.addAll(generalDao.findAliasData(max));
				System.out.println("finalData" + finalData);

			}
			return finalData;

		case 8:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], "
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], "
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time],"
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1], ISNULL(CoUNT(s.id), 0)  [value2] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			if (type.equals("All")) {

				if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
						|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
						|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				}
			} else if (!type.equals("All")) {
				String idss = "0";
				DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
						"select Top 1 * from dealer_in_org where orgId =" + type);
				System.out.println("dealerorg---->" + dealer);
				if (dealer != null) {
					List<Long> longLists = stationDao.getSiteIdBasedOnRole(Roles.DealerAdmin.toString(),
							dealer.getDealerId());

					idss = Arrays.toString(longLists.toArray()).replace("[", "").replace("]", "");

					idss = idss.isEmpty() ? "0" : idss;

				}

				OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
						"select Top 1 * from owner_in_org where orgId =" + type);
				System.out.println("ownerorg---->" + owner);
				if (owner != null) {
					List<Long> longLists = stationDao.getSiteIdBasedOnRole(Roles.Owner.toString(), owner.getOwnerId());
					System.out.println("list of ids based on owner" + longLists);
					idss = Arrays.toString(longLists.toArray()).replace("[", "").replace("]", "");

					idss = idss.isEmpty() ? "0" : idss;

				}

				hql = hql.replace("GROUP BY", "AND si.siteId In (" + idss + ") GROUP BY ");
			}
			boolean nullFlag3 = true;
			for (Map<String, Object> map : generalDao.findAliasData(hql)) {
				if (Double.parseDouble(map.get("value1").toString()) != 0
						&& Double.parseDouble(map.get("value2").toString()) != 0)
					nullFlag3 = false;
			}

			if (nullFlag3)
				return null;
			System.out.println("947----->" + hql);
			return generalDao.findAliasData(hql);

		case 9:
			if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

				List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				if (type.equals("All")) {

					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status  end) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status  end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 "
							+ "WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ "(SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN (" + ids
							+ ")))";

				} else {

					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status  end ) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status  end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 "
							+ "WHERE sn.status='" + type
							+ "' and  p.id IN (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + ")))";
				}

			} else if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {

				if (type.equals("All")) {
					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status end) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0  "
							+ "WHERE (sn.status='Inoperative' or sn.status='Blocked')";

				} else {

					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status end) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 "
							+ "WHERE sn.status='" + type + "'";

				}
			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				if (type.equals("All")) {

					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status  end) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status  end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 "
							+ "WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ "(SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN (" + ids
							+ ")))";

				} else {

					hql = "SELECT COUNT(p.id) AS 'ports',"
							+ "count(case when sn.status = 'Inoperative' then sn.status  end ) as down,"
							+ "count(case when sn.status = 'Blocked' then sn.status  end) as falut "
							+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
							+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 "
							+ "WHERE sn.status='" + type
							+ "' and  p.id IN (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + ")))";
				}
			}
			break;

		case 10:
			hql = "SELECT COUNT(p.id) AS 'ports',"
					+ "count(case when sn.status = 'Inoperative' then sn.status end) as down,"
					+ "count(case when sn.status = 'Blocked' then sn.status end) as falut "
					+ "from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
					+ "inner join station st On st.id =p.station_id  AND st.preProduction = 0 ";

			hql1 = "SELECT sn.errorCode, st.referNo,sn.status,p.displayName ,"
					+ " convert(varchar, st.stationTimeStamp, 120) AS lastContactTime "
					+ " from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
					+ " INNER JOIN station st ON st.id= p.station_id " + " AND st.preProduction = 0 ";
			if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

				List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = hql + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
						+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN (" + ids
						+ ")))";

				hql1 = hql1 + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
						+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN (" + ids
						+ " )))   Order By st.stationTimeStamp Desc";

			} else if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {

				if (type.equals("All")) {
					hql = hql + " WHERE (sn.status='Inoperative' or sn.status='Blocked')";

					hql1 = hql1
							+ " WHERE (sn.status='Inoperative' or sn.status='Blocked')  Order By st.stationTimeStamp Desc";

				} else {

					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + ")))";

					hql1 = hql1 + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + " )))   Order By st.stationTimeStamp Desc";
				}
			} else if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {
				hql = "SELECT COUNT(p.id) AS 'ports',"
						+ " count(case when sn.status = 'Inoperative' then sn.status end) as down,"
						+ " count(case when sn.status = 'Blocked' then sn.status end) as falut "
						+ " From statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
						+ " INNER join station st On st.id = p.station_id "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = st.manufacturerId "
						+ " WHERE st.preProduction = 0 AND um.userId=" + u.getId();

				hql1 = "SELECT sn.errorCode, st.referNo,sn.status,p.displayName ,"
						+ " convert(varchar, st.stationTimeStamp, 120) AS lastContactTime "
						+ " from statusNotification sn INNER JOIN port p ON sn.port_id = p.id "
						+ " INNER JOIN station st ON st.id= p.station_id "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = st.manufacturerId "
						+ " WHERE st.preProduction = 0 AND um.userId= " + u.getId();
			} else {
				if (type.equals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + " )))";

					hql1 = hql1
							+ " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN  (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + " )))  Order By st.stationTimeStamp Desc";
				} else {
					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + " )))";

					hql1 = hql1 + " WHERE (sn.status='Inoperative' or sn.status='Blocked') AND p.id IN "
							+ " (SELECT id From port Where station_id IN (SELECT id FROM station WHERE siteId IN ("
							+ ids + " )))  Order By st.stationTimeStamp Desc";
				}
			}
			System.out.println("query for count of fault " + hql);

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("count", generalDao.findAliasData(hql));
			map.put("data", generalDao.findAliasData(hql1));
			finalData.add(map);

			return finalData;

		case 11:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], " + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour, u.creationDate) AND r.rolename='Driver' "
						+ "AND  DATEPART(year, u.creationDate) = DATEPART(year, GETDATE()) And DATEPART(month, u.creationDate) = DATEPART(month, GETDATE()) "
						+ "AND DATEPART(day,u.creationDate) = DATEPART(day, GETUTCDATE()) GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], " + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, u.creationDate)  AND r.rolename='Driver' "
						+ "AND  DATEPART(year, u.creationDate) = DATEPART(year, GETDATE()) AND DATEPART(week, u.creationDate) = DATEPART(week, GETDATE()) "
						+ "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN  days mo ON mo.id = DATEPART(day, u.creationDate) AND r.rolename='Driver' "
						+ "AND  DATEPART(year, u.creationDate) = DATEPART(year, GETDATE()) AND DATEPART(month, u.creationDate) = DATEPART(month, GETDATE()) "
						+ "GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time]," + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, u.creationDate) AND r.rolename='Driver'"
						+ "AND  u.creationDate BETWEEN DATEADD(month, -3, GETUTCDATE()) AND GETUTCDATE()"
						+ "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time]," + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN  month mo ON mo.id = DATEPART(month, u.creationDate) AND r.rolename='Driver' "
						+ "AND u.creationDate BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE()"
						+ "GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, u.creationDate) AND r.rolename='Driver' "
						+ "AND  DATEPART(year, u.creationDate) = DATEPART(year, GETDATE()) "
						+ "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], " + "count(u.UserId) [total],  "
						+ "count(case WHEN p.status = 'ACTIVE' then u.UserId END) [active], "
						+ "count(case when p.status = 'INACTIVE' then u.UserId END) [new]    "
						+ "from  Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id  "
						+ "INNER JOIN role r ON ur.role_id = r.id INNER JOIN accounts ac ON ac.user_id = u.UserId "
						+ "INNER JOIN profile p ON p.user_id = u.UserId "
						+ "RIGHT JOIN month mo ON mo.id = DATEPART(month, u.creationDate)  "
						+ "AND  DATEPART(year, u.creationDate) = " + period
						+ "GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			if (type.equals("All")) {
				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getOrgIdsOfGroupDealerAdmin(u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP", "AND u.orgId IN(" + ids + ") GROUP ");

				} else if (!(roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Support.toString())))
					hql = hql.replace("GROUP", "AND u.orgId =" + u.getOrgId() + " GROUP ");

			} else {
				if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {
					hql = hql.replace("GROUP", "AND u.orgId =" + type + " GROUP ");
				} else {
					hql = hql.replace("GROUP", "AND u.orgId =" + u.getOrgId() + " GROUP ");
				}
			}

			return generalDao.findAliasData(hql);

		case 13:

			if (period == 1) {
				hql = "SELECT mo.id [year],mo.value+':00' [time], "
						+ "Count(Distinct(s.userId)) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  hours mo ON mo.value =  DATENAME(hour,  s.startTimeStamp) "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) And DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ "AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id";

			} else if (period == 7) {

				hql = "SELECT mo.id [year],mo.value [time], " + "Count(Distinct(s.userId)) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  week mo ON mo.id = DATEPART(WEEKDAY, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(week, s.startTimeStamp) = DATEPART(week, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 30) {

				hql = "SELECT mo.id [year],CONVERT(VARCHAR(6), mo.value)+'-'+ DATENAME(month,GETUTCDATE()) [time], "
						+ "Count(Distinct(s.userId)) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  days mo ON mo.id = DATEPART(day, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) AND DATEPART(month, s.startTimeStamp) = DATEPART(month, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.value, mo.id ORDER BY mo.id ";

			} else if (period == 90) {
				hql = "SELECT mo.id [year],mo.monthName [time]," + "Count(Distinct(s.userId)) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -3, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";
			} else if (period == 180) {

				hql = "SELECT mo.id [year],mo.monthName [time]," + "Count(Distinct(s.userId)) [value] FROM session s "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN  month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
						+ "WHERE s.startTimeStamp BETWEEN DATEADD(month, -6, GETUTCDATE()) AND GETUTCDATE()"
						+ " AND st.preProduction = 0  GROUP BY mo.monthName, mo.id ORDER BY mo.id ";

			} else if (period == 365) {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "Count(Distinct(s.userId)) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = DATEPART(year, GETDATE()) "
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			} else {

				hql = "SELECT mo.id [year], mo.monthName [time], "
						+ "Count(Distinct(s.userId)) [value] FROM session s  "
						+ "INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id  "
						+ "INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp)  "
						+ "AND  DATEPART(year, s.startTimeStamp) = " + period
						+ " AND st.preProduction = 0  GROUP BY mo.monthName,mo.id ORDER BY mo.id ";

			}

			if (type.equals("All")) {
				if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnGrpDealerAdminId(u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				}

				else if (!(roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");

				}
			} else {

				if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Support.toString())))
					hql = hql.replace("GROUP BY", "AND si.siteId =" + type + " GROUP BY ");
				else {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("GROUP BY", "AND si.siteId In (" + ids + ") GROUP BY ");
				}
			}
			boolean nullFlag6 = true;
			for (Map<String, Object> map1 : generalDao.findAliasData(hql)) {
				if (Double.parseDouble(map1.get("value").toString()) != 0)
					nullFlag6 = false;
			}

			if (nullFlag6)
				return null;

			return generalDao.findAliasData(hql);

		case 14:

			if (type.equals("All")) {

				if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					if (period == 1) {
						hql = "SELECT DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE()))+':00' [time], "
								+ " DATEADD(hh, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2],"
								+ " coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId "
								+ " RIGHT JOIN hours mo ON  DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())) = DATENAME(hh, s.startTimeStamp) "
								+ " AND s.startTimeStamp BETWEEN DATEADD(hh, -24, GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())), "
								+ " mo.value, mo.id,DATEADD(hh, -mo.id, GETUTCDATE())  ORDER BY month";

					} else if (period == 7) {

						hql = "SELECT DATENAME(WEEKDAY, DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
								+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2], "
								+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s  "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId "
								+ "RIGHT JOIN week mo ON DATENAME(weekday,DATEADD(dd, -mo.id, GETUTCDATE())) = DATENAME(WEEKDAY, s.startTimeStamp) "
								+ "AND s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE()  "
								+ " AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.value, mo.id,DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE()))  "
								+ "ORDER BY month";

					} else if (period == 30) {

						hql = "SELECT "
								+ "CONVERT(VARCHAR(6), DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())))+'-'+ DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
								+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2],"
								+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId "
								+ " RIGHT JOIN days mo ON DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) = DATEPART(day, s.startTimeStamp) "
								+ " AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.id, DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) "
								+ " ORDER BY month";

					} else if (period == 365) {

						hql = "SELECT  DATENAME(month, DATEADD(month, -(mo.id-1), GETUTCDATE())) [time], DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())) [YEAR], coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) as  [value1], ISNULL(CoUNT(s.id), 0) AS [value2] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
								+ " AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())), mo.monthName,mo.id ORDER BY YEAR ASC, mo.id DESC";
					}
				} else {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					if (period == 1) {

						hql = "SELECT DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE()))+':00' [time], "
								+ " DATEADD(hh, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2],"
								+ " coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId "
								+ " RIGHT JOIN hours mo ON  DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())) = DATENAME(hh, s.startTimeStamp) "
								+ " AND st.siteId IN (" + ids
								+ ")  AND s.startTimeStamp BETWEEN DATEADD(hh, -24, GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())), "
								+ " mo.value, mo.id,DATEADD(hh, -mo.id, GETUTCDATE())  ORDER BY month";

					} else if (period == 7) {

						hql = "SELECT DATENAME(WEEKDAY, DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
								+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2], "
								+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s  "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON  st.siteId = si.siteId RIGHT JOIN week mo ON DATENAME(weekday,DATEADD(dd, -mo.id, GETUTCDATE())) = DATENAME(WEEKDAY, s.startTimeStamp) "
								+ " AND st.siteId IN (" + ids + ") AND s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE()  "
								+ " AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.value, mo.id,DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE()))  "
								+ "ORDER BY month";

					} else if (period == 30) {

						hql = "SELECT "
								+ "CONVERT(VARCHAR(6), DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())))+'-'+ DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
								+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2],"
								+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId RIGHT JOIN days mo ON DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) = DATEPART(day, s.startTimeStamp) "
								+ " AND st.siteId IN (" + ids + ") AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.id, DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) "
								+ " ORDER BY month";

					} else if (period == 365) {

						hql = "SELECT  DATENAME(month, DATEADD(month, -(mo.id-1), GETUTCDATE())) [time], DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())) [YEAR], coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) as  [value1], ISNULL(CoUNT(s.id), 0) AS [value2] FROM session s "
								+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
								+ " INNER JOIN site si ON st.siteId = si.siteId RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
								+ " AND st.siteId IN (" + ids + ") AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
								+ ", GETUTCDATE()) AND GETUTCDATE() "
								+ "  AND st.preProduction = 0  GROUP BY DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())), mo.monthName,mo.id ORDER BY YEAR ASC, mo.id DESC";
					}

				}

			} else {
				if (period == 1) {
					hql = "SELECT DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE()))+':00' [time], "
							+ " DATEADD(hh, -mo.id, GETUTCDATE()) [MONTH],ISNULL(CoUNT(s.id), 0)  [value2], "
							+ " coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
							+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
							+ " INNER JOIN site si ON st.siteId = si.siteId "
							+ " RIGHT JOIN hours mo ON  DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())) = DATENAME(hh, s.startTimeStamp) "
							+ " AND st.siteId=" + type
							+ " AND s.startTimeStamp BETWEEN DATEADD(hh, -24, GETUTCDATE()) AND GETUTCDATE() "
							+ "  AND st.preProduction = 0  GROUP BY DATENAME(hour, DATEADD(hh, -mo.id, GETUTCDATE())), "
							+ " mo.value, mo.id,DATEADD(hh, -mo.id, GETUTCDATE())  ORDER BY month";

				} else if (period == 7) {

					hql = "SELECT DATENAME(WEEKDAY, DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
							+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2], "
							+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s  "
							+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
							+ " INNER JOIN site si ON st.siteId = si.siteId " + "AND si.siteId=" + type
							+ " RIGHT JOIN week mo ON DATENAME(weekday,DATEADD(dd, -mo.id, GETUTCDATE())) = DATENAME(WEEKDAY, s.startTimeStamp) "
							+ " AND s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ ", GETUTCDATE()) AND GETUTCDATE()  "
							+ "  AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.value, mo.id,DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE()))  "
							+ " ORDER BY month";

				} else if (period == 30) {

					hql = "SELECT "
							+ "CONVERT(VARCHAR(6), DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())))+'-'+ DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())) [time], "
							+ "DATEADD(dd, -mo.id, GETUTCDATE()) [MONTH], ISNULL(CoUNT(s.id), 0)  [value2],"
							+ "coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) [value1] FROM session s "
							+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
							+ " INNER JOIN site si ON st.siteId = si.siteId " + "AND si.siteId=" + type
							+ " RIGHT JOIN days mo ON DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) = DATEPART(day, s.startTimeStamp) "
							+ " AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ ", GETUTCDATE()) AND GETUTCDATE() "
							+ "  AND st.preProduction = 0  GROUP BY DATENAME(month,DATEADD(dd, -mo.id, GETUTCDATE())), mo.id, DATEPART(day, DATEADD(dd, -mo.id, GETUTCDATE())) "
							+ " ORDER BY month";

				} else if (period == 365) {

					hql = "SELECT  DATENAME(month, DATEADD(month, -(mo.id-1), GETUTCDATE())) [time], DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())) [YEAR], coalesce(Round(SUM(s.finalCostInSlcCurrency),2),0) as  [value1], ISNULL(CoUNT(s.id), 0) AS [value2] FROM session s "
							+ " INNER JOIN port p on p.id = s.port_id INNER JOIN station st on p.station_id = st.id "
							+ " INNER JOIN site si ON st.siteId = si.siteId  RIGHT JOIN month mo ON mo.id = DATEPART(month, s.startTimeStamp) "
							+ " AND si.siteId=" + type + "  AND  s.startTimeStamp BETWEEN DATEADD(dd, -" + period
							+ ", GETUTCDATE()) AND GETUTCDATE() "
							+ "  AND st.preProduction = 0  GROUP BY DATEPART(Year, DATEADD(month, -(mo.id-1), GETUTCDATE())), mo.monthName,mo.id ORDER BY YEAR ASC, mo.id DESC";

				}
			}

			break;

		case 15:

			if (type.equals("All")) {

				if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {

					hql = "SELECT COUNT(st.id) AS stations FROM station st";

				} else {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = "SELECT COUNT(st.id) AS stations FROM station st WHERE st.siteId IN (" + ids + ")";
				}

			} else {

				if (!(roleName.equalsIgnoreCase(Roles.Admin.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Accounts.toString()))
						|| !(roleName.equalsIgnoreCase(Roles.Support.toString())))
					hql = "SELECT COUNT(st.id) AS stations FROM station st WHERE st.siteId = " + type;
				else {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = "SELECT COUNT(st.id) AS stations FROM station st WHERE st.siteId In ( " + ids + ")";
				}

			}
			break;

		case 16:

			hql = "SELECT COUNT(u.UserId) AS count FROM Users u INNER JOIN usersinroles ur ON u.UserId = ur.user_id inner JOIN role r ON r.id = ur.role_id WHERE r.rolename ='"
					+ type + "'";

			break;

		case 17:

			if ((roleName.equalsIgnoreCase(Roles.Admin.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Accounts.toString()))
					|| (roleName.equalsIgnoreCase(Roles.Support.toString()))) {

				hql = "SELECT "
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s ";

				if (!type.equals("All"))
					hql = hql + " WHERE s.siteId=" + type;

			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = "SELECT "
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
						+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
						+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
						+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
						+ " FROM station s Where s.siteId IN (" + ids + ")";

				if (!type.equals("All"))
					hql = "SELECT "
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN s.stationTimeStamp  END) AS unAvailable,"
							+ " COUNT (CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active'  THEN s.stationTimeStamp END ) AS available,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Maintenance' THEN isnull(s.stationTimeStamp,0) END) AS maintenance,"
							+ " COUNT (CASE WHEN s.stationAvailStatus='Disable' THEN isnull(s.stationTimeStamp,0) END) AS decomition,"
							+ " COUNT (CASE WHEN s.stationAvailStatus ='Not Installed' THEN isnull(s.stationTimeStamp,0) END) AS comingSoon"
							+ " FROM station s  Where s.siteId IN (" + type + ")";

			}

			break;

		case 18:

			if (period == 1)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, count(s.id) AS transactions  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";
			else if (period == 7)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, count(s.id) AS transactions  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";
			else if (period == 30)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, count(s.id) AS transactions  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";
			else if (period == 365)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, count(s.id) AS transactions  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";
			else
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, count(s.id) AS transactions  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId " + " WHERE YEAR(s.startTimeStamp) = " + period
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY transactions DESC";

			if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {

				System.out.println("top 5");
				List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("WHERE", "WHERE si.siteId In(" + ids + ") AND ");
			} else {
				System.out.println("top 5 else");
				if (!type.contentEquals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE  si.siteId In(" + ids + ") AND ");
				}
			}

			break;

		case 19:

			if (period == 1)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";
			else if (period == 7)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";
			else if (period == 30)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";
			else if (period == 365)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";
			else
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, isNULL(ROUND(sum(finalCostInSlcCurrency),2),0) AS revenue   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId " + " WHERE YEAR(s.startTimeStamp) = " + period
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY revenue DESC";

			if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {
				List<Long> longList = (!type.contentEquals("All")
						&& roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString()))
								? stationDao.getSiteIdBasedOnOrg(Long.parseLong(type))
								: stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("WHERE", "WHERE si.siteId In(" + ids + ") AND ");
			} else {
				if (!type.contentEquals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE  si.siteId In(" + ids + ") AND ");

					System.out.println("top " + hql);
				}
			}

			break;
		case 20:

			if (period == 1)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";
			else if (period == 7)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";
			else if (period == 30)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";
			else if (period == 365)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";
			else
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  isNULL(ROUND(sum(kilowattHoursUsed),2),0) AS kwh  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId " + " WHERE YEAR(s.startTimeStamp) = " + period
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY kwh DESC";
			if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {
				List<Long> longList = (!type.contentEquals("All")
						&& roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString()))
								? stationDao.getSiteIdBasedOnOrg(Long.parseLong(type))
								: stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("WHERE", "WHERE si.siteId In(" + ids + ") AND ");
			} else {
				if (!type.contentEquals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE   si.siteId In(" + ids + ") AND ");
				}
			}

			break;

		case 21:

			hql = "SELECT s.siteName, CONCAT(  s.streetNo,' ',s.streetName,',', s.city,' ', s.postal_code,' ',s.country) AS address, s.installationDate,   "
					+ "(SELECT orgName FROM organization WHERE id= s.org) AS ownerName, "
					+ "(select top 1 userid from users_in_org where orgId=(SELECT id FROM organization WHERE id= s.org))as ownerId,"
					+ "(Isnull((SELECT orgName FROM organization WHERE  id = (SELECT orgid FROM USERs WHERE userId="
					+ " (SELECT TOP 1 ownerId FROM owner_in_org WHERE orgId = s.org) )),'EVGateway'))AS dealerName ,"
					+ "(select Top 1 userid from users_in_org where orgId=(SELECT id FROM organization WHERE id = "
					+ "(SELECT orgid FROM USERs WHERE userId= (SELECT TOP 1 ownerId FROM owner_in_org WHERE orgId = s.org) ))) AS dealerId , "
					+ "(SELECT u.email FROM Users u WHERE u.userId IN  (SELECT Top 1 ownerId FROM owner_in_org WHERE orgId = s.org )) AS email, "
					+ "(SELECT a.countryCode FROM Users u INNER JOIN address a On a.user_id = u.userId WHERE u.userId IN (SELECT Top 1 ownerId FROM owner_in_org WHERE orgId = s.org)) AS countryCode, "
					+ "(SELECT a.phone FROM Users u INNER JOIN address a On a.user_id = u.userId WHERE u.userId IN (SELECT Top 1 ownerId FROM owner_in_org WHERE orgId = s.org)) AS phone,s.currencyType  "
					+ " FROM site s WHERE s.siteId=" + type;

			break;

		case 22:

			if (period == 1)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  COUNT(Distinct(s.userId)) AS drivers  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE())  AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";
			else if (period == 7)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code,  COUNT(Distinct(s.userId)) AS drivers   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND DATEPART(day, s.startTimeStamp) = DATEPART(day, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE()) "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";
			else if (period == 30)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, COUNT(Distinct(s.userId)) AS drivers    "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE DATEPART(month, s.startTimeStamp) = DATEPART(month, GETUTCDATE()) AND YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";
			else if (period == 365)
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, COUNT(Distinct(s.userId)) AS drivers  "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId "
						+ " WHERE YEAR(s.startTimeStamp) = YEAR(GETUTCDATE())  "
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";
			else
				hql = "SELECT TOP 5  si.siteName [name], si.city, si.country, si.streetName, si.streetNo, si.postal_code, COUNT(Distinct(s.userId)) AS drivers   "
						+ " from session s inner join port p on p.id=s.port_id inner JOIN  station st on p.station_id = st.id  "
						+ "	inner JOIN  site si ON st.siteId = si.siteId " + " WHERE YEAR(s.startTimeStamp) = " + period
						+ "	 AND st.preProduction = 0  GROUP BY si.siteName,si.city, si.country, si.streetName, si.streetNo, si.postal_code ORDER BY drivers DESC";

			if (!(roleName.equalsIgnoreCase(Roles.Admin.toString())
					|| roleName.equalsIgnoreCase(Roles.Accounts.toString())
					|| roleName.equalsIgnoreCase(Roles.Support.toString()))) {
				List<Long> longList = (!type.contentEquals("All")
						&& roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString()))
								? stationDao.getSiteIdBasedOnOrg(Long.parseLong(type))
								: stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("WHERE", "WHERE si.siteId In(" + ids + ") AND ");
			} else {
				if (!type.contentEquals("All")) {
					List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.parseLong(type));
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;
					hql = hql.replace("WHERE", "WHERE  si.siteId In(" + ids + ") AND ");
				}
			}

			break;

		default:
			break;
		}
		System.out.println("1766----->" + hql);
		return generalDao.findAliasData(hql);

	}

	private List<Long> convertToLongList(List<BigDecimal> bigDecimalList) {

		List<Long> longs = bigDecimalList.stream().map(BigDecimal::longValue).collect(Collectors.toList());

		return longs;
	}

	@SuppressWarnings({ "unused" })
	private long getOrgIdForUserAdmin(Long userId, String roleName) {
		// List<Long> longList = new ArrayList<>();
		long orgId = 0l;

		if (roleName.equalsIgnoreCase(Roles.Dealer.toString())
				|| roleName.equalsIgnoreCase(Roles.DealerAdmin.toString())) {

			orgId = (long) generalDao.findIdBySqlQuery("SELECT orgId FROM users_in_org WHERE dealerId=" + userId);

		} else if (roleName.equalsIgnoreCase(Roles.Owner.toString())) {

			long dealerId = (long) generalDao
					.findIdBySqlQuery("SELECT dealerId FROM owner_in_dealer WHERE ownerId=" + userId);

			orgId = (long) generalDao.findIdBySqlQuery("SELECT orgId FROM users_in_org WHERE dealerId=" + dealerId);

		} else if (roleName.equalsIgnoreCase(Roles.SiteHost.toString())) {

			long siteId = generalDao.findIdBySqlQuery("SELECT siteId FROM users_in_sites WHERE userId=" + userId);

			orgId = (long) generalDao.findIdBySqlQuery("SELECT org FROM site WHERE siteId=" + siteId);

		}

		return orgId;

	}

	@Override
	public List<Map<String, Object>> getMapStations(String type, String id, String useruid)
			throws UserNotFoundException {

		LOGGER.info("ReportDaoImpl.getMapStations() -  [" + id + "] ");

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Role role = userDao.getCurrentRole();

		String hql = "";

		if (type.equals("station")) {

			if (id.equals("All")) {

				hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId ";

				if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())) {
					hql = hql + " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
							+ " WHERE s.preProduction = 0 AND um.userId= " + user.getId();
				}

				else if (role.getRolename().equalsIgnoreCase(Roles.DealerAdmin.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Dealer.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Owner.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.SiteHost.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(role.getRolename(), user.getId());

					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				}

			} else
				hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select  dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId " + " WHERE s.id=" + id;

		}

		else if (type.equals("site")) {

			if (id.equals("All")) {
				hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId ";

				if (!(role.getRolename().equalsIgnoreCase(Roles.Admin.toString())
						|| !(role.getRolename().equalsIgnoreCase(Roles.Admin.toString()))
						|| !(role.getRolename().equalsIgnoreCase(Roles.Admin.toString()))
						|| role.getRolename().equalsIgnoreCase(Roles.Driver.toString()))
						|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {
					List<Long> longList = stationDao.getSiteIdBasedOnRole(role.getRolename(), user.getId());
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");
					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				}

			} else
				hql = "SELECT s.referNo, s.id , s.siteId,s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId " + " WHERE   s.siteId="
						+ id;

		} else if (type.equals("dashBoard")) {
			System.out.println("DashBoard");
			if (id.equals("All")) {

				hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId ";

				if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())) {
					hql = hql + " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
							+ " WHERE s.preProduction = 0 AND um.userId= " + user.getId();
				}

				else if (role.getRolename().equalsIgnoreCase(Roles.DealerAdmin.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.SiteHost.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Owner.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Dealer.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> longList = stationDao.getSiteIdBasedOnRole(role.getRolename(), user.getId());

					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				}
			} else {

				hql = "SELECT s.referNo, s.id ,s.siteId, s.stationName,s.stationAddress, g.latitude, g.longitude, "
						+ "CASE WHEN DATEADD(minute, -15 ,GETUTCDATE()) <  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'available' "
						+ " WHEN DATEADD(minute, -15 ,GETUTCDATE()) >  s.stationTimeStamp AND s.stationAvailStatus ='Active' THEN 'unavailable' "
						+ " WHEN  s.stationAvailStatus ='Maintenance' THEN 'inMaintenance'  "
						+ "	WHEN  s.stationAvailStatus ='Disable'  THEN 'decomition'  "
						+ "	WHEN  s.stationAvailStatus ='Not Installed'  THEN 'comingSoon'  "
						+ " ELSE 'NA' END AS status, "
						+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
						+ "(select top 1 orgid from dealer_in_org "
						+ "where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
						+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
						+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
						+ " From station s INNER JOIN geoLocation g ON g.id = s.coordinateId ";

				DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
						"select Top 1 * from dealer_in_org where orgId =" + id);
				OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
						"select Top 1 * from owner_in_org where orgId =" + id);
				if (role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

					List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
							"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
									+ "(SELECT  dealerId FROM dealer_in_org WHERE orgId IN  (" + id + ")))"));
					String ids = Arrays.toString(dealerSite.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				} else if (dealer != null) {
					List<Long> longList = stationDao.getSiteIdBasedOnRole(Roles.DealerAdmin.toString(),
							dealer.getDealerId());
					System.out.println("list of ids based on dealer" + longList);
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				}

				else if (owner != null) {
					List<Long> longList = stationDao.getSiteIdBasedOnRole(Roles.Owner.toString(), owner.getOwnerId());
					System.out.println("list of ids based on owner" + longList);
					String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

					ids = ids.isEmpty() ? "0" : ids;

					hql = hql + " WHERE  s.siteId In (" + ids + ")";

				}

			}
		}

		return generalDao.findAliasData(hql);
	}

	@Override
	public List<Map<String, Object>> getSiteOnMap(String string, String id, String useruid)
			throws UserNotFoundException {

		System.out.println("DashBoard");
		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Role role = userDao.getCurrentRole();
		String hql = "";
		if (id.equals("All")) {

			hql = "SELECT s.siteId,s.siteName, g.latitude,s.uuid, g.longitude, count(st.id) as stationCount, "

					+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
					+ "(select top 1 orgid from dealer_in_org "
					+ " where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
					+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
					+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
					+ " From site s left join station st on s.siteId = st.siteId INNER JOIN geoLocation g ON g.id = s.coordinateId "
					+ " group by  s.siteId,s.siteName, g.latitude, g.longitude,s.uuid ";

			if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())) {
				hql = hql.replace("group by ",
						"Inner Join user_in_manufacturer um on um.manufacturerId = st.manufacturerId"
								+ " WHERE st.preProduction = 0 AND um.userId= " + user.getId() + " group by ");

			}

			else if (role.getRolename().equalsIgnoreCase(Roles.DealerAdmin.toString())
					|| role.getRolename().equalsIgnoreCase(Roles.SiteHost.toString())
					|| role.getRolename().equalsIgnoreCase(Roles.Owner.toString())
					|| role.getRolename().equalsIgnoreCase(Roles.Dealer.toString())
					|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())
					|| role.getRolename().equalsIgnoreCase(Roles.Utility.toString())) {

				List<Long> longList = stationDao.getSiteIdBasedOnRole(role.getRolename(), user.getId());

				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;
				hql = hql.replace("group by ", "WHERE  s.siteId In (" + ids + ") group by ");

			}
		} else {

			hql = "SELECT s.siteId,s.siteName,g.latitude, s.uuid,g.longitude, count(st.id) as stationCount,"
					+ " isNull((Select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid in  "
					+ "(select top 1 orgid from dealer_in_org "
					+ "where dealerId in(select top 1 dealerid from owner_in_dealer where ownerId "
					+ " in (select ownerid from owner_in_org where orgid in (select org from site where siteid =s.siteId)))"
					+ ")),(select url from logo_image where createdBy ='Dealer' and logoType='mini' and orgid =1 )) as url "
					+ " From site  s left join station st on s.siteId = st.siteId  INNER JOIN geoLocation g ON g.id = s.coordinateId"
					+ " group by  s.siteId,s.siteName, g.latitude, g.longitude,s.uuid ";

			DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
					"select Top 1 * from dealer_in_org where orgId =" + id);
			OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
					"select Top 1 * from owner_in_org where orgId =" + id);
			if (role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

				List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
						"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
								+ "(SELECT  dealerId FROM dealer_in_org WHERE orgId IN  (" + id + ")))"));
				String ids = Arrays.toString(dealerSite.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = hql.replace("group by ", "WHERE  s.siteId In (" + ids + ") group by ");

			} else if (dealer != null) {
				List<Long> longList = stationDao.getSiteIdBasedOnRole(Roles.DealerAdmin.toString(),
						dealer.getDealerId());
				System.out.println("list of ids based on dealer" + longList);
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = hql.replace("group by ", "WHERE  s.siteId In (" + ids + ") group by ");

			}

			else if (owner != null) {
				List<Long> longList = stationDao.getSiteIdBasedOnRole(Roles.Owner.toString(), owner.getOwnerId());
				System.out.println("list of ids based on owner" + longList);
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				hql = hql.replace("group by ", "WHERE  s.siteId In (" + ids + ") group by ");

			}

		}
		return generalDao.findAliasData(hql);
	}

}
