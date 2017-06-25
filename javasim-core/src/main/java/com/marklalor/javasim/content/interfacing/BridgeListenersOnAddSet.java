package com.marklalor.javasim.content.interfacing;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.collect.ForwardingSet;

/**
 * Trying to ease the clutter and pain of adding a listener
 * that us actually forwarding calls from another listener.
 * 
 */
public abstract class BridgeListenersOnAddSet<E, T> extends ForwardingSet<E>
{
    private Set<E> delegate;
    private Set<Consumer<T>> listeners;
    
    public BridgeListenersOnAddSet(Set<E> delegate)
    {
        this.delegate = delegate;
        this.listeners = new HashSet<>(8);
    }
    
    @Override
    public boolean add(E element)
    {
        getHook(element).accept(this::change);;
        return super.add(element);
    }
    
    public Set<Consumer<T>> getListeners()
    {
        return listeners;
    }
    
    private void change(T event)
    {
        listeners.forEach(consumer -> consumer.accept(event));
    }
    
    public abstract Consumer<Consumer<T>> getHook(E element);

    @Override
    public boolean addAll(Collection<? extends E> collection)
    {
        return standardAddAll(collection);
    }

    @Override
    protected Set<E> delegate()
    {
        return delegate;
    }
    
}
