package com.evgateway.server.dao.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.DataTableDao;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.dao.TableSearchDao;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.Error;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;
import com.evgateway.server.services.UserService;

@Repository
public class TableSearchDaoIml<I> extends DaoImpl<BaseEntity, I> implements TableSearchDao<BaseEntity, I> {

	@Autowired
	private UserDao<?, ?> userDao;
	@Autowired
	private UserService userService;

	@Autowired
	private DataTableDao<?, ?> dataTableDao;

	@Autowired
	private StationDao<?, ?> stationDao;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> filterWithUser(String text, String roleName, Pageable pageable, T newEntity,
			String useruid) throws InterruptedException, UserNotFoundException, ServerException {
		String hql = "";

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Role role = userDao.getCurrentRole();

		if (role.getRolename().equals(Roles.SuperAdmin.toString())) {

			if (roleName.equals(Roles.Admin.toString())) {
				hql = "SELECT distinct u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status "
						+ " From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Admin')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR u.email LIKE '%" + text.trim() + "%') ";

			}
		}

		else if (role.getRolename().equals(Roles.Admin.toString())) {

			if (roleName.equals(Roles.GroupDealerAdmin.toString())) {
				hql = "SELECT u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,"
						+ " oo.orgName as groupadmin,"
						+ " u.creationDate, a.phone,  p.status From Users u INNER JOIN address a On a.user_id = u.userId "
						+ " inner join organization oo on oo.id=u.orgid INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, "
						+ " Role r where u.userId =ur.user_id and ur.role_id=r.id "
						+ " AND r.rolename IN ('GroupDealerAdmin')  " + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email "
						+ "LIKE '%" + text.trim() + "%' or oo.orgName Like '%" + text.trim() + "%')";

			}

			else if (roleName.equals(Roles.DealerAdmin.toString())) {
				hql = "SELECT distinct u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId = u.userId)) AS orgName "
						+ " From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('DealerAdmin')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%' OR u.email LIKE '%" + text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%' ) GROUP BY u.userId, u.enabled,u.email, u.orgId,u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Dealer.toString())) {
				hql = "SELECT distinct u.userId AS id,u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (Select orgName from organization where id in(SELECT orgid FROM Users WHERE userId IN "
						+ "(SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId) )) AS dealerName,"

						+ " isNull((SELECT orgName FROM organization WHERE id IN "
						+ "(SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)),'-')"
						+ " AS orgName " + " From Users u INNER JOIN address a On a.user_id = u.userId "
						+ "INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Dealer')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%'  OR u.email LIKE '%" + text.trim()
						+ "%' OR (SELECT (firstName +' ' +lastName) FROM Users WHERE userId IN (SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId IN "
						+ "(SELECT dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId))) LIKE '%"
						+ text.trim()
						+ "%' or (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)) LIKE '%"
						+ text.trim() + "%' )"
						+ " GROUP BY u.userId, u.enabled,u.email, u.orgId,u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Owner.toString())) {
				hql = "SELECT distinct u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName, u.creationDate,a.phone, p.status, "
						+ " (SELECT orgName FROM organization WHERE id = u.orgId) AS dealerOrg,"
						+ " isnull((select Distinct orgname from organization where id  in(select top 1 orgid"
						+ " from enterpriseuser_in_org where enterpriseId "
						+ " in(select  dealerid from owner_in_dealer where ownerId in(u.userId )))),'-') as dealerorgs ,"
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId = u.userId)) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId "
						+ " INNER JOIN profile p ON p.user_id = u.userId"
						+ " ,Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Owner')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%' OR u.email LIKE '%" + text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id = u.orgId) LIKE '%" + text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email,u.orgId, u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.SiteHost.toString())) {
				hql = "SELECT distinct  u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId IN (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p ON p.user_id= u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('SiteHost') "
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%' OR u.email LIKE '%" + text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId IN (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email, u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Driver.toString())) {

				hql = "SELECT distinct u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,oo.orgName,u.creationDate, a.phone, ac.accountBalance,ISNULL(ac.currencyType ,'USD' ) AS currencySym,p.status From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN accounts ac On ac.user_id = u.userId "
						+ "INNER JOIN profile p On p.user_id = u.userId Inner join organization oo on u.orgId =oo.id "
						+ "  INNER JOIN creadential c ON c.account_id= ac.id,"
						+ " Usersinroles ur, Role r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('Driver')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR a.phone LIKE '%"
						+ text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim()
						+ "%'  OR u.email LIKE '%" + text.trim() + "%' or  ac.digitalId LIKE '%" + text.trim()
						+ "%' OR c.rfidHex LIKE '%" + text.trim() + "%')"
						+ "GROUP BY u.UserId,u.enabled,u.email, u.firstName, u.lastName,oo.orgName, a.phone, ac.accountBalance,ac.currencyType, p.status,u.creationDate";

			} else if (roleName.equals(Roles.Manufacturer.toString())) {

				hql = "SELECT distinct u.userId AS id,u.email, u.firstName, u.lastName, a.phone, p.status ,u.creationDate, m.manfname  "
						+ "from Users u INNER JOIN address a On a.user_id = u.userId   "
						+ "INNER JOIN profile p ON p.user_id = u.userId  "
						+ "INNER JOIN user_in_manufacturer um ON um.userId = u.userId "
						+ "INNER JOIN manufacturer m ON m.id = um.manufacturerId , "
						+ "Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Manufacturer')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%'  OR  u.email LIKE '%" + text.trim() + "%' OR m.manfname LIKE '%"
						+ text.trim() + "%')";

			} else if (roleName.equals(Roles.Accounts.toString())) {
				hql = "SELECT distinct u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status "
						+ "From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, Role"
						+ " r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('Accounts')  "
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%'  OR u.email " + "LIKE '%" + text.trim() + "%' )";

			} else if (roleName.equals(Roles.FleetManager.toString())) {
				hql = "SELECT u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate,"
						+ " a.phone, p.status,fg.companyName  "
						+ "From Users u INNER JOIN address a On a.user_id = u.userId"
						+ " INNER JOIN fleet_in_owner fg ON fg.userId= u.userid"
						+ " INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, Role"
						+ " r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('FleetManager')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%' OR fg.companyName LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR u.email "
						+ "LIKE '%" + text.trim() + "%' OR p.status " + "LIKE '%" + text.trim() + "%' )";

			} else if (roleName.equals(Roles.Utility.toString())) {
				hql = "SELECT u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone,  p.status From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, Role r where u.userId =ur.user_id and ur.role_id=r.id AND u.orgId="
						+ user.getOrgId() + " AND r.rolename IN ('Utility')  " + " AND (u.firstName LIKE '%"
						+ text.trim() + "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%"
						+ text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim()
						+ "%'  OR u.email " + "LIKE '%" + text.trim() + "%')";

			} else if (roleName.equals(Roles.FleetDriver.toString())) {
				hql = "SELECT u.userId AS id,a.id as addressId, u.email,ISNULL(ac.currencyType ,'USD' ) AS currencySym, u.firstName ,"
						+ "ac.accountBalance, isNull(u.lastName,'') as lastName,u.creationDate, a.phone , p.status From Users u "
						+ "INNER JOIN address a On a.user_id = u.userId "
						+ "INNER JOIN accounts ac On ac.user_id = u.userId Inner join organization oo on u.orgId =oo.id "
						+ "INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur,"
						+ " Role r where u.userId =ur.user_id and ur.role_id=r.id and r.rolename in('FleetDriver')"
						+ " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR a.phone LIKE '%" + text.trim() + "%'  OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%'  or u.email " + "LIKE '%" + text.trim() + "%')";

			} else if (roleName.equals(Roles.Support.toString())) {
				hql = "SELECT u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone,  p.status From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, Role r where u.userId =ur.user_id and ur.role_id=r.id "
						+ " AND r.rolename IN ('Support')  " + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email "
						+ "LIKE '%" + text.trim() + "%')";

			} else if (roleName.equals(Roles.Maintenance.toString())) {
				hql = "SELECT u.userId AS id, u.email,oo.orgName, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS"
						+ " lastName,u.creationDate, a.phone,  p.status From Users u "
						+ " INNER JOIN address a On a.user_id = u.userId "
						+ " INNER JOIN maintainance_in_org mo On mo.mantainceId = u.userId "
						+ " INNER JOIN organization oo On oo.id = mo.orgId "
						+ " INNER JOIN profile p On p.user_id = u.userId, Usersinroles ur, "
						+ "Role r where u.userId =ur.user_id and ur.role_id=r.id "
						+ " AND r.rolename IN ('Maintenance')  " + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR oo.orgName LIKE  '%" + text.trim()
						+ "%'  OR a.phone LIKE '%" + text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%"
						+ text.trim() + "%'  OR u.email " + "LIKE '%" + text.trim() + "%')";

			}
		} else if (role.getRolename().equals(Roles.DealerAdmin.toString())) {

			if (roleName.equals(Roles.Dealer.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "Dealer");

				hql = "SELECT distinct u.userId AS id,u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (select orgName from organization where id in(SELECT orgid FROM Users WHERE userId IN (SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId) )) AS dealerName,"
						+ " isNull((SELECT orgName FROM organization WHERE id IN "
						+ "(SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)),'-')"
						+ " AS orgName "
						+ " From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and"
						+ " u.userId In (" + ids + ") and r.rolename in ('Dealer')" + " AND (u.firstName LIKE '%"
						+ text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR a.phone LIKE '%"
						+ text.trim() + "%' OR u.email LIKE '%" + text.trim()
						+ "%' OR (SELECT (firstName +' ' +lastName) FROM Users WHERE userId IN (SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId) ) LIKE  '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId IN (SELECT dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId))) LIKE  '%"
						+ text.trim()
						+ "%' or (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email,u.orgId, u.firstName, u.lastName, a.phone , p.status,u.creationDate ";

			} else if (roleName.equals(Roles.Owner.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "Owner");

				hql = "SELECT distinct u.userId AS id,u.email, u.firstName, u.lastName, a.phone,u.creationDate, p.status, "
						+ " (SELECT orgName FROM organization WHERE id = u.orgId) AS dealerOrg,"
						+ " isnull((select Distinct orgname from organization where id  in(select top 1 orgid"
						+ " from enterpriseuser_in_org where enterpriseId "
						+ " in(select  dealerid from owner_in_dealer where ownerId in(u.userId )))),'-') as dealerorgs, "
						+ " (SELECT orgName FROM organization WHERE id IN (select orgId from owner_in_org where ownerId =u.UserId)) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId "
						+ " INNER JOIN profile p ON p.user_id = u.userId"
						+ " ,Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and u.userId In ("
						+ ids + ") and  r.rolename in('Owner')" + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email LIKE '%"
						+ text.trim() + "%' OR (SELECT orgName FROM organization WHERE id = u.orgId) LIKE  '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId = u.userId)) LIKE  '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email, u.orgId,u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.SiteHost.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "SiteHost");

				hql = "SELECT u.userId AS id, p.status,u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone,  "
						+ " (SELECT orgName FROM organization WHERE id IN  "
						+ " (SELECT orgId FROM users_in_org WHERE userId IN  "
						+ " (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) AS orgName "
						+ " from Users u  " + " INNER JOIN address a On a.user_id = u.userId  "
						+ " Inner Join profile p ON p.user_id=u.userId  " + " , Usersinroles ur,Role r  "
						+ "  where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('SiteHost')  AND u.userId In ("
						+ ids + ")" + " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%"
						+ text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR u.email LIKE '%"
						+ text.trim()
						+ "%' OR  (SELECT orgName FROM organization WHERE id IN   (SELECT orgId FROM users_in_org WHERE userId IN   (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) LIKE  '%"
						+ text.trim()
						+ "%')  Group BY u.userId,u.email, u.firstName,u.orgId, u.lastName, a.phone ,p.status,u.creationDate";

			} else if (roleName.equals(Roles.Driver.toString())) {

				hql = "SELECT distinct u.userId AS id, u.email,ISNULL(u.firstName,'') AS firstName,oo.orgName,u.creationDate,"
						+ " ISNULL(u.lastName,'') AS lastName, a.phone, ac.accountBalance,ISNULL(ac.currencyType ,'USD' ) "
						+ " AS currencySym, p.status From Users u INNER JOIN address a On a.user_id = u.userId "
						+ " INNER JOIN accounts ac On ac.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId "
						+ " Inner join organization oo on u.orgId =oo.id"
						+ " INNER JOIN creadential c ON c.account_id= ac.id, Usersinroles ur, "
						+ " Role r where u.userId =ur.user_id and ur.role_id=r.id AND u.orgId In (" + user.getOrgId()
						+ ")" + " AND r.rolename IN ('Driver')" + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR a.phone LIKE '%"
						+ text.trim() + "%' OR u.email LIKE '%" + text.trim() + "%' or  ac.digitalId LIKE '%"
						+ text.trim() + "%' OR c.rfidHex LIKE '%" + text.trim() + "%')  "
						+ "GROUP BY u.UserId,u.enabled,u.email, u.firstName,u.creationDate, u.lastName, oo.orgName,a.phone, ac.accountBalance,ac.currencyType, p.status";

			}
		} else if (role.getRolename().equals(Roles.Dealer.toString())) {

			if (roleName.equals(Roles.Owner.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "Owner");

				hql = "SELECT distinct u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status, "
						+ " (SELECT orgName FROM organization WHERE id = u.orgId) AS dealerOrg,"
						+ " isnull((select Distinct orgname from organization where id  in(select top 1 orgid"
						+ " from enterpriseuser_in_org where enterpriseId "
						+ " in(select  dealerid from owner_in_dealer where ownerId in(u.userId )))),'-') as dealerorgs, "
						+ " (SELECT orgName FROM organization WHERE id IN (select orgId from owner_in_org where ownerId =u.UserId)) AS orgName "
						+ " FROM  Users u INNER JOIN address a On a.user_id = u.userId    "
						+ " INNER JOIN profile p ON p.user_id = u.userId"
						+ " ,Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and "
						+ "r.rolename in('Owner') AND u.userId In (" + ids + ") AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR u.email LIKE '%"
						+ text.trim() + "%' OR  (SELECT orgName FROM organization WHERE id = u.orgId) LIKE  '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (select orgId from owner_in_org where ownerId =u.UserId)) LIKE  '%"
						+ text.trim()
						+ "%') GROUP BY u.userId,u.orgId, u.enabled,u.email, u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			}

		} else if (role.getRolename().equals(Roles.Owner.toString())) {

			if (roleName.equals(Roles.SiteHost.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "SiteHost");

				hql = "SELECT distinct u.userId AS id, p.status,u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone,  "
						+ " (SELECT orgName FROM organization WHERE id IN  "
						+ " (SELECT orgId FROM users_in_org WHERE userId IN  "
						+ " (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId  "
						+ " Inner Join profile p ON p.user_id=u.userId  " + ", Usersinroles ur,Role r  "
						+ "  where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('SiteHost') AND u.userId In ("
						+ ids + ")" + " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%"
						+ text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN  (SELECT TOP 1 orgId FROM users_in_org WHERE userId IN  (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) LIKE '%"
						+ text.trim()
						+ "%') Group BY u.userId,u.email,u.orgId, u.firstName, u.lastName, a.phone ,p.status,u.creationDate";
			}
		} else if (role.getRolename().equals(Roles.GroupDealerAdmin.toString())) {

			if (roleName.equals(Roles.DealerAdmin.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "DealerAdmin");

				hql = "SELECT u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId = u.userId)) AS orgName "
						+ " From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename  in('DealerAdmin') AND u.userId IN ("
						+ ids + ")  " + " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%"
						+ text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%'OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%' ) GROUP BY u.userId, u.enabled,u.email, u.orgId,u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Dealer.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "Dealer");

				hql = "SELECT u.userId AS id,u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (select orgName from organization where id in (SELECT orgid FROM Users WHERE userId IN (SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId) )) AS dealerName,"
						+ " isNull((SELECT orgName FROM organization WHERE id IN "
						+ "(SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)),'-')"
						+ " AS orgName "
						+ " From Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p On p.user_id = u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Dealer') AND u.userId IN ("
						+ ids + ")  " + " AND (u.firstName LIKE '%" + text.trim() + "%' OR u.lastName LIKE  '%"
						+ text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR u.email LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT (firstName +' ' +lastName) FROM Users WHERE userId IN (SELECT TOP 1 dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM dealer_in_org WHERE dealerId IN (SELECT dealerAdminId FROM dealer_in_dealerAdmin WHERE dealerId = u.userId))) LIKE '%"
						+ text.trim()
						+ "%' or (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM enterpriseuser_in_org WHERE enterpriseId =u.userid)) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email, u.orgId,u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Owner.toString())) {

				String ids = getUserIdBasedOnRole(user.getId(), "Owner");

				hql = "SELECT u.userId AS id,u.email,ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName, u.creationDate,a.phone, p.status, "
						+ " (SELECT orgName FROM organization WHERE id = u.orgId) AS dealerOrg,"
						+ " isnull((select Distinct orgname from organization where id  in(select top 1 orgid"
						+ " from enterpriseuser_in_org where enterpriseId "
						+ " in(select dealerid from owner_in_dealer where ownerId in(u.userId )))),'-') as dealerorgs, "
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId = u.userId)) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId "
						+ " INNER JOIN profile p ON p.user_id = u.userId"
						+ " ,Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('Owner') "
						+ " AND u.userId IN (" + ids + ") " + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR u.email LIKE '%"
						+ text.trim() + "%' OR (SELECT orgName FROM organization WHERE id = u.orgId) LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId = u.userId)) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email,u.orgId, u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.SiteHost.toString())) {
				String ids = getUserIdBasedOnRole(user.getId(), "SiteHost");
				hql = "SELECT u.userId AS id, u.email, ISNULL(u.firstName,'') AS firstName, ISNULL(u.lastName,'') AS lastName,u.creationDate, a.phone, p.status,"
						+ " (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId IN (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) AS orgName "
						+ " from Users u INNER JOIN address a On a.user_id = u.userId INNER JOIN profile p ON p.user_id= u.userId,"
						+ " Usersinroles ur,Role r where u.userId =ur.user_id  and ur.role_id=r.id and r.rolename in('SiteHost') "
						+ "AND u.userId IN (" + ids + ")  " + " AND (u.firstName LIKE '%" + text.trim()
						+ "%' OR u.lastName LIKE  '%" + text.trim() + "%' OR a.phone LIKE '%" + text.trim()
						+ "%'OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%'  OR u.email LIKE '%"
						+ text.trim()
						+ "%' OR (SELECT orgName FROM organization WHERE id IN (SELECT orgId FROM owner_in_org WHERE ownerId IN (SELECT ownerId FROM  siteHost_in_owner WHERE sitehostId IN (u.userId)))) LIKE '%"
						+ text.trim()
						+ "%') GROUP BY u.userId, u.enabled,u.email, u.firstName, u.lastName, a.phone , p.status,u.creationDate";

			} else if (roleName.equals(Roles.Driver.toString())) {
				String ids = getUserIdBasedOnRole(user.getId(), "Driver");
				hql = "SELECT u.userId AS id, u.email,ISNULL(u.firstName,'') AS firstName,u.creationDate,oo.orgName, ISNULL(u.lastName,'') AS lastName,"
						+ " a.phone, ac.accountBalance,ISNULL(ac.currencyType ,'USD' ) AS currencySym, p.status From Users u"
						+ " INNER JOIN address a On a.user_id = u.userId INNER JOIN accounts ac On ac.user_id = u.userId "
						+ "INNER JOIN profile p On p.user_id = u.userId Inner join organization oo on u.orgId =oo.id"
						+ " INNER JOIN creadential c ON c.account_id= ac.id, Usersinroles ur, Role r where u.userId =ur.user_id and ur.role_id=r.id AND"
						+ " r.rolename  IN ('Driver') AND u.orgId IN (" + ids + ")  AND (u.firstName LIKE '%"
						+ text.trim() + "%' OR u.lastName LIKE  '%" + text.trim()
						+ "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim() + "%' OR a.phone LIKE '%"
						+ text.trim() + "%' OR CONCAT(u.firstName, ' ', u.lastName) LIKE '%" + text.trim()
						+ "%' OR u.email LIKE '%" + text.trim() + "%' or  ac.digitalId LIKE '%" + text.trim()
						+ "%' OR c.rfidHex LIKE '%" + text.trim() + "%')  "
						+ "GROUP BY u.UserId,u.enabled,u.email, u.firstName,u.creationDate,oo.orgName, u.lastName, a.phone, ac.accountBalance,ac.currencyType, p.status";

			}

		}
		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, hql);

		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.USER_NOT_EXIST.toString(),
					Integer.toString(Error.USER_NOT_EXIST.getCode()));

		return pagedResult;

	}

	private List<Long> convertToLongList(List<BigDecimal> bigDecimalList) {

		List<Long> longs = bigDecimalList.stream().map(BigDecimal::longValue).collect(Collectors.toList());

		return longs;
	}

	public <T> Page<T> getPage(Collection<T> content, Pageable pageable, Long object) {

		return new PageImpl<T>((List<T>) content, pageable, object);

	}

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> filterStationwithStatus(String id, String text, String status, Pageable pageable,
			T newEntity, String useruid) throws InterruptedException, UserNotFoundException, ServerException {

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Role role = userDao.getCurrentRole();

		String hql = "";

		hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
				+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
				+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId  WHERE s.siteId=" + status
				+ " AND s.preProduction = 0 AND (s.referNo LIKE '%" + text.trim() + "%' OR s.stationName LIKE '%"
				+ text.trim() + "%' OR st.siteName LIKE '" + text.trim() + "%' OR s.stationAvailStatus LIKE '%"
				+ text.trim() + "%' OR s.evseType LIKE '%" + text.trim() + "%')"
				+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
				+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp  ";

		if (status.equals("Available"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId "
					+ " WHERE DATEADD(minute, -15 ,GETUTCDATE()) < s.stationTimeStamp AND s.stationAvailStatus='Active'  "
					+ " AND (s.referNo LIKE '%" + text.trim() + "%' OR s.stationName LIKE '%" + text.trim()
					+ "%' OR st.siteName LIKE '%" + text.trim() + "%' OR s.stationAvailStatus LIKE '%" + text.trim()
					+ "%' OR s.evseType LIKE '%" + text.trim() + "%')"
					+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";
		else if (status.equalsIgnoreCase("undefined") || status.equalsIgnoreCase("SiteHost")
				|| status.equalsIgnoreCase("DealerAdmin") || status.equalsIgnoreCase("Dealer")
				|| status.equalsIgnoreCase("Owner") || status.equalsIgnoreCase("Manufacturer"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId where (s.referNo LIKE '%"
					+ text.trim() + "%' OR s.stationName LIKE '%" + text.trim() + "%' OR st.siteName LIKE '%"
					+ text.trim() + "%' OR s.stationAvailStatus LIKE '%" + text.trim() + "%' OR s.evseType LIKE '%"
					+ text.trim() + "%'"
					+ " ) GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";

		else if (status.equals("UnAvailable"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId "
					+ " WHERE DATEADD(minute, -15 ,GETUTCDATE()) > s.stationTimeStamp AND s.stationAvailStatus='Active' "
					+ " AND (s.referNo LIKE '%" + text.trim() + "%' OR s.stationName LIKE '%" + text.trim()
					+ "%' OR st.siteName LIKE '%" + text.trim() + "%' OR s.stationAvailStatus LIKE '%" + text.trim()
					+ "%' OR s.evseType LIKE '%" + text.trim() + "%')"
					+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";

		else if (status.equals("Maintenance"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId "
					+ " WHERE s.stationAvailStatus='Maintenance' " + " AND (s.referNo LIKE '%" + text.trim()
					+ "%' OR s.stationName LIKE '%" + text.trim() + "%' OR st.siteName LIKE '%" + text.trim()
					+ "%' OR s.stationAvailStatus LIKE '%" + text.trim() + "%' OR s.evseType LIKE '%" + text.trim()
					+ "%')"
					+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";

		else if (status.equals("Decommission"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId  "
					+ " WHERE s.stationAvailStatus='Disable' " + " AND (s.referNo LIKE '%" + text.trim()
					+ "%' OR s.stationName LIKE '%" + text.trim() + "%' OR st.siteName LIKE '%" + text.trim()
					+ "%' OR s.stationAvailStatus LIKE '%" + text.trim() + "%' OR s.evseType LIKE '%" + text.trim()
					+ "%')"
					+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";

		else if (status.equals("ComingSoon"))
			hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
					+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
					+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId "
					+ " WHERE s.stationAvailStatus='Not Installed' " + " AND (s.referNo LIKE '%" + text.trim()
					+ "%' OR s.stationName LIKE '%" + text.trim() + "%' OR st.siteName LIKE '" + text.trim()
					+ "%' OR s.stationAvailStatus LIKE '%" + text.trim() + "%' OR s.evseType LIKE '%" + text.trim()
					+ "%')"
					+ " GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
					+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp ";

		if (!(role.getRolename().equalsIgnoreCase(Roles.Admin.toString()))) {
			if (id.equalsIgnoreCase("All")) {
				id = String.valueOf(user.getId());
			}
			List<Long> longList = stationDao.getSiteIdBasedOnRole(role.getRolename(), Long.parseLong(id));
			String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

			ids = ids.isEmpty() ? "0" : ids;

			if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())) {
				hql = hql.replace(" s.siteId  WHERE",
						" s.siteId  inner join user_in_manufacturer um on um.manufacturerId=s.manufacturerId WHERE um.userId="
								+ user.getId() + " and  s.siteId In(" + ids + ") AND ");
			} else if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())
					&& status.equalsIgnoreCase("undefined")) {
				hql = hql.replace("where",
						" s.siteId  inner join user_in_manufacturer um on um.manufacturerId=s.manufacturerId WHERE um.userId="
								+ user.getId() + " and  s.siteId In(" + ids + ")  and  ");
			} else if (status.equalsIgnoreCase("undefined")) {
				hql = hql.replace("where", "   WHERE s.siteId In(" + ids + ") and  ");
			} else {
				hql = hql.replace("GROUP ", " and s.siteId In(" + ids + ") GROUP ");
			}

		}

		System.out.println("hql qury" + hql);

		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, hql);
		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.STATION_NOT_EXIST.toString(),
					Integer.toString(Error.STATION_NOT_EXIST.getCode()));
		return pagedResult;
	}

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> filterPortwithStatus(String id, String text, String status, Pageable pageable,
			T newEntity, String useruid) throws InterruptedException, UserNotFoundException, ServerException {

		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Role role = userDao.getCurrentRole();

		String hql = "";

		List<Long> ls = stationDao.getSiteIdBasedOnRole(role.getRolename(), u.getId());
		String ids = Arrays.toString(ls.toArray()).replace("[", "").replace("]", "");
		ids = ids.isEmpty() || ids.equals(null) ? "0" : ids;
		if (id.equals("All")) {

			if (status.equals("All")) {

				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id INNER JOIN "
						+ "statusNotification sn ON sn.port_id= p.id WHERE  (s.referNo LIKE '%" + text.trim()
						+ "%' OR p.displayName LIKE '%" + text.trim() + "%' OR sn.status LIKE '%" + text.trim()
						+ "%' OR  st.siteName LIKE '%" + text.trim() + "%' OR  s.stationName LIKE '%" + text.trim()
						+ "%')";

				if (role.getRolename().equalsIgnoreCase(Roles.DealerAdmin.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Dealer.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Owner.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.SiteHost.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {
					hql = hql.replace("WHERE", "  WHERE s.siteId IN (" + ids + ") AND ");
				}

			} else {

				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id INNER JOIN "
						+ "statusNotification sn ON sn.port_id= p.id WHERE sn.status ='" + status
						+ "' AND (s.referNo LIKE '%" + text.trim() + "%' OR p.displayName LIKE '%" + text.trim()
						+ "%' OR sn.status LIKE '%" + text.trim() + "%' OR  st.siteName LIKE '%" + text.trim()
						+ "%' OR  s.stationName LIKE '%" + text.trim() + "%')";
				if (role.getRolename().equalsIgnoreCase(Roles.DealerAdmin.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Dealer.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.Owner.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.SiteHost.toString())
						|| role.getRolename().equalsIgnoreCase(Roles.GroupDealerAdmin.toString())) {
					hql = hql.replace("WHERE", "  WHERE s.siteId IN (" + ids + ") AND ");
				}
			}

		} else {
			List<Long> longList2 = stationDao.getSiteIdBasedOnOrg(Long.parseLong(id));
			String ids2 = Arrays.toString(longList2.toArray()).replace("[", "").replace("]", "");
			ids2 = ids2.isEmpty() ? "0" : ids2;

			if (status.equals("All")) {

				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id INNER JOIN "
						+ "statusNotification sn ON sn.port_id= p.id  WHERE s.siteId IN (" + ids2
						+ ") and (s.referNo LIKE '%" + text.trim() + "%' OR p.displayName LIKE '%" + text.trim()
						+ "%' OR sn.status LIKE '%" + text.trim() + "%' OR  st.siteName LIKE '%" + text.trim()
						+ "%' OR  s.stationName LIKE '%" + text.trim() + "%')";

			} else {

				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id INNER JOIN "
						+ "statusNotification sn ON sn.port_id= p.id   WHERE s.siteId IN (" + ids2
						+ ") and sn.status ='" + status + "' AND (s.referNo LIKE '%" + text.trim()
						+ "%' OR p.displayName LIKE '%" + text.trim() + "%' OR sn.status LIKE '%" + text.trim()
						+ "%' OR  st.siteName LIKE '%" + text.trim() + "%' OR  s.stationName LIKE '%" + text.trim()
						+ "%')";
			}

		}

		if (role.getRolename().equalsIgnoreCase(Roles.Manufacturer.toString())) {
			hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id "
					+ " INNER JOIN statusNotification sn ON sn.port_id= p.id "
					+ " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
					+ " WHERE  um.userId= " + u.getId() + " AND  (s.referNo LIKE '%" + text.trim()
					+ "%' OR p.displayName LIKE '%" + text.trim() + "%' OR sn.status LIKE '%" + text.trim()
					+ "%' OR  st.siteName LIKE '%" + text.trim() + "%' OR  s.stationName LIKE '%" + text.trim() + "%')";
			if (!status.equals("All")) {
				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId INNER JOIN port p ON p.station_id = s.id "
						+ " INNER JOIN statusNotification sn ON sn.port_id= p.id "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
						+ " WHERE  um.userId= " + u.getId() + " AND  sn.status ='" + status + "' and (s.referNo LIKE '%"
						+ text.trim() + "%' OR p.displayName LIKE '%" + text.trim() + "%' OR sn.status LIKE '%"
						+ text.trim() + "%' OR  st.siteName LIKE '%" + text.trim() + "%' OR  s.stationName LIKE '%"
						+ text.trim() + "%')";
			}
		}

		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, hql);

		System.out.println("pagedResult.getNumberOfElements()):" + pagedResult);
		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.PORT_NOT_EXIST.toString(),
					Integer.toString(Error.PORT_NOT_EXIST.getCode()));

		return pagedResult;
	}

	@Override
	public <T> PagedResult<T> filterWithVehicleGroup(String filter, String rolename, Pageable pageable, User user)
			throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		String hql = "";

		Role role = userDao.getCurrentRole();

		System.out.println("role name" + role.getRolename());
		if (role.getRolename().equals(Roles.Admin.toString()))
			hql = "Select distinct v.id, v.groupName, (SELECT COUNT(id) FROM fleetgroup_vinNumber WHERE fleetgroupId= v.id) AS count, "
					+ "(SELECT companyName FROM fleet_in_owner"
					+ " WHERE userId IN (SELECT fleetmangerid  FROM fleetgroup_owner WHERE vehiclegroupid =v.id)) as fleetManager"
					+ " From vehicleGroup v left join fleetgroup_vinNumber vm on vm.fleetgroupId=v.id  "
					+ "left join vehicle_groupRFIDS vg on vg.vehicleGroup_id = v.id " + "Where v.groupName LIKE '%"
					+ filter.trim() + "%' or vg.rfid like '%" + filter.trim() + "%'  or vm.vinNumber like '%"
					+ filter.trim() + "%' ";

		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, hql);
		if (pagedResult.getNumberOfElements() == 0)
			throw new ServerException(Error.VEHICLE_GROUP_NOT_EXIST.toString(),
					Integer.toString(Error.VEHICLE_GROUP_NOT_EXIST.getCode()));
		return pagedResult;
	}

	@Override
	public <T> PagedResult<T> filterWithTickets(String filter, String rolename, Pageable pageable, User user)
			throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		String hql = "";

		if (rolename.equals(Roles.Admin.toString()))
			hql = "Select ir.* , u.email   From issue_reporting ir INNER JOIN  Users u ON u.UserId = ir.userId  Where ir.issue LIKE '%"
					+ filter.trim() + "%' OR ir.createdBy LIKE '%" + filter.trim() + "%' OR ir.ticketId LIKE '%"
					+ filter.trim() + "%'  OR u.email LIKE '%" + filter.trim() + "%' ";

		else if (rolename.equals(Roles.DealerAdmin.toString()))

			hql = "Select ir.* , u.email   From issue_reporting ir INNER JOIN  Users u ON u.UserId = ir.userId  Where (ir.issue LIKE '%"
					+ filter.trim() + "%' OR ir.createdBy LIKE '%" + filter.trim() + "%' OR ir.ticketId LIKE '%"
					+ filter.trim() + "%'  OR u.email LIKE '%" + filter.trim() + "%') AND ir.orgId = " + user.getOrgId()
					+ " ";

		else

			hql = "Select ir.* , u.email   From issue_reporting ir INNER JOIN  Users u ON u.UserId = ir.userId  Where (ir.issue LIKE '%"
					+ filter.trim() + "%' OR ir.createdBy LIKE '%" + filter.trim() + "%' OR ir.ticketId LIKE '%"
					+ filter.trim() + "%'  OR u.email LIKE '%" + filter.trim() + "%') AND ir.userId = " + user.getId()
					+ " ";

		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, hql);
		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.TICKET_NOT_EXIST.toString(),
					Integer.toString(Error.TICKET_NOT_EXIST.getCode()));

		return pagedResult;
	}

	@Override
	public void indexBooks() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> PagedResult<T> filter(String text, Pageable pageable, Collection<String> fields, T newEntity)
			throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "unchecked", "null" })
	public String getUserIdBasedOnRole(long userId, String role) {

		Role currentRole = userService.getCurrentRole();

		List<Long> dealerorgIds = null;

		if (currentRole.getRolename().equals(Roles.GroupDealerAdmin.toString())) {

			if (role.equals(Roles.DealerAdmin.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT dealerId FROM dealer_in_org WHERE orgId IN "
								+ "(SELECT dealerOrgId FROM group_in_dealer  WHERE groupId IN "
								+ "(SELECT orgId FROM users_in_org WHERE userId  =" + userId + " ))"));

			else if (role.equals(Roles.Dealer.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId In "
								+ "(SELECT dealerId FROM dealer_in_org WHERE orgId IN "
								+ "(SELECT dealerOrgId FROM group_in_dealer WHERE groupId IN "
								+ "(SELECT orgId FROM users_in_org WHERE userId  =" + userId + " )))"));

			else if (role.equals(Roles.Owner.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
								+ "(SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId In "
								+ "(SELECT dealerId FROM dealer_in_org WHERE orgId IN "
								+ "(SELECT dealerOrgId FROM group_in_dealer  WHERE groupId IN "
								+ "(SELECT orgId FROM users_in_org WHERE userId  =" + userId + "))))"));

			else if (role.equals(Roles.SiteHost.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT sitehostId FROM siteHost_in_owner WHERE ownerId IN"
								+ "(SELECT ownerId FROM owner_in_dealer WHERE dealerId IN "
								+ "(SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId In "
								+ "(SELECT dealerId FROM dealer_in_org WHERE orgId IN "
								+ "(SELECT dealerOrgId FROM group_in_dealer  WHERE groupId IN "
								+ "(SELECT orgId FROM users_in_org WHERE userId  =" + userId + ")))))"));

			else if (role.equals(Roles.Driver.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT dealerOrgId FROM group_in_dealer  WHERE groupId IN "
								+ "(SELECT orgId FROM users_in_org WHERE userId  =" + userId + " )"));

		} else if (currentRole.getRolename().equals(Roles.DealerAdmin.toString())) {

			if (role.equals(Roles.Dealer.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
						"SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId = " + userId));

			else if (role.equals(Roles.Owner.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
						"SELECT ownerId FROM owner_in_dealer WHERE dealerId IN (SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId ="
								+ userId + ")"));

			else if (role.equals(Roles.SiteHost.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject(" SELECT sitehostId FROM siteHost_in_owner WHERE ownerId IN ("
								+ "SELECT ownerId FROM owner_in_dealer WHERE dealerId IN (SELECT dealerId FROM dealer_in_dealerAdmin WHERE dealerAdminId = "
								+ userId + ") )"));

		} else if (currentRole.getRolename().equals(Roles.Dealer.toString())) {

			if (role.equals(Roles.Owner.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject("SELECT ownerId FROM owner_in_dealer WHERE dealerId IN (" + userId + ")"));

			else if (role.equals(Roles.SiteHost.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao
						.findAllSingalObject(" SELECT sitehostId FROM siteHost_in_owner WHERE ownerId IN ("
								+ "SELECT ownerId FROM owner_in_dealer WHERE dealerId IN (" + userId + "))"));

		} else if (currentRole.getRolename().equals(Roles.Owner.toString())) {

			if (role.equals(Roles.SiteHost.toString()))
				dealerorgIds = convertToLongList((List<BigDecimal>) generalDao.findAllSingalObject(
						" SELECT sitehostId FROM siteHost_in_owner WHERE ownerId IN (" + userId + ")"));
		}

		String ids = Arrays.toString(dealerorgIds.toArray()).replace("[", "").replace("]", "");
		return ids = ids.isEmpty() ? "0" : ids;
	}

	@Override
	public <T> PagedResult<T> filterWithPromoCodeiduserid(String filter, Pageable pageable, long id)
			throws UserNotFoundException, ServerException {

		String query = "";
		query = "select pc.id,pc.promoCode,pc.amount as amounts,pc.amountType as category,"
				+ " pc.creationDate,pc.email,pc.promoId,pc.transactionReferenceId as transactionId FROM promoCodeHistory pc where  (pc.email like '%"
				+ filter.trim() + "%' or pc.promoCode like '%" + filter.trim() + "%'  ) and userId=" + id;
		PagedResult<T> pagedResult = dataTableDao.getDataAlias(pageable, query);
		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.PromoCodeHistory_InValid.toString(),
					Integer.toString(Error.PromoCodeHistory_InValid.getCode()));
		return pagedResult;
	}

	@Override
	public <T> PagedResult<T> filterWithfindSession(String filter, String role, String party_id, Pageable pageable)
			throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		String query = "";
		if (role.equalsIgnoreCase("EMSP")) {
			query = "DECLARE @json nvarchar(max); WITH src (n) AS (  "
					+ "SELECT os.country_code ,os.party_id ,os.id as 'sessionId',FORMAT(os.start_date_time,'yyyy-MM-ddTHH:mm:ssZ') AS 'start_date_time',FORMAT(os.end_date_time,'yyyy-MM-ddTHH:mm:ssZ') AS 'end_date_time',  "
					+ "os.kwh ,os.auth_method ,os.authorization_reference ,os.location_id ,os.evse_uid ,os.connector_id ,os.meter_id ,os.currency ,os.status ,FORMAT(os.last_updated,'yyyy-MM-ddTHH:mm:ssZ') AS 'last_updated' ,oct.country_code as 'cdr_token.country_code',oct.party_id as 'cdr_token.party_id',  "
					+ "oct.uid AS 'cdr_token.uid',oct.type AS 'cdr_token.type',oct.contract_id AS 'cdr_token.contract_id',ROUND(op.incl_vat, 4) AS 'total_cost.incl_vat',ROUND(op.excl_vat, 4) AS 'total_cost.excl_vat',  "
					+ "charging_periods=(SELECT FORMAT(ocp.start_date_time,'yyyy-MM-ddTHH:mm:ssZ') AS 'start_date_time',ocp.tariff_id as 'tariff_id',  "
					+ "dimensions=(SELECT ocd.type,ROUND(ocd.volume, 4) as 'volume' FROM ocpi_cdr_dimension ocd  "
					+ "WHERE ocd.chargingPeriod_id = ocp.id FOR JSON PATH) FROM ocpi_charging_period ocp "
					+ "WHERE ocp.session_id = os.id FOR JSON PATH)  FROM ocpi_session os "
					+ "inner JOIN ocpi_cdr_token oct ON oct.id = os.cdrToken  "
					+ "inner JOIN ocpi_price op ON op.id = os.total_cost where os.id like  '%" + filter.trim()
					+ "%' and oct.party_id = '" + party_id
					+ "' and os.status='COMPLETED' order by os.last_updated desc FOR JSON PATH)  "
					+ " SELECT @json = src.n FROM src SELECT @json as 'JSON_F52E2B61-18A1-11d1-B105-00805F49916B'";
		}

		PagedResult<T> pagedResult = dataTableDao.getDataAliasWithJSONQuery(pageable, query);

		if (pagedResult.getTotalElements() == 0)
			throw new ServerException(Error.OcpiSession_InValid.toString(),
					Integer.toString(Error.OcpiSession_InValid.getCode()));
		return pagedResult;
	}

	@Override
	public <T> PagedResult<List<Map<String, Object>>> filterWithTariff(String filter, String rolename,
			Pageable pageable, User user) throws UserNotFoundException {
		// TODO Auto-generated method stub
		String hql = "SELECT * FROM tariff WHERE currency LIKE '%" + filter.trim() + "%'";

		PagedResult<List<Map<String, Object>>> pagedResult = dataTableDao.getDataAlias(pageable, hql);

		return pagedResult;
	}

}