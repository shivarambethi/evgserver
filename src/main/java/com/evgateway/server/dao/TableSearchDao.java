package com.evgateway.server.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.User;

@SuppressWarnings("hiding")
public interface TableSearchDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	<T> PagedResult<T> filterWithUser(String text, String roleName, Pageable pageable, T newEntity, String useruid)
			throws InterruptedException, UserNotFoundException, ServerException;

	<T> PagedResult<T> filterStationwithStatus(String id, String text, String status, Pageable pageable, T newEntity,
			String useruid) throws InterruptedException, UserNotFoundException, ServerException;

	<T> PagedResult<T> filterPortwithStatus(String id, String text, String status, Pageable pageable, T newEntity,
			String useruid) throws InterruptedException, UserNotFoundException, ServerException;

	<T> PagedResult<T> filterWithTickets(String filter, String rolename, Pageable pageable, User user)
			throws UserNotFoundException, ServerException;

	void indexBooks() throws Exception;

	<T> PagedResult<T> filter(String text, Pageable pageable, Collection<String> fields, T newEntity)
			throws InterruptedException;

	<T> PagedResult<T> filterWithVehicleGroup(String filter, String rolename, Pageable pageable, User user)
			throws UserNotFoundException, ServerException;

	<T> PagedResult<T> filterWithPromoCodeiduserid(String filter, Pageable pageable, long id)
			throws UserNotFoundException, ServerException;

	<T> PagedResult<T> filterWithfindSession(String filter, String role, String party_id, Pageable pageable)
			throws UserNotFoundException, ServerException;

	<T> PagedResult<List<Map<String, Object>>> filterWithTariff(String filter, String rolename, Pageable pageable,
			User user) throws UserNotFoundException;

}
