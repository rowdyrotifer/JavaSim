package com.marklalor.javasim.gui;

import com.marklalor.javasim.newsimulation.SimObject;

public class SimGUI
{
    private SimObject sim;

    public SimGUI(SimObject sim)
    {
        this.sim = sim;
    }
    
    public SimObject getSim()
    {
        return sim;
    }
    
}
