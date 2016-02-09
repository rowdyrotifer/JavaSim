package com.marklalor.javasim.menu.menus;

import java.awt.Desktop;
import java.io.IOException;

import javax.swing.JFrame;

import com.marklalor.javasim.Home;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.menu.MenuHandler;

public class HomeMenuHandler extends MenuHandler
{
    private Home home;
    
    public HomeMenuHandler(Home home)
    {
        this.home = home;
    }
    
    public Home getHome()
    {
        return home;
    }
    
    public void openSimulation()
    {
        
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
    
    public void refreshSimulations()
    {
        getHome().loadSimulations();
    }
    
    public void close()
    {
        getHome().getFrame().dispose();
    }
    
    public void minimize()
    {
        home.getFrame().setState(JFrame.ICONIFIED);
    }
    
    public void showConsole()
    {
        getHome().getConsole().getFrame().setVisible(true);
    }
}
