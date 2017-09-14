package com.marklalor.javasim.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.marklalor.javasim.functions.identifier.FunctionContract;
import com.marklalor.javasim.functions.identifier.single.FullyQualifiedDataBundleIdentifier;
import com.marklalor.javasim.functions.identifier.single.FullyQualifiedDataIdentifier;
import com.marklalor.javasim.functions.identifier.single.FunctionIdentifier;
import com.marklalor.javasim.functions.implementations.FunctionType;
import com.marklalor.javasim.functions.instance.FullyQualifiedBundleInstance;

public class CheckedFunctionDecorator implements Function {
	private static final Logger logger = LoggerFactory.getLogger(CheckedFunctionDecorator.class);

	private final Function delegateFunction;

	public CheckedFunctionDecorator(Function realFunction) {
		this.delegateFunction = realFunction;
	}
	
	/**
	 * Wrapper around a Function implementation that ensures that the inputs and
	 * outputs abide to the function's FunctionContract.
	 */
	@Override
	public FullyQualifiedBundleInstance apply(FullyQualifiedBundleInstance input) {
		FunctionContract contract = getIdentifier().getContract();
		
		// Check inputs
		if (unequal(input, contract.getInputs())) {
			logger.warn("Input check failed. {}", this.delegateFunction.getIdentifier());
			throw new RuntimeException("The input did not match the function contract.");
		}
		
		// Delegate call to decorated instance.
		final FullyQualifiedBundleInstance output = this.delegateFunction.apply(input);
		
		// Check outputs
		if (unequal(output, contract.getOutputs())) {
			logger.warn("Input check failed. {}", this.delegateFunction.getIdentifier());
			throw new RuntimeException("The output did not match the function contract.");
		}
		
		return output;
	}

	private static boolean unequal(FullyQualifiedBundleInstance c1, FullyQualifiedDataBundleIdentifier functionBundleIdentifier) {
		HashMultiset<FullyQualifiedDataIdentifier> set1 = HashMultiset.create(c1.getInstanceMap().keySet());
		HashMultiset<FullyQualifiedDataIdentifier> set2 = HashMultiset.create(functionBundleIdentifier.getIdentifiers());
		return !set1.equals(set2);
	}

	@Override
	public FunctionIdentifier getIdentifier() {
		return this.delegateFunction.getIdentifier();
	}

	@Override
	public FunctionType getFunctionType() {
		return this.delegateFunction.getFunctionType();
	}
	
}
