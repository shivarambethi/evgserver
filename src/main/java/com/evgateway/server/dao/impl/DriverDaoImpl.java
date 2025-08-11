package com.evgateway.server.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.evgateway.server.dao.DriverDao;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Vehicles;

@Repository
public class DriverDaoImpl<I> extends DaoImpl<BaseEntity, I> implements DriverDao<BaseEntity, I> {

	@Override
	public List<Vehicles> getVehicles() {
		// TODO Auto-generated method stub
		String hql = "FROM Vehicles u ORDER BY u.id";
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	public List<Vehicles> getVehiclesByAccID(Long id) {
		// TODO Auto-generated method stub

		return null;

	}

}
