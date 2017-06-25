package com.marklalor.javasim.content;

import com.marklalor.javasim.content.interfacee.Content;
import com.marklalor.javasim.content.interfacee.OutputMedium;
import com.marklalor.javasim.content.output.CanvasOutputMedium;

import javafx.scene.canvas.Canvas;

public abstract class CanvasFigureBase implements Content<Canvas>
{
    private OutputMedium<Canvas> output;
    
    public CanvasFigureBase()
    {
        this.output = new CanvasOutputMedium();
    }
    
    @Override
    public OutputMedium<Canvas> getOutput()
    {
        return this.output;
    }
}
