package com.marklalor.javasim.menu.menus;

import com.marklalor.javasim.menu.MenuHandler;
import com.marklalor.javasim.menu.MenuUtils;

public class SimulationMenuHandler extends MenuHandler<JavaSimMenu>
{   
    public void reset()
    {
        getMenu().getSimulation().resetAction();
    }
    
    public void reloadSimulation()
    {
        getMenu().getHome().run(getMenu().getSimulation().getInfo());
        getMenu().getSimulation().close();
    }
    
    public void resizeMenuItem()
    {
        MenuUtils.showRelative(getMenu().getSimulation().getResize(), getMenu().getSimulation().getImage());
    }
    
    public void fullscreen()
    {
        getMenu().getSimulation().toggleFullscreen();
    }
}