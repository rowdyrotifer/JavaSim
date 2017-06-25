package com.marklalor.javasim.content.interfacing.ui;

import javax.swing.JComponent;

import com.marklalor.javasim.content.interfacing.output.OutputData;

import javafx.embed.swing.SwingNode;
import javafx.scene.Node;

public abstract class SwingInterfacerBase<T, S extends OutputData<T>> extends InterfacerBase<T, S>
{
    private SwingNode swingNode;
    
    public SwingInterfacerBase(S data)
    {
        super(data);
        createNode();
    }
    
    protected abstract JComponent createJComponent();
    
    @Override
    protected Node createNode()
    {
        this.swingNode = new SwingNode();
        this.swingNode.setContent(createJComponent());
        return this.swingNode;
    }
    
    @Override
    public Node getNode()
    {
        return swingNode;
    }
}
