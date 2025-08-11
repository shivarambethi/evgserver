package com.evgateway.server.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evgateway.server.dao.DataTableDao;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.GridKeyRequests;
import com.evgateway.server.pojo.User;

@Service
@SuppressWarnings("deprecation")
public class DataTableServiceImpl implements DataTableService {

	final static Logger LOGGER = LoggerFactory.getLogger(DataTableServiceImpl.class);

	@Autowired
	private DataTableDao<?, ?> dataTableDao;

	@Autowired
	private UserDao<?, ?> userDao;

	public PagedResult<User> getDealers(int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getDealers() - with page [" + page + "]");

		Pageable pagable = PageRequest.of(page, size);

		return dataTableDao.getUsersByRole(pagable, new User(), "Dealer");

	}

	@Override
	public PagedResult<User> getOwners(int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getOwners() - with page [" + page + "]");

		return dataTableDao.getUsersByRole(PageRequest.of(page, size), new User(), "Owner");
	}

	@Override
	public PagedResult<User> getSiteHosts(int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getSiteHosts() - with page [" + page + "]");

		return dataTableDao.getUsersByRole(PageRequest.of(page, size), new User(), "SiteHost");
	}

	@Override
	public PagedResult<User> getDrivers(int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getDrivers() - with page [" + page + "]");

		return dataTableDao.getUsersByRole(PageRequest.of(page, size), new User(), "Driver");
	}

	@Override
	public PagedResult<Credential> getRFIDByAccountId(long accountId, int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getRFIDByAccountId() - with page [" + page + "]");

		return dataTableDao.getRFIDByAccountId(accountId, PageRequest.of(page, size), new Credential());
	}

	@Override
	public PagedResult<GridKeyRequests> getRFIDRequest(int page, int size) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.debug("DataTableServiceImpl.getPortByStatus() - with page [" + page + "]");

		return dataTableDao.getRFIDRequest(PageRequest.of(page, size), new GridKeyRequests());
	}

}
