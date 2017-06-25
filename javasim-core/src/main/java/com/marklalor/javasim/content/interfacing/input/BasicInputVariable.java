package com.marklalor.javasim.content.interfacing.input;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class BasicInputVariable<T> implements InputVariable<T>
{
    private List<T> values;
    private int index;
    private final int id;
    private final String name;
    private final Optional<String> description;
    
    private Set<Consumer<InputVariableChangeEvent<?>>> listeners;
    
    public BasicInputVariable(int id) {
        this(id, "var-" + id);
    }
    
    public BasicInputVariable(int id, String name) {
        this(id, name, null);
    }
    
    public BasicInputVariable(int id, String name, String description)
    {
        this.id = id;
        this.name = name;
        this.description = description == null ? Optional.empty() : Optional.of(description);
        this.index = 0;
        
        this.listeners = new HashSet<>();
    }

    @Override
    public List<T> getAll()
    {
        return values;
    }

    @Override
    public void setAll(List<T> values)
    {
        this.values = values;
    }

    @Override
    public int getID()
    {
        return id;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Optional<String> getDescription()
    {
        return description;
    }

    @Override
    public T getCurrent()
    {
        return get(this.index);
    }

    @Override
    public T get(int index)
    {
        return values.get(index);
    }

    @Override
    public void setCurrent(int index)
    {
        this.index = index;
    }

    @Override
    public void addChangeListener(Consumer<InputVariableChangeEvent<?>> listener)
    {
        this.listeners.add(listener);
    }
    
}
