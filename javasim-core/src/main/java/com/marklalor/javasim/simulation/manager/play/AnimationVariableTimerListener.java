package com.marklalor.javasim.simulation.manager.play;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.control.Control;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class AnimationVariableTimerListener extends SimulationManager implements ActionListener
{
    private List<Control<?>> animationControls;
    private Control<?> currentAnimationControl;
    private List<Object> currentAnimationControlValueQueue;
    
    public AnimationVariableTimerListener(Simulation simulation)
    {
        super(simulation);
    }
    
    public List<Control<?>> getAnimationControls()
    {
        return animationControls;
    }
    
    public void setAnimationControls(List<Control<?>> animationControls)
    {
        this.animationControls = animationControls;
    }
    
    public Control<?> getCurrentAnimationControl()
    {
        return currentAnimationControl;
    }
    
    public void setCurrentAnimationControl(Control<?> currentAnimationControl)
    {
        this.currentAnimationControl = currentAnimationControl;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if(currentAnimationControl == null || currentAnimationControlValueQueue.size() == 0)
        {
            currentAnimationControl = animationControls.remove(0);
            currentAnimationControlValueQueue = new ArrayList<Object>(Arrays.asList(currentAnimationControl.getAnimateValues()));
        }
        
        Object value = currentAnimationControlValueQueue.remove(0);
        currentAnimationControl.setValue(value);
        
        getSimulation().resetAction();
        getSimulation().getPlayManager().getFrequencyMonitor().frequencyTick();
        
        int delay = 0;
        if(getSimulation().getPlayManager().getVariableFrameNumber() == 0)
            delay = getSimulation().getAnimate().getStartDelay();
        else
            delay = getSimulation().getAnimate().getFrameDelay();
        
        boolean last = (animationControls.isEmpty() && currentAnimationControlValueQueue.isEmpty());
        
        if(last)
            delay = getSimulation().getAnimate().getStopDelay();
        
        try
        {
            getSimulation().getPlayManager().getGifSequenceWriter().writeToSequence(getSimulation().getImage().getAggregateImage(), delay);
        }
        catch(IOException e1)
        {
            JavaSim.getLogger().error("Could not write (variable) animation frame to file.", e1);
        }
        
        if(last)
            getSimulation().getPlayManager().stop();
        
        getSimulation().getPlayManager().incrementVariableFrameNumber();
    }
}
