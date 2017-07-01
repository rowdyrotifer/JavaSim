package com.marklalor.javasim.simulation.interfaces;

import java.util.Set;

import com.marklalor.javasim.simulation.interfaces.interfaces.SequentialOutputGenerator;
import com.marklalor.javasim.simulation.interfaces.interfaces.SimExitable;

public interface SimInterfaces
{
    public Set<SimExitable> getExitables();
    
    public Set<SequentialOutputGenerator> getSequentialOutputGenerators();
}
