package com.marklalor.javasim.newsimulation;

import java.util.List;

import com.marklalor.javasim.content.interfacing.sim.Simulation;

public class SimulationAdministrator
{
    private List<SimObject> simulations;

    public void launch(Class<? extends Simulation> clazz, SimulationLaunchParameters params)
    {
        try
        {
            simulations.add(new SimObject(clazz));
            //TODO: create gui if not exist
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
    }
    
}
