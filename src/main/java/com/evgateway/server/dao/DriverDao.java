package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Vehicles;

public interface DriverDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Vehicles> getVehicles();

	public List<Vehicles> getVehiclesByAccID(Long id);

}
