package com.marklalor.javasim.content.interfacing.sim.interfaces;

import com.marklalor.javasim.content.interfacing.output.OutputData;

public abstract class OutputBindableBase<U extends OutputData<?>> implements OutputBindable<U>
{
    private U data;
    
    @Override
    public void bind(U data)
    {
        this.data = data;
    }
    
    @Override
    public U getBoundOutput()
    {
        return this.data;
    }
}
