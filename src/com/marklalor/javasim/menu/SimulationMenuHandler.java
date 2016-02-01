package com.marklalor.javasim.menu;

import java.awt.Desktop;
import java.io.IOException;

import javax.swing.JFrame;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;

public class SimulationMenuHandler extends MenuHandler
{
    private Simulation simulation;
    
    public SimulationMenuHandler(Simulation simulation)
    {
        this.simulation = simulation;
    }
    
    public Simulation getSimulation()
    {
        return simulation;
    }
    
    public void newSimulation()
    {
        getSimulation().getHome().run(getSimulation().getInfo());
    }
    
    public void saveImage()
    {
        getSimulation().save(getSimulation().getDefaultFile());
    }
    
    public void saveImageAs()
    {
        getSimulation().saveAs();
    }
    
    public void animateMenuItem()
    {
        getSimulation().getAnimate().getFrame().setLocationRelativeTo(getSimulation().getImage().getFrame());
        getSimulation().getAnimate().getFrame().setVisible(true);
    }
    
    public void openContentFolder()
    {
        try
        {
            Desktop.getDesktop().open(getSimulation().getContentDirectory());
        }
        catch(IOException e1)
        {
            JavaSim.getLogger().error("Could not open content folder on native system.", e1);
        }
    }
    
    public void openHomeFolder()
    {
        try
        {
            Desktop.getDesktop().open(getSimulation().getHome().getApplicationPreferences().getSimulationsDirectory());
        }
        catch(IOException e1)
        {
            JavaSim.getLogger().error("Could not open home folder on native system.", e1);
        }
    }
    
    public void closeSimulation()
    {
        getSimulation().getHome().removeSimulation(getSimulation());
    }
    
    public void print()
    {
        getSimulation().print();
    }
    
    public void openProperties()
    {
        getSimulation().getHome().openPreferences();
    }
    
    public void copy()
    {
        getSimulation().copyImageToClipboard();
    }
    
    public void play()
    {
        getSimulation().setStopForBreakpoint(false);
        getSimulation().play();
    }
    
    public void playUntilBreakpoint()
    {
        getSimulation().setStopForBreakpoint(true);
        getSimulation().play();
    }
    
    public void stop()
    {
        getSimulation().stop();
    }
    
    public void nextFrame()
    {
        getSimulation().draw();
        getSimulation().incrementFrameNumber();
    }
    
    public void decreaseSpeed()
    {
        getSimulation().decreaseAnimationSpeed();
    }
    
    public void increaseSpeed()
    {
        getSimulation().increaseAnimationSpeed();
    }
    
    public void reset()
    {
        getSimulation().resetAction();
    }
    
    public void reloadSimulation()
    {
        getSimulation().getHome().run(getSimulation().getInfo());
        getSimulation().close();
    }
    
    public void resizeMenuItem()
    {
        getSimulation().getResize().getFrame().setLocationRelativeTo(getSimulation().getImage().getFrame());
        getSimulation().getResize().getFrame().setVisible(true);
    }
    
    public void fullscreen()
    {
        getSimulation().toggleFullscreen();
    }
    
    public void showConsole()
    {
        getSimulation().getHome().getConsole().setVisible(true);
    }
    
    public void minimize()
    {
        getMenu().getFrameHolder().getFrame().setState(JFrame.ICONIFIED);
    }
}
