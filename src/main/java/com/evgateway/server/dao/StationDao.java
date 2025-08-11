package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Organization;

public interface StationDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public List<Long> getStationIdBasedOnRole(String roleName, Long userId);

	public List<Long> getSiteIdBasedOnRole(String roleName, Long userId);

	public List<Long> getOgrIdBasedOnRole(String roleName);

	public List<Long> getSiteIdBasedOnOrg(Long orgId) throws UserNotFoundException;

	public List<Long> getStationsBasedOnSite(Long siteId);

	public List<Long> getOwnerIdBasedOnOrg(long orgId);

	public List<Long> getSiteIdBasedOnDiffOrg(String network);

	public List<Long> getOrgIdsOfGroupDealerAdmin(Long id) throws UserNotFoundException;

	public List<Long> getSiteIdBasedOnGrpDealerAdminId(Long id) throws UserNotFoundException;

	public List<Long> getSiteIdBasedMultipleOrg(Long orgId, Long orgId1) throws UserNotFoundException;

	public List<Long> getSiteIdBasedOnFleetManagerId(Long id) throws UserNotFoundException;

	public List<Long> getSiteIdBasedOnAdminIdForFleet(Long id) throws UserNotFoundException;

	public Boolean isTure(Long id, Long orgId, String role, Long userId, String type);

	public List<Long> getStationsBasedOnSiteIds(String siteId);
}
