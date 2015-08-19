package com.marklalor.javasim.simulation.frames.subframes;

import com.marklalor.javasim.simulation.frames.Image;
import com.marklalor.javasim.simulation.frames.ImageSubframe;

public class Control extends ImageSubframe
{
	private static final long serialVersionUID = 7063568782126029536L;
	
	public Control(Image owner)
	{
		super(owner);
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height + getInsets().top);
	}
}