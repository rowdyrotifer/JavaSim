package com.marklalor.javasim.model;

import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class BasicDataInstance implements DataInstance {

	private final DataIdentifier identifier;
	private final Object value;

	public BasicDataInstance(DataIdentifier identifier, Object value) {
		this.identifier = identifier;
		this.value = value;
	}

	@Override
	public DataIdentifier getDataIdentifier() {
		return this.identifier;
	}

	@Override
	public Object getJavaValue() {
		return this.value;
	}
}
