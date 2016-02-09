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
//    
//    public void action(ActionEvent e)
//    {
//        Method handlingMethod = menuHandlingMap.get((e.getSource()));
//        try
//        {
//            handlingMethod.invoke(this);
//        }
//        catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e1)
//        {
//            JavaSim.getLogger().error("Could not invoke handler method for the JMenuItem. {}", e);
//        }
//    }
}
