package com.marklalor.javasim.model;

import com.marklalor.javasim.functions.identifier.DataIdentifier;

public interface DataInstance {
	
	public DataIdentifier getDataIdentifier();

	public Object getJavaValue();

}
