package com.marklalor.javasim.simulation.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.simulation.Simulation;

public class Image implements FrameHolder
{
    private JFrame frame;
    
    private Simulation simulation;
    
    // Draggable behavior
    private boolean draggable = false;
    private Point2D origin = null;
    
    private JLabel imageLabel;
    
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
                if(draggable)
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
                if(draggable && origin != null)
                    Image.this.frame.setLocation((int) (e.getLocationOnScreen().getX() - origin.getX()), (int) (e.getLocationOnScreen().getY() - origin.getY()));
            }
            
            @Override
            public void mouseMoved(MouseEvent e)
            {
            }
        });
        
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                Image.this.getSimulation().getHome().removeSimulation(getSimulation());
                super.windowClosing(e);
            }
        });

        
        frame.getContentPane().setLayout(new BorderLayout());
        imageLabel = new JLabel();
        frame.getContentPane().add(imageLabel);
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
    
    // Set size making sure the top inset is considered.
    public void setSize(int width, int height)
    {
        frame.setSize(width, height + frame.getInsets().top);
    }
    
    public void repaint()
    {
        BufferedImage image = getSimulation().getCurrentImage();
        BufferedImage backgroundImage = createBackgroundImage();
        
        if (image.getWidth() != backgroundImage.getWidth())
            JavaSim.getLogger().error("Image width does not equal background image width.");
        if (image.getHeight() != backgroundImage.getHeight())
            JavaSim.getLogger().error("Image height does not equal background image height.");
        
        
        if (image.getColorModel().hasAlpha())
        {
            BufferedImage combined = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = combined.getGraphics();
            g.drawImage(backgroundImage, 0, 0, null);
            g.drawImage(image, 0, 0, null);
            g.dispose();
            imageLabel.setIcon(new ImageIcon(combined));
        }
        else
            imageLabel.setIcon(new ImageIcon(image));
        
        frame.repaint();
    }

    private final int boxSize = 10;
    private final Color boxColorLight = new Color(0xFFFFFF);
    private final Color boxColorDark = new Color(0xC3C3C3);
    
    private BufferedImage createBackgroundImage()
    {
        int width  = getFrame().getWidth() - getFrame().getInsets().right - getFrame().getInsets().left;
        int height = getFrame().getHeight() - getFrame().getInsets().top - getFrame().getInsets().bottom;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        
        int ax = (int) Math.ceil(width / (double) boxSize);
        int ay = (int) Math.ceil(height / (double) boxSize);
        
        for(int i = 0; i < ax; i++)
        {
            for(int j = 0; j < ay; j++)
            {
                graphics.setColor((i + j) % 2 == 0 ? boxColorLight : boxColorDark);
                graphics.fillRect(i * boxSize, j * boxSize, boxSize, boxSize);
            }
        }
        
        graphics.dispose();
        return image;
    }
}
