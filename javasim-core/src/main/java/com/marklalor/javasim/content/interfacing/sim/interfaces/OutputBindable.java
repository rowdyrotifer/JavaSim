package com.marklalor.javasim.content.interfacing.sim.interfaces;

import com.marklalor.javasim.content.interfacing.output.OutputData;

public interface OutputBindable<U extends OutputData<?>>
{
    public void bind(U data);
    
    public U getBoundOutput();
}
