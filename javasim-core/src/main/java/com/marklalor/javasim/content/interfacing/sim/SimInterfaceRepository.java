package com.marklalor.javasim.content.interfacing.sim;

import java.util.LinkedHashSet;
import java.util.Set;

import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SequentialOutputGenerator;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimExitable;
import com.marklalor.javasim.content.interfacing.sim.interfaces.SimInterface;
import com.marklalor.javasim.content.interfacing.typemaps.SimInterfaceType;

public class SimInterfaceRepository implements SimInterfaces
{
    private final SetMultimap<SimInterfaceType, SimInterface> interfaces;
    
    private final Set<SimExitable> exitables;
    private final Set<SequentialOutputGenerator> sequentialOutputGenerators;
    
    public SimInterfaceRepository()
    {
        this.interfaces = SetMultimapBuilder.hashKeys().linkedHashSetValues().build();
        this.exitables = new LinkedHashSet<>();
        this.sequentialOutputGenerators = new LinkedHashSet<>();
    }
    
    @Override
    public Set<SimExitable> getExitables()
    {
        return exitables;
    }
    
    @Override
    public Set<SequentialOutputGenerator> getSequentialOutputGenerators()
    {
        return sequentialOutputGenerators;
    }
    
    public void add(SimInterface simInterface)
    {
        boolean any = false;
        for (SimInterfaceType type : SimInterfaceType.values())
        {
            if (type.getSimInterfaceClass().isAssignableFrom(simInterface.getClass()))
            {
                registerInterface(type, simInterface);
                any = true;
            }
        }
        
        if (!any)
            throw new IllegalArgumentException("The provided SimInterface is supported (not in SimInterfaceType");
    }

    private void registerInterface(SimInterfaceType type, SimInterface simInterface)
    {
        //Put into the main set multimap
        this.interfaces.put(type, simInterface);
        //Put into its specific set
        this.addToSet(type, simInterface);
    }

    private boolean addToSet(SimInterfaceType type, SimInterface simInterface)
    {
        switch(type)
        {
            case EXITABLE:
                return this.exitables.add((SimExitable) simInterface);
            case SEQUENTIAL_OUTPUT_GENERATING:
                return this.sequentialOutputGenerators.add((SequentialOutputGenerator) simInterface);
            default: return false;
        }
    }
}
