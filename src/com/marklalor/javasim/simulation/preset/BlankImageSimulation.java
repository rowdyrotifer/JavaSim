package com.marklalor.javasim.simulation.preset;

import java.awt.Color;
import java.awt.Graphics2D;

import com.marklalor.javasim.simulation.Simulation;

/**
 * Simulation wrapper class for a blank 
 */
public abstract class BlankImageSimulation extends Simulation
{
	private Color backgroundColor = Color.WHITE;
	
	/**
	 * Create a simulation that paints the screen white initially and on reset.
	 */
	public BlankImageSimulation() { }
	
	/**
	 * Create a simulation that paints the screen the specified color initially and on reset.
	 */
	public BlankImageSimulation(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
	
	@Override
	public void initialize()
	{
		//Simulation is intended for images, show the image pane. 
		getImage().setVisible(true);
	}
	
	@Override
	public void reset(Graphics2D permanent)
	{
		//Start by painting over the whole screen.
		permanent.setColor(backgroundColor);
		permanent.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public Color getBackgroundColor()
	{
		return backgroundColor;
	}
	
	public void setBackgroundColor(Color backgroundColor)
	{
		this.backgroundColor = backgroundColor;
	}
}
