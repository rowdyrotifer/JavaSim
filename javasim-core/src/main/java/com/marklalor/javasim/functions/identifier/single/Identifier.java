package com.marklalor.javasim.functions.identifier.single;

/**
 * Only meant to help define {@link DataIdentifier} and
 * {@link FunctionIdentifier}, not to be used on its own (hence the
 * package-private visibility modifier).
 */
class Identifier {
	private final String group;
	private final String name;
	
	public Identifier(String group, String name) {
		this.group = group;
		this.name = name;
	}

	public Identifier(String identifier) {
		int i = identifier.lastIndexOf('.');
		
		if (i != -1) {
			this.group = identifier.substring(0, i);
			this.name = identifier.substring(i);
		} else {
			this.group = "";
			this.name = identifier;
		}
	}

	public String getGroup() {
		return this.group;
	}

	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return "Identifier [group=" + this.group + ", name=" + this.name + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.group == null ? 0 : this.group.hashCode());
		result = prime * result + (this.name == null ? 0 : this.name.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (! (obj instanceof Identifier)) {
			return false;
		}
		Identifier other = (Identifier) obj;
		if (this.group == null) {
			if (other.group != null) {
				return false;
			}
		} else if (!this.group.equals(other.group)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		return true;
	}
}
