package com.marklalor.javasim.simulation.manager.play;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.stream.FileImageOutputStream;
import javax.swing.Timer;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.misc.image.GifSequenceWriter;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.control.Control;
import com.marklalor.javasim.simulation.manager.SimulationManager;

public class PlayManager extends SimulationManager
{
    
    private GifSequenceWriter gifSequenceWriter;
    private boolean stopAtBreakpoint;
    

    private int frameNumber;
    private Timer manualTimer;
    private ManualTimerListener manualTimerListener;
    private Timer animationTimer;
    private AnimationTimerListener animationTimerListener;
    
    private int variableFrameNumber;
    private Timer animationVariableTimer;
    private AnimationVariableTimerListener animationVariableTimerListener;
    
    private FrequencyMonitor frequencyMonitor;
    
    public PlayManager(Simulation simulation)
    {
        super(simulation);
        
        this.frequencyMonitor = new FrequencyMonitor(simulation);
        this.frameNumber = 0;
        this.variableFrameNumber = 0;
        
        setUpTimers();
    }

    private void setUpTimers()
    {
        manualTimerListener = new ManualTimerListener(getSimulation());
        manualTimer = new Timer(0, manualTimerListener);
        
        animationTimerListener = new AnimationTimerListener(getSimulation());
        animationTimer = new Timer(0, animationTimerListener);
        
        animationVariableTimerListener = new AnimationVariableTimerListener(getSimulation());
        animationVariableTimer = new Timer(0, animationVariableTimerListener);
    }
    
    public Timer getManualTimer()
    {
        return manualTimer;
    }
    
    public ManualTimerListener getManualTimerListener()
    {
        return manualTimerListener;
    }
    
    public Timer getAnimationTimer()
    {
        return animationTimer;
    }
    
    public AnimationTimerListener getAnimationTimerListener()
    {
        return animationTimerListener;
    }
    
    public Timer getAnimationVariableTimer()
    {
        return animationVariableTimer;
    }
    
    public AnimationVariableTimerListener getAnimationVariableTimerListener()
    {
        return animationVariableTimerListener;
    }
    
    public int getFrameNumber()
    {
        return frameNumber;
    }
    
    public void setFrameNumber(int frameNumber)
    {
        this.frameNumber = frameNumber;
    }
    
    public void incrementFrameNumber()
    {
        this.frameNumber++;
    }
    
    public int getVariableFrameNumber()
    {
        return variableFrameNumber;
    }
    
    public void setVariableFrameNumber(int variableFrameNumber)
    {
        this.variableFrameNumber = variableFrameNumber;
    }
    
    public void incrementVariableFrameNumber()
    {
        this.variableFrameNumber++;
    }

    public void setStopAtBreakpoint(boolean stopAtBreakpoint)
    {
        this.stopAtBreakpoint = stopAtBreakpoint;
    }

    public boolean shouldStopForBreakpoint()
    {
        return stopAtBreakpoint;
    }
    
    public GifSequenceWriter getGifSequenceWriter()
    {
        return gifSequenceWriter;
    }
    
    public FrequencyMonitor getFrequencyMonitor()
    {
        return frequencyMonitor;
    }
    
    public void play()
    {
        if(!getManualTimer().isRunning())
            getManualTimer().start();
    }
    
    public boolean isCreatingAnimation()
    {
        return getAnimationTimer().isRunning() || getAnimationVariableTimer().isRunning();
    }
    
    public void startAnimate()
    {
        this.setStopAtBreakpoint(getSimulation().getAnimate().getStopAtBreakpoint());
        
        getSimulation().resetAction();
        
        try
        {
            gifSequenceWriter = new GifSequenceWriter(
                    new FileImageOutputStream(getSimulation().getAnimate().getFile()),
                    BufferedImage.TYPE_INT_ARGB,
                    getSimulation().getAnimate().getFrameDelay(),
                    getSimulation().getAnimate().getLoop());
            return;
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Error trying to create GifSequenceWriter", e);
        }
        
        getAnimationTimer().start();
    }
    
    public void startAnimateVariable()
    {
        setVariableFrameNumber(0);
        
        try
        {
            gifSequenceWriter = new GifSequenceWriter(
                    new FileImageOutputStream(getSimulation().getAnimate().getFile()),
                    BufferedImage.TYPE_INT_ARGB,
                    getSimulation().getAnimate().getFrameDelay(),
                    getSimulation().getAnimate().getLoop());
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Error trying to create GifSequenceWriter", e);
            return;
        }
        
        getAnimationVariableTimerListener().setAnimationControls(new ArrayList<Control<?>>(getSimulation().getAnimate().getAddedControls()));
        getAnimationVariableTimerListener().setCurrentAnimationControl(null);
        
        getAnimationVariableTimer().start();
    }
    
    private static final int changeInSpeed = 10; // TODO: maybe make this scale in some way to
    // accommodate for the 1/x behavior.
    
    public void decreaseAnimationSpeed()
    {
        getManualTimer().setDelay(getManualTimer().getDelay() + changeInSpeed);
        getFrequencyMonitor().calculateFrequency();
    }
    
    public void increaseAnimationSpeed()
    {
        getManualTimer().setDelay(getManualTimer().getDelay() - changeInSpeed >= 1 ? getManualTimer().getDelay() - changeInSpeed : 0);
        getFrequencyMonitor().calculateFrequency();
    }
    
    public void stop()
    {
        JavaSim.getLogger().debug("Stopping timers ");
        setStopAtBreakpoint(false);
        getFrequencyMonitor().stop();
        getSimulation().resolveTitle();
        
        if(getManualTimer().isRunning())
            getManualTimer().stop();
        if(getAnimationTimer().isRunning())
            getAnimationTimer().stop();
        if(getAnimationVariableTimer().isRunning())
            getAnimationVariableTimer().stop();
        
    }
}