package com.marklalor.javasim.functions.identifier;

import com.marklalor.javasim.data.annotations.SimFunction;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimOutput;

public class Identifier {
	private final String group;
	private final String name;
	private final String version;
	
	public static Identifier of(SimFunction annotation) {
		return new Identifier(annotation.group(), annotation.name(), annotation.version());
	}

	public static Identifier of(SimInput annotation) {
		return new Identifier(annotation.group(), annotation.name(), annotation.version());
	}

	public static Identifier of(SimOutput annotation) {
		return new Identifier(annotation.group(), annotation.name(), annotation.version());
	}

	public Identifier(String group, String name, String version) {
		this.group = group;
		this.name = name;
		this.version = version;
	}

	@Override
	public String toString() {
		return this.group + "." + this.name + "/v=" + this.version;
	}

	public String getGroup() {
		return this.group;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getVersion() {
		return this.version;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.group == null ? 0 : this.group.hashCode());
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		result = prime * result + (this.version == null ? 0 : this.version.hashCode());
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
		if(!(obj instanceof Identifier)) {
			return false;
		}
		Identifier other = (Identifier) obj;
		if(this.group == null) {
			if(other.group != null) {
				return false;
			}
		}
		else if(!this.group.equals(other.group)) {
			return false;
		}
		if(this.name == null) {
			if(other.name != null) {
				return false;
			}
		}
		else if(!this.name.equals(other.name)) {
			return false;
		}
		if(this.version == null) {
			if(other.version != null) {
				return false;
			}
		}
		else if(!this.version.equals(other.version)) {
			return false;
		}
		return true;
	}
}
