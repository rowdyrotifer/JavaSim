package com.marklalor.javasim.content.interfacing.output;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.marklalor.javasim.content.interfacing.BridgeListenersOnAddSet;

public final class OutputController
{
    private final BridgeListenersOnAddSet<OutputData<?>, OutputDataChangeEvent> outputData;
    
    public OutputController()
    {
        this.outputData = new BridgeListenersOnAddSet<OutputData<?>, OutputDataChangeEvent>(new HashSet<OutputData<?>>(8))
        {
            @Override
            public Consumer<Consumer<OutputDataChangeEvent>> getHook(OutputData<?> element)
            {
                return element::addChangeListener;
            }
        };
    }
    
    public Set<OutputData<?>> getOutputData()
    {
        return outputData;
    }
    
    public void addOutputDataChangeListener(Consumer<OutputDataChangeEvent> listener)
    {
        outputData.getListeners().add(listener);
    }
}
