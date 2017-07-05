package com.marklalor.javasim.functions.localjava.output;

import java.io.OutputStream;

import com.marklalor.javasim.data.output.OutputBytes;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputBytesTrackable extends Trackable implements OutputBytes {
	public OutputBytesTrackable(DataIdentifier identifier) {
		super(identifier);
	}

	@Override
	public void put(boolean value) {
		super.put(value);
	}
	
	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}
}
