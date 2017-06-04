package com.marklalor.javasim.simulation.frames;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

public abstract class Draggable implements FrameHolder
{
    private JFrame frame;
    
    private boolean draggable = false;
    private Point2D origin = null;
    
    private MouseListener mouseListener = new MouseAdapter()
    {
        @Override
        public void mouseReleased(MouseEvent e)
        {
            Draggable.this.origin = null;
        }
        
        @Override
        public void mousePressed(MouseEvent e)
        {
            Draggable.this.origin = e.getPoint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e)
        {
            if(origin != null)
                Draggable.this.frame.setLocation((int) (e.getLocationOnScreen().getX() - origin.getX()), (int) (e.getLocationOnScreen().getY() - origin.getY()));
        }
        
    };
    
    public Draggable()
    {
        frame = new JFrame();
    }
    
    public void setDraggable(boolean draggable)
    {
        this.draggable = draggable;
        if (draggable)
            frame.addMouseListener(mouseListener);
        else
            frame.removeMouseListener(mouseListener);
    }
    
    public boolean getDraggable()
    {
        return draggable;
    }
    
    @Override
    public JFrame getFrame()
    {
        return frame;
    }
    
    @Override
    public void setFrame(JFrame frame)
    {
        this.frame = frame;
    }
}
