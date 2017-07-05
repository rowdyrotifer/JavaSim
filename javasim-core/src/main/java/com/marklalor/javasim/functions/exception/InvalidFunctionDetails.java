package com.marklalor.javasim.functions.exception;

public interface InvalidFunctionDetails {
	
	public InvalidFunctionReason getReason();

	public String getMessage();

	public Object[] getObjects();

}
