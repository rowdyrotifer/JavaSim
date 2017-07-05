package com.marklalor.javasim.functions;

import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashMultiset;
import com.marklalor.javasim.functions.identifier.DataIdentifier;
import com.marklalor.javasim.functions.identifier.FunctionIdentifier;
import com.marklalor.javasim.model.DataInstance;

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
	public Collection<DataInstance> apply(Collection<DataInstance> input) {
		FunctionContract contract = getIdentifier().getContract();
		
		// Check inputs
		if(unequal(input, contract.getInputs())) {
			logger.warn("Input check failed. {}", this.delegateFunction.getIdentifier());
			throw new RuntimeException("The input did not match the function contract.");
		}
		
		// Delegate call to decorated instance.
		final Collection<DataInstance> realOutput = this.delegateFunction.apply(input);
		
		// Check outputs
		if(unequal(realOutput, contract.getOutputs())) {
			logger.warn("Input check failed. {}", this.delegateFunction.getIdentifier());
			throw new RuntimeException("The output did not match the function contract.");
		}
		
		return realOutput;
	}

	private static boolean unequal(Collection<DataInstance> c1, Collection<DataIdentifier> c2) {
		HashMultiset<DataIdentifier> set1 = HashMultiset.create(c1.stream().map(DataInstance::getDataIdentifier).collect(Collectors.toList()));
		HashMultiset<DataIdentifier> set2 = HashMultiset.create(c2);
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
