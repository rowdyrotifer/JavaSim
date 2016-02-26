package com.marklalor.javasim.simulation.frames;

import com.marklalor.javasim.menu.Menu;

public interface MenuHolder<T extends Menu>
{
    void setMenu(T menu);
    T getMenu();
}
