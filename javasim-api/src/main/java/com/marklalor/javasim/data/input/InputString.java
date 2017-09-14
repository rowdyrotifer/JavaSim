package com.marklalor.javasim.data.input;

import java.io.StringReader;

public interface InputString extends InputData {
	
	public String get();
	
	// if it's a lot of data being set in chunks, processing may begin
	// and be paused by blocking operations everything has been sent.
	
	public StringReader getReader();
}
