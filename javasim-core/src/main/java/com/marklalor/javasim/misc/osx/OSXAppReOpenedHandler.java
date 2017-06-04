package com.marklalor.javasim.misc.osx;

import java.awt.Frame;

import com.apple.eawt.AppEvent.AppReOpenedEvent;
import com.apple.eawt.AppReOpenedListener;
import com.apple.eawt.Application;
import com.marklalor.javasim.JavaSim;

public class OSXAppReOpenedHandler implements OSXRegisterableHandler, AppReOpenedListener
{   
    @Override
    public void appReOpened(AppReOpenedEvent e)
    {
        //See if there are any visible frames.
        boolean anyVisible = false;
        for(Frame frame : Frame.getFrames())
            if (frame.isVisible())
                anyVisible = true;
        
        //If there are none, show the home frame and select the simulation list.
        if (!anyVisible)
        {
            JavaSim.getHome().getFrame().setVisible(true);
            JavaSim.getHome().getFrame().toFront();
            JavaSim.getHome().getSimulationList().requestFocus();
        }
    }

    @Override
    public void register()
    {
        Application.getApplication().addAppEventListener(this);
    }
}
