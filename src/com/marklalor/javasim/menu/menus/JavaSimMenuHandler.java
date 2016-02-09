package com.marklalor.javasim.menu.menus;

import java.awt.Desktop;
import java.io.IOException;

import javax.swing.JFrame;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.menu.MenuHandler;
import com.marklalor.javasim.menu.MenuUtils;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.SubFrame;
import com.marklalor.javasim.simulation.frames.image.Image;

public class JavaSimMenuHandler extends MenuHandler
{
    private Simulation simulation;
    private Home home;
    
    public JavaSimMenuHandler(Home home, Simulation simulation)
    {
        this.home = home;
        this.simulation = simulation;
    }
    
    public void setHome(Home home)
    {
        this.home = home;
    }
    
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
   
    public Home getHome()
    {
        return home;
    }
    
    public Simulation getSimulation()
    {
        return simulation;
    }
    
    public void newSimulation()
    {
        getHome().run(getSimulation().getInfo());
    }
    
    public void openSimulation()
    {
        
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
        MenuUtils.showRelative(getSimulation().getAnimate(), getSimulation().getImage());
    }
    
    public void refresh()
    {
        
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
            Desktop.getDesktop().open(getHome().getApplicationPreferences().getSimulationsDirectory());
        }
        catch(IOException e1)
        {
            JavaSim.getLogger().error("Could not open home folder on native system.", e1);
        }
    }
    
    public void close()
    {
        if (getMenu().getFrameHolder() instanceof Image)
            getMenu().getFrameHolder().getFrame().dispose();
        else
        {
            getMenu().getFrameHolder().getFrame().setVisible(false);
            if (getMenu().getFrameHolder() instanceof SubFrame)
            {
                SubFrame subFrame = (SubFrame)getMenu().getFrameHolder();
                subFrame.getImage().getFrame().toFront();
                subFrame.getImage().getFrame().requestFocus();
            }
        }
    }
    
    public void closeAll()
    {
        JavaSim.getLogger().debug("Closing all simulations.");
    }
    
    public void print()
    {
        getSimulation().print();
    }
    
    public void openProperties()
    {
        getHome().openPreferences();
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
        getHome().run(getSimulation().getInfo());
        getSimulation().close();
    }
    
    public void resizeMenuItem()
    {
        MenuUtils.showRelative(getSimulation().getResize(), getSimulation().getImage());
    }
    
    public void fullscreen()
    {
        getSimulation().toggleFullscreen();
    }
    
    public void home()
    {
        getHome().getFrame().setVisible(true);
        getHome().getFrame().toFront();
        getHome().getSimulationList().requestFocus();
    }
    
    public void console()
    {
        MenuUtils.show(getHome().getConsole());
    }
    
    public void controls()
    {
        MenuUtils.show(getSimulation().getControls());
    }
    
    public void minimize()
    {
        getMenu().getFrameHolder().getFrame().setState(JFrame.ICONIFIED);
    }
}
