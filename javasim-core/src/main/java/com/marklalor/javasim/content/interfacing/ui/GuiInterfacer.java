package com.marklalor.javasim.content.interfacing.ui;

import com.marklalor.javasim.content.interfacing.output.OutputData;

import javafx.scene.Node;

public interface GuiInterfacer<T>
{
    
    public OutputData<T> getData();
    
    public Node getNode();
    
}
