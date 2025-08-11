package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.BaseEntity;

public interface AddressDao<T extends BaseEntity,I> extends Dao<BaseEntity, I> {

	public void save(Object obj);

	public List<Address> getAddress();

	public Address getAddress(long id) throws UserNotFoundException;

	public Address addAddress(Address address);

	public void updateAddress(Address address) throws UserNotFoundException;

	public void deleteAddress(long id) throws UserNotFoundException;

}
