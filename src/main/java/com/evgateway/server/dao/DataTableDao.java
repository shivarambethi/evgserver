package com.evgateway.server.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.BaseEntity;

@SuppressWarnings("hiding")
public interface DataTableDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	<T> PagedResult<T> getUsersByRole(Pageable pageable, T newsEntry, String roleName) throws UserNotFoundException;

	<T> PagedResult<T> getRFIDByAccountId(long accountId, Pageable pageable, T newsEntry) throws UserNotFoundException;

	<T> PagedResult<T> getPortByStatus(String id, String status, Pageable pageable, T newsEntry, String useruid)
			throws UserNotFoundException;

	<T> PagedResult<T> getRFIDRequest(Pageable pageable, T newsEntry) throws UserNotFoundException;

	<T> PagedResult<T> getData(Pageable pageable, T newsEntry, String propertyName, Object id, boolean condition)
			throws UserNotFoundException;

	<T> PagedResult<T> getDataSQL(Pageable pageable, T newsEntry, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAlias(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataHQL(Pageable pageable, T newsEntry, String query, List<Map<String, Object>> datas,
			boolean condition) throws UserNotFoundException;

	<T> PagedResult<T> getDriverGroup(Pageable pageable, T newsEntry) throws UserNotFoundException;

	<T> PagedResult<T> getStationByConectortType(String id, Pageable pageable, T newsEntry)
			throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasWithJSONQuery(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasforimage(Pageable pageable, String query)
			throws UserNotFoundException, ServerException;

	<T> PagedResult<T> getDataAliasforcharging(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasforStation(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasForAPP(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasForApp(Pageable pageable, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataSQLForApp(Pageable pageable, T newsEntry, String query) throws UserNotFoundException;

	<T> PagedResult<T> getDataAliasforcluster(Pageable pageable, String query)
			throws UserNotFoundException, ServerException;

}
