package com.evgateway.server.dao;

import java.util.List;

import com.evgateway.server.exception.DuplicateUserException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.pojo.BaseEntity;
import com.evgateway.server.pojo.Menu;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

public interface RoleDao<T extends BaseEntity, I> extends Dao<BaseEntity, I> {

	public void save(Object obj);

	public Role getRole(String roleName) throws UserNotFoundException;

	public User getCurrentUser();

	public List<Role> getRoles();

	public Role getRole(long id) throws UserNotFoundException;;

	public Role addRole(Role role) throws DuplicateUserException;

	public void updateRole(Role role) throws DuplicateUserException, UserNotFoundException;

	public void deleteRole(long id) throws UserNotFoundException;

	public void setRolebyId(Long id, boolean b) throws UserNotFoundException;

	public List<Role> getListOfRoles();

	public List<Menu> getMenus();

}
