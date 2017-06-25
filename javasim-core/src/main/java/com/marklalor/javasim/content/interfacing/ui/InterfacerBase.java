package com.marklalor.javasim.content.interfacing.ui;

import com.marklalor.javasim.content.interfacing.output.OutputData;

import javafx.scene.Node;

public abstract class InterfacerBase<T, S extends OutputData<T>> implements GuiInterfacer<T>
{
    private S data;
    private Node node;
    
    public InterfacerBase(S data)
    {
        this.data = data;
        this.node = createNode();
    }
    
    protected abstract Node createNode();
    
    @Override
    public S getData()
    {
        return data;
    }
    
    @Override
    public Node getNode()
    {
        return node;
    }
    
}
