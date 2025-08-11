package com.evgateway.server.dao;

import com.evgateway.server.pojo.BaseEntity;

public interface AdminDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public void commanDeleteById(String tableName, long paramValue, String paramName);

	public void cancelReservation(String reserVationId, String portalReqId);

}
