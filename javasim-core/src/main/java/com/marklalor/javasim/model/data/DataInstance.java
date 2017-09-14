package com.marklalor.javasim.model.data;

import java.util.Optional;

public interface DataInstance {
	
	/**
	 * This method returns the value of this data instance, blocking if the
	 * value is not yet available.
	 *
	 * @return {@link Optional#empty()} if no value was supplied, otherwise the
	 *         value of this data instance.
	 */
	public Optional<Object> getJavaValue();

}
