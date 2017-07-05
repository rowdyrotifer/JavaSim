package com.marklalor.javasim.functions.localjava.output;

import com.marklalor.javasim.data.output.OutputInt32;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputInt32Trackable extends Trackable implements OutputInt32 {
	public OutputInt32Trackable(DataIdentifier identifier) {
		super(identifier);
	}

	@Override
	public void put(int value) {
		super.put(value);
	}
}
