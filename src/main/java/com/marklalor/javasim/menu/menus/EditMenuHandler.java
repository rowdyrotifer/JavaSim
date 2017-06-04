package com.marklalor.javasim.menu.menus;

import com.marklalor.javasim.menu.MenuHandler;

public class EditMenuHandler extends MenuHandler<JavaSimMenu>
{   
    public void copy()
    {
        getMenu().getSimulation().copyImageToClipboard();
    }
}