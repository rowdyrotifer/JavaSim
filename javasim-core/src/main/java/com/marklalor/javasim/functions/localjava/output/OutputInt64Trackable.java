package com.marklalor.javasim.functions.localjava.output;

import com.marklalor.javasim.data.output.OutputInt64;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputInt64Trackable extends Trackable implements OutputInt64 {
	public OutputInt64Trackable(DataIdentifier identifier) {
		super(identifier);
	}

	@Override
	public void put(long value) {
		super.put(value);
	}
}
