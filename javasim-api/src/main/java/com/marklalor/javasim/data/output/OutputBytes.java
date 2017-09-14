package com.marklalor.javasim.data.output;

import java.io.OutputStream;

public interface OutputBytes extends OutputData {

	public void put(byte[] value);
	
	public OutputStream getOutputStream();
	
}
