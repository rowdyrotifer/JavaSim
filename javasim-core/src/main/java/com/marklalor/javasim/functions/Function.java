package com.marklalor.javasim.functions;

import java.util.Map;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.functions.identifier.single.FunctionIdentifier;
import com.marklalor.javasim.functions.implementations.FunctionType;
import com.marklalor.javasim.model.data.DataInstance;

/**
 * The heart and soul of the program. Transforms data by means of whatever
 * function implementation your heart desires... and is implemented.
 */
public interface Function {

	FunctionIdentifier getIdentifier();
	
	Map<DataIdentifier, DataInstance> apply(Map<DataIdentifier, DataInstance> input);
	
	FunctionType getFunctionType();
	
}
