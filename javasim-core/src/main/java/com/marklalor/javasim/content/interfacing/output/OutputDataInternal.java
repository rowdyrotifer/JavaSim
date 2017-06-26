package com.marklalor.javasim.content.interfacing.output;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;

public abstract class OutputDataInternal<T> implements OutputData<T>
{
    private final T data;
    private final String name;
    private final Optional<String> description;

    private final Set<Consumer<OutputDataChangeEvent>> listeners;
    
    public OutputDataInternal(T data, String name)
    {
        this(data, name, null);
    }

    public OutputDataInternal(T data, String name, String description)
    {
        this.data = data;
        
        this.name = name;
        this.description = Optional.ofNullable(description);
        
        this.listeners = new HashSet<>(4);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public T getData()
    {
        return data;
    }

    @Override
    public Optional<String> getDescription()
    {
        return description;
    }
    
    /*
     * Internal properties.
     */
    
    public abstract OutputDataType getOutputDataType();
        
    public void addChangeListener(Consumer<OutputDataChangeEvent> listener)
    {
        this.listeners.add(listener);
    }
    
    public void markChanged(OutputDataChangeEvent event)
    {
        event.setOutput(this);
        this.listeners.forEach(consumer -> consumer.accept(event));
    }
}