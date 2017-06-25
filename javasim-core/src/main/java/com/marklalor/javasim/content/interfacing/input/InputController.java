package com.marklalor.javasim.content.interfacing.input;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import com.marklalor.javasim.content.interfacing.BridgeListenersOnAddSet;

public class InputController
{
    private final BridgeListenersOnAddSet<InputVariable<?>, InputVariableChangeEvent<?>> inputVariables;
    
    private final Set<SimInputs> inputs;

    public InputController()
    {
        this.inputVariables = new BridgeListenersOnAddSet<InputVariable<?>, InputVariableChangeEvent<?>>(new HashSet<InputVariable<?>>())
        {
            @Override
            public Consumer<Consumer<InputVariableChangeEvent<?>>> getHook(InputVariable<?> element)
            {
                return element::addChangeListener;
            }
        };
        
        this.inputs = new HashSet<SimInputs>(8);
    }
    
    public Set<InputVariable<?>> getInputVariables()
    {
        return inputVariables;
    }
    
    public Set<SimInputs> getInputs()
    {
        return inputs;
    }
    
    public void addInputVariableChangeListener(Consumer<InputVariableChangeEvent<?>> listener)
    {
        inputVariables.getListeners().add(listener);
    }
}
