package com.evgateway.server.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.DataTableDao;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.dao.StationDao;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.Error;
import com.evgateway.server.messages.PagedResult;
import com.evgateway.server.pojo.AccountTransactions;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Credentials;
import com.evgateway.server.pojo.GridKeyRequests;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class DataTableDaoImpl<I> extends DaoImpl<BaseEntity, I> implements DataTableDao<BaseEntity, I> {

	final static Logger LOGGER = LoggerFactory.getLogger(DataTableDaoImpl.class);

	@Autowired
	private GeneralDao<?, ?> generalDao;

	@Autowired
	private StationDao<?, ?> stationDao;

	@Autowired
	private UserDao<?, ?> userDao;

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getUsersByRole(Pageable pageable, T newsEntry, String roleName)
			throws UserNotFoundException {

		LOGGER.info("DataTableDaoImpl.getUsersByRole() - From [" + newsEntry.getClass() + "] with " + pageable);

		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		Join<User, Role> join = from.join("roles", JoinType.LEFT);
		cq.where(cb.equal(join.get("rolename"), roleName));
		cq.orderBy(cb.asc(from.get(pageable.getSort().iterator().next().getProperty())));

		if (pageable.getSort().iterator().next().getDirection().isDescending())
			cq.orderBy(cb.desc(from.get(pageable.getSort().iterator().next().getProperty())));

		@SuppressWarnings("unchecked")
		TypedQuery<T> criteria = (TypedQuery<T>) getCurrentSession().createQuery(cq);
		criteria.setMaxResults(pageable.getPageSize());
		criteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		List<T> data = generalDao.findAllCriteriaQueryManyToMany(newsEntry, criteria);

		@SuppressWarnings("deprecation")
		Criteria userCriteria = getCurrentSession().createCriteria(User.class);

		Criteria suppCrit = userCriteria.createCriteria("roles");
		suppCrit.add(Restrictions.eq("rolename", roleName));

		Long totatCount = generalDao.countCriteriaQuery(userCriteria);

		if (totatCount == 0)
			return new PagedResult<T>(new PageImpl<T>(new ArrayList<T>(), pageable, 0));

		PagedResult<T> pagedResult = new PagedResult<T>((Page<T>) getPage(data, pageable, totatCount));

		return pagedResult;
	}

	public <T> Page<T> getPage(Collection<T> content, Pageable pageable, Long object) {

		return new PageImpl<T>((List<T>) content, pageable, object);

	}

	@Override
	public <T> PagedResult<T> getRFIDByAccountId(long accountId, Pageable pageable, T newsEntry)
			throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getRFIDByAccountId() - From [" + newsEntry.getClass() + "] with ");

		@SuppressWarnings("deprecation")
		Criteria credentialCriteria = getCurrentSession().createCriteria(Credentials.class, "credential");
		credentialCriteria.add(Restrictions.in("credential.account.id", accountId));

		Long totatCount = generalDao.countCriteriaQuery(credentialCriteria);

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		List<T> data = (List<T>) generalDao.findAllCriteriaQuery(newsEntry, credentialCriteria);

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@Override
	public <T> PagedResult<T> getPortByStatus(String id, String status, Pageable pageable, T newsEntry, String useruid)
			throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getPortByStatus() - From [" + newsEntry.getClass() + "] with " + status);

		User u = generalDao.findOneSQLQuery(new User(), "select * from users where uid ='" + useruid + "'");
		Set<Role> roles = u.getRoles();
		String roleName = "";

		String hql = "";

		if (roles != null)
			for (Role role : roles) {
				roleName = role.getRolename();
				break;
			}

		if (roleName.equalsIgnoreCase(Roles.Manufacturer.toString())) {

			hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName "
					+ " FROM station s " + " INNER JOIN site st On s.siteId = st.siteId "
					+ " INNER JOIN port p ON p.station_id = s.id "
					+ " INNER JOIN statusNotification sn ON sn.port_id= p.id "
					+ " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
					+ " WHERE s.preProduction = 0 AND um.userId= " + u.getId();
			if (!status.equals("All")) {
				hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName "
						+ " FROM station s " + " INNER JOIN site st On s.siteId = st.siteId "
						+ " INNER JOIN port p ON p.station_id = s.id "
						+ " INNER JOIN statusNotification sn ON sn.port_id= p.id "
						+ " Inner Join user_in_manufacturer um on um.manufacturerId = s.manufacturerId"
						+ " WHERE s.preProduction = 0 AND um.userId= " + u.getId() + "AND sn.status ='" + status + "' ";

			}

		} else {

			if (id.equals("All")) {

				List<Long> longList = stationDao.getSiteIdBasedOnRole(roleName, u.getId());
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				if (status.equals("All")) {

					hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId LEFT OUTER  JOIN port p ON p.station_id = s.id INNER JOIN "
							+ "statusNotification sn ON sn.port_id= p.id WHERE s.siteId IN (" + ids
							+ ") AND s.preProduction = 0";

				} else {

					hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId LEFT OUTER  JOIN port p ON p.station_id = s.id INNER JOIN "
							+ "statusNotification sn ON sn.port_id= p.id WHERE s.siteId IN (" + ids
							+ ") AND sn.status ='" + status + "' AND s.preProduction = 0";
				}

			} else {

				List<Long> longList = stationDao.getSiteIdBasedOnOrg(Long.valueOf(id));
				String ids = Arrays.toString(longList.toArray()).replace("[", "").replace("]", "");

				ids = ids.isEmpty() ? "0" : ids;

				if (status.equals("All")) {

					hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId LEFT OUTER  JOIN port p ON p.station_id = s.id INNER JOIN "
							+ "statusNotification sn ON sn.port_id= p.id WHERE s.siteId IN (" + ids
							+ ") AND s.preProduction = 0";

				} else {

					hql = "SELECT p.id, s.referNo, sn.status as statusNo, p.displayName, p.station_id, s.stationName, st.siteName FROM station s INNER JOIN site st On s.siteId = st.siteId LEFT OUTER  JOIN port p ON p.station_id = s.id INNER JOIN "
							+ "statusNotification sn ON sn.port_id= p.id WHERE s.siteId IN (" + ids
							+ ") AND sn.status ='" + status + "' AND s.preProduction = 0";
				}

			}
		}
		PagedResult<T> pagedResult = getDataAlias(pageable, hql);

		return pagedResult;
	}

	@Override
	public <T> PagedResult<T> getRFIDRequest(Pageable pageable, T newsEntry) throws UserNotFoundException {
		// TODO Auto-generated method stub

		LOGGER.info("DataTableDaoImpl.getRFIDRequest() - From [" + newsEntry.getClass() + "] with " + pageable);

		@SuppressWarnings("deprecation")
		Criteria credentialCriteria = getCurrentSession().createCriteria(GridKeyRequests.class, "rfid");

		Long totatCount = generalDao.countCriteriaQuery(credentialCriteria);

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		credentialCriteria.addOrder(Order.desc("dateGenerated"));

		List<T> data = (List<T>) generalDao.findAllCriteriaQuery(newsEntry, credentialCriteria);

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getData(Pageable pageable, T newsEntry, String propertyName, Object id, boolean condition)
			throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getData() - From [" + newsEntry.getClass() + "] with " + pageable);

		Criteria credentialCriteria = getCurrentSession().createCriteria(newsEntry.getClass(), "object");

		if (condition)
			credentialCriteria.add(Restrictions.in(propertyName, id));

		Long totatCount = generalDao.countCriteriaQuery(credentialCriteria);

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		if (null != pageable.getSort()) {
			if (pageable.getSort().iterator().next().getDirection().isAscending())
				credentialCriteria.addOrder(Order.asc(pageable.getSort().iterator().next().getProperty()));
			else if (pageable.getSort().iterator().next().getDirection().isDescending())
				credentialCriteria.addOrder(Order.desc(pageable.getSort().iterator().next().getProperty()));

		}

		List<T> data = (List<T>) generalDao.findAllCriteriaQuery(newsEntry, credentialCriteria);

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataHQL(Pageable pageable, T newsEntry, String query, List<Map<String, Object>> datas,
			boolean condition) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getDataHQL() - From [" + newsEntry.getClass() + "] with " + pageable);

		@SuppressWarnings("deprecation")
		Criteria credentialCriteria = getCurrentSession().createCriteria(newsEntry.getClass(), "object");

		if (condition) {
			for (Map<String, Object> map : datas) {

				credentialCriteria.add(Restrictions.in(map.get("name").toString(), map.get("value")));

			}
		}
		System.out.println(credentialCriteria);

		Long totatCount = generalDao.countCriteriaQuery(credentialCriteria);

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		if (null != pageable.getSort()) {
			if (pageable.getSort().iterator().next().getDirection().isAscending())
				credentialCriteria.addOrder(Order.asc(pageable.getSort().iterator().next().getProperty()));
			else if (pageable.getSort().iterator().next().getDirection().isDescending())
				credentialCriteria.addOrder(Order.desc(pageable.getSort().iterator().next().getProperty()));

		}

		List<T> data = (List<T>) generalDao.findAllCriteriaQuery(newsEntry, credentialCriteria);

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataSQL(Pageable pageable, T newsEntry, String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getDataSQL() - From [" + query + "] with " + pageable);

		if (null != pageable.getSort()) {
			query = query + "  ORDER BY " + pageable.getSort().toString().replace(":", " ");

		}

		Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query).addEntity(newsEntry.getClass());

		Long totatCount = (long) credentialCriteria.list().size();

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		List<T> data = (List<T>) credentialCriteria.list();

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataSQLForApp(Pageable pageable, T newsEntry, String query)
			throws UserNotFoundException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getDataSQLForApp() - From [" + query + "] with " + pageable);

		Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query).addEntity(newsEntry.getClass());

		Long totatCount = (long) credentialCriteria.list().size();

		System.out.println("totatCount :" + totatCount);
		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		// if (null != pageable.getSort()) {
		// if (pageable.getSort().iterator().next().getDirection().isAscending())
		// credentialCriteria.addOrder(Order.asc(pageable.getSort().iterator().next().getProperty()));
		// else if (pageable.getSort().iterator().next().getDirection().isDescending())
		// credentialCriteria.addOrder(Order.desc(pageable.getSort().iterator().next().getProperty()));
		//
		// }

		List<T> data = (List<T>) credentialCriteria.list();

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAlias(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);
			System.out.println(null != pageable.getSort());
			if (null != pageable.getSort()) {
				query = query + "  ORDER BY " + pageable.getSort().toString().replace(":", " ");

			}
			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<T> data = (List<T>) credentialCriteria.list();

			PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

			return (PagedResult<T>) pagedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasForApp(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			// if (null != pageable.getSort()) {
			// if (pageable.getSort().iterator().next().getDirection().isAscending())
			// credentialCriteria.addOrder(Order.asc(pageable.getSort().iterator().next().getProperty()));
			// else if (pageable.getSort().iterator().next().getDirection().isDescending())
			// credentialCriteria.addOrder(Order.desc(pageable.getSort().iterator().next().getProperty()));
			//
			// }

			List<T> data = (List<T>) credentialCriteria.list();

			PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

			return (PagedResult<T>) pagedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasForAPP(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
			// if (null != pageable.getSort()) {
			// if (pageable.getSort().iterator().next().getDirection().isAscending())
			// credentialCriteria.addOrder(Order.asc(pageable.getSort().iterator().next().getProperty()));
			// else if (pageable.getSort().iterator().next().getDirection().isDescending())
			// credentialCriteria.addOrder(Order.desc(pageable.getSort().iterator().next().getProperty()));
			//
			// }

			List<T> data = (List<T>) credentialCriteria.list();

			PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

			return (PagedResult<T>) pagedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasforStation(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<T> data = (List<T>) credentialCriteria.list();

			PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

			return (PagedResult<T>) pagedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasforcharging(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<T> data = (List<T>) credentialCriteria.list();

			PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

			return (PagedResult<T>) pagedResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasforimage(Pageable pageable, String query)
			throws UserNotFoundException, ServerException {

		// TODO Auto-generated method stub

		LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

		Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

		Long totatCount = (long) credentialCriteria.list().size();

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List<T> data = (List<T>) credentialCriteria.list();

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));

		return (PagedResult<T>) pagedResult;

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasforcluster(Pageable pageable, String query)
			throws UserNotFoundException, ServerException {

		// TODO Auto-generated method stub

		LOGGER.info("DataTableDaoImpl.getDataAlias() - From [" + query + "] with " + pageable);

		Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

		Long totatCount = (long) credentialCriteria.list().size();

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List<T> data = (List<T>) credentialCriteria.list();

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));
		if (pagedResult.getNumberOfElements() == 0)
			throw new ServerException(Error.SITE_CLUSTER_NOT_EXIST.toString(),
					Integer.toString(Error.SITE_CLUSTER_NOT_EXIST.getCode()));

		return (PagedResult<T>) pagedResult;

	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDriverGroup(Pageable pageable, T newsEntry) throws UserNotFoundException {

		LOGGER.info("DataTableDaoImpl.getDriverGroup() - From [" + newsEntry.getClass() + "] with " + pageable);

		String query = "Select id, groupName, (SELECT COUNT(id) FROM driverProfileGroup_userId WHERE id= dg.id)AS count From driver_profile_groups dg";

		Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

		Long totatCount = (long) credentialCriteria.list().size();

		credentialCriteria.setMaxResults(pageable.getPageSize());

		credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

		credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

		List<T> data = (List<T>) credentialCriteria.list();

		PagedResult<T> pagedResult = new PagedResult<T>(getPage(data, pageable, totatCount));
		return (PagedResult<T>) pagedResult;

	}

	@Override
	public <T> PagedResult<T> getStationByConectortType(String id, Pageable pageable, T newsEntry)
			throws UserNotFoundException {
		// TODO Auto-generated method stub

		String hql = "Select s.id, s.referNo,  s.stationName ,CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country) AS address, "
				+ " st.siteName AS sites, s.evseType , s.stationAvailStatus As stnStatus,s.stationTimeStamp as LastContactTime "
				+ " FROM station s INNER JOIN site st ON st.siteId = s.siteId  INNER JOIN port p ON p.station_id = s.id "
				+ " WHERE p.standard=" + id
				+ " AND s.preProduction = 0 GROUP BY s.id, s.referNo, CONCAT(  st.streetNo,' ',st.streetName,',', st.city,' ', st.postal_code,' ',st.country), s.stationName,"
				+ " st.siteName , s.evseType , s.stationAvailStatus ,s.stationTimeStamp  ";

		PagedResult<T> pagedResult = getDataAliasforcharging(pageable, hql);

		return pagedResult;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> PagedResult<T> getDataAliasWithJSONQuery(Pageable pageable, String query) throws UserNotFoundException {
		try {
			// TODO Auto-generated method stub

			LOGGER.info("DataTableDaoImpl.getDataAliasWithJSONQuery() - From [" + query + "] with " + pageable);

			Query<?> credentialCriteria = getCurrentSession().createSQLQuery(query);

			Long totatCount = (long) credentialCriteria.list().size();

			credentialCriteria.setMaxResults(pageable.getPageSize());

			credentialCriteria.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());

			credentialCriteria.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);

			List<Map<String, Object>> data = (List<Map<String, Object>>) credentialCriteria.list();

			System.out.println(data);

			ObjectMapper objectMapper = new ObjectMapper();

			TypeReference<List<Map<String, Object>>> mapType = new TypeReference<List<Map<String, Object>>>() {
			};
			PagedResult<T> pagedResult;
			List<T> response = new ArrayList<>();
			if (data.size() > 0 && data.get(0).get("JSON_F52E2B61-18A1-11d1-B105-00805F49916B") != null) {
				response = (List<T>) objectMapper
						.readValue(data.get(0).get("JSON_F52E2B61-18A1-11d1-B105-00805F49916B").toString(), mapType);

				pagedResult = new PagedResult<T>(getPage(response, pageable, totatCount));

				return (PagedResult<T>) pagedResult;
			} else {

				pagedResult = new PagedResult<T>(getPage(response, pageable, totatCount));
				return (PagedResult<T>) pagedResult;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
