package com.marklalor.javasim.misc.osx;

import java.io.File;
import java.util.List;

import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;
import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.SimulationInfo;

public class OSXOpenFilesHandler implements OSXRegisterableHandler, OpenFilesHandler
{
    @Override
    public void openFiles(AppEvent.OpenFilesEvent e)
    {
        List<File> files = e.getFiles();
        JavaSim.getLogger().info("OSXOpenFilesHandler opening: {}", files);
        for(File file : files)
        {
            JavaSim.getLogger().debug("Running opened simulation from initialized Home.");
            JavaSim.getHome().run(new SimulationInfo(file));
        }
    }
    
    @Override
    public void register()
    {
        Application.getApplication().setOpenFileHandler(this);
    }
}
