package com.marklalor.javasim.data.output;

import java.awt.image.BufferedImage;

public interface OutputImage extends OutputData {
	public void put(BufferedImage image);
}
