package com.marklalor.javasim.simulation.interfaces;

import com.marklalor.javasim.content.interfacing.input.InputVariable;
import com.marklalor.javasim.content.interfacing.input.SimInputs;
import com.marklalor.javasim.content.interfacing.output.OutputDataInternal;
import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;
import com.marklalor.javasim.simulation.interfaces.interfaces.SimInterface;

public interface SimController
{
    public <T> OutputDataInternal<?> createOutputData(OutputDataType type);
    
    public <T> void registerInputVariable(InputVariable<T> variable);
    
    public void registerSimInterface(SimInterface simInterface);
    
    public void registerInputs(SimInputs inputs);
}
