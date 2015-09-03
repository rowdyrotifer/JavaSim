package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.marklalor.javasim.simulation.Simulation;

public class Image extends JFrame
{
	private static final long serialVersionUID = 7211460258372253616L;
	
	private Simulation simulation;
	
	//Draggable behavior
	private boolean draggable = false;
	private Point2D origin = null;
	
	private ImageJPanel imagePanel;
	
	public Image(Simulation simulation)
	{
		this.simulation = simulation;
		this.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
				Image.this.origin = null;
			}
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (draggable)
					Image.this.origin = e.getPoint();
			}
			@Override public void mouseExited(MouseEvent e)  { }
			@Override public void mouseEntered(MouseEvent e) { }
			@Override public void mouseClicked(MouseEvent e) { }
		});
		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (draggable && origin != null)
					Image.this.setLocation((int)(e.getLocationOnScreen().getX() - origin.getX()), (int)(e.getLocationOnScreen().getY() - origin.getY()));
			}
			@Override public void mouseMoved(MouseEvent e) { }
		});
		
		this.imagePanel = new ImageJPanel(this);
		setLayout(new BorderLayout());
		this.getContentPane().add(imagePanel, BorderLayout.CENTER);
	}
	
	public Simulation getSimulation()
	{
		return simulation;
	}
	
	public void setDraggable(boolean draggable)
	{
		this.draggable = draggable;
	}
	
	public boolean isDraggable()
	{
		return draggable;
	}
	
	//Set size making sure the top inset is considered.
	@Override
	public void setSize(int width, int height)
	{
		super.setSize(width, height + getInsets().top);
	}
	
	public void paintImage()
	{
		repaint();
	}
	
	//Drawing panel:
	private class ImageJPanel extends JPanel
	{
		private static final long serialVersionUID = -6540125631576837932L;
		
		private Image parent;
		
		public ImageJPanel(Image parent)
		{
			super(true);
			this.parent = parent;
			this.addMouseMotionListener(this.parent.getSimulation());
			this.addMouseListener(this.parent.getSimulation());
			this.addMouseWheelListener(this.parent.getSimulation());
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawImage(parent.getSimulation().getCurrentImage(), 0, getInsets().top, null);
		}
	}
}
