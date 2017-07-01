package com.marklalor.javasim.data.output;

import java.awt.image.BufferedImage;

import com.marklalor.javasim.data.Terminal;

public interface OutputImage
{
    @Terminal
    public void put(BufferedImage image);
}
