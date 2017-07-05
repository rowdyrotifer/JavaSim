package com.marklalor.javasim.functions;

import java.util.Collection;

import com.google.common.collect.ImmutableList;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class FunctionContract {
	private final Collection<DataIdentifier> inputs;
	private final Collection<DataIdentifier> outputs;

	public FunctionContract(Collection<DataIdentifier> inputs, Collection<DataIdentifier> outputs) {
		this.inputs = ImmutableList.copyOf(inputs);
		this.outputs = ImmutableList.copyOf(outputs);
	}

	public Collection<DataIdentifier> getInputs() {
		return this.inputs;
	}

	public Collection<DataIdentifier> getOutputs() {
		return this.outputs;
	}

	@Override
	public String toString() {
		return "FunctionContract [inputs=" + this.inputs + ", outputs=" + this.outputs + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.inputs == null ? 0 : this.inputs.hashCode());
		result = prime * result + (this.outputs == null ? 0 : this.outputs.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) {
			return true;
		}
		if(obj == null) {
			return false;
		}
		if(!(obj instanceof FunctionContract)) {
			return false;
		}
		FunctionContract other = (FunctionContract) obj;
		if(this.inputs == null) {
			if(other.inputs != null) {
				return false;
			}
		}
		else if(!this.inputs.equals(other.inputs)) {
			return false;
		}
		if(this.outputs == null) {
			if(other.outputs != null) {
				return false;
			}
		}
		else if(!this.outputs.equals(other.outputs)) {
			return false;
		}
		return true;
	}
}
