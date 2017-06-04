package com.marklalor.javasim.menu.menus;

import javax.swing.JFrame;

import com.marklalor.javasim.menu.MenuHandler;
import com.marklalor.javasim.menu.MenuUtils;

public class WindowMenuHandler extends MenuHandler<JavaSimMenu>
{
    public void home()
    {
        getMenu().getHome().getFrame().setVisible(true);
        getMenu().getHome().getFrame().toFront();
        getMenu().getHome().getSimulationList().requestFocus();
    }
    
    public void console()
    {
        getMenu().getHome().getConsole().getFrame().setLocationRelativeTo(getMenu().getFrameHolder().getFrame());
        getMenu().getHome().getConsole().getFrame().setLocation(getMenu().getHome().getConsole().getFrame().getLocation().x + (getMenu().getFrameHolder().getFrame().getWidth() / 2) + (getMenu().getHome().getConsole().getFrame().getWidth() / 2), getMenu().getFrameHolder().getFrame().getY());
        getMenu().getHome().getConsole().getFrame().setVisible(true);
        MenuUtils.show(getMenu().getHome().getConsole());
    }
    
    public void controls()
    {
        MenuUtils.show(getMenu().getSimulation().getControls());
    }
    
    public void minimize()
    {
        getMenu().getFrameHolder().getFrame().setState(JFrame.ICONIFIED);
    }
}
