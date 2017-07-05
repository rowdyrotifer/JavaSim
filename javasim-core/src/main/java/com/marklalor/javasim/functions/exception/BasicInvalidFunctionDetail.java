package com.marklalor.javasim.functions.exception;

public class BasicInvalidFunctionDetail implements InvalidFunctionDetails {
	private final InvalidFunctionReason reason;

	public BasicInvalidFunctionDetail(InvalidFunctionReason reason) {
		this.reason = reason;
	}

	@Override
	public InvalidFunctionReason getReason() {
		return this.reason;
	}
	
	@Override
	public String getMessage() {
		return null;
	}
	
	@Override
	public Object[] getObjects() {
		return null;
	}
}
