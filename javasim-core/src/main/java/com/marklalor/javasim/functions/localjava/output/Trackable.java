package com.marklalor.javasim.functions.localjava.output;

import java.util.Optional;

import com.marklalor.javasim.functions.identifier.DataIdentifier;

public abstract class Trackable {
	private final DataIdentifier dataIdentifier;
	private Optional<Object> value;

	public Trackable(DataIdentifier identifier) {
		this.dataIdentifier = identifier;
		this.value = Optional.empty();
	}

	protected void put(Object value) {
		this.value = Optional.of(value);
	}

	public DataIdentifier getDataIdentifier() {
		return this.dataIdentifier;
	}
	
	public Optional<Object> getValue() {
		return this.value;
	}
}
