package com.ph.ibm.validation;

public interface Validator<T> {
	public boolean validate(T type);
}