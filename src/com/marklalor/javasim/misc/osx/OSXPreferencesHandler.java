package com.marklalor.javasim.misc.osx;

import java.io.IOException;

import com.apple.eawt.AppEvent.PreferencesEvent;
import com.apple.eawt.Application;
import com.apple.eawt.PreferencesHandler;
import com.marklalor.javasim.JavaSim;

public class OSXPreferencesHandler implements OSXRegisterableHandler, PreferencesHandler
{
    @Override
    public void handlePreferences(PreferencesEvent event)
    {
        JavaSim.getLogger().info("OS X handlePreferences called.");
        
        try
        {
            Runtime.getRuntime().exec(new String[]{"open", "-R", JavaSim.getHome().getApplicationPreferences().getFile().getAbsolutePath()});
        }
        catch(IOException e)
        {
            JavaSim.getLogger().error("Error using \"open -R\" to open the preferences file on OS X", e);
        }
    }
    
    @Override
    public void register()
    {
        Application.getApplication().setPreferencesHandler(this);
    }
}
