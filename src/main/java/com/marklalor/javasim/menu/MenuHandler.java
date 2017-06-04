package com.marklalor.javasim.menu;

public abstract class MenuHandler<T extends Menu>
{
    private T menu;
    
    //User needs to make sure Menu subclass is T.
    @SuppressWarnings("unchecked")
    public void setMenu(Menu menu)
    {
        this.menu = (T) menu;
    }
    
    public T getMenu()
    {
        return menu;
    }
}