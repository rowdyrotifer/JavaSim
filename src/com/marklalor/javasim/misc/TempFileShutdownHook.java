package com.marklalor.javasim.misc;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import com.marklalor.javasim.JavaSim;

public class TempFileShutdownHook implements Runnable
{
    private final File tempFile;
    
    public TempFileShutdownHook(File tempFile)
    {
        this.tempFile = tempFile;
    }
    
    @Override
    public void run()
    {
        JavaSim.getLogger().debug("TempFileShutdownHook reached, deleting temporary directory {}", tempFile);
        
        try
        {
            FileUtils.deleteDirectory(tempFile);
        }
        catch(IOException e)
        {
            JavaSim.getLogger().debug("Could not delete temporary directory {}", tempFile);
        }
    }
}
