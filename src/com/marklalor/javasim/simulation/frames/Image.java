package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.marklalor.javasim.simulation.Simulation;

public class Image implements Minimizable
{
	private Simulation simulation;
	private JFrame frame;	
	
	//Draggable behavior
	private boolean draggable = false;
	private Point2D origin = null;
	
	private ImageJPanel imagePanel;
	
	public Image(Simulation simulation)
	{
		this.simulation = simulation;
		
		frame = new JFrame();
		
		frame.addMouseListener(new MouseListener()
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
		
		frame.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
				if (draggable && origin != null)
					Image.this.frame.setLocation((int)(e.getLocationOnScreen().getX() - origin.getX()), (int)(e.getLocationOnScreen().getY() - origin.getY()));
			}
			@Override public void mouseMoved(MouseEvent e) { }
		});
		
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				Image.this.getSimulation().delete();
				super.windowClosing(e);
			}
		});
		

        frame.setLayout(new BorderLayout());
        
		imagePanel = new ImageJPanel(this);
		frame.getContentPane().add(imagePanel, BorderLayout.CENTER);
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
	
	public JFrame getFrame()
    {
        return frame;
    }
	
	//Set size making sure the top inset is considered.
	public void setSize(int width, int height)
	{
		frame.setSize(width, height + frame.getInsets().top);
	}
	
	public void paintImage()
	{
		frame.repaint();
	}
	
	//Drawing panel:
	@SuppressWarnings("serial")
    private class ImageJPanel extends JPanel
	{	
		private Image parent;
		
		public ImageJPanel(Image parent)
		{
			super(true);
			this.parent = parent;
			//TODO: readd functionality in a more per-case method.
//			this.addMouseMotionListener(this.parent.getSimulation());
//			this.addMouseListener(this.parent.getSimulation());
//			this.addMouseWheelListener(this.parent.getSimulation());
		}

		@Override
		protected void paintComponent(Graphics g)
		{
			g.clearRect(0, 0, getWidth(), getHeight());
			g.drawImage(parent.getSimulation().getCurrentImage(), 0, getInsets().top, null);
		}
	}

	@Override
	public void minimize()
	{
		if (frame.isVisible())
			frame.setState(JFrame.ICONIFIED);
	}
}
