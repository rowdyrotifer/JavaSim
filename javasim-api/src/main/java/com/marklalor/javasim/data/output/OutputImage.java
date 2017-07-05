package com.marklalor.javasim.data.output;

import java.awt.image.BufferedImage;

public interface OutputImage extends Output
{
    public void put(BufferedImage image);
}
