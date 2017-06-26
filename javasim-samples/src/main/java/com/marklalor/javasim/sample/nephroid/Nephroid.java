package com.marklalor.javasim.sample.nephroid;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.content.interfacing.output.BufferedImageOutputData;
import com.marklalor.javasim.content.interfacing.sim.SimController;
import com.marklalor.javasim.content.interfacing.sim.Simulation;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SequentialOutputGenerator;
import com.marklalor.javasim.content.interfacing.typemaps.OutputDataType;

public class Nephroid implements Simulation, SequentialOutputGenerator  
{
    public static void main(String[] args)
    {
        JavaSim.launch(Nephroid.class, args);
    }
    
    private final BufferedImageOutputData output;

    public void init(SimController controller)
    {
        controller.registerSimInterface(this);
        output = (BufferedImageOutputData) controller.createOutputData(OutputDataType.CACHED_BUFFERED_IMAGE);
    }

    public void generateNextOutputData()
    {
        // TODO Auto-generated method stub
        
    }
}
