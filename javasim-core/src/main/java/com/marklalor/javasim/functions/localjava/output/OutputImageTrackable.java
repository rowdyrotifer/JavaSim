package com.marklalor.javasim.functions.localjava.output;

import java.awt.image.BufferedImage;

import com.marklalor.javasim.data.output.OutputImage;
import com.marklalor.javasim.functions.identifier.DataIdentifier;

public class OutputImageTrackable extends Trackable implements OutputImage {
	public OutputImageTrackable(DataIdentifier identifier) {
		super(identifier);
	}
	
	@Override
	public void put(BufferedImage image) {
		super.put(image);
	}
}
