package com.marklalor.javasim.simulation.manager.play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class ManualTimerListener extends SimulationManager implements ActionListener
{
    public ManualTimerListener(Simulation simulation)
    {
        super(simulation);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        getSimulation().getImage().renderAggregateImage(false);
        getSimulation().getImage().repaint();
        getSimulation().getPlayManager().incrementFrameNumber();
        getSimulation().getPlayManager().getFrequencyMonitor().frequencyTick();
    }
}
