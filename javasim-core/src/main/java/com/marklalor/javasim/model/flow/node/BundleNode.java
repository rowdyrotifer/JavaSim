package com.marklalor.javasim.model.flow.node;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.model.flow.BasicIdentifierContract;

public class BundleNode implements DataNode {
	private final BasicIdentifierContract<DataIdentifier> identifiers;

	public BundleNode(BasicIdentifierContract<DataIdentifier> identifiers) {
		this.identifiers = identifiers;
	}
	
	public BasicIdentifierContract<DataIdentifier> getIdentifiers() {
		return this.identifiers;
	}

	@Override
	public boolean isBundle() {
		return true;
	}

}
