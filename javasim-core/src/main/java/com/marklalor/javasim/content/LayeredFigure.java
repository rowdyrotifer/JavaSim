package com.marklalor.javasim.content;

import java.util.List;

import com.marklalor.javasim.content.figure.FigureLayer;

public interface LayeredFigure extends Figure
{
    public List<FigureLayer> getLayers();
}
