package com.marklalor.javasim.content.interfacing.output;

public abstract class OutputDataChangeEvent
{
    private OutputData<?> output;
    
    public OutputData<?> getOutput()
    {
        return output;
    }
    
    void setOutput(OutputData<?> output)
    {
        this.output = output;
    }
    
}
