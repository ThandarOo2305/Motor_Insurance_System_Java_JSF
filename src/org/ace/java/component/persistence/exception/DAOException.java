package org.ace.java.component.persistence.exception;

/**
 * @author Zaw Than Oo
 */
public class DAOException extends RuntimeException {
	private static final long serialVersionUID = -7145931735592880571L;
	private String errorCode;

	public DAOException(String errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
