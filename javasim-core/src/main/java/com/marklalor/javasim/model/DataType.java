package com.marklalor.javasim.model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Function;

import javax.imageio.ImageIO;

import com.marklalor.javasim.functions.identifier.DataIdentifier;
import com.marklalor.javasim.functions.localjava.output.JavaAPIFactory;
import com.marklalor.javasim.functions.localjava.output.OutputBooleanTrackable;
import com.marklalor.javasim.functions.localjava.output.OutputBytesTrackable;
import com.marklalor.javasim.functions.localjava.output.OutputImageTrackable;
import com.marklalor.javasim.functions.localjava.output.OutputInt32Trackable;
import com.marklalor.javasim.functions.localjava.output.OutputInt64Trackable;
import com.marklalor.javasim.functions.localjava.output.OutputStringTrackable;
import com.marklalor.javasim.functions.localjava.output.Trackable;

/**
 * Keep this in sync with: 1. data_type.proto 2. DataItemSerializer.java
 *
 */
public enum DataType {
	BYTES(
			byte[].class,
			Function.identity(),
			byte[].class,
			JavaAPIFactory::inputBytes,
			OutputBytesTrackable::new),
	BOOLEAN(
			boolean.class,
			Function.identity(),
			boolean.class,
			JavaAPIFactory::inputBoolean,
			OutputBooleanTrackable::new),
	STRING(
			String.class,
			Function.identity(),
			String.class,
			JavaAPIFactory::inputString,
			OutputStringTrackable::new), // maybe can add more here & at bytes if stream before an option.
	INT32(
			int.class,
			Function.identity(),
			int.class,
			JavaAPIFactory::inputInt32,
			OutputInt32Trackable::new),
	INT64(
			long.class,
			Function.identity(),
			long.class,
			JavaAPIFactory::inputInt64,
			OutputInt64Trackable::new),
	IMAGE(
			byte[].class,
			image -> {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				try {
					ImageIO.write(image, "png", baos);
				}
				catch(IOException e) {
					return null;
				}
				return baos.toByteArray();
			},
			BufferedImage.class,
			JavaAPIFactory::inputImage,
			OutputImageTrackable::new);

	private final Class<?> protobufType;
	private final Function<Object, Object> javaToProtoMapper;

	private final Class<?> javaType;
	private final Function<Object, Object> javaToAPIInputMapper;
	private final Function<DataIdentifier, Trackable> javaToAPITrackable;

	private <T, U> DataType(
			Class<T> protobufType,
			Function<U, T> javaToProtoMapper,
			Class<U> javaType,
			Function<U, Object> javaToAPIInputMapper,
			Function<DataIdentifier, Trackable> javaToAPITrackable) {
		this.protobufType = protobufType;
		this.javaToProtoMapper = (Function<Object, Object>) javaToProtoMapper;
		this.javaType = javaType;
		this.javaToAPIInputMapper = (Function<Object, Object>) javaToAPIInputMapper;
		this.javaToAPITrackable = javaToAPITrackable;
	}

	public Object javaToAPIInput(Object inputValue) {
		return this.javaToAPIInputMapper.apply(inputValue);
	}

	public Object javaToProtocolBuffersValue(Object value) {
		return this.javaToProtoMapper.apply(value);
	}

	public Class<?> getProtobufType() {
		return this.protobufType;
	}

	public Class<?> getJavaType() {
		return this.javaType;
	}

	public Trackable getAPITrackable(DataIdentifier identifier) {
		return this.javaToAPITrackable.apply(identifier);
	}
}
