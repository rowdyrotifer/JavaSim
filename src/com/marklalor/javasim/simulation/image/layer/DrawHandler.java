package com.marklalor.javasim.simulation.image.layer;

import java.awt.Graphics2D;

public interface DrawHandler
{
    public void draw(ImageLayer sender, Graphics2D graphics);
    public void reset(ImageLayer sender, Graphics2D graphics);
}
