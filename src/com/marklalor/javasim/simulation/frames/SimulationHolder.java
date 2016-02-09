package com.marklalor.javasim.simulation.frames;

import com.marklalor.javasim.simulation.Simulation;

public interface SimulationHolder
{
    Simulation getSimulation();
    void setSimulation(Simulation simulation);
}
