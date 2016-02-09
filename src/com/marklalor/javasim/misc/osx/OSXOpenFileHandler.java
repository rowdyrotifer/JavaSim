package com.marklalor.javasim.misc.osx;

import java.io.File;
import java.util.List;

import com.apple.eawt.AppEvent;
import com.apple.eawt.Application;
import com.apple.eawt.OpenFilesHandler;

import com.marklalor.javasim.JavaSim;

public class OSXOpenFileHandler implements OpenFilesHandler
{
    @Override
    public void openFiles(AppEvent.OpenFilesEvent e)
    {
        List<File> files = e.getFiles();
        JavaSim.getLogger().info("OS X open: {}", files);
        for(File file : files)
        {
            JavaSim.openFile(file);
        }
    }
    
    public static void register()
    {
        JavaSim.getLogger().debug("Registering OSXOpenFileHandler");
        Application.getApplication().setOpenFileHandler(new OSXOpenFileHandler());
    }
}
