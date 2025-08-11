package com.evgateway.server.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;

import org.hibernate.Criteria;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.User;

@SuppressWarnings("hiding")
public interface GeneralDao<T extends BaseEntity, I> {

	<T> T update(T newsEntry) throws UserNotFoundException;

	<T> T savOrupdate(T newsEntry) throws UserNotFoundException;

	<T> void delete(T newsEntry) throws UserNotFoundException;

	<T> T save(T type) throws UserNotFoundException;

	<T> T merge(T newsEntry);

	<T> T findOneById(T newsEntry, long id) throws UserNotFoundException;

	<T> List<T> findAll(T newsEntry);

	<T> T findOneSQLQuery(T newsEntry, String query) throws UserNotFoundException;

	<T> T findOneHQLQuery(T newsEntry, String query) throws UserNotFoundException;

	<T> List<T> findAllSQLQuery(T newsEntry, String query) throws UserNotFoundException;

	<T> List<T> findAllHQLQuery(T newsEntry, String query) throws UserNotFoundException;

	Long countSQL(String sql) throws UserNotFoundException;

	<T> List<T> findAllByIdHQLQuery(T newsEntry, String query, String parameter, List<Long> ids);

	<T> List<T> findAllCriteriaQuery(T newsEntry, Criteria criteria) throws UserNotFoundException;

	Long countCriteriaQuery(Criteria criteria) throws UserNotFoundException;

	<T> List<T> findAllCriteriaQueryManyToMany(T newsEntry, TypedQuery<T> criteria) throws UserNotFoundException;

	List<?> findAllByIdSQLQuery(String query) throws UserNotFoundException;

	<T> void queryExecute(String query);

	<T> List<Map<String, Object>> findAliasData(String hql);
	<T> List<Map<String, String>> findAliasDataforac(String hql);

	List<?> findAllSingalObject(String query);

	<T> List<T> findAllNamedQuery(String queryName, String fieldName, List<?> ls);

	public String getSingleRecord(String hql);

	public List<Map<String, Object>> getMapData(String hql);

	<T> List<Map<String, Object>> findAliasData(String query, String parameter, String parameterValue);

	public String deleteSqlQuiries(String hql);

	public long findIdBySqlQuery(String string);

	public Float findIdByOneSqlQuery(String hql);

	<T> List<T> findAllWithMaxRecord(T newsEntry, String query, int limit);

	<T> List<T> getUserByRole(T newsEntry, String roleName) throws UserNotFoundException;

	public void excuteUpdateUserData(String query);

	String listOfStringData(String queryString);

	long getCountByUserId(Long long1) throws ServerException;

	User getUser(long userId) throws UserNotFoundException;

	int updateSqlQuery(String query);

	BigDecimal getSingleRecordofOrgId(String hql);

	String findIdBySqlQueryString(String query);

	<T> T evict(T newsEntry) throws UserNotFoundException;

	void flush() throws UserNotFoundException;

	String getSingleString(String hql);

	List<String> findAllSingalObjectForsingle(String query);

}
