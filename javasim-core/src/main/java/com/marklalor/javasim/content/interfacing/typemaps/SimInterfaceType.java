package com.marklalor.javasim.content.interfacing.typemaps;

import com.marklalor.javasim.simulation.interfaces.interfaces.SequentialOutputGenerator;
import com.marklalor.javasim.simulation.interfaces.interfaces.SimExitable;
import com.marklalor.javasim.simulation.interfaces.interfaces.SimInterface;

public enum SimInterfaceType
{
    EXITABLE(SimExitable.class),
    SEQUENTIAL_OUTPUT_GENERATING(SequentialOutputGenerator.class);
    
    private final Class<? extends SimInterface> clazz;
    
    SimInterfaceType(Class<? extends SimInterface> clazz)
    {
        this.clazz = clazz;
    }
    
    public Class<? extends SimInterface> getSimInterfaceClass()
    {
        return clazz;
    }
}