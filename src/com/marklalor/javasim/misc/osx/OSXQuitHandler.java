package com.marklalor.javasim.misc.osx;

import com.apple.eawt.AppEvent.QuitEvent;
import com.apple.eawt.Application;
import com.apple.eawt.QuitHandler;
import com.apple.eawt.QuitResponse;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;

public class OSXQuitHandler implements OSXRegisterableHandler, QuitHandler
{
    @Override
    public void handleQuitRequestWith(QuitEvent e, QuitResponse r)
    {
        //Decide if the quit event should be cancelled.
        if (!JavaSim.getHome().getActiveSimulations().isEmpty())
        {
            for(Simulation simulation : JavaSim.getHome().getActiveSimulations())
            {
                if (simulation.isCreatingAnimation())
                {
                    r.cancelQuit();
                    return;
                }
            }
        }
        
        //No pre-quit actions are done here because that's better don with a cross-platform shutdown thread.
        
        //If it was not cancelled, perform quit.
        r.performQuit();
    }
    
    @Override
    public void register()
    {
        Application.getApplication().setQuitHandler(this);
    }
}
