package com.marklalor.javasim.functions.localjava.output;

import com.marklalor.javasim.data.output.OutputBoolean;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputBooleanTrackable extends Trackable implements OutputBoolean {
	public OutputBooleanTrackable(DataIdentifier identifier) {
		super(identifier);
	}

	@Override
	public void put(boolean value) {
		super.put(value);
	}
}
