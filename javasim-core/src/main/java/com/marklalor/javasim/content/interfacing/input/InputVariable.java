package com.marklalor.javasim.content.interfacing.input;

import java.util.List;
import java.util.function.Consumer;

import com.marklalor.javasim.content.interfacing.Describable;

public interface InputVariable<T> extends Describable
{
    public T getCurrent();
    
    public T get(int index);
    
    public void setCurrent(int index);
    
    public List<T> getAll();
    
    public void setAll(List<T> values);
    
    public int getID();
    
    public void addChangeListener(Consumer<InputVariableChangeEvent<?>> event);
}
