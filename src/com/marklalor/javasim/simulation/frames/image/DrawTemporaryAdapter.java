package com.marklalor.javasim.simulation.frames.image;

import java.awt.Graphics2D;

public class DrawTemporaryAdapter implements DrawHandler
{
    public void draw(ImageLayer sender, Graphics2D graphics)
    {
        
    }
    
    public void reset(ImageLayer sender, Graphics2D graphics)
    {
        throw new RuntimeException("Should not reset a temporary layer.");
    }
}
