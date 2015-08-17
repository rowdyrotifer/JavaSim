package com.marklalor.javasim.simulation.frames;

import javax.swing.JDialog;

public class Control extends JDialog
{
	private Image owner;
	
	public Control(Image owner)
	{
		super(owner);
		this.owner = owner;
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height + getInsets().top);
	}
}
