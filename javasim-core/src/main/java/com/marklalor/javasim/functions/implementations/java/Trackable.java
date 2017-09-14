package com.marklalor.javasim.functions.implementations.java;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.marklalor.javasim.data.output.OutputBoolean;
import com.marklalor.javasim.data.output.OutputBytes;
import com.marklalor.javasim.data.output.OutputImage;
import com.marklalor.javasim.data.output.OutputInt32;
import com.marklalor.javasim.data.output.OutputInt64;
import com.marklalor.javasim.data.output.OutputString;
import com.marklalor.javasim.model.data.DataInstance;

public abstract class Trackable implements DataInstance {
	private final CountDownLatch latch = new CountDownLatch(1);
	private Object value;

	protected void putInternal(Object value) {
		this.value = value;
		this.latch.countDown();
	}
	
	public void terminateTracking() {
		this.latch.countDown();
	}

	@Override
	public Optional<Object> getJavaValue() {
		try {
			this.latch.await();
			return Optional.ofNullable(this.value);
		} catch (InterruptedException e) {
			// User never gave a value, but the function returned.
			// (terminateTracking should be called when the function returns)
			return Optional.empty();
		}
	}
	
	public static class OutputBooleanTrackable extends Trackable implements OutputBoolean {
		@Override
		public void put(boolean value) {
			putInternal(value);
		}
	}

	public static class OutputInt32Trackable extends Trackable implements OutputInt32 {
		@Override
		public void put(int value) {
			putInternal(value);
		}
	}
	
	public static class OutputInt64Trackable extends Trackable implements OutputInt64 {
		@Override
		public void put(long value) {
			putInternal(value);
		}
	}
	
	public static class OutputBytesTrackable extends Trackable implements OutputBytes {
		@Override
		public void put(byte[] value) {
			putInternal(value);
		}

		@Override
		public OutputStream getOutputStream() {
			throw new UnsupportedOperationException();
		}
	}
	
	public static class OutputStringTrackable extends Trackable implements OutputString {
		@Override
		public void put(String value) {
			putInternal(value);
		}
		
		@Override
		public StringWriter getOutputStream() {
			throw new UnsupportedOperationException();
		}
	}

	public static class OutputImageTrackable extends Trackable implements OutputImage {
		@Override
		public void put(BufferedImage image) {
			putInternal(image);
		}
	}
}
