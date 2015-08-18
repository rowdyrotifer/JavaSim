package com.marklalor.javasim.component;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

//TODO: port over to simulationimage...

public class SimulationJFrame extends JFrame
{
	private boolean draggable = false;
	private Point2D origin = null;
	
	public SimulationJFrame()
	{
		this.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				origin = null;
			}
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (draggable)
					origin = e.getPoint();
			}
			@Override
			public void mouseExited(MouseEvent e){}
			@Override
			public void mouseEntered(MouseEvent e){}
			@Override
			public void mouseClicked(MouseEvent e){}
		});
		
		this.addMouseMotionListener(new MouseMotionListener()
		{
			
			@Override
			public void mouseMoved(MouseEvent e){}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (draggable && origin != null)
					setLocation((int)(e.getLocationOnScreen().getX() - origin.getX()), (int)(e.getLocationOnScreen().getY() - origin.getY()));
			}
		});
	}
	
	public void setDraggable(boolean draggable)
	{
		this.draggable = draggable;
	}
	
	public boolean isDraggable()
	{
		return draggable;
	}
}
