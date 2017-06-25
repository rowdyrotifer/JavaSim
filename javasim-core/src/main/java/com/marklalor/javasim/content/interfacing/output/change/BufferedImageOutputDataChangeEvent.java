package com.marklalor.javasim.content.interfacing.output.change;

import java.awt.image.BufferedImage;

import com.marklalor.javasim.content.interfacing.output.OutputDataChangeEvent;

public class BufferedImageOutputDataChangeEvent extends OutputDataChangeEvent
{
    private BufferedImage current;

    public BufferedImageOutputDataChangeEvent(BufferedImage current)
    {
        this.current = current;
    }
    
    public BufferedImage getCurrent()
    {
        return current;
    }
}
