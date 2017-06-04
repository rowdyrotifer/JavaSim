package com.marklalor.javasim.simulation.frames;

import com.marklalor.javasim.simulation.Simulation;

public interface SimulationHolder
{
    void setSimulation(Simulation simulation);
    Simulation getSimulation();
}
