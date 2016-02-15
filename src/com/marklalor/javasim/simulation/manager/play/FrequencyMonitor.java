package com.marklalor.javasim.simulation.manager.play;

import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class FrequencyMonitor extends SimulationManager
{
    public FrequencyMonitor(Simulation simulation)
    {
        super(simulation);
    }

    private static final int calculateCountMax = 10;
    private static final float autoRefreshTime = 0.5f; // seconds;
    

    // For finding refresh rate
    private int frequency = 0;
    private long startTime;
    private boolean calculating = true;
    private int calculateCount = 0;
    
    public void stop()
    {
        calculating = false;
        frequency = 0;
    }
    
    public void calculateFrequency()
    {
        frequency = -1;
        calculating = true;
        calculateCount = 0;
    }
    
    public void frequencyTick()
    {
        if(calculating)
        {
            if(calculateCount == 0)
                startTime = System.nanoTime();
            else if(calculateCount == calculateCountMax)
            {
                frequency = (int) ((double) (calculateCount) / (System.nanoTime() - startTime) * 1000000000);
                getSimulation().resolveTitle();
                calculating = false;
            }
            calculateCount++;
        }
        else
        {
            if((System.nanoTime() - startTime) / 1000000000 > autoRefreshTime)
                calculateFrequency();
        }
    }
    
    public int getFrequency()
    {
        return frequency;
    }
}
