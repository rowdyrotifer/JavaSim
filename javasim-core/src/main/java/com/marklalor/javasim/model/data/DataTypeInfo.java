package com.marklalor.javasim.model.data;

import java.io.InputStream;

import com.marklalor.javasim.data.input.InputData;
import com.marklalor.javasim.data.input.InputBytes;
import com.marklalor.javasim.functions.implementations.java.Trackable;
import com.marklalor.javasim.functions.implementations.java.Trackable.OutputBytesTrackable;

/**
 * Waiting on JEP 301: Enhanced Enums... http://openjdk.java.net/jeps/301
 *
 */
public abstract class DataTypeInfo<ProtoType, JavaType> {
	// BYTES(
	// byte[].class,
	// Function.identity(),
	// byte[].class,
	// value -> new InputBytes() {
	// @Override
	// public byte[] get() {
	// return value;
	// }
	//
	// @Override
	// public InputStream getStream() {
	// throw new UnsupportedOperationException("Not yet implemented");
	// }
	// },
	// OutputBytesTrackable::new),
	// BOOLEAN(
	// boolean.class,
	// Function.identity(),
	// boolean.class,
	// value -> (InputBoolean) () -> value,
	// OutputBooleanTrackable::new),
	// STRING(
	// String.class,
	// Function.identity(),
	// String.class,
	// value -> new InputString() {
	// @Override
	// public String get() {
	// return value;
	// }
	//
	// @Override
	// public StringReader getReader() {
	// throw new UnsupportedOperationException("Not yet implemented");
	// }
	// },
	// OutputStringTrackable::new), // maybe can add more here & at bytes
	// // if stream before an option.
	// INT32(
	// int.class,
	// Function.identity(),
	// int.class,
	// value -> (InputInt32) () -> value,
	// OutputInt32Trackable::new),
	// INT64(
	// long.class,
	// Function.identity(),
	// long.class,
	// value -> (InputInt64) () -> value,
	// OutputInt64Trackable::new),
	// IMAGE(
	// byte[].class,
	// image -> {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// try {
	// ImageIO.write(image, "png", baos);
	// } catch (IOException e) {
	// return null;
	// }
	// return baos.toByteArray();
	// },
	// BufferedImage.class,
	// value -> (InputImage) () -> value,
	// OutputImageTrackable::new);

	private final Class<ProtoType> protobufType;
	private final Class<JavaType> javaType;

	private DataTypeInfo(Class<ProtoType> protobufType, Class<JavaType> javaType) {
		this.protobufType = protobufType;
		this.javaType = javaType;
	}

	public abstract Trackable getAPITrackable();

	public abstract InputData getAPIInput(JavaType javaValue);

	public abstract ProtoType getProtocolBuffersValue(JavaType javaValue);

	public Class<?> getProtobufType() {
		return this.protobufType;
	}

	public Class<?> getJavaType() {
		return this.javaType;
	}

	public static class DataTypeBoolean extends DataTypeInfo<Boolean, Boolean> {
		public DataTypeBoolean() {
			super(Boolean.class, Boolean.class);
		}
		
		@Override
		public Trackable getAPITrackable() {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public InputData getAPIInput(Boolean javaValue) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public Boolean getProtocolBuffersValue(Boolean javaValue) {
			return javaValue;
		}
		
	}

	public static class DataTypeBytes extends DataTypeInfo<byte[], byte[]> {
		public DataTypeBytes() {
			super(byte[].class, byte[].class);
		}

		@Override
		public Trackable getAPITrackable() {
			return new OutputBytesTrackable();
		}
		
		@Override
		public InputData getAPIInput(byte[] javaValue) {
			return new InputBytes() {
				@Override
				public byte[] get() {
					return javaValue;
				}
				
				@Override
				public InputStream getStream() {
					throw new UnsupportedOperationException("Not yet implemented");
				}
			};
		}
		
		@Override
		public byte[] getProtocolBuffersValue(byte[] javaValue) {
			return javaValue;
		}
	}
}
