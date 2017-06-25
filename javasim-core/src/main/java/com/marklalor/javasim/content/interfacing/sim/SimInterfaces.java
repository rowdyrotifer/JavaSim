package com.marklalor.javasim.content.interfacing.sim;

import java.util.Set;

import com.marklalor.javasim.content.interfacing.sim.interfaces.SequentialOutputGenerator;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimExitable;

public interface SimInterfaces
{
    public Set<SimExitable> getExitables();
    
    public Set<SequentialOutputGenerator> getSequentialOutputGenerators();
}
