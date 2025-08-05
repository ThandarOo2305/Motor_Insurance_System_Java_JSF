package org.ace.java.component.idgen.exception;

import org.springframework.transaction.TransactionSystemException;

public class CustomIDGeneratorException extends TransactionSystemException {
	private static final long serialVersionUID = 6928151061832081771L;
	private String errorCode;

	public CustomIDGeneratorException(String errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public CustomIDGeneratorException(String errorCode, String message, Throwable throwable) {
		super(message, throwable);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}
}
