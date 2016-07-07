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
    private static final long autoRefreshTime = (long) 0.5e9; // 0.5 seconds in nanoseconds;
    public static final int NO_FREQUENCY = -1;
    

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
        frequency = NO_FREQUENCY;
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
            if(System.nanoTime() - startTime > autoRefreshTime)
                calculateFrequency();
        }
    }
    
    public int getFrequency()
    {
        return frequency;
    }
}
