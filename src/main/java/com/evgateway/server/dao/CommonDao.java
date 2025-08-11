package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Zone;

public interface CommonDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Zone> getZone();

}
