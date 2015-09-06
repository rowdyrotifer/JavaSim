package com.marklalor.javasim.simulation.frames.subframes;

import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.marklalor.javasim.control.Control;
import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.ImageSubframe;

public class Controls extends ImageSubframe
{
	private static final long serialVersionUID = 7063568782126029536L;
	private Map<String, Control<?>> controls;
	private int autoKeyN = 0;
	
	public Controls(Image owner)
	{
		super(owner);
		controls = new HashMap<String, Control<?>>();
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		setTitle("Controls");
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height + getInsets().top);
	}

	public <T> void add(Control<T> control)
	{
		controls.put(control.getName(), control);
		JPanel controlPanel = control.getPanel();
		getContentPane().add(controlPanel);
	}
	
	public Map<String, Control<?>> getControls()
	{
		return controls;
	}
	
	public void addSeparator()
	{
		getContentPane().add(new JSeparator(JSeparator.HORIZONTAL));
	}
}