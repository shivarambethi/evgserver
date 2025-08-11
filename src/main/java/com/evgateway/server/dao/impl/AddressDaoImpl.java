package com.evgateway.server.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.dao.AddressDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.BaseEntity;

@Repository
public class AddressDaoImpl<I> extends DaoImpl<BaseEntity, I> implements AddressDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(AddressDaoImpl.class);

	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteAddress(long id) throws UserNotFoundException {
		Address address = getAddress(id);
		if (address != null) {
			getCurrentSession().delete(address);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Address> getAddress() {
		String hql = "FROM Address a ORDER BY a.id";
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Address getAddress(long id) throws UserNotFoundException {

		logger.debug("AddressDaoImpl.getAddress() - [" + id + "]");
		Address addressObject = (Address) getCurrentSession().get(
				Address.class, id);

		if (addressObject == null) {
			throw new UserNotFoundException("Address id [" + id + "] not found");
		} else {
			return addressObject;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Address addAddress(Address address) {
		logger.debug("AddressDaoImpl.addAddress() - [" + address + "]");

		getCurrentSession().save(address);

		return address;

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateAddress(Address address) throws UserNotFoundException {

		try {
			Address addressToUpdate = getAddress(address.getId());
			
			if (StringUtils.isNotBlank(address.getAddressLine1()))
				addressToUpdate.setAddressLine1(address.getAddressLine1());
			if (StringUtils.isNotBlank(address.getCity()))
				addressToUpdate.setCity(address.getCity());
			if (StringUtils.isNotBlank(address.getCountry()))
				addressToUpdate.setCountry(address.getCountry());
			if (StringUtils.isNotBlank(address.getState()))
				addressToUpdate.setState(address.getState());
			//if (StringUtils.isNotBlank(address.getAddressLine2()))
			addressToUpdate.setAddressLine2(address.getAddressLine2());
			if (address.getPhone() != null)
				addressToUpdate.setPhone(address.getPhone());
			if (address.getZipCode() != null)
				addressToUpdate.setZipCode(address.getZipCode());
			
			if (address.getUser() != null && address.getUser().getId() != 0)
				addressToUpdate.setUser(address.getUser());

			getCurrentSession().update(addressToUpdate);
			//logger.info("User successfully Updated Address "+address);


		} catch (UserNotFoundException e) {
			logger.error(address.getUser()+" Updating Address Un Successful : " + e );
		}

	}

	@Override
	public void save(Object obj) {

		getCurrentSession().save(obj);

	}
}
