package org.ace.accounting.user.service.interfaces;

import java.util.List;


import org.ace.accounting.user.User;
import org.ace.java.component.SystemException;

public interface IUserService {

	public List<User> findAllUser() throws SystemException;

	public User findUser(String userCode) throws SystemException;

	public boolean authenticate(String username, String password) throws SystemException;

	public User addNewUser(User user) throws SystemException;

	public User updateUser(User user) throws SystemException;

	public void deleteUser(User user) throws SystemException;

	public String getDecodedPassword(User user) throws SystemException;

	public void changePassword(String username, String newPassword, String oldPassword);
}
