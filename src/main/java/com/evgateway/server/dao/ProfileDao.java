package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Language;
import com.evgateway.server.pojo.Profile;
import com.evgateway.server.pojo.User;
import com.evgateway.server.pojo.Zone;

public interface ProfileDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Profile> getProfiles();

	public Profile getProfile(long id) throws UserNotFoundException;

	public Profile getProfileByUser(User user) throws UserNotFoundException;

	public Profile addProfile(Profile profile);

	public void updateProfile(Profile profile) throws UserNotFoundException;

	public void deleteProfile(long id) throws UserNotFoundException;

	public Profile getByUserId(long userId) throws UserNotFoundException;

	public List<Language> getLanguages();

	public List<Zone> getZones();

	public int deleteProfileByUserId(long userId);

	public int deleteSecurityAnswersByUserId(long userId);

	public int deletePasswordByUserId(long userId);

	public int deleteAddressByUserId(long userId);

	public int deleteToolConfigByProfileId(long profileId);

}
