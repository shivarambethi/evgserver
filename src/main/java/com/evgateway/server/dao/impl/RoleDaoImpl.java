package com.evgateway.server.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.dao.RoleDao;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Menu;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@Repository
public class RoleDaoImpl<I> extends DaoImpl<BaseEntity, I> implements RoleDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(RoleDaoImpl.class);

	

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteRole(long id) throws UserNotFoundException {
		Role role = getRole(id);
		if (role != null) {
			getCurrentSession().delete(role);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Object obj) {
		try {
			getCurrentSession().save(obj);
		} catch (Exception e) {
			logger.error("Save method is  :" + e);
		}

	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Role getRole(String roleName) throws UserNotFoundException {

		Query query = getCurrentSession().createQuery("from Role where rolename = :rolename ");
		query.setString("rolename", roleName);

		logger.info(query.toString());

		if (query.list().size() == 0) {
			throw new UserNotFoundException("User [" + roleName + "] not found");
		} else {
			logger.info("User List Size: " + query.list().size());
			List<Role> list = (List<Role>) query.list();
			Role roleObject = (Role) list.get(0);
			logger.info("Role Object : " + roleObject);
			return roleObject;
		}
	}

	@Override
	public User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			return (User) auth.getPrincipal();
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Role> getRoles() {

		String hql = "FROM Role r ORDER BY r.id";
		return getCurrentSession().createQuery(hql).list();

	}

	@Override
	@Transactional(readOnly = true)
	public Role getRole(long id) throws UserNotFoundException {
		logger.debug("UserDAOImpl.getRole() - [" + id + "]");
		Role roleObject = (Role) getCurrentSession().get(Role.class, id);

		if (roleObject == null) {
			throw new UserNotFoundException("Role id [" + id + "] not found");
		} else {
			return roleObject;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Role addRole(Role role) throws DuplicateUserException {

		logger.debug("UserDAOImpl.addRole() - [" + role.getRolename() + "]");
		try {

			Role roleCheck = getRole(role.getRolename());
			String message = "The user [" + roleCheck.getRolename() + "] already exists";
			throw new DuplicateUserException(message);
		} catch (UserNotFoundException e) {
			return (Role) getCurrentSession().merge(role);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateRole(Role role) throws DuplicateUserException, UserNotFoundException {

		Role roleToUpdate = getRole(role.getId());

		try {
			Role roleCheck = getRole(role.getRolename());
			if (roleCheck.getId() == roleToUpdate.getId()) {

				roleToUpdate.setRolename(role.getRolename());
				getCurrentSession().update(roleToUpdate);

			} else {
				String message = "The user [" + roleCheck.getRolename() + "] already exists";
				throw new DuplicateUserException(message);
			}
		} catch (UserNotFoundException e) {
			roleToUpdate.setRolename(role.getRolename());

			getCurrentSession().update(roleToUpdate);
		}

	}

	@Override
	public void setRolebyId(Long id, boolean b) {
		// getCurrentSession().createSQLQuery("Update users")

	}

	@Override
	public List<Role> getListOfRoles() {
		logger.info("UserDAOImpl.getListOfRoles() ");

		return null;

	}

	@Override
	public List<Menu> getMenus() {
		// TODO Auto-generated method stub
		String hql = "FROM Menu r ORDER BY r.id";
		return getCurrentSession().createQuery(hql).list();
	}

}
