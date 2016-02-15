package com.marklalor.javasim.simulation.manager;

import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.SimulationHolder;

public class SimulationManager implements SimulationHolder
{
    private Simulation simulation;
    
    public SimulationManager(Simulation simulation)
    {
        this.simulation = simulation;
    }

    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }

    @Override
    public Simulation getSimulation()
    {
        return simulation;
    }
}
