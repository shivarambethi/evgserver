package com.evgateway.server.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.dao.ProfileDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.Zone;

@Repository
public class ProfileDaoImpl<I> extends DaoImpl<BaseEntity, I> implements ProfileDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(ProfileDaoImpl.class);

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteProfile(long id) throws UserNotFoundException {
		Profile profile = getProfile(id);
		if (profile != null) {
			getCurrentSession().delete(profile);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Profile> getProfiles() {
		String hql = "FROM Profile a ORDER BY a.id";
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Profile getProfile(long id) throws UserNotFoundException {

		logger.debug("getProfile() - [" + id + "]");
		Profile profileObject = (Profile) getCurrentSession().get(Profile.class, id);

		if (profileObject == null) {
			throw new UserNotFoundException("Profile id [" + id + "] not found");
		} else {
			return profileObject;
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Profile addProfile(Profile profile) {
		logger.debug("addProfile() - [" + profile + "]");

		getCurrentSession().save(profile);

		return profile;

	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void updateProfile(Profile profile) throws UserNotFoundException {

		try {
			Profile profileToUpdate = getProfile(profile.getId());

			if (StringUtils.isNotBlank(profile.getStatus()))
				profileToUpdate.setStatus(profile.getStatus());

			if (profile.getUser() != null && profile.getUser().getId() != null)
				profileToUpdate.setUser(profile.getUser());
			
			logger.info(""+profileToUpdate);
			

			getCurrentSession().update(profileToUpdate);

		} catch (UserNotFoundException e) {
			logger.error("updateProfile() - [" + e + "]");
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public Profile getProfileByUser(User user) throws UserNotFoundException {

		String hql = "FROM Profile where user = :user";
		List pls = getCurrentSession().createQuery(hql).setParameter("user", user).list();
		Profile profile = null;

		if (pls != null && pls.size() != 0)
			profile = (Profile) pls.get(0);

		return profile;
	}

	@Override
	@Transactional(readOnly = true)
	public Profile getByUserId(long userId) throws UserNotFoundException {

		logger.debug("getByUserId() - [" + userId + "]");
		Profile profile = null;
		User userObject = (User) getCurrentSession().get(User.class, userId);

		if (userObject == null) {

			throw new UserNotFoundException("Profile  [" + userId + "] not found");
		}

		profile = getProfileByUser(userObject);

		return profile;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Language> getLanguages() {
		String hql = "FROM Language a ORDER BY a.id";
		return getCurrentSession().createQuery(hql).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Zone> getZones() {
		String hql = "FROM Zone z ORDER BY z.countryCode";
		return getCurrentSession().createQuery(hql).list();
	}

	@Override
	public int deleteProfileByUserId(long userId) {
		logger.debug("deleteProfileByUserId() - [" + userId + "]");
		Query query = getCurrentSession().createQuery("delete from Profile where user.id=:userId");
		return query.setLong("userId", userId).executeUpdate();
	}

	@Override
	public int deleteSecurityAnswersByUserId(long userId) {
		logger.debug("deleteSecurityAnswersByUserId() - [" + userId + "]");
		Query query = getCurrentSession().createQuery("delete from SecurityAnswers where user.id=:userId");
		return query.setLong("userId", userId).executeUpdate();
	}

	@Override
	public int deletePasswordByUserId(long userId) {
		logger.debug("deletePasswordByUserId() - [" + userId + "]");
		Query query = getCurrentSession().createQuery("delete from Password where user.id=:userId");
		return query.setLong("userId", userId).executeUpdate();
	}

	@Override
	public int deleteAddressByUserId(long userId) {
		logger.debug("deleteAddressByUserId() - [" + userId + "]");
		Query query = getCurrentSession().createQuery("delete from Address where user.id=:userId");
		return query.setLong("userId", userId).executeUpdate();
	}

	@Override
	public int deleteToolConfigByProfileId(long profileId) {
		logger.debug("deleteAddressByUserId() - [" + profileId + "]");
		Query query = getCurrentSession().createQuery("delete from ToolConfig where profile.id=:profileId");
		return query.setLong("profileId", profileId).executeUpdate();
	}
}
