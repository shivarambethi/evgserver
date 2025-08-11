package com.evgateway.server.dao.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.GeneralDao;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.GridKeyRequests;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@Repository
@SuppressWarnings("hiding")
public class GeneralDaoImpl<T extends BaseEntity, I> implements GeneralDao<T, I> {

	@Autowired
	protected SessionFactory sessionFactory;

	Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}

	final static Logger LOGGER = LoggerFactory.getLogger(GeneralDaoImpl.class);

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAll(T newsEntry) {
		LOGGER.info("GeneralDaoImpl.findAll() - From [" + newsEntry.getClass() + "]");
		@SuppressWarnings("deprecation")
		final Criteria crit = getCurrentSession().createCriteria(newsEntry.getClass());
		return crit.list();
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	@Transactional(readOnly = true)
	public <T> T findOneById(T newsEntry, long id) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.findOneById() - From [" + newsEntry + "]");

		if (newsEntry == null) {
			throw new UserNotFoundException(newsEntry.getClass() + " id [" + id + "] not found");
		} else {
			newsEntry = (T) getCurrentSession().get(newsEntry.getClass(), id);
			return newsEntry;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> T save(T newsEntry) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.save() - [" + newsEntry.getClass() + "]");
		getCurrentSession().save(newsEntry);
		return newsEntry;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> T update(T newsEntry) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.update() - [" + newsEntry.getClass() + "]");
		getCurrentSession().update(newsEntry);
		return newsEntry;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> T savOrupdate(T newsEntry) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.savOrupdate() - [" + newsEntry.getClass() + "]");
		getCurrentSession().saveOrUpdate(newsEntry);
		return newsEntry;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> T evict(T newsEntry) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.evict() - [" + newsEntry.getClass() + "]");
		getCurrentSession().evict(newsEntry);
		return newsEntry;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void flush() throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.flush() - []");
		getCurrentSession().flush();

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> void delete(T newsEntry) throws UserNotFoundException {
		LOGGER.info("GeneralDaoImpl.delete() - [" + newsEntry.getClass() + "]");
		getCurrentSession().delete(newsEntry);

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public <T> T merge(T newsEntry) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.merge() - [" + newsEntry.getClass() + "]");
		getCurrentSession().merge(newsEntry);
		return newsEntry;
	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllSQLQuery(T newsEntry, String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAllSQLQuery() - [" + query + "] ");
		@SuppressWarnings({ "unchecked", })
		List<T> list = getCurrentSession().createSQLQuery(query).addEntity(newsEntry.getClass()).list();
		return list;
	}

	@SuppressWarnings({ "unchecked", })
	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllHQLQuery(T newsEntry, String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAllHQLQuery() - [" + query + "] ");
		List<T> list = getCurrentSession().createQuery(query).list();
		return list;
	}

	@Override
	@Transactional(readOnly = true)
	public List<?> findAllByIdSQLQuery(String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.getAllIdsBySQLQuery() - [" + query + "] ");

		return getCurrentSession().createSQLQuery(query).list();
	}

	@SuppressWarnings({ "unchecked", "null" })
	@Override
	@Transactional(readOnly = true)
	public <T> T findOneSQLQuery(T newsEntry, String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findOneSQLQuery() - [" + query + "]");
		newsEntry = (T) getCurrentSession().createSQLQuery(query).addEntity(newsEntry.getClass()).uniqueResult();

		return newsEntry;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> T findOneHQLQuery(T newsEntry, String query) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findOneHQLQuery() - [" + query + "]");
		newsEntry = (T) getCurrentSession().createQuery(query).uniqueResult();

		return newsEntry;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllCriteriaQuery(T newsEntry, Criteria criteria) throws UserNotFoundException {
		// TODO Auto-generated method stub
		criteria.setProjection(null);
		LOGGER.info("GeneralDaoImpl.findAllCriteriaQuery() - [" + criteria + "]");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		// criteria.setResultTransformer(DistinctRootEntityResultTransformer.INSTANCE);
		List<T> data = criteria.list();

		return data;

	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllCriteriaQueryManyToMany(T newsEntry, TypedQuery<T> criteria)
			throws UserNotFoundException {
		// TODO Auto-generated method stub

		LOGGER.info("GeneralDaoImpl.findAllCriteriaQueryManyToMany() - [" + criteria + "]");
		List<T> data = criteria.getResultList();
		if (data.size() == 0)
			throw new UserNotFoundException(criteria + " data not found");
		else {
			return data;
		}

	}

	@Override
	@Transactional(readOnly = true)
	public Long countCriteriaQuery(Criteria criteria) throws UserNotFoundException {
		// TODO Auto-generated method stub
		criteria.setProjection(Projections.rowCount());
		LOGGER.info("GeneralDaoImpl.countCriteriaQuery() - [" + criteria + "]");
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (Long) criteria.uniqueResult();

	}

	@Override
	@Transactional(readOnly = true)
	public Long countSQL(String sql) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.countSQL() - [" + sql + "]");

		return (long) getCurrentSession().createSQLQuery(sql).list().size();

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllByIdHQLQuery(T newsEntry, String query, String parameter, List<Long> ids) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.getAllByIdHQLQuery() - [" + query + " with " + ids + " ] ");
		Query<?> q = getCurrentSession().createQuery(query);
		q.setParameterList(parameter, ids);
		return (List<T>) q.list();

	}

	@Override
	@Transactional(readOnly = true)
	public List<?> findAllSingalObject(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAllSingalObject() - [" + query + "  ] ");

		return getCurrentSession().createSQLQuery(query).list();

	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> findAllSingalObjectForsingle(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAllSingalObject() - [" + query + "  ] ");

		return getCurrentSession().createSQLQuery(query).list();

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> List<Map<String, Object>> findAliasData(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAliasData() - [" + query + "  ] ");

		return getCurrentSession().createSQLQuery(query).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> List<Map<String, String>> findAliasDataforac(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAliasData() - [" + query + "  ] ");

		return getCurrentSession().createSQLQuery(query).setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

	}

	@Override
	@Transactional(readOnly = true)
	public <T> void queryExecute(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.queryExecute() - [" + query + "  ] ");
		getCurrentSession().createSQLQuery(query).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> findAllNamedQuery(String queryName, String fieldName, List<?> ls) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.findAllNamedQuery() - [" + queryName + "  ] ");
		return getCurrentSession().getNamedQuery(queryName).setParameterList(fieldName, ls).list();
	}

	@Override
	@Transactional(readOnly = true)
	public String getSingleRecord(String hql) {

		String result = (String) getCurrentSession().createSQLQuery(hql).setMaxResults(1).uniqueResult();

		if (result == null)
			return "";

		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public String getSingleString(String hql) {

		String result = findAllSingalObject(hql).get(0).toString();

		if (result == null)
			return "";

		return result;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Transactional(readOnly = true)
	public <T> List<Map<String, Object>> findAliasData(String query, String parameter, String parameterValue) {
		LOGGER.info("GeneralDaoImpl.findAliasData() - [" + query + " with [parameter,parameterValue] = [" + parameter
				+ "," + parameterValue + " ] ");
		return getCurrentSession().createQuery(query).setParameter(parameter, parameterValue)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
	}

	@SuppressWarnings("deprecation")
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getMapData(String hql) {

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapObj = getCurrentSession().createSQLQuery(hql)
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP).list();

		return mapObj;
	}

	@Override
	@Transactional
	public String deleteSqlQuiries(String hql) {
		LOGGER.info("GeneralDaoImpl.deleteSqlQuiries() - From [" + hql + "] ");

		sessionFactory.getCurrentSession().createSQLQuery(hql).executeUpdate();

		return "success";
	}

	@Override
	public long findIdBySqlQuery(String hql) {
		LOGGER.info("GeneralDaoImpl.findIdBySqlQuery() - From [" + hql + "] ");

		BigDecimal id = (BigDecimal) getCurrentSession().createSQLQuery(hql).uniqueResult();

		return id == null ? 0 : id.longValue();
	}

	@Override
	public Float findIdByOneSqlQuery(String hql) {
		LOGGER.info("GeneralDaoImpl.findIdBySqlQuery() - From [" + hql + "] ");

		String id = (String) getCurrentSession().createSQLQuery(hql).uniqueResult();

		return id != null ? Float.valueOf(id) : 0;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public <T> List<T> findAllWithMaxRecord(T newsEntry, String query, int limit) {
		// TODO Auto-generated method stub
		LOGGER.info(
				"GeneralDaoImpl.findAllWithMaxRecord() - From [" + newsEntry.getClass() + "] with limit = " + limit);

		return getCurrentSession().createQuery(query).setMaxResults(limit).list();
	}

	@Override
	@Transactional(readOnly = true)
	public <T> List<T> getUserByRole(T newsEntry, String roleName) throws UserNotFoundException {
		// TODO Auto-generated method stub
		LOGGER.info("DataTableDaoImpl.getUsersByRole() - From [" + newsEntry.getClass() + "] ");

		CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> from = cq.from(User.class);
		Join<User, Role> join = from.join("roles", JoinType.LEFT);
		cq.where(cb.equal(join.get("rolename"), roleName));

		@SuppressWarnings("unchecked")
		TypedQuery<T> criteria = (TypedQuery<T>) getCurrentSession().createQuery(cq);

		List<T> data = findAllCriteriaQueryManyToMany(newsEntry, criteria);

		return data;
	}

	@Override
	@Transactional(readOnly = true)
	public int updateSqlQuery(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.queryExecute() - [" + query + "  ] ");
		return getCurrentSession().createSQLQuery(query).executeUpdate();
	}

	/*
	 * @SuppressWarnings({ "unchecked", "deprecation" })
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public List<Map<String, Object>>
	 * findAliasDataWithPageLimit(String query, int startRecNo, int noOfRecords) {
	 * LOGGER.info("GeneralDaoImpl.findAliasData() - [" + query + " ] "); return
	 * getCurrentSession().createSQLQuery(query).setResultTransformer(Criteria.
	 * ALIAS_TO_ENTITY_MAP)
	 * .setFirstResult(startRecNo).setMaxResults(noOfRecords).list(); }
	 */
	/*
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public <T> List<T> getStationsByLatLng(T
	 * newsEntry,String lat, String lng) throws UserNotFoundException { // TODO
	 * Auto-generated method stub
	 * LOGGER.info("DataTableDaoImpl.getUsersByRole() - From [" +
	 * newsEntry.getClass() + "] ");
	 * 
	 * CriteriaBuilder cb = getCurrentSession().getCriteriaBuilder();
	 * CriteriaQuery<Station> cq = cb.createQuery(Station.class); Root<Station> from
	 * = cq.from(Station.class); Join<Station, GeoLocation> join =
	 * from.join("coordinateId", JoinType.LEFT);
	 * cq.where(cb.equal(join.get("latitude"), lat));
	 * cq.where(cb.equal(join.get("longitude"), lng));
	 * 
	 * @SuppressWarnings("unchecked") TypedQuery<T> criteria = (TypedQuery<T>)
	 * getCurrentSession().createQuery(cq);
	 * 
	 * List<T> data = findAllCriteriaQueryManyToMany(newsEntry, criteria);
	 * 
	 * return data; }
	 */

	/*
	 * @Override
	 * 
	 * @Transactional public void excuteUpdateUserData(String query) { // TODO
	 * Auto-generated method stub LOGGER.info("GeneralDaoImpl.queryExecute() - [" +
	 * query + "  ] "); getCurrentSession().createSQLQuery(query).executeUpdate(); }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public String listOfStringData(String
	 * queryString) {
	 * 
	 * return
	 * getCurrentSession().createSQLQuery(queryString).list().toString().replace(
	 * "[", "'").replace("]", "'") .replace(", ", "','");
	 * 
	 * }
	 * 
	 * @Override public long getCountByUserId(Long id) throws ServerException {
	 * 
	 * Query query =
	 * getCurrentSession().createQuery("From GridKeyRequests where userId = :id ");
	 * query.setParameter("id", id); List gkRequestList = query.list(); long
	 * count=0; Iterator<GridKeyRequests> iterator = gkRequestList.iterator(); while
	 * (iterator.hasNext()) { count += iterator.next().getCount(); } return count;
	 * 
	 * }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public User getUser(long userId) throws
	 * UserNotFoundException { User userObject = (User)
	 * getCurrentSession().get(User.class,userId); if (userObject == null) { throw
	 * new UserNotFoundException("User id [" + userId + "] not found"); } else {
	 * return userObject; } }
	 * 
	 * @Override
	 * 
	 * @Transactional(readOnly = true) public int updateSqlQuery(String query) { //
	 * TODO Auto-generated method stub
	 * LOGGER.info("GeneralDaoImpl.queryExecute() - [" + query + "  ] "); return
	 * getCurrentSession().createSQLQuery(query).executeUpdate(); }
	 * 
	 * @Override public BigDecimal getSingleRecordofOrgId(String hql) {
	 * 
	 * BigDecimal result = (BigDecimal)
	 * getCurrentSession().createSQLQuery(hql).setMaxResults(1).uniqueResult();
	 * 
	 * return result; }
	 */

	@Override
	@Transactional
	public void excuteUpdateUserData(String query) {
		// TODO Auto-generated method stub
		LOGGER.info("GeneralDaoImpl.queryExecute() - [" + query + "  ] ");
		getCurrentSession().createSQLQuery(query).executeUpdate();
	}

	@Override
	@Transactional(readOnly = true)
	public String listOfStringData(String queryString) {

		return getCurrentSession().createSQLQuery(queryString).list().toString().replace("[", "'").replace("]", "'")
				.replace(", ", "','");

	}

	@Override
	public long getCountByUserId(Long id) throws ServerException {

		Query query = getCurrentSession().createQuery("From GridKeyRequests where userId = :id ");
		query.setParameter("id", id);
		List gkRequestList = query.list();
		long count = 0;
		Iterator<GridKeyRequests> iterator = gkRequestList.iterator();
		while (iterator.hasNext()) {
			count += iterator.next().getCount();
		}
		return count;

	}

	@Override
	@Transactional(readOnly = true)
	public User getUser(long userId) throws UserNotFoundException {
		User userObject = (User) getCurrentSession().get(User.class, userId);
		if (userObject == null) {
			throw new UserNotFoundException("User id [" + userId + "] not found");
		} else {
			return userObject;
		}
	}

	@Override
	public BigDecimal getSingleRecordofOrgId(String hql) {
		BigDecimal result = (BigDecimal) getCurrentSession().createSQLQuery(hql).setMaxResults(1).uniqueResult();
		return result;
	}

	@Override
	public String findIdBySqlQueryString(String hql) {

		Timestamp id = (Timestamp) getCurrentSession().createSQLQuery(hql).uniqueResult();
		if (id == null)
			return "0";
		else
			return id.toString();
	}

}
