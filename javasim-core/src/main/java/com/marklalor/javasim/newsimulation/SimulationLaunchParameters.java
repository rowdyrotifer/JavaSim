package com.marklalor.javasim.newsimulation;

public class SimulationLaunchParameters
{
    public static final SimulationLaunchParameters DEFAULT = new SimulationLaunchParameters();
    
    private final boolean gui;
    
    public SimulationLaunchParameters(String[] args)
    {
        this();
    }
    
    public SimulationLaunchParameters()
    {
        this.gui = true;
    }
    
    public boolean useGui()
    {
        return gui;
    }
}
