package com.marklalor.javasim.simulation.manager.play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class AnimationTimerListener extends SimulationManager implements ActionListener
{
    public AnimationTimerListener(Simulation simulation)
    {
        super(simulation);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        getSimulation().getImage().renderAggregateImage(false);
        getSimulation().getImage().repaint();
        
        getSimulation().getPlayManager().getFrequencyMonitor().frequencyTick();
        
        int frame = getSimulation().getPlayManager().getFrameNumber();
        int startFrame = getSimulation().getAnimate().getStartFrame();
        int stopFrame = getSimulation().getAnimate().getStopFrame();
        
        // Determine the delay depending on where we are in the animation (use start, stop, or intermediate delay)
        int delay = 0;
        
        if(frame == startFrame)
            delay = getSimulation().getAnimate().getStartDelay();
        if(frame != stopFrame && frame != startFrame)
            delay = getSimulation().getAnimate().getFrameDelay();
        if(frame == stopFrame || !getSimulation().getPlayManager().getAnimationTimer().isRunning())
            delay = getSimulation().getAnimate().getStopDelay();
        
        if(frame % getSimulation().getAnimate().getSaveEvery() == 0 ||
                frame == startFrame ||
                frame == stopFrame ||
                !getSimulation().getPlayManager().getAnimationTimer().isRunning())
            try
            {
                getSimulation().getPlayManager().getGifSequenceWriter().writeToSequence(getSimulation().getImage().getAggregateImage(), delay);
            }
            catch(IOException e1)
            {
                JavaSim.getLogger().error("Could not write animation frame to file.", e1);
            }
        
        if(frame == stopFrame)
            getSimulation().getPlayManager().stop();
        
        getSimulation().getPlayManager().incrementFrameNumber();
    }
}
