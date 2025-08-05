package org.ace.accounting.process.service;

import org.ace.accounting.process.interfaces.IUserProcessService;
import org.ace.accounting.user.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

@Service(value = "UserProcessService")
@Scope(value = "session", proxyMode = ScopedProxyMode.INTERFACES)
public class UserProcessService implements IUserProcessService {

	private User user;

	@Override
	public void registerUser(User user) {
		this.user = user;
	}

	@Override
	public User getLoginUser() {
		return user;
	}
}
