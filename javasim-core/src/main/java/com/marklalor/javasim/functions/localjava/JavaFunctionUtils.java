package com.marklalor.javasim.functions.localjava;

import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimOutput;
import com.marklalor.javasim.data.input.InputBoolean;
import com.marklalor.javasim.data.input.InputBytes;
import com.marklalor.javasim.data.input.InputImage;
import com.marklalor.javasim.data.input.InputInt32;
import com.marklalor.javasim.data.input.InputInt64;
import com.marklalor.javasim.data.input.InputString;
import com.marklalor.javasim.data.output.OutputBoolean;
import com.marklalor.javasim.data.output.OutputBytes;
import com.marklalor.javasim.data.output.OutputImage;
import com.marklalor.javasim.data.output.OutputInt32;
import com.marklalor.javasim.data.output.OutputInt64;
import com.marklalor.javasim.data.output.OutputString;
import com.marklalor.javasim.functions.identifier.DataIdentifier;
import com.marklalor.javasim.functions.identifier.Identifier;
import com.marklalor.javasim.model.DataType;

public class JavaFunctionUtils {
	private JavaFunctionUtils() {
	}

	public static boolean isValidDataParameter(Parameter parameter) {
		// If either have neither or both SimInput/SimOutput
		if(parameter.getAnnotation(SimInput.class) == null == (parameter.getAnnotation(SimOutput.class) == null)) {
			return false;
		}
		else if(parameter.getAnnotation(SimInput.class) != null && !isInputType(parameter.getType())) {
			return false;
		}
		else if(parameter.getAnnotation(SimOutput.class) != null && !isOutputType(parameter.getType())) {
			return false;
		}
		
		return true;
	}
	
	public static DataIdentifier createDataIdentifier(Parameter parameter) {
		final SimInput simInput = parameter.getAnnotation(SimInput.class);
		final SimOutput simOutput = parameter.getAnnotation(SimOutput.class);
		
		if(simInput == null && simOutput == null) {
			throw new IllegalArgumentException("Parameter " + parameter.getName()
					+ " does not have a SimInput nor a SimOutput annotation.");
		}
		else if(simInput != null && simOutput != null) {
			throw new IllegalArgumentException("Parameter " + parameter.getName()
					+ " may not have both a SimInput and a SimOutput annotation.");
		}
		else {
			if(simInput != null) {
				return new DataIdentifier(Identifier.of(simInput), typeOf(parameter));
			}
			else if(simOutput != null) {
				return new DataIdentifier(Identifier.of(simOutput), typeOf(parameter));
			}
			else {
				throw new IllegalStateException("Both simInput and simOutput are null!");
			}
		}
	}
	
	private static DataType typeOf(Parameter parameter) {
		DataType type = DATA_CLASS_TO_DATA_TYPE.get(parameter.getType());
		if(type == null) {
			throw new IllegalArgumentException("Parameter is not either an input or output type.");
		}
		
		return type;
	}

	public static boolean isInputType(Class<?> clazz) {
		return INPUT_TYPES.contains(clazz);
	}

	public static boolean isOutputType(Class<?> clazz) {
		return OUTPUT_TYPES.contains(clazz);
	}
	
	private static final BiMap<Class<?>, DataType> INPUT_CLASS_BIMAP_DATA_TYPE = ImmutableBiMap.<Class<?>, DataType> builder()
			.put(InputBoolean.class, DataType.BOOLEAN)
			.put(InputBytes.class, DataType.BYTES)
			.put(InputImage.class, DataType.IMAGE)
			.put(InputInt32.class, DataType.INT32)
			.put(InputInt64.class, DataType.INT64)
			.put(InputString.class, DataType.STRING)
			.build();
	
	private static final BiMap<Class<?>, DataType> OUTPUT_CLASS_BIMAP_DATA_TYPE = ImmutableBiMap.<Class<?>, DataType> builder()
			.put(OutputBoolean.class, DataType.BOOLEAN)
			.put(OutputBytes.class, DataType.BYTES)
			.put(OutputImage.class, DataType.IMAGE)
			.put(OutputInt32.class, DataType.INT32)
			.put(OutputInt64.class, DataType.INT64)
			.put(OutputString.class, DataType.STRING)
			.build();
	
	private static final Map<Class<?>, DataType> DATA_CLASS_TO_DATA_TYPE = ImmutableMap.<Class<?>, DataType> builder()
			.putAll(INPUT_CLASS_BIMAP_DATA_TYPE)
			.putAll(OUTPUT_CLASS_BIMAP_DATA_TYPE)
			.build();
	
	private static final Set<Class<?>> INPUT_TYPES = ImmutableSet.copyOf(
			INPUT_CLASS_BIMAP_DATA_TYPE.keySet());
	
	private static final Set<Class<?>> OUTPUT_TYPES = ImmutableSet.copyOf(
			OUTPUT_CLASS_BIMAP_DATA_TYPE.keySet());
	
	private static final Set<Class<?>> ALL_TYPES = Sets.union(INPUT_TYPES, OUTPUT_TYPES);
}
