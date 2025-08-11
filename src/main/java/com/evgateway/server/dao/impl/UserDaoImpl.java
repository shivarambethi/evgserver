package com.evgateway.server.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.evgateway.server.cenum.Roles;
import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.dao.UserDao;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.messages.Error;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

@Repository
@Component
public class UserDaoImpl<I> extends DaoImpl<BaseEntity, I> implements UserDao<BaseEntity, I> {

	static Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	String userName = null, firstName = null, lastName = null, email = null, password = null;

	/*
	 * @Autowired private PasswordEncoder passwordEncoder;
	 */

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public User addUser(User user) throws DuplicateUserException {
		try {
			User userCheck = getUser(user.getUsername());
			String message = "The user [" + userCheck.getUsername() + "] already exists";
			throw new DuplicateUserException(message);
		} catch (UserNotFoundException e) {

			getCurrentSession().save(user);
			return user;
		}
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

	@SuppressWarnings("unchecked")
	@Override
	public User getUser(String usersName) throws UserNotFoundException {

		Query query = getCurrentSession().createQuery(
				"from User u INNER JOIN Address a On a.user.id = u.id where u.enabled is true and u.username = :usersName Or u.email =:usersName Or a.phone=:usersName");
		query.setString("usersName", usersName);
		if (query.list().size() == 0) {
			throw new UserNotFoundException("User [" + usersName + "] not found");
		} else {

			List<User> list = (List<User>) query.list();
			User userObject = (User) list.get(0);

			return userObject;
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUser(User user) throws UserNotFoundException, DuplicateUserException {
		User userToUpdate = getUser(user.getId());

		try {
			User userCheck = getUser(user.getUsername());

			if (userCheck.getId() == userToUpdate.getId()) {
				userToUpdate.setEnabled(user.isEnabled());
				userToUpdate.setPasswords(user.getPasswords());
				// System.out.println("@@@@@Update method " + user.getPasswords());
				userToUpdate.setUsername(user.getUsername());
				userToUpdate.setRoles(user.getRoles());
				getCurrentSession().update(userToUpdate);
			} else {
				String message = "The user [" + userCheck.getUsername() + "] already exists";
				throw new DuplicateUserException(message);
			}
		} catch (UserNotFoundException e) {
			userToUpdate.setEnabled(user.isEnabled());
			userToUpdate.setPasswords(user.getPasswords());
			// System.out.println("######Update method in catch " + user.getPasswords());
			userToUpdate.setUsername(user.getUsername());
			userToUpdate.setRoles(user.getRoles());
			getCurrentSession().update(userToUpdate);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(long userId) throws UserNotFoundException {
		User user = getUser(userId);

		if (user != null) {

			getCurrentSession().delete(user);

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public List<User> getUsers() {

		return getCurrentSession().getNamedQuery("@HQL_GET_ALL_USERS").list();
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Object obj) {
		try {
			getCurrentSession().save(obj);

		} catch (Exception e) {
			logger.error("Error while updating the details : " + obj);
		}

	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveOrUpdate(Object obj) {
		try {
			getCurrentSession().saveOrUpdate(obj);
		} catch (Exception e) {
			logger.error("Save method is  :" + e);
		}

	}

	@SuppressWarnings("unchecked")
	public Role getRole(String roleName) throws UserNotFoundException {

		Query query = getCurrentSession().createQuery("from Role where rolename = :rolename ");
		query.setString("rolename", roleName);

		if (query.list().size() == 0) {
			throw new UserNotFoundException("User [" + roleName + "] not found");
		} else {
			List<Role> list = (List<Role>) query.list();
			Role roleObject = (Role) list.get(0);
			return roleObject;
		}
	}

//	@Override
//	public User getCurrentUser() {
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		if (auth != null && auth.getPrincipal() instanceof UserDetails) {
//			return (User) auth.getPrincipal();
//		} else
//			return null;
//	}

	@Override
	public Role getCurrentRole() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			for (GrantedAuthority role : auth.getAuthorities())
				return (Role) role;
		else
			return null;
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Password getPassword(User user) {

		String hql = "FROM Password where user = :user";
		List pls = getCurrentSession().createQuery(hql).setParameter("user", user).list();
		Password password = null;
		if (pls != null && pls.size() != 0)
			password = (Password) pls.get(0);
		return password;
	}

	@Override
	public void addUsersinGroups(Long group_id, Integer user_id) {
		// TODO Auto-generated method stub
		String queryForUsersinGroups = "INSERT Into usersingroup values(" + group_id + "," + user_id + ");";

		getCurrentSession().createSQLQuery(queryForUsersinGroups).executeUpdate();

	}

	@Override
	public void addUsersinRoles(Long role_id, Long user_id) {
		String queryForUsersinRoles = "INSERT Into usersinroles values(" + user_id + "," + role_id + ");";

		Query sqlquery = getCurrentSession().createSQLQuery(queryForUsersinRoles);

		sqlquery.executeUpdate();

	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getEmail(String email) {

		Query query = getCurrentSession().createQuery("from User where email = :email ");
		query.setString("email", email);

		List<User> list = (List<User>) query.list();

		return list;

	}

	@Override
	public User findUserByEmail(String userEmail) throws ServerException, UserNotFoundException {
		User userObject = null;
		List<?> user = getCurrentSession().createCriteria(User.class).add(Restrictions.like("email", userEmail)).list();

		if (user != null && user.size() > 0)
			userObject = (User) user.get(0);
		if (userObject == null) {
			throw new ServerException(Error.SECURITY_QUETIONS_AND_ANSWERS.toString(),
					Integer.toString(Error.SECURITY_QUETIONS_AND_ANSWERS.getCode()));
		} else {
			return userObject;
		}
	}

	@Override
	public Password findByToken(String token) throws ServerException {
		// logger.debug("UserDAOImpl.getPasswordToken() - [" + token + "]");
		Password passwordObject = null;
		List<?> tempList = getCurrentSession().createCriteria(Password.class)
				.add(Restrictions.like("passwordVerificationToken", token)).list();
		if (tempList != null && tempList.size() > 0)
			passwordObject = (Password) tempList.get(0);
		if (passwordObject == null) {
			throw new ServerException(Error.PASSWORD_FIND_BY_TOKEN.toString(),
					Integer.toString(Error.PASSWORD_FIND_BY_TOKEN.getCode()));
		} else {
			return passwordObject;
		}
	}

	@Override
	public Object changeUserPassword(User user, String password) throws ServerException {
		// TODO Auto-generated method stub
		Password passwordObject = (Password) getCurrentSession()
				.createQuery("From Password where user_id = " + user.getId()).list().get(0);
		String passwordforalf = password;
		// password = passwordEncoder.encode(password);
		passwordObject.setPassword(password);
		passwordObject.setLastChangedDate(new Date());
		getCurrentSession().saveOrUpdate(passwordObject);
		Set<Role> roles = user.getRoles();
		String urole = null;
		for (Role role : roles) {
			urole = role.getRolename();
		}

		return passwordObject;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAssignList() {
		List<String> ls = new ArrayList<String>(2);

		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAssignListWithoutEndUser() {
		List<String> ls = new ArrayList<String>(2);

		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getAssignListWithoutManager() {
		List<String> ls = new ArrayList<String>(2);

		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getManagerList() {
		List<String> ls = new ArrayList<String>(2);

		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<User> getUserNodeRef(String userName) throws UserNotFoundException {
		List<User> tempList = getCurrentSession().createCriteria(User.class)
				.add(Restrictions.like("username", userName)).list();
		return tempList;
	}

	@SuppressWarnings("unchecked")
	public List<Map<Object, Object>> getipandport() {
		String sql = "select * from alfipandport";
		SQLQuery query = getCurrentSession().createSQLQuery(sql);

		query.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP);
		List<Map<Object, Object>> result = query.list();

		return result;
	}

	@SuppressWarnings("unchecked")
	@Transactional

	@Override
	public User getPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getDriverList() {
		List<String> ls = new ArrayList<String>(2);
		ls.add(Roles.Driver.toString());
		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getSubOwnerList() {
		List<String> ls = new ArrayList<String>(2);
		ls.add(Roles.SiteHost.toString());
		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getOwnerList() {
		List<String> ls = new ArrayList<String>(2);
		ls.add(Roles.Owner.toString());
		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getDealerList() {
		List<String> ls = new ArrayList<String>(2);
		ls.add(Roles.Dealer.toString());
		return getCurrentSession().getNamedQuery("@HQL_GET_ASSIGN_USERS").setParameterList("rolename", ls).list();
	}

	@Override
	public List<Address> getUserByPhone(String phoneNo) {
		String sql = "From Address where phone = '" + phoneNo + "'";

		return getCurrentSession().createQuery(sql).list();
	}

	@Override
	public List<Address> getUserBySecondryPhone(String secondaryPhone) {
		String sql = "From Address where secondaryPhone=" + secondaryPhone;

		return getCurrentSession().createQuery(sql).list();
	}

	@Override
	public User getUserByEmail(String email) throws UserNotFoundException {
		// TODO Auto-generated method stub
		Query query = getCurrentSession().createQuery("from User where email =:usersName");
		query.setString("usersName", email);
		if (query.list().size() == 0) {
			throw new UserNotFoundException("User [" + email + "] not found");
		} else {

			List<User> list = (List<User>) query.list();
			User userObject = (User) list.get(0);

			return userObject;
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	@Transactional(readOnly = true)
	public User getUsers(String usersName) throws UserNotFoundException {
		logger.info("usersName inside getUser() : " + usersName);
		Query query = getCurrentSession().createQuery("from User where username = :usersName Or email =:usersName ");
		query.setString("usersName", usersName);
		if (query.list().size() == 0) {
			throw new UserNotFoundException("User [" + usersName + "] not found");
		} else {
			List<User> list = (List<User>) query.list();
			User userObject = (User) list.get(0);
			return userObject;
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional(readOnly = true)
	public String getVehicles(String vin) throws ServerException {
		String data2 = null;
		String hql = "select COUNT(*) from Vehicles where vin = '" + vin + "'";
		logger.info("GeneralDaoImpl.Vehicles- [" + hql + "]");
		List pls = getCurrentSession().createSQLQuery(hql).list();
		if (pls != null && (Integer) pls.get(0) > 0) {
			data2 = "A Vehicle already exists with this VIN";
		}
		return data2;
	}

}