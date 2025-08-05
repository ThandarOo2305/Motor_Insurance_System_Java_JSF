package org.ace.accounting.process.interfaces;

import org.ace.accounting.user.User;

public interface IUserProcessService {
	public void registerUser(User user);
	public User getLoginUser();
}
