package com.marklalor.javasim.model.flow;

import java.util.Collection;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;

public class OutputContract extends BasicIdentifierContract<DataIdentifier> {
	
	public OutputContract(Collection<DataIdentifier> identifiers) {
		super(identifiers);
	}

}
