package com.marklalor.javasim.functions.implementations.java;

import java.lang.reflect.Parameter;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.marklalor.javasim.data.annotations.SimInput;
import com.marklalor.javasim.data.annotations.SimOutput;
import com.marklalor.javasim.data.input.InputBoolean;
import com.marklalor.javasim.data.input.InputBytes;
import com.marklalor.javasim.data.input.InputData;
import com.marklalor.javasim.data.input.InputImage;
import com.marklalor.javasim.data.input.InputInt32;
import com.marklalor.javasim.data.input.InputInt64;
import com.marklalor.javasim.data.input.InputString;
import com.marklalor.javasim.data.output.OutputBoolean;
import com.marklalor.javasim.data.output.OutputBytes;
import com.marklalor.javasim.data.output.OutputData;
import com.marklalor.javasim.data.output.OutputImage;
import com.marklalor.javasim.data.output.OutputInt32;
import com.marklalor.javasim.data.output.OutputInt64;
import com.marklalor.javasim.data.output.OutputString;
import com.marklalor.javasim.model.data.DataType;

public class JavaFunctionUtils {
	private JavaFunctionUtils() {
	}
	
	public static boolean isValidDataParameter(Parameter parameter) {
		// If either have neither or both SimInput/SimOutput
		if (parameter.getAnnotation(SimInput.class) == null == (parameter.getAnnotation(SimOutput.class) == null)) {
			return false;
		} else if (parameter.getAnnotation(SimInput.class) != null && !InputData.class.isAssignableFrom(parameter.getType())) {
			return false;
		} else if (parameter.getAnnotation(SimOutput.class) != null && !OutputData.class.isAssignableFrom(parameter.getType())) {
			return false;
		}

		return true;
	}

	public static FullyQualifiedDataIdentifier createDataIdentifier(Parameter parameter) {
		final SimInput simInput = parameter.getAnnotation(SimInput.class);
		final SimOutput simOutput = parameter.getAnnotation(SimOutput.class);

		if (simInput == null && simOutput == null) {
			throw new IllegalArgumentException("Parameter " + parameter.getName()
					+ " does not have a SimInput nor a SimOutput annotation.");
		} else if (simInput != null && simOutput != null) {
			throw new IllegalArgumentException("Parameter " + parameter.getName()
					+ " may not have both a SimInput and a SimOutput annotation.");
		} else {
			if (simInput != null) {
				return BasicFullyQualifiedIdentifier.of(simInput, typeOf(parameter));
			} else if (simOutput != null) {
				return BasicFullyQualifiedIdentifier.of(simOutput, typeOf(parameter));
			} else {
				throw new IllegalStateException("Both simInput and simOutput are null!");
			}
		}
	}

	private static DataType typeOf(Parameter parameter) {
		DataType type = DATA_CLASS_TO_DATA_TYPE.get(parameter.getType());
		if (type == null) {
			throw new IllegalArgumentException("Parameter is not either an input or output type.");
		}

		return type;
	}
	
	private static final BiMap<Class<? extends InputData>, DataType> INPUT_CLASS_BIMAP_DATA_TYPE = ImmutableBiMap.<Class<? extends InputData>, DataType> builder()
			.put(InputBoolean.class, DataType.BOOLEAN)
			.put(InputBytes.class, DataType.BYTES)
			.put(InputImage.class, DataType.IMAGE)
			.put(InputInt32.class, DataType.INT32)
			.put(InputInt64.class, DataType.INT64)
			.put(InputString.class, DataType.STRING)
			.build();

	private static final BiMap<Class<? extends OutputData>, DataType> OUTPUT_CLASS_BIMAP_DATA_TYPE = ImmutableBiMap.<Class<? extends OutputData>, DataType> builder()
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
}
