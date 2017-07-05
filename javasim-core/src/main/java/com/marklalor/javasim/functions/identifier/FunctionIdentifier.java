package com.marklalor.javasim.functions.identifier;

import com.marklalor.javasim.functions.FunctionContract;

public class FunctionIdentifier {
	private final Identifier identifier;
	private final FunctionContract contract;

	public FunctionIdentifier(Identifier identifier, FunctionContract contract) {
		this.identifier = identifier;
		this.contract = contract;
	}

	public Identifier getIdentifier() {
		return this.identifier;
	}
	
	public FunctionContract getContract() {
		return this.contract;
	}
	
	@Override
	public String toString() {
		return "FunctionIdentifier [identifier=" + this.identifier + ", contract=" + this.contract + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.contract == null ? 0 : this.contract.hashCode());
		result = prime * result + (this.identifier == null ? 0 : this.identifier.hashCode());
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
		if(!(obj instanceof FunctionIdentifier)) {
			return false;
		}
		FunctionIdentifier other = (FunctionIdentifier) obj;
		if(this.contract == null) {
			if(other.contract != null) {
				return false;
			}
		}
		else if(!this.contract.equals(other.contract)) {
			return false;
		}
		if(this.identifier == null) {
			if(other.identifier != null) {
				return false;
			}
		}
		else if(!this.identifier.equals(other.identifier)) {
			return false;
		}
		return true;
	}
}
