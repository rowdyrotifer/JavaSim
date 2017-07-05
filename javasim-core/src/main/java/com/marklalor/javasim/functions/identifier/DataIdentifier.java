package com.marklalor.javasim.functions.identifier;

import com.marklalor.javasim.model.DataType;

public class DataIdentifier {
	private final Identifier identifier;
	private final DataType dataType;
	
	public DataIdentifier(Identifier identifier, DataType dataType) {
		this.identifier = identifier;
		this.dataType = dataType;
	}
	
	public DataType getDataType() {
		return this.dataType;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.dataType == null ? 0 : this.dataType.hashCode());
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
		if(!(obj instanceof DataIdentifier)) {
			return false;
		}
		DataIdentifier other = (DataIdentifier) obj;
		if(this.dataType != other.dataType) {
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
	
	@Override
	public String toString() {
		return "DataIdentifier [identifier=" + this.identifier + ", dataType=" + this.dataType + "]";
	}
}
