package com.marklalor.javasim.model.flow;

import java.util.Map;

import com.google.common.util.concurrent.ListenableFuture;
import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.model.data.DataInstance;

public interface FlowResults {
	
	public Map<DataIdentifier, ListenableFuture<DataInstance>> getOutputs();
	
}
