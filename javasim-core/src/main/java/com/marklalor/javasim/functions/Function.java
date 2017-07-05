package com.marklalor.javasim.functions;

import java.util.Collection;

import com.marklalor.javasim.functions.identifier.FunctionIdentifier;
import com.marklalor.javasim.model.DataInstance;

public interface Function {

	public FunctionIdentifier getIdentifier();
	
	public FunctionType getFunctionType();

	public Collection<DataInstance> apply(Collection<DataInstance> input);
	
}
