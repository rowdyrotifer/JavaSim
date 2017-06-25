package com.marklalor.javasim.util;

import java.io.File;

import org.apache.commons.lang3.SystemUtils;

public class PathUtils
{
    private static File BASE_DIRECTORY;
    
    static
    {
        if (SystemUtils.IS_OS_MAC)
        {
            BASE_DIRECTORY = new File(
                    System.getProperty("user.home")  + 
                    File.separator + "Library" + 
                    File.separator + "Application Support" + 
                    File.separator + "JavaSim");
        }
        else if (SystemUtils.IS_OS_WINDOWS)
        {
            BASE_DIRECTORY = new File(
                    System.getenv("APPDATA") + 
                    File.separator + "JavaSim");
        }
        else if (SystemUtils.IS_OS_LINUX)
        {
            BASE_DIRECTORY = new File(
                    File.separator + "var" + 
                    File.separator + "lib" + 
                    File.separator + "javasim");
        }
        else
        {
            BASE_DIRECTORY = new File(
                    System.getProperty("user.home") + 
                    File.separator + "JavaSim");
        }
    }
    
    public static File getBaseDirectory()
    {
        return BASE_DIRECTORY;
    }
    
}
