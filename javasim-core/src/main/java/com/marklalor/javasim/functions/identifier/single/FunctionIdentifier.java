package com.marklalor.javasim.functions.identifier.single;

import com.marklalor.javasim.functions.identifier.FunctionContract;

public class FunctionIdentifier extends Identifier {
	private final FunctionContract contract;
	
	public FunctionIdentifier(FunctionContract contract, String identifier) {
		super(identifier);
		this.contract = contract;
	}

	public FunctionIdentifier(FunctionContract contract, String group, String name) {
		super(group, name);
		this.contract = contract;
	}

	public FunctionContract getContract() {
		return this.contract;
	}
	
	@Override
	public String toString() {
		return "FunctionIdentifier [contract=" + this.contract + ", identifier=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.contract == null ? 0 : this.contract.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (! (obj instanceof FunctionIdentifier)) {
			return false;
		}
		FunctionIdentifier other = (FunctionIdentifier) obj;
		if (this.contract == null) {
			if (other.contract != null) {
				return false;
			}
		} else if (!this.contract.equals(other.contract)) {
			return false;
		}
		return true;
	}
}
