package com.evgateway.server.services;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.evgateway.server.controller.advice.ServerException;
import com.evgateway.server.exception.UserNotFoundException;
import com.evgateway.server.form.UserResigtarion;
import com.evgateway.server.pojo.Address;
import com.evgateway.server.pojo.Password;
import com.evgateway.server.pojo.Role;
import com.evgateway.server.pojo.User;

public interface UserService extends UserDetailsService {
//	public User getCurrentUser() throws UserNotFoundException, ServerException;

	public Role getCurrentRole();

	Map<String, Object> getOrgData(long orgId, String referNo);

	User getUser(long id) throws UserNotFoundException, ServerException;

	Role getRole(String roleName) throws UserNotFoundException;

	User getUser(String username) throws UserNotFoundException, ServerException;

	Long getUserStatus(String username) throws UserNotFoundException, ServerException;

	void registerUser(UserResigtarion userForm) throws ServerException, UserNotFoundException, ServerException;

	boolean isUsrPwdExprd(long userId) throws UserNotFoundException, ServerException;

	Map<String, Object> getColor(Long orgId);

	User findUserByEmail(String userEmail) throws ServerException, UserNotFoundException;

	void setPassword(Map<String, Object> map) throws ServerException, UserNotFoundException;

	void createPasswordResetTokenForUser(User user, String token) throws UserNotFoundException;

	User findUserByEmailForPassword(String userEmail) throws ServerException, UserNotFoundException;

	void activateUser(String email) throws ServerException, UserNotFoundException;

	void updatePassword(String id, Map<String, Object> password, boolean check)
			throws UserNotFoundException, ServerException;

	Password getPasswordResetToken(String token) throws ServerException, UserNotFoundException;

	Password changeUserPassword(User user, String password) throws ServerException, UserNotFoundException;

	User getUserByUID(String uid) throws UsernameNotFoundException;

	String generatePassword();

	List<Address> getUserByPhone(String phoneNumber) throws UserNotFoundException;

	String getLogo(Long orgId, String type) throws UserNotFoundException;
}
