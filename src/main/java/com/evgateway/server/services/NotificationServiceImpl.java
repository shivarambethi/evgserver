package com.evgateway.server.services;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.Notification;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private UserService userService;

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Override
	public void addNotification(String comment, long stationId, String title, User user)
			throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

//		User user = userService.getCurrentUser();

		Notification noti = new Notification();
		noti.setComment(comment);
		noti.setCreatedBy(user.getUsername());
		noti.setCreatedDate(new Date());
		noti.setSeen(false);
		noti.setStationid(stationId);
		noti.setTitle(title);
		noti.setUserId(user.getId());
		noti.setOrgId(user.getOrgId());

		generalDao.save(noti);

	}

	@Override
	public void updateNotification(Notification noti) throws UserNotFoundException {
		// TODO Auto-generated method stub

		generalDao.update(noti);
	}

	@Override
	public void deleteNotification(long id) throws UserNotFoundException {
		// TODO Auto-generated method stub

		Notification noti = getNotificationById(id);

		generalDao.delete(noti);

	}

	@Override
	public void deleteNotificationByUserId(long userId) throws UserNotFoundException {
		// TODO Auto-generated method stub

		Notification noti = generalDao.findOneHQLQuery(new Notification(), "From Notification Where userId=" + userId);

		if (noti != null)
			generalDao.delete(noti);

	}

	@Override
	public Notification getNotificationById(long id) throws UserNotFoundException {
		// TODO Auto-generated method stub
		return generalDao.findOneById(new Notification(), id);
	}

	@Override
	public List<Notification> getNotifications(String useruid) throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		Role role = userService.getCurrentRole();

		String hql = "";

		if (role.getRolename().equals(Roles.Admin.toString())) {
			hql = "select top 5 * From Notification t Where t.orgId=1 and t.comment= 'RFID Request' ORDER BY t.createdDate DESC";
		} else if (role.getRolename().equals(Roles.Driver.toString())) {
			hql = "select top 5 * From Notification  t Where t.userId =" + user.getId()
					+ " and t.comment= 'RFID Request' ORDER BY t.createdDate DESC";
		} else {
			hql = "select top 5 * From Notification  t Where t.orgId=" + user.getOrgId()
					+ "  and t.comment= 'RFID Request' ORDER BY t.createdDate DESC";
		}

		return generalDao.findAllSQLQuery(new Notification(), hql);

	}

	@Override
	public List<Notification> getNotificationForSite(String useruid) throws UserNotFoundException, ServerException {
		// TODO Auto-generated method stub

		User user = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");

		Role role = userService.getCurrentRole();

		String hql = "";

		if (role.getRolename().equals(Roles.Admin.toString())) {
			hql = "select * From Notification t Where t.orgId=1 and t.comment= 'Site Image Request' ORDER BY t.createdDate DESC";
		} else if (role.getRolename().equals(Roles.Driver.toString())) {
			hql = "select * From Notification t Where t.userId =" + user.getId()
					+ " and t.comment= 'Site Image Request' ORDER BY t.createdDate DESC";
		} else {
			hql = "Select * From Notification t Where t.orgId=" + user.getOrgId()
					+ "  and t.comment= 'Site Image Request' ORDER BY t.createdDate DESC";
		}

		return generalDao.findAllSQLQuery(new Notification(), hql);
	}

	@Override
	public int getNotificationCount(String type) {
		List<Map<String, Object>> findAliasData = generalDao.findAliasData(
				"select sum(IIF(seen=0,1,0)) as seencount from notification where comment='" + type + "'");

		System.out.println("findAliasData====" + findAliasData);
		int count = 0;
		if (findAliasData.get(0).get("seencount") != null)
			count = Integer.valueOf(findAliasData.get(0).get("seencount").toString());

		return count;
	}
}
