package org.ace.accounting.common.validation;

public interface IDataValidator<T> {
	public ValidationResult validate(T obj,boolean transaction);
}
