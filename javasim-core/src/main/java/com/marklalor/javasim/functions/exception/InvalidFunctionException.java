package com.marklalor.javasim.functions.exception;

import java.util.Collection;
import java.util.Collections;

public class InvalidFunctionException extends Exception {
	private static final long serialVersionUID = 1702601877128096694L;
	
	private final Collection<InvalidFunctionDetails> reasons;
	
	public InvalidFunctionException(InvalidFunctionDetails reason) {
		this(Collections.singletonList(reason), null);
	}

	public InvalidFunctionException(Collection<InvalidFunctionDetails> reasons) {
		this(reasons, null);
	}

	public InvalidFunctionException(Collection<InvalidFunctionDetails> reasons, String message) {
		this(reasons, message, null);
	}

	public InvalidFunctionException(InvalidFunctionDetails reason, String message) {
		this(Collections.singletonList(reason), message, null);
	}

	public InvalidFunctionException(InvalidFunctionDetails reason, String message, Throwable cause) {
		this(Collections.singletonList(reason), message, cause);
	}

	public InvalidFunctionException(Collection<InvalidFunctionDetails> reasons, String message, Throwable cause) {
		super(message, cause);
		this.reasons = reasons;
	}

	public Collection<InvalidFunctionDetails> getReasons() {
		return this.reasons;
	}
}
