package org.ace.java.component;

import org.springframework.transaction.TransactionSystemException;

public class SystemException extends TransactionSystemException {
	private static final long serialVersionUID = -4310366412683752065L;
	private String errorCode;
	private Object response;

	public SystemException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public SystemException(String errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}

	public SystemException(String errorCode, Object response, String message) {
		super(message);
		this.errorCode = errorCode;
		this.response = response;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object getResponse() {
		return response;
	}
}
