package com.marklalor.javasim.content.interfacing.sim;

import com.marklalor.javasim.content.interfacing.input.InputVariable;
import com.marklalor.javasim.content.interfacing.input.SimInputs;
import com.marklalor.javasim.content.interfacing.output.OutputDataInternal;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimInterface;
import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;

public interface SimController
{
    public <T> OutputDataInternal<?> createOutputData(OutputDataType type);
    
    public <T> void registerInputVariable(InputVariable<T> variable);
    
    public void registerSimInterface(SimInterface simInterface);
    
    public SimInterfaces getSimInterfaces();
    
    public void registerInputs(SimInputs inputs);
}
