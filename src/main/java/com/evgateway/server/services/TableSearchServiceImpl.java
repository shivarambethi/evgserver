package com.evgateway.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.evgateway.server.dao.TableSearchDao;
import com.evgateway.server.messages.PagedResult;

@Service
public class TableSearchServiceImpl implements TableSearchService {

	@Autowired
	private TableSearchDao<?, ?> tableSearchDao;

	@Override
	public <T> PagedResult<T> filter(String text, int page, int size) throws InterruptedException {
		// TODO Auto-generated method stub

		Pageable pagable = PageRequest.of(page, size);

		//@SuppressWarnings("unchecked")
		//PagedResult<T> users = (PagedResult<T>) tableSearchDao.filter(text, pagable, new User());

		return null;

	}
	
	

}
