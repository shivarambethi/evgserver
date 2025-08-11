package com.evgateway.server.services;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.GridKeyRequests;
import com.evgateway.server.pojo.User;

@Transactional(propagation = Propagation.REQUIRED)
public interface DataTableService {

	public PagedResult<User> getDealers(int page, int size) throws UserNotFoundException;

	public PagedResult<User> getOwners(int page, int size) throws UserNotFoundException;

	public PagedResult<User> getSiteHosts(int page, int size) throws UserNotFoundException;

	public PagedResult<User> getDrivers(int page, int size) throws UserNotFoundException;

	// public PagedResult<User> getOwnersByDealer(int page, int size) throws
	// UserNotFoundException;

	// public PagedResult<User> getSiteHostsByOwner(int page, int size) throws
	// UserNotFoundException;

	public PagedResult<Credential> getRFIDByAccountId(long acoountId, int page, int size) throws UserNotFoundException;

	public PagedResult<GridKeyRequests> getRFIDRequest(int page, int size) throws UserNotFoundException;

}
