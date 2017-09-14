package com.marklalor.javasim.model.flow.edge;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.model.flow.node.BundleNode;

public class DelegationEdge implements FlowEdge {
	private final DataIdentifier delegateIdentifier;
	private final BundleNode input;
	private final BundleNode output;
	
	public DelegationEdge(BundleNode input, BundleNode output, DataIdentifier delegateIdentifier) {
		this.input = input;
		this.output = output;
		this.delegateIdentifier = delegateIdentifier;
	}

	public BundleNode getInput() {
		return this.input;
	}

	public BundleNode getOutput() {
		return this.output;
	}

	public DataIdentifier getDelegateIdentifier() {
		return this.delegateIdentifier;
	}

	@Override
	public boolean isFunction() {
		return false;
	}

}
