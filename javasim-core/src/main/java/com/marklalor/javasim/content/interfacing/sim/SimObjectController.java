package com.marklalor.javasim.content.interfacing.sim;

import com.marklalor.javasim.content.interfacing.input.InputVariable;
import com.marklalor.javasim.content.interfacing.input.SimInputs;
import com.marklalor.javasim.content.interfacing.output.OutputData;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimInterface;
import com.marklalor.javasim.newsimulation.SimObject;

public class SimObjectController implements SimController
{
    private final SimObject simObject;
    
    public SimObjectController(final SimObject simObject)
    {
        this.simObject = simObject;
    }
    
    public SimObject getSimObject()
    {
        return simObject;
    }

    @Override
    public <T> void registerOutputData(OutputData<T> data)
    {
        getSimObject().getOutputController().getOutputData().add(data);
    }

    @Override
    public <T> void registerInputVariable(InputVariable<T> variable)
    {
        getSimObject().getInputController().getInputVariables().add(variable);
    }

    @Override
    public void registerSimInterface(SimInterface simInterface)
    {
        getSimObject().getInterfaceRepository().add(simInterface);
    }

    @Override
    public void registerInputs(SimInputs inputs)
    {
        getSimObject().getInputController().getInputs().add(inputs);
    }

    @Override
    public SimInterfaces getSimInterfaces()
    {
        return getSimObject().getInterfaceRepository();
    }
}
