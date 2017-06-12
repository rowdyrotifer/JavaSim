package com.marklalor.javasim.content.figure;

import java.awt.Graphics2D;
import java.awt.Image;

public interface FigureLayer
{
    public Image getImage();
    
    public void draw(Graphics2D graphics);
}
