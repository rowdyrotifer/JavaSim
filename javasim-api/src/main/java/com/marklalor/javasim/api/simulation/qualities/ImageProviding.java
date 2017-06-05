package com.marklalor.javasim.api.simulation.qualities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public interface ImageProviding
{
    void draw(Graphics2D graphics);
    
    BufferedImage getImage();
}
