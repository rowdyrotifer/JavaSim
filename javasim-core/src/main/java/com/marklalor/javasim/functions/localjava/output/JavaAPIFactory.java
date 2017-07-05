package com.marklalor.javasim.functions.localjava.output;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;

import com.marklalor.javasim.data.input.InputBoolean;
import com.marklalor.javasim.data.input.InputBytes;
import com.marklalor.javasim.data.input.InputImage;
import com.marklalor.javasim.data.input.InputInt32;
import com.marklalor.javasim.data.input.InputInt64;
import com.marklalor.javasim.data.input.InputString;

public class JavaAPIFactory {
	public static InputBytes inputBytes(byte[] value) {
		final ByteArrayInputStream stream = new ByteArrayInputStream(value);
		return new InputBytes() {
			@Override
			public InputStream getStream() {
				return stream;
			}
			
			@Override
			public byte[] get() {
				return value;
			}
		};
	}
	
	public static InputBoolean inputBoolean(boolean value) {
		return () -> value;
	}

	public static InputString inputString(String value) {
		final StringReader reader = new StringReader(value);
		return new InputString() {
			@Override
			public StringReader getReader() {
				return reader;
			}
			
			@Override
			public String get() {
				return value;
			}
		};
	}
	
	public static InputInt32 inputInt32(int value) {
		return () -> value;
	}

	public static InputInt64 inputInt64(long value) {
		return () -> value;
	}

	public static InputImage inputImage(BufferedImage value) {
		return () -> value;
	}
}
