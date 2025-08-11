package com.evgateway.server.services;

import java.util.List;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.Notification;
import com.evgateway.server.pojo.User;

public interface NotificationService {

	public void addNotification(String comment, long stationId, String title, User user) throws UserNotFoundException, ServerException;

	public Notification getNotificationById(long id) throws UserNotFoundException;

	public List<Notification> getNotifications(String useruid) throws UserNotFoundException, ServerException;

	public void updateNotification(Notification noti) throws UserNotFoundException;

	public void deleteNotification(long id) throws UserNotFoundException;

	void deleteNotificationByUserId(long userId) throws UserNotFoundException;

	public List<Notification> getNotificationForSite(String useruid) throws UserNotFoundException, ServerException;

	public int getNotificationCount(String type);

}
