package com.evgateway.server.dao.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.ReportAndStatsDao;
import com.evgateway.server.pojo.BaseEntity;

@Repository
@Component
public class ReportAndStatsDaoImpl<I> extends DaoImpl<BaseEntity, I> implements ReportAndStatsDao<BaseEntity, I> {

	@Autowired
	private GeneralDao<?, ?> generalDao;

	final static Logger LOGGER = LoggerFactory.getLogger(ReportAndStatsDaoImpl.class);

	@Override
	public List<Map<String, Object>> getTotalDataForReportAnalytics(String query) {
		// TODO Auto-generated method stub

		return generalDao.findAliasData(query);
	}

	

}
