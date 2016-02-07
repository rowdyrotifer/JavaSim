package com.marklalor.javasim.simulation.frames.subframes;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.marklalor.javasim.control.Control;
import com.marklalor.javasim.simulation.frames.SubFrame;
import com.marklalor.javasim.simulation.frames.image.Image;

public class Controls extends SubFrame
{
    private Map<String, Control<?>> controls;
    
    public Controls(Image image)
    {
        super(image);
        this.controls = new HashMap<String, Control<?>>();
        getFrame().setTitle("Controls");
        getFrame().getContentPane().setLayout(new BoxLayout(getFrame().getContentPane(), BoxLayout.Y_AXIS));
    }
    
    public <T> void add(Control<T> control)
    {
        controls.put(control.getName(), control);
        JPanel controlPanel = control.getPanel();
        getFrame().getContentPane().add(controlPanel);
    }
    
    public Map<String, Control<?>> getControls()
    {
        return controls;
    }
    
    public void addSeparator()
    {
        getFrame().getContentPane().add(new JSeparator(JSeparator.HORIZONTAL));
    }
}
