package com.marklalor.javasim.menu;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JMenuItem;

import com.marklalor.javasim.JavaSim;

public abstract class MenuHandler
{
    private Menu menu;
    private Map<JMenuItem, Method> menuHandlingMap;
    
    public MenuHandler()
    {
        menuHandlingMap = new HashMap<JMenuItem, Method>();
    }
    
    public void setMenu(Menu menu)
    {
        this.menu = menu;
    }
    
    public Menu getMenu()
    {
        return menu;
    }
    
    public void mapMenuItemToMethod(JMenuItem menuItem, Method method)
    {
        menuHandlingMap.put(menuItem, method);
    }
    
    public void removeMapping(JMenuItem menuItem)
    {
        menuHandlingMap.remove(menuItem);
    }
    
    public void action(ActionEvent e)
    {
        Method handlingMethod = menuHandlingMap.get((e.getSource()));
        try
        {
            handlingMethod.invoke(this);
        }
        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
        {
            JavaSim.getLogger().error("Could not invoke handler method for the JMenuItem. {}", e);
        }
    }
}
