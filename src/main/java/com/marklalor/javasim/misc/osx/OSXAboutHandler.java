package com.marklalor.javasim.misc.osx;

import com.apple.eawt.AboutHandler;
import com.apple.eawt.AppEvent.AboutEvent;
import com.apple.eawt.Application;
import com.marklalor.javasim.JavaSim;

public class OSXAboutHandler implements OSXRegisterableHandler, AboutHandler
{   
    @Override
    public void handleAbout(AboutEvent arg0)
    {
        JavaSim.getLogger().debug("OSXAboutHandler called.");
    }
    
    @Override
    public void register()
    {
        Application.getApplication().setAboutHandler(this);
    }
}
