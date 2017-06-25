package com.marklalor.javasim.content.interfacing.output;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public abstract class OutputDataBasic<T> implements OutputData<T>
{
    private final T data;
    
    private final String name;
    private final Optional<String> description;
    
    private Set<Consumer<OutputDataChangeEvent>> listeners;
    
    public OutputDataBasic(T data, String name)
    {
        this(data, name, null);
    }

    public OutputDataBasic(T data, String name, String description)
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
    
    @Override
    public void addChangeListener(Consumer<OutputDataChangeEvent> listener)
    {
        this.listeners.add(listener);
    }
    
    @Override
    public void markChanged(OutputDataChangeEvent event)
    {
        event.setOutput(this);
        this.listeners.forEach(consumer -> consumer.accept(event));
    }
}