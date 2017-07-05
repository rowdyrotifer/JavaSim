package com.marklalor.javasim.model;

import com.marklalor.javasim.functions.identifier.DataIdentifier;

public interface DataSeries {

	public DataIdentifier getIdentifier();
	
	public DataInstance getData(int identifier);

}
