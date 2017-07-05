package com.marklalor.javasim.model;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;

import com.marklalor.javasim.data.input.InputBytes;

public class DataTypeTest {
	
	@Test
	public void testBytes() {
		byte[] value = new byte[] { 1, 2, 3 };
		Object result = DataType.BYTES.javaToAPIInput(value);
		assertThat(result, instanceOf(InputBytes.class));
		Assert.assertArrayEquals(value, ((InputBytes) result).get());
	}
	
}
