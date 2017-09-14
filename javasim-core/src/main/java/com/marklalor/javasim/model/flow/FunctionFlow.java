package com.marklalor.javasim.model.flow;

import com.marklalor.javasim.functions.identifier.FunctionIdentifier;
import com.marklalor.javasim.functions.instance.BundleInstance;

public interface FunctionFlow {
	
	public FunctionIdentifier getIdentifier();

	public FlowResults execute(BundleInstance inputs);

}
