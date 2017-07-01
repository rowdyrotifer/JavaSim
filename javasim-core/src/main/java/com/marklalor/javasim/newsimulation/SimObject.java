package com.marklalor.javasim.newsimulation;

import com.marklalor.javasim.content.interfacing.input.InputController;
import com.marklalor.javasim.content.interfacing.output.OutputController;
import com.marklalor.javasim.simulation.interfaces.SimInterfaceRepository;
import com.marklalor.javasim.simulation.interfaces.SimObjectController;
import com.marklalor.javasim.simulation.interfaces.Simulation;

/**
 * <p>
 * No GUI is accessible here on purpose. Do not do any interaction with the GUI at this level or
 * below. The GUI will access the SimObject to optionally hook into it and interact with it.
 * </p>
 * <p>
 * This way, the simulation can be controlled via a GUI, a CLI, or any Java program at all!
 * </p>
 * 
 */
public class SimObject
{
    private final OutputController outputController;
    private final InputController inputController;
    private final SimInterfaceRepository interfaceRepository;
    
    public SimObject(Class<? extends Simulation> clazz) throws InstantiationException, IllegalAccessException
    {
        // All the import guys... these are populated with
        // goodies by the user via the SimObjectController.
        this.outputController = new OutputController();
        this.inputController = new InputController();
        this.interfaceRepository = new SimInterfaceRepository();
        
        /*
         * Create an instance of the user class and pass it a sim controller
         * We shouldn't need to keep a reference to the simulation. Trying NOT
         * to unnecessarily pack things into the Simulation class with inheritance
         * tools when inheritance does not help (would just be a god class with a lot of
         * empty methods if I went without the way of the controller.
         */
        clazz.newInstance().init(new SimObjectController(this));
    }
    
    public InputController getInputController()
    {
        return inputController;
    }
    
    public OutputController getOutputController()
    {
        return outputController;
    }
    
    public SimInterfaceRepository getInterfaceRepository()
    {
        return interfaceRepository;
    }
}
