package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.GridKeyRequests;

public interface GridkeyDao {

	public void addGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException;

	public List<GridKeyRequests> getGridKeyRequestByUserId(long userId) throws ServerException, UserNotFoundException;

	public void updateGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException;

	public void deleteGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException;

	public List<GridKeyRequests> getGridKeyRequest() throws UserNotFoundException;

	public GridKeyRequests getGridKeyRequestById(Long id) throws UserNotFoundException;

	public long getCountByUserId(Long id) throws ServerException, UserNotFoundException;

	String decimal2hex(Long d);

}
