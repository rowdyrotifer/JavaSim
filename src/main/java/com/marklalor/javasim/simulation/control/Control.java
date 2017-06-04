package com.marklalor.javasim.simulation.control;

import javax.swing.JPanel;

public abstract class Control<T>
{
    private static int controlCount = 0;
    private JPanel panel;
    private String name;
    
    public Control()
    {
        this(null);
    }
    
    public Control(String name)
    {
        if(name == null)
            name = "_control_" + (controlCount++);
        this.name = name;
    }
    
    protected abstract JPanel createPanel();
    
    public JPanel getPanel()
    {
        if(this.panel == null) //TODO: put this in the constructor...?
            this.panel = createPanel();
        
        return this.panel;
    }
    
    public String getName()
    {
        return name;
    }
    
    public abstract T getValue();
    
    public abstract boolean setValue(Object value);
    
    public abstract JPanel createAnimatePanel();
    
    public abstract Object[] getAnimateValues();
}
