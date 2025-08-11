package com.evgateway.server.dao;

import java.util.List;
import java.util.Map;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

public interface UserDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public User addUser(User user) throws DuplicateUserException;

	public User getUser(long userId) throws UserNotFoundException;

	public User getUser(String username) throws UserNotFoundException;

	public void updateUser(User user) throws UserNotFoundException, DuplicateUserException;

	public void deleteUser(long userId) throws UserNotFoundException;

	public List<User> getUsers();

	public void save(Object obj);

	void saveOrUpdate(Object obj);

//	public User getCurrentUser();

	

	public Password getPassword(User user);

	

	public void addUsersinGroups(Long group_id, Integer user_id2);

	public void addUsersinRoles(Long id, Long id2);

	public Object getEmail(String email);

	public User findUserByEmail(String userEmail) throws ServerException, UserNotFoundException;

	public Password findByToken(String token) throws ServerException;

	public Object changeUserPassword(User user, String password) throws ServerException;

	

	public List<User> getAssignList();

	Role getCurrentRole();

	public List<User> getManagerList();

	public List<?> getUserNodeRef(String userName) throws UserNotFoundException;

	public List<Map<Object, Object>> getipandport();

	public List<User> getAssignListWithoutEndUser();

	public List<User> getAssignListWithoutManager();



	public User getPhone(String phone);

	public List<User> getDriverList();

	public List<User> getSubOwnerList();

	public List<User> getOwnerList();

	public List<User> getDealerList();

	public List<Address> getUserByPhone(String phone);

	public List<Address> getUserBySecondryPhone(String secondaryPhone);

	public User getUserByEmail(String email) throws UserNotFoundException;

	public User getUsers(String userName) throws UserNotFoundException;

	public String getVehicles(String vin) throws ServerException;

}
