package com.marklalor.javasim.menu;

public abstract class MenuHandler
{
    private Menu menu;
    
    public void setMenu(Menu menu)
    {
        this.menu = menu;
    }
    
    public Menu getMenu()
    {
        return menu;
    }
}