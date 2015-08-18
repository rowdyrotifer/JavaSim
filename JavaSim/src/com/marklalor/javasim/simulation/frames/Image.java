package com.marklalor.javasim.simulation.frames;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.marklalor.javasim.component.SimulationJFrame;
import com.marklalor.javasim.simulation.Simulation;

public class Image extends SimulationJFrame
{
	private static final long serialVersionUID = 7211460258372253616L;
	
	private Simulation simulation;
	public Image(Simulation simulation)
	{
		this.simulation = simulation;
	}
	
	public Simulation getSimulation()
	{
		return simulation;
	}
	
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height + getInsets().top);
	}
	
	private BufferedImage paintImage;
	
	public void paintImage(BufferedImage paintImage)
	{
		this.paintImage = paintImage;
		this.repaint();
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.clearRect(0, 0, getWidth(), getHeight());
		g.drawImage(paintImage, 0, getInsets().top, null);
	}
}
