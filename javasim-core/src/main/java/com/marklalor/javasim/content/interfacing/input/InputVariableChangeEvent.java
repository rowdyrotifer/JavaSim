package com.marklalor.javasim.content.interfacing.input;

public class InputVariableChangeEvent<T>
{
    private final InputVariable<T> inputVariable;
    private final T previous, current;
    
    public InputVariableChangeEvent(InputVariable<T> inputVariable, T previous, T current)
    {
        this.inputVariable = inputVariable;
        this.previous = previous;
        this.current = current;
    }
    
    public InputVariable<T> getInputVariable()
    {
        return inputVariable;
    }
    
    public T getPrevious()
    {
        return previous;
    }
    
    public T getCurrent()
    {
        return current;
    }
}
