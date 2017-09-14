package com.marklalor.javasim.model.flow.node;

import com.marklalor.javasim.functions.identifier.single.DataIdentifier;

public class SingletonNode implements DataNode {
	private final DataIdentifier delegateIdentifier;
	private final BundleNode delegate;
	private final DataIdentifier identifier;
	
	public SingletonNode(DataIdentifier delegateIdentifier, BundleNode delegate, DataIdentifier identifier) {
		this.delegateIdentifier = delegateIdentifier;
		this.delegate = delegate;
		this.identifier = identifier;
	}
	
	public DataIdentifier getDelegateIdentifier() {
		return this.delegateIdentifier;
	}
	
	public BundleNode getDelegate() {
		return this.delegate;
	}
	
	public DataIdentifier getIdentifier() {
		return this.identifier;
	}
	
	@Override
	public boolean isBundle() {
		return false;
	}
	
}
