package com.evgateway.server.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.DealerInOrg;
import com.evgateway.server.pojo.OwnerInOrg;
import com.evgateway.server.services.UserService;

@Repository
public class StationDaoImpl<I> extends DaoImpl<BaseEntity, I> implements StationDao<BaseEntity, I> {

	private final Logger LOGGER = LoggerFactory.getLogger(StationDaoImpl.class);

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Autowired
	private UserService userService;

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getStationIdBasedOnRole(String roleName, Long userId) {
		// TODO Auto-generated method stub

		LOGGER.info("StationDaoImpl.getStationIdBasedOnRole() - id: [" + roleName + "]");

		List<Long> longList = new ArrayList<>();

		if (roleName.equalsIgnoreCase(Roles.Admin.toString())) {

			List<Long> adminSite = convertToLongList(
					(List<BigDecimal>) generalDao.findAllSingalObject("Select id From station"));
			longList.addAll(adminSite);

		} else if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

			List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
							+ userId + ")"));

			String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
			ids = ids.isEmpty() ? "0" : ids;

			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					" Select Distinct id From station Where siteId In (SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
							+ "(SELECT  dealerId FROM dealer_in_org WHERE orgId IN  (" + ids + "))))"));
			longList.addAll(dealerSite);

		} else if (roleName.equalsIgnoreCase(Roles.DealerAdmin.toString())) {

			List<Long> dealerAdminSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"Select Distinct id From station Where siteId In (SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId ="
							+ userId + " )) "));

			longList.addAll(dealerAdminSite);

		} else if (roleName.equalsIgnoreCase(Roles.Dealer.toString())) {

			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao
					.findAllSingalObject("Select Distinct id From station Where siteId IN\r\n"
							+ " (SELECT siteId FROM site WHERE org IN ( SELECT orgId FROM owner_in_org WHERE ownerId IN (\r\n"
							+ " SELECT ownerId FROM owner_in_dealer WHERE dealerId =" + userId + "))) "));

			longList.addAll(dealerSite);

		} else if (roleName.equalsIgnoreCase(Roles.Owner.toString())) {

			List<Long> ownerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"Select Distinct id From station Where siteId In (SELECT siteId FROM users_in_sites WHERE userId="
							+ userId + ")"));

			longList.addAll(ownerSite);

		} else if (roleName.equalsIgnoreCase(Roles.SiteHost.toString())) {

			List<Long> siteHostSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"Select Distinct id From station Where siteId In (SELECT siteId FROM users_in_sites WHERE userId="
							+ userId + ")"));

			longList.addAll(siteHostSite);
		} else if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {

			List<Long> manusite = convertToLongList((List<BigDecimal>) generalDao
					.findAllSingalObject("Select Distinct id From station s where s.manufacturerId in(select "
							+ "manufacturerId from user_in_manufacturer where userId=" + userId + ")"));
			longList.addAll(manusite);

		} else if (roleName.equalsIgnoreCase(Roles.Utility.toString())) {

			String sql = "Select Distinct id From station where siteid in (select  s.siteId from site s inner join site_utils su on su.siteId = s.siteId "
					+ "inner join user_in_utility uiu on uiu.name = su.utility_name  OR uiu.name = su.reportagency_name where uiu.userId = "
					+ userId + ")";

			List<Long> manusite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(sql));
			longList.addAll(manusite);

		}
		return longList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getStationsBasedOnSiteIds(String siteId) {
		// TODO Auto-generated method stub
		List<Long> longList = new ArrayList<>();

		List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao
				.findAllSingalObject("select st.id from station st where st.siteid=" + siteId));

		longList.addAll(dealerSite);
		return longList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedOnRole(String roleName, Long userId) {

		LOGGER.info("StationDaoImpl.getSiteIdBasedOnRole() - id: [" + roleName + "," + userId + "]");

		List<Long> longList = new ArrayList<>();

		if (roleName.equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {

			List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
							+ userId + ")"));

			String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
			ids = ids.isEmpty() ? "0" : ids;

			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT Distinct siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
							+ "(SELECT  dealerId FROM dealer_in_org WHERE orgId IN  (" + ids + ")))"));
			longList.addAll(dealerSite);

		} else if (roleName.equalsIgnoreCase(Roles.Admin.toString())
				|| roleName.equalsIgnoreCase(Roles.Support.toString())
				|| roleName.equalsIgnoreCase(Roles.Accounts.toString())) {
			List<Long> adminSite = convertToLongList(
					(List<BigDecimal>) generalDao.findAllSingalObject("Select siteId From site"));
			longList.addAll(adminSite);

		} else if (roleName.equalsIgnoreCase(Roles.DealerAdmin.toString())) {
			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT Distinct siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId ="
							+ userId + " )"));

			longList.addAll(dealerSite);

		}

		else if (roleName.equalsIgnoreCase(Roles.Dealer.toString())) {

			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT Distinct siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId ="
							+ userId + " )"));

			longList.addAll(dealerSite);

		} else if (roleName.equalsIgnoreCase(Roles.Owner.toString())) {
			List<Long> ownerSite = convertToLongList((List<BigDecimal>) generalDao
					.findAllSingalObject("SELECT Distinct siteId FROM users_in_sites WHERE userId=" + userId));
			longList.addAll(ownerSite);

		} else if (roleName.equalsIgnoreCase(Roles.SiteHost.toString())) {

			List<Long> siteHostSite = convertToLongList((List<BigDecimal>) generalDao
					.findAllSingalObject("SELECT Distinct siteId From users_in_sites Where userId = " + userId));

			longList.addAll(siteHostSite);

		} else if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {

			String sql = "select Distinct s.siteId from station stn inner join site s on stn.siteid=s.siteid "
					+ "inner join user_in_manufacturer um on um.manufacturerId=stn.manufacturerId  where um.userId="
					+ userId;
			List<Long> siteHostSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(sql));

			longList.addAll(siteHostSite);

		} else if (roleName.equalsIgnoreCase(Roles.Utility.toString())) {

			String sql = "select Distinct s.siteId from site s inner join site_utils su on su.siteId = s.siteId "
					+ "inner join user_in_utility uiu on uiu.name = su.utility_name  OR uiu.name = su.reportagency_name where uiu.userId = "
					+ userId;
			List<Long> siteHostSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(sql));

			longList.addAll(siteHostSite);

		}

		return longList;

	}

	private List<Long> convertToLongList(List<BigDecimal> bigDecimalList) {

		List<Long> longs = bigDecimalList.stream().map(BigDecimal::longValue).collect(Collectors.toList());

		return longs;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getOgrIdBasedOnRole(String roleName) {
		// TODO Auto-generated method stub
		LOGGER.info("StationDaoImpl.getOgrIdBasedOnRole() - id: [" + roleName + "]");

		List<Long> longList = new ArrayList<>();

		if (roleName.equalsIgnoreCase(Roles.Admin.toString())) {

			List<Long> adminOrg = convertToLongList(
					(List<BigDecimal>) generalDao.findAllSingalObject("SELECT id FROM organization Where id !=1"));
			longList.addAll(adminOrg);

		} else if (roleName.equalsIgnoreCase(Roles.DealerAdmin.toString())) {

			List<Long> dealerAdminOrg = convertToLongList(
					(List<BigDecimal>) generalDao.findAllSingalObject("SELECT orgId FROM dealer_in_org"));

			longList.addAll(dealerAdminOrg);

		} else if (roleName.equalsIgnoreCase(Roles.Owner.toString())) {

			List<Long> ownerAdminOrg = convertToLongList(
					(List<BigDecimal>) generalDao.findAllSingalObject("SELECT orgId FROM owner_in_org"));

			longList.addAll(ownerAdminOrg);

		}
		return longList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedMultipleOrg(Long orgId, Long orgId1) throws UserNotFoundException {
		// TODO Auto-generated method stub

		LOGGER.info("StationDaoImpl.getStationIdBasedOnRole() - id: [" + orgId + "]");

		List<Long> longList = new ArrayList<>();

		OwnerInOrg owner = generalDao.findOneSQLQuery(new OwnerInOrg(),
				"select Top 1 * from owner_in_org where orgId in (" + orgId + ", " + orgId1 + ")");
		DealerInOrg dealer = generalDao.findOneSQLQuery(new DealerInOrg(),
				"select Top 1 * from dealer_in_org where orgId in (" + orgId + ", " + orgId1 + ")");
		System.out.println("dealerorg---->" + dealer);

		if (owner != null) {
			List<Long> ownerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_org WHERE ownerId IN "
							+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId in (" + orgId + ", " + orgId1
							+ ")))"));

			longList.addAll(ownerSite);
			System.out.println("siteid based om owner" + longList);
		}

		if (dealer != null) {
			List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
					"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
							+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId in (" + orgId + "," + orgId1
							+ ")))"));

			longList.addAll(dealerSite);
			System.out.println("siteid based om dealer" + longList);
		}
		return longList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedOnOrg(Long orgId) throws UserNotFoundException {
		// TODO Auto-generated method stub

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

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedOnDiffOrg(String network) {
		// TODO Auto-generated method stub

		LOGGER.info("StationDaoImpl.getStationIdBasedOnRole() - id: [" + network + "]");

		List<Long> longList = new ArrayList<>();

		List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
						+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId in (" + network + ")))"));

		longList.addAll(dealerSite);

		return longList;

	}

	@Override
	public List<Long> getOrgIdsOfGroupDealerAdmin(Long id) throws UserNotFoundException {
		@SuppressWarnings("unchecked")

		List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
						+ id + ")"));

		return dealerorgIds;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedOnGrpDealerAdminId(Long userId) throws UserNotFoundException {
		// TODO Auto-generated method stub

		LOGGER.info("StationDaoImpl.getSiteIdBasedOnGrpDealerAdminId() - id: [" + userId + "]");

		List<Long> dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"select dealerorgId from group_in_dealer where groupId in (select orgId from users_in_org where userid="
						+ userId + ")"));
		String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
		ids = ids.isEmpty() ? "0" : ids;

		List<Long> dealerSite = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"SELECT siteId From users_in_sites Where userId IN (SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
						+ "(SELECT  dealerId FROM dealer_in_org WHERE orgId IN  (" + ids + ")))"));

		return dealerSite;

	}

	@Override
	public List<Long> getStationsBasedOnSite(Long siteId) {
		// TODO Auto-generated method stub
		LOGGER.info("StationDaoImpl.getStationsBasedOnSite() - id: [" + siteId + "]");

		List<Long> longList = new ArrayList<>();

		@SuppressWarnings("unchecked")
		List<Long> staions = convertToLongList(
				(List<BigDecimal>) generalDao.findAllSingalObject("SELECT id From station Where siteId =" + siteId));

		longList.addAll(staions);

		return longList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getOwnerIdBasedOnOrg(long orgId) {
		// TODO Auto-generated method stub

		LOGGER.info("StationDaoImpl.getStationIdBasedOnRole() - id: [" + orgId + "]");

		List<Long> orgList = new ArrayList<>();

		List<Long> ownerOrg = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"Select orgId from owner_in_org oo where oo.ownerId in (select ownerId from owner_in_dealer o where o.dealerId in  (Select d.dealerId from dealer_in_org d where o.dealerId IN "
						+ "(SELECT TOP 1 userId FROM users_in_org WHERE orgId =" + orgId + ")))"));

		orgList.addAll(ownerOrg);

		return orgList;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getSiteIdBasedOnFleetManagerId(Long id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("StationDaoImpl.getSiteIdBasedOnFleetManagerId() - id: [" + id + "]");

		List<Long> orgList = new ArrayList<>();

		List<Long> ownerOrg = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"Select siteid from fleetgroup_in_site where fleetGroupId in (select id from fleet_group where fleetMangerId = "
						+ id + ")"));

		orgList.addAll(ownerOrg);

		return orgList;
	}

	@Override
	public List<Long> getSiteIdBasedOnAdminIdForFleet(Long id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("StationDaoImpl.getSiteIdBasedOnAdminIdForFleet() - id: [" + id + "]");

		List<Long> orgList = new ArrayList<>();

		List<Long> ownerOrg = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
				"Select siteid from fleetgroup_in_site where fleetGroupId in (select id from fleet_group )"));

		orgList.addAll(ownerOrg);

		return orgList;
	}

	@Override
	public Boolean isTure(Long id, Long orgId, String role, Long userId, String type) {
		// TODO Auto-generated method stub

		System.out.println(id);
		if (type.equals("Station")) {
			List<Long> stationIdBasedOnRole = getStationIdBasedOnRole(role, userId);
			return new HashSet<>(stationIdBasedOnRole).contains(id);
		} else if (type.equals("Site")) {
			List<Long> siteIdBasedOnRole = getSiteIdBasedOnRole(role, userId);
			System.out.println(siteIdBasedOnRole);
			System.out.println(new HashSet<>(siteIdBasedOnRole).contains(id));
			return new HashSet<>(siteIdBasedOnRole).contains(id);

		}
		return false;
	}

}
