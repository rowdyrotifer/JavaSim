package com.marklalor.javasim.menu.menus;

import java.awt.Desktop;
import java.io.IOException;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.menu.MenuHandler;
import com.marklalor.javasim.menu.MenuUtils;
import com.marklalor.javasim.simulation.frames.SimulationFrame;
import com.marklalor.javasim.simulation.image.Image;

public class FileMenuHandler extends MenuHandler<JavaSimMenu>
{   
    public void newSimulation()
    {
        getMenu().getHome().run(getMenu().getSimulation().getInfo());
    }
    
    public void openSimulation()
    {
        
    }
    
    public void saveImage()
    {
        getMenu().getSimulation().save(getMenu().getSimulation().getDefaultFile());
    }
    
    public void saveImageAs()
    {
        getMenu().getSimulation().saveAs();
    }
    
    public void animateMenuItem()
    {
        MenuUtils.showRelative(getMenu().getSimulation().getAnimate(), getMenu().getSimulation().getImage());
    }
    
    public void refresh()
    {
        
    }
    
    public void openContentFolder()
    {
        try
        {
            Desktop.getDesktop().open(getMenu().getSimulation().getContentDirectory());
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Could not open content folder on native system.", e);
        }
    }
    
    public void openHomeFolder()
    {
        try
        {
            Desktop.getDesktop().open(getMenu().getHome().getApplicationPreferences().getSimulationsDirectory());
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
            if (getMenu().getFrameHolder() instanceof SimulationFrame)
            {
                SimulationFrame subFrame = (SimulationFrame)getMenu().getFrameHolder();
                subFrame.getSimulation().getImage().getFrame().toFront();
                subFrame.getSimulation().getImage().getFrame().requestFocus();
            }
        }
    }
    
    public void closeAll()
    {
        
    }
    
    public void print()
    {
        getMenu().getSimulation().print();
    }
}
