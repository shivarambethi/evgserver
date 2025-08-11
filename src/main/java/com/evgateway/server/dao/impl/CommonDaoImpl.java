package com.evgateway.server.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.evgateway.server.dao.CommonDao;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Zone;

@Repository
public class CommonDaoImpl<I> extends DaoImpl<BaseEntity, I> implements CommonDao<BaseEntity, I> {

	@SuppressWarnings("unchecked")
	@Override
	public List<Zone> getZone() {
		// TODO Auto-generated method stub
		String sql = "From Zone z";
		return getCurrentSession().createQuery(sql).list();
	}

}
