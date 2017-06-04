package com.marklalor.javasim.menu.menus;

import com.marklalor.javasim.menu.MenuHandler;

public class AnimationMenuHandler extends MenuHandler<JavaSimMenu>
{
    public void play()
    {
        getMenu().getSimulation().getPlayManager().setStopAtBreakpoint(false);
        getMenu().getSimulation().getPlayManager().play();
    }
    
    public void playUntilBreakpoint()
    {
        getMenu().getSimulation().getPlayManager().setStopAtBreakpoint(true);
        getMenu().getSimulation().getPlayManager().play();
    }
    
    public void stop()
    {
        getMenu().getSimulation().getPlayManager().stop();
    }
    
    public void nextFrame()
    {
        getMenu().getSimulation().getPlayManager().incrementFrameNumber();
        getMenu().getSimulation().getImage().renderAggregateImage(false);
        getMenu().getSimulation().getImage().repaint();
    }
    
    public void decreaseSpeed()
    {
        getMenu().getSimulation().getPlayManager().decreaseAnimationSpeed();
    }
    
    public void increaseSpeed()
    {
        getMenu().getSimulation().getPlayManager().increaseAnimationSpeed();
    }
}