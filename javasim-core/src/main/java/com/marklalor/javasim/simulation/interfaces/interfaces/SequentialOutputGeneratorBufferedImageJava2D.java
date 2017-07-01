package com.marklalor.javasim.simulation.interfaces.interfaces;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import com.marklalor.javasim.content.interfacing.output.OutputData;

public abstract class SequentialOutputGeneratorBufferedImageJava2D
extends OutputBindableBase<OutputData<List<BufferedImage>>>
implements SequentialOutputGenerator
{
    public abstract void draw(Graphics2D context);
    
    @Override
    public void generateNextOutputData()
    {
        final BufferedImage image = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = image.createGraphics();
        draw(graphics);
        graphics.dispose();
        getBoundOutput().getData().add(image);
    }
}
