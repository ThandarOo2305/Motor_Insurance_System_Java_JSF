package org.ace.java.component.persistence;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.internal.sessions.AbstractSession;
import org.eclipse.persistence.sessions.DatabaseLogin;
import org.eclipse.persistence.sessions.Session;

public class JPASessionCustomizer implements SessionCustomizer {
	public void customize(Session session) {
		((AbstractSession) session).setIsConcurrent(true);
		DatabaseLogin login = (DatabaseLogin) session.getDatasourceLogin();
		login.setTransactionIsolation(DatabaseLogin.TRANSACTION_READ_UNCOMMITTED);
	}
}
