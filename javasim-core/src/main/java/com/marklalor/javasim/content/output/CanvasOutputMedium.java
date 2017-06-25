package com.marklalor.javasim.content.output;

import com.marklalor.javasim.content.interfacee.OutputMedium;

import javafx.scene.canvas.Canvas;

public class CanvasOutputMedium implements OutputMedium<Canvas>
{
    private Canvas canvas;
    
    public CanvasOutputMedium()
    {
        this.canvas = new Canvas();
    }
    
    @Override
    public Canvas getNode()
    {
        return canvas;
    }
}
