package com.marklalor.javasim.functions.implementations.java;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.marklalor.javasim.data.annotations.SimFunction;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimOutput;
import com.marklalor.javasim.functions.Function;
import com.marklalor.javasim.functions.exception.BasicInvalidFunctionDetail;
import com.marklalor.javasim.functions.exception.InvalidFunctionException;
import com.marklalor.javasim.functions.exception.InvalidFunctionReason;
import com.marklalor.javasim.functions.identifier.FunctionContract;
import com.marklalor.javasim.functions.identifier.single.DataIdentifier;
import com.marklalor.javasim.functions.identifier.single.FunctionIdentifier;
import com.marklalor.javasim.functions.implementations.FunctionType;

public class JavaFunction implements Function {
	private final static Logger logger = LoggerFactory.getLogger(JavaFunction.class);
	
	private final Object instance;
	private final Method backingMethod;
	
	private static class IOEntry {
		private final boolean input; // false = output
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
		} catch (InstantiationException e) {
			logger.error("Could not create an instance of {} (is there no nullary constructor?)", userClass, e);
			throw new InvalidFunctionException(new BasicInvalidFunctionDetail(InvalidFunctionReason.JAVA_INSTANCE_FAILED));
		} catch (IllegalAccessException e) {
			logger.error("Could not create instance of {} for function.", userClass, e);
			throw new InvalidFunctionException(new BasicInvalidFunctionDetail(InvalidFunctionReason.JAVA_INSTANCE_FAILED));
		}

		this.orderedInputsOutputs = new ArrayList<>();
		this.inputs = new ArrayList<>();
		this.outputs = new ArrayList<>();
		// variables orderedInputsOutputs, inputs, and outputs will be loaded
		loadInputsOutputs();
		
		this.identifier = createIdentifier();
	}
	
	private void loadInputsOutputs() {
		final Parameter[] reflectParameters = this.backingMethod.getParameters();
		
		// Cannot find info on the order of
		// java.lang.reflect.Executable.getParameters(),
		// so forcefully check that the parameters are as we expect before
		// assuming anything...
		int expectedIndex = 0;
		Field indexField;
		try {
			indexField = Parameter.class.getField("index");
		} catch (NoSuchFieldException | SecurityException e) {
			logger.error("Cannot verify input indexes!", e);
			throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
		}
		indexField.setAccessible(true);
		// end reflection nonsense
		
		for (final Parameter parameter : reflectParameters) {
			if (JavaFunctionUtils.isValidDataParameter(parameter)) {
				// begin more reflection nonsense
				try {
					int realIndex = (int) indexField.get(parameter);
					if (expectedIndex != realIndex) {
						throw new RuntimeException("The expected index was unequal to the real index! Aborting.");
					}
					expectedIndex++;
				} catch (IllegalArgumentException e) {
					logger.error("Error verifying function parameter indices.", e);
					throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
				} catch (IllegalAccessException e) {
					logger.error("Error verifying function parameter indices.", e);
					throw new RuntimeException("Cannot verify parameter indexes. Aborting.");
				}
				// finally end reflection nonsense
				DataIdentifier identifier = JavaFunctionUtils.createDataIdentifier(parameter);
				if (parameter.getAnnotation(SimInput.class) != null) {
					this.inputs.add(identifier);
					this.orderedInputsOutputs.add(new IOEntry(true, identifier));
				} else if (parameter.getAnnotation(SimOutput.class) != null) {
					this.outputs.add(identifier);
					this.orderedInputsOutputs.add(new IOEntry(false, identifier));
				}
			}
		}
	}
	
	private FunctionIdentifier createIdentifier() {
		SimFunction annotation = this.backingMethod.getAnnotation(SimFunction.class);
		return new FunctionIdentifier(BasicFullyQualifiedIdentifier.of(annotation),
				new FunctionContract(
						new BasicDataIdentifierContract<DataIdentifier>(this.inputs),
						new BasicDataIdentifierContract<DataIdentifier>(this.outputs)));
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
	public FullyQualifiedBundleInstance apply(FullyQualifiedBundleInstance inputs) {
		final Object[] args = new Object[this.orderedInputsOutputs.size()];
		final Trackable[] trackables = new Trackable[this.outputs.size()];
		int i = 0, j = 0;
		for (IOEntry entry : this.orderedInputsOutputs) {
			// Input parameters
			if (entry.input) {
				FullyQualifiedDataInstance dataFunctionInstance = inputs.getInstanceMap().get(entry.identifier);
				if (dataFunctionInstance != null) {
					// Add input argument.
					args[i] = dataFunctionInstance
							.getDataIdentifier()
							.getDataType()
							.getAPIInput(dataFunctionInstance.getJavaValue());
				} else {
					throw new IllegalArgumentException(this.identifier + " not in input");
				}
			}
			// Output parameters
			else {
				// The trackable is also an instance
				// of the desired API Output class :)
				final Trackable trackable = entry.identifier.getDataType().getAPITrackable(entry.identifier);
				trackables[j] = trackable;
				j++;
				args[i] = trackable;
			}
			
			i++;
		}
		
		try {
			this.backingMethod.invoke(this.instance, args);
		} catch (Exception e) {
			logger.error("Exception occured while invoking method", e);
			return null;
		}
		
		Map<DataIdentifier, FullyQualifiedDataInstance> result = Arrays.stream(trackables)
				.collect(Collectors.toMap(
						Trackable::getDataIdentifier,
						trackable -> trackable.getInstance().orElseThrow(
								() -> new IllegalStateException("No data was supplied by the function for "
										+ trackable.getDataIdentifier()))));
		
		return new FullyQualifiedBundleInstance(result);
	}

}
