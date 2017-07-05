package com.marklalor.javasim.controller;

public class CoreLaunchParameters
{
    public static final CoreLaunchParameters DEFAULT = new CoreLaunchParameters();
    
    private final boolean gui;
    
    public CoreLaunchParameters(String[] args)
    {
        this();
    }
    
    public CoreLaunchParameters()
    {
        this.gui = true;
    }
    
    public boolean useGui()
    {
        return gui;
    }
}
