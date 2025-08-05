package org.ace.accounting.user.service;

import java.util.List;


import javax.annotation.Resource;

import org.ace.accounting.user.User;
import org.ace.accounting.user.persistence.interfaces.IUserDAO;
import org.ace.accounting.user.service.interfaces.IUserService;
import org.ace.java.component.ErrorCode;
import org.ace.java.component.SystemException;
import org.ace.java.component.persistence.exception.DAOException;
import org.ace.java.component.service.BaseService;
import org.ace.java.component.service.PasswordCodecHandler;
import org.ace.java.component.service.interfaces.IDataRepService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "UserService")
public class UserService extends BaseService implements IUserService {

	@Resource(name = "UserDAO")
	private IUserDAO userDAO;

	@Resource(name = "DataRepService")
	private IDataRepService<User> dataRepService;

	@Resource(name = "PasswordCodecHandler")
	private PasswordCodecHandler codecHandler;

	/*
	 * @Autowired private PasswordEncoder encoder;
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public List<User> findAllUser() throws SystemException {
		List<User> result = null;
		try {
			result = userDAO.findAll();
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to find all of User)", e);
		}
		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User findUser(String userCode) throws SystemException {
		User user = userDAO.find(userCode);
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean authenticate(String usercode, String password) throws SystemException {
		try {
			User user = userDAO.find(usercode);
			if (user != null) {
				String encodedPassword = codecHandler.encode(password);
				if (user.getPassword().equals(encodedPassword)) {
					return true;
				}
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to change passowrd", e);
		}
		return false;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User addNewUser(User user) throws SystemException {
		try {
			user.setPassword(codecHandler.encode(user.getPassword()));
			dataRepService.insert(user);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to add a User", e);
		}
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public User updateUser(User user) throws SystemException {
		try {
			user.setPassword(codecHandler.encode(user.getPassword()));
			dataRepService.update(user);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to update user", e);
		}
		return user;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void deleteUser(User user) throws SystemException {
		try {
			dataRepService.delete(user);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to delete user", e);
		}
	}

	public String getDecodedPassword(User user) {
		return codecHandler.decode(user.getPassword());
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void changePassword(String usercode, String newPassword, String oldPassword) {
		try {
			User user = userDAO.find(usercode);
			if (user != null && oldPassword.equals(codecHandler.decode(user.getPassword()))) {
				String encryptedPassword = codecHandler.encode(newPassword);
				userDAO.changePassword(usercode, encryptedPassword);
			} else {
				throw new SystemException(ErrorCode.OLD_PASSWORD_DOES_NOT_MATCH, "Old password does not match.");
			}
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to change passowrd", e);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void resetPassword(String usercode) {
		try {
			String encryptedPassword = codecHandler.encode(User.DEFAULT_PASSWORD);
			userDAO.resetPassword(usercode, encryptedPassword);
		} catch (DAOException e) {
			throw new SystemException(e.getErrorCode(), "Failed to reset passowrd", e);
		}
	}

}