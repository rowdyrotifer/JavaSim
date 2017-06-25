package com.marklalor.javasim.content.interfacing.sim;

import com.marklalor.javasim.content.interfacing.input.InputVariable;
import com.marklalor.javasim.content.interfacing.input.SimInputs;
import com.marklalor.javasim.content.interfacing.output.OutputData;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimInterface;

public interface SimController
{
    public <T> void registerOutputData(OutputData<T> data);
    
    public <T> void registerInputVariable(InputVariable<T> variable);
    
    public void registerSimInterface(SimInterface simInterface);
    
    public SimInterfaces getSimInterfaces();
    
    public void registerInputs(SimInputs inputs);
}
