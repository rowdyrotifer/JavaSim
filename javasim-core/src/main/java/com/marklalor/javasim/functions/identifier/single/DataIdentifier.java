package com.marklalor.javasim.functions.identifier.single;

import com.marklalor.javasim.model.data.DataType;

public class DataIdentifier extends Identifier {
	private final DataType dataType;
	
	public DataIdentifier(DataType dataType, String identifier) {
		super(identifier);
		this.dataType = dataType;
	}
	
	public DataIdentifier(DataType dataType, String group, String name) {
		super(group, name);
		this.dataType = dataType;
	}
	
	public DataType getDataType() {
		return this.dataType;
	}

	@Override
	public String toString() {
		return "DataIdentifier [dataType=" + this.dataType + ", Identifier=" + super.toString() + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (this.dataType == null ? 0 : this.dataType.hashCode());
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
		if (! (obj instanceof DataIdentifier)) {
			return false;
		}
		DataIdentifier other = (DataIdentifier) obj;
		if (this.dataType != other.dataType) {
			return false;
		}
		return true;
	}

}
