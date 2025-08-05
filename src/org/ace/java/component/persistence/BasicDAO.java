package org.ace.java.component.persistence;

import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.ace.java.component.ErrorCode;
import org.ace.java.component.persistence.exception.DAOException;

public class BasicDAO {
	
	@Resource(name = "SQL_ERROR_CODE")
	private Properties properties;

	@PersistenceContext
	protected EntityManager em;

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	public DAOException translate(String message, SQLException sqlex) {
		String errorCode = properties.getProperty(sqlex.getErrorCode() + "");
		if (errorCode == null || errorCode.equals("")) {
			return new DAOException(ErrorCode.NO_SQL_ERROR_CODE_CONFIG, "There is no SQL ERROR CODE(" + sqlex.getErrorCode() + ") in configuration", sqlex);
		} else {
			return new DAOException(errorCode, message, sqlex);
		}
	}

	public RuntimeException translate(String message, RuntimeException e) {
		e.printStackTrace();
		DAOException dae = null;
		Throwable throwable = e;
		while (throwable != null && !(throwable instanceof SQLException)) {
			throwable = throwable.getCause();
		}
		if (throwable instanceof SQLException) {
			dae = translate(message, (SQLException) throwable);
		}
		if (dae != null) {
			return dae;
		} else {
			return new DAOException(ErrorCode.DAO_RUNTIME_ERROR, e.getMessage(), e);
		}
	}
}
