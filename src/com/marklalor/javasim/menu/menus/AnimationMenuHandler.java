package com.marklalor.javasim.menu.menus;

import com.marklalor.javasim.menu.MenuHandler;

public class AnimationMenuHandler extends MenuHandler<JavaSimMenu>
{
    public void play()
    {
        getMenu().getSimulation().setStopForBreakpoint(false);
        getMenu().getSimulation().play();
    }
    
    public void playUntilBreakpoint()
    {
        getMenu().getSimulation().setStopForBreakpoint(true);
        getMenu().getSimulation().play();
    }
    
    public void stop()
    {
        getMenu().getSimulation().stop();
    }
    
    public void nextFrame()
    {
        getMenu().getSimulation().getImage().renderAggregateImage(false);
        getMenu().getSimulation().getImage().repaint();
        getMenu().getSimulation().incrementFrameNumber();
    }
    
    public void decreaseSpeed()
    {
        getMenu().getSimulation().decreaseAnimationSpeed();
    }
    
    public void increaseSpeed()
    {
        getMenu().getSimulation().increaseAnimationSpeed();
    }
}