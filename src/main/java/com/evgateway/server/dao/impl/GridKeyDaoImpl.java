package com.evgateway.server.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.GridkeyDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.GridKeyRequests;

@Repository
public class GridKeyDaoImpl implements GridkeyDao {

	@Autowired
	private GeneralDao<?, ?> generalDao;

	static Logger logger = LoggerFactory.getLogger(GridKeyDaoImpl.class);

	@Override
	public long getCountByUserId(Long id) throws ServerException, UserNotFoundException {
		// TODO Auto-generated method stub
		List<GridKeyRequests> gridkeys = getGridKeyRequestByUserId(id);

		long count = 0;
		for (GridKeyRequests grkey : gridkeys) {
			count = count + grkey.getCount();
		}

		return count;
	}

	@Override
	public String decimal2hex(Long d) {
		String digits = "0123456789ABCDEF";
		if (d == 0)
			return "0";
		String hex = "";
		while (d > 0) {
			int digit = (int) (d % 16); // rightmost digit
			hex = digits.charAt(digit) + hex; // string concatenation
			d = d / 16;
		}
		return hex;
	}

	@Override
	public void addGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException {
		// TODO Auto-generated method stub
		generalDao.save(gridKeyRequests);
	}

	@Override
	public void updateGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException {
		// TODO Auto-generated method stub
		generalDao.update(gridKeyRequests);
	}

	@Override
	public void deleteGridKeyRequest(GridKeyRequests gridKeyRequests) throws ServerException, UserNotFoundException {
		// TODO Auto-generated method stub
		generalDao.delete(gridKeyRequests);
	}

	@Override
	public GridKeyRequests getGridKeyRequestById(Long id) throws UserNotFoundException {
		// TODO Auto-generated method stub

		GridKeyRequests request = (GridKeyRequests) generalDao.findOneHQLQuery(new GridKeyRequests(),
				"From GridKeyRequests where id = " + id);
		return request;
	}

	@Override
	public List<GridKeyRequests> getGridKeyRequest() throws UserNotFoundException {
		// TODO Auto-generated method stub
		return generalDao.findAllHQLQuery(new GridKeyRequests(), "From GridKeyRequests");
	}

	@Override
	public List<GridKeyRequests> getGridKeyRequestByUserId(long userId) throws ServerException, UserNotFoundException {
		// TODO Auto-generated method stub

		List<GridKeyRequests> request = (List<GridKeyRequests>) generalDao.findAllHQLQuery(new GridKeyRequests(),
				"From GridKeyRequests where userId  = " + userId);
		return request;
	}

}
