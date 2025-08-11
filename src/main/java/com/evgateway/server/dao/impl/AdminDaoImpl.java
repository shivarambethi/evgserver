package com.evgateway.server.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.evgateway.server.dao.AdminDao;
import com.evgateway.server.pojo.BaseEntity;

@Repository
public class AdminDaoImpl<I> extends DaoImpl<BaseEntity, I> implements AdminDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(AdminDaoImpl.class);

	@Override
	public void commanDeleteById(String tableName, long paramValue, String paramName) {
		// TODO Auto-generated method stub
		String query = "Delete " + tableName + " where " + paramName + " =" + paramValue;
		getCurrentSession().createSQLQuery(query).executeUpdate();

	}

	@Override
	public void cancelReservation(String portalReqId, String reservationId) {

		String queryForUpdateReservationId = "update ocpp_reservation set reqId='" + portalReqId
				+ "' where reservationId=" + reservationId + "";
		// System.err.println("portalRequId>>>"+queryForUpdateReservationId);
		getCurrentSession().createSQLQuery(queryForUpdateReservationId).executeUpdate();
	}

}
