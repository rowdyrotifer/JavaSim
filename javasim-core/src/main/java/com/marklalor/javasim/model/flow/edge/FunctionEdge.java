package com.marklalor.javasim.model.flow.edge;

import com.marklalor.javasim.functions.Function;
import com.marklalor.javasim.model.flow.node.BundleNode;

public class FunctionEdge implements FlowEdge {
	private final BundleNode input;
	private final BundleNode output;
	private final Function function;
	
	public FunctionEdge(BundleNode input, BundleNode output, Function function) {
		this.input = input;
		this.output = output;
		this.function = function;
	}

	public BundleNode getInput() {
		return this.input;
	}

	public BundleNode getOutput() {
		return this.output;
	}

	public Function getFunction() {
		return this.function;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

}
