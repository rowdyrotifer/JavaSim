package com.marklalor.javasim.functions.localjava;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklalor.javasim.data.annotations.SimFunction;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimOutput;
import com.marklalor.javasim.functions.Function;
import com.marklalor.javasim.functions.FunctionContract;
import com.marklalor.javasim.functions.FunctionType;
import com.marklalor.javasim.functions.exception.BasicInvalidFunctionDetail;
import com.marklalor.javasim.functions.exception.InvalidFunctionException;
import com.marklalor.javasim.functions.exception.InvalidFunctionReason;
import com.marklalor.javasim.functions.identifier.DataIdentifier;
import com.marklalor.javasim.functions.identifier.FunctionIdentifier;
import com.marklalor.javasim.functions.identifier.Identifier;
import com.marklalor.javasim.functions.localjava.output.Trackable;
import com.marklalor.javasim.model.BasicDataInstance;
import com.marklalor.javasim.model.DataInstance;

public class JavaFunction implements Function {
	private final static Logger logger = LoggerFactory.getLogger(JavaFunction.class);
	
	private final Object instance;
	private final Method backingMethod;
	
	private static class IOEntry {
		private final boolean input;
		private final DataIdentifier identifier;
		
		public IOEntry(boolean input, DataIdentifier identifier) {
			this.input = input;
			this.identifier = identifier;
		}
	}

	private final List<IOEntry> orderedInputsOutputs;
	private final List<DataIdentifier> inputs;
	private final List<DataIdentifier> outputs;
	
	private final FunctionIdentifier identifier;

	public JavaFunction(Method method) throws InvalidFunctionException {
		this.backingMethod = method;
		
		// Create an instance to use for this function
		Class<?> userClass = method.getDeclaringClass();
		try {
			this.instance = userClass.newInstance();
		}
		catch(InstantiationException e) {
			logger.error("Could not create an instance of {} (is there no nullary constructor?)", userClass, e);
			throw new InvalidFunctionException(new BasicInvalidFunctionDetail(InvalidFunctionReason.JAVA_INSTANCE_FAILED));
		}
		catch(IllegalAccessException e) {
			logger.error("Could not create instance of {} for function.", userClass, e);
			throw new InvalidFunctionException(new BasicInvalidFunctionDetail(InvalidFunctionReason.JAVA_INSTANCE_FAILED));
		}

		this.orderedInputsOutputs = new ArrayList<>();
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		loadInputsOutputs(); // orderedInputsOutputs, inputs, and outputs will be loaded in one loop
		
		this.identifier = createIdentifier();
	}
	
	private void loadInputsOutputs() {
		final Parameter[] reflectParameters = this.backingMethod.getParameters();
		
		// Cannot find info on the order of java.lang.reflect.Executable.getParameters()
		// so, forcefully check that the parameters are as we expect...
		int expectedIndex = 0;
		Field indexField;
		try {
			indexField = Parameter.class.getField("index");
		}
		catch(NoSuchFieldException | SecurityException e) {
			logger.error("Cannot verify input indexes!", e);
			throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
		}
		indexField.setAccessible(true);
		// end reflection nonsense
		
		for(final Parameter parameter : reflectParameters) {
			if(JavaFunctionUtils.isValidDataParameter(parameter)) {
				// begin more reflection nonsense
				try {
					int realIndex = (int) indexField.get(parameter);
					if(expectedIndex != realIndex) {
						throw new RuntimeException("The expected index was unequal to the real index! Aborting.");
					}
					expectedIndex++;
				}
				catch(IllegalArgumentException e) {
					logger.error("Error verifying function parameter indices.", e);
					throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
				}
				catch(IllegalAccessException e) {
					logger.error("Error verifying function parameter indices.", e);
					throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
				}
				// finally end reflection nonsense
				DataIdentifier identifier = JavaFunctionUtils.createDataIdentifier(parameter);
				if(parameter.getAnnotation(SimInput.class) != null) {
					this.inputs.add(identifier);
					this.orderedInputsOutputs.add(new IOEntry(true, identifier));
				}
				else if(parameter.getAnnotation(SimOutput.class) != null) {
					this.outputs.add(identifier);
					this.orderedInputsOutputs.add(new IOEntry(false, identifier));
				}
			}
		}
	}
	
	private FunctionIdentifier createIdentifier() {
		SimFunction annotation = this.backingMethod.getAnnotation(SimFunction.class);
		return new FunctionIdentifier(Identifier.of(annotation), new FunctionContract(this.inputs, this.outputs));
	}
	
	@Override
	public FunctionIdentifier getIdentifier() {
		return this.identifier;
	}
	
	@Override
	public FunctionType getFunctionType() {
		return FunctionType.JAVA;
	}
	
	@Override
	public Collection<DataInstance> apply(Collection<DataInstance> inputs) {
		final Object[] args = new Object[this.orderedInputsOutputs.size()];
		final Trackable[] trackables = new Trackable[this.outputs.size()];
		int i = 0, j = 0;
		for(IOEntry entry : this.orderedInputsOutputs) {
			// Input parameters
			if(entry.input) {
				final Optional<DataInstance> inputInstance = inputs
						.stream()
						.filter(instance -> instance.getDataIdentifier().equals(entry.identifier))
						.findFirst();
				if(inputInstance.isPresent()) {
					// Add input argument.
					args[i] = inputInstance.get().getDataIdentifier().getDataType().javaToAPIInput(inputInstance.get().getJavaValue());
				}
				else {
					throw new IllegalArgumentException(this.identifier + " not in input");
				}
			} // Output parameters
			else {
				// This trackable is also an instance of the desired API Output class :)
				final Trackable trackable = entry.identifier.getDataType().getAPITrackable(entry.identifier);
				trackables[j] = trackable;
				j++;
				args[i] = trackable;
			}

			i++;
		}

		try {
			this.backingMethod.invoke(this.instance, args);
		}
		catch(Exception e) {
			logger.error("Exception occured while invoking method", e);
			return Collections.emptyList();
		}

		return Arrays
				.stream(trackables)
				.map(trackable -> new BasicDataInstance(
						trackable.getDataIdentifier(),
						trackable.getValue()))
				.collect(Collectors.toList());
	}

}
