package com.marklalor.javasim.functions.localjava.output;

import java.io.StringWriter;

import com.marklalor.javasim.data.output.OutputString;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputStringTrackable extends Trackable implements OutputString {
	public OutputStringTrackable(DataIdentifier identifier) {
		super(identifier);
	}

	@Override
	public void put(String value) {
		super.put(value);
	}

	@Override
	public StringWriter getOutputStream() {
		throw new UnsupportedOperationException();
	}
}
