package com.marklalor.javasim.menu.menus;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.menu.MenuHandler;

public class HelpMenuHandler extends MenuHandler<JavaSimMenu>
{
    private static URI gitHub = null;
    static
    {
        try
        {
            gitHub = new URI("http://github.com/MarkLalor/JavaSim");
        }
        catch(URISyntaxException e)
        {
            JavaSim.getLogger().error("Could not create GitHub URI object!", e);
        }
    }
    
    public void openGitHub()
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if(desktop != null && desktop.isSupported(Desktop.Action.BROWSE))
        {
            try
            {
                desktop.browse(gitHub);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
