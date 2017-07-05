package com.marklalor.javasim.model.data;

import java.io.OutputStream;

public interface ProtoMessage {

	public byte[] toByteArray();

	public void writeTo(OutputStream output);

}
