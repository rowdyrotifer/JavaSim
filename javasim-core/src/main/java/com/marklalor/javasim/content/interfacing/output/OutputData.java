package com.marklalor.javasim.content.interfacing.output;

import java.util.function.Consumer;

import com.marklalor.javasim.content.interfacing.Describable;
import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;

public interface OutputData<T> extends Describable
{
    public T getData();
    
    // Would rather not have a set data unless it seems very useful later
    // Prefer immutable all the way for implementations of OutputData<T>
    
    public void addChangeListener(Consumer<OutputDataChangeEvent> listener);
    
    public void markChanged(OutputDataChangeEvent event);
    
    // Define a new one for new output data implementations
    public OutputDataType getChangeEventType();
}
