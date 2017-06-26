package com.marklalor.javasim.content.interfacing.sim;

import com.marklalor.javasim.content.interfacing.input.InputVariable;
import com.marklalor.javasim.content.interfacing.input.SimInputs;
import com.marklalor.javasim.content.interfacing.output.OutputDataInternal;
import com.marklalor.javasim.content.interfacing.output.OutputDataWrapper;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimInterface;
import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;
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

    @SuppressWarnings("unchecked")
    @Override
    public <T> OutputDataInternal<?> createOutputData(OutputDataType type)
    {
        //getSimObject().getOutputController().getOutputData().add(data);
        try
        {
            // Create the OutputDataInstance and wrap it up so that this instance
            // can be swapped out later by the user interface.
            OutputDataInternal<?> outputData = type.getClassType().newInstance();
            OutputDataWrapper<?> wrapped = new OutputDataWrapper<>(outputData);
            
            // This is the unchecked cast. This will fail if the user sets
            // their return value as a class not specified by the OutputDataType
            return type.getClassType().cast(wrapped);
        }
        catch(InstantiationException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch(IllegalAccessException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        throw new RuntimeException("Could not create output data object for " + type);
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
