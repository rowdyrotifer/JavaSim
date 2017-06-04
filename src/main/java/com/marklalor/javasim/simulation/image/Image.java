package com.marklalor.javasim.simulation.image;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.marklalor.javasim.JavaSim;
import com.marklalor.javasim.menu.menus.JavaSimMenu;
import com.marklalor.javasim.simulation.Simulation;
import com.marklalor.javasim.simulation.frames.Draggable;
import com.marklalor.javasim.simulation.frames.MenuHolder;
import com.marklalor.javasim.simulation.frames.SimulationHolder;
import com.marklalor.javasim.simulation.image.layer.ImageLayer;

public class Image extends Draggable implements SimulationHolder, MenuHolder<JavaSimMenu>
{
    private Simulation simulation;
    private JavaSimMenu menu;
    private Dimension size;
    private ImagePopupMenu popupMenu;
    
    private BufferedImage aggregateImage;
    
    private List<ImageLayer> layers;
    
    private JLabel imageLabel;
    
    public Image(Simulation simulation)
    {
        super();
        
        getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        this.simulation = simulation;
        this.layers = new ArrayList<ImageLayer>(1);
        
        getFrame().addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosed(WindowEvent e)
            {
                JavaSim.getLogger().debug("Removing simulation on window close.");
                Image.this.getSimulation().getControls().getFrame().dispose();
                Image.this.getSimulation().getHome().removeSimulation(getSimulation());
                JavaSim.getLogger().debug("Removed simulation on window close.");
            }
        });
        
        getFrame().addComponentListener(new ComponentAdapter()
        {
            @Override
            public void componentResized(ComponentEvent e)
            {
                final Insets insets = getFrame().getInsets();
                Image.this.size = new Dimension((int) getFrame().getSize().getWidth() - insets.left - insets.right, (int) getFrame().getSize().getHeight() - insets.top - insets.bottom);
                Image.this.getSimulation().resetAction();
            }
        });
        
        getFrame().getContentPane().setLayout(new BorderLayout());
        imageLabel = new JLabel();
        getFrame().getContentPane().add(imageLabel, BorderLayout.CENTER);
        
        popupMenu = new ImagePopupMenu(getSimulation());
        imageLabel.setComponentPopupMenu(popupMenu.getMenu());
        
        getFrame().getRootPane().putClientProperty("Window.documentFile", getSimulation().getInfo().getFile());
        getFrame().getRootPane().putClientProperty("Window.documentModified", false);
    }
    
    public void addLayer(ImageLayer layer)
    {
        layer.setImage(this);
        getLayers().add(layer);
        Collections.sort(getLayers());
    }
    
    public void removeLayer(ImageLayer layer)
    {
        getLayers().remove(layer);
        Collections.sort(getLayers());
    }
    
    public List<ImageLayer> getLayers()
    {
        return layers;
    }
    
    public void renderAggregateImage(boolean reset)
    {
        setAggregateImage(new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB));
        Graphics2D graphics = getAggregateImage().createGraphics();
        
        //Runs through each layer in index order.
        for(ImageLayer layer : getLayers())
        {
            layer.renderBufferedImage(reset);
            if(layer.getBufferedImage() != null)
                graphics.drawImage(layer.getBufferedImage(), 0, 0, getWidth(), getHeight(), null);
        }
        
        graphics.dispose();
    }
    
    public BufferedImage getAggregateImage()
    {
        return aggregateImage;
    }
    
    public void setAggregateImage(BufferedImage aggregateImage)
    {
        this.aggregateImage = aggregateImage;
    }
    
    public void repaint()
    {
        if(getAggregateImage().getColorModel().hasAlpha())
        {
            BufferedImage backgroundImage = createBackgroundImage();
            BufferedImage aggregateImage = getAggregateImage();
            
            BufferedImage combinedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics graphics = combinedImage.getGraphics();
            graphics.drawImage(backgroundImage, 0, 0, null);
            graphics.drawImage(aggregateImage, 0, 0, null);
            graphics.dispose();
            
            imageLabel.setIcon(new ImageIcon(combinedImage));
        }
        else
        {
            imageLabel.setIcon(new ImageIcon(getAggregateImage()));
        }
        
        getFrame().repaint();
    }
    
    private final double boxSize = 10;
    private final Color boxColorLight = new Color(0xFFFFFF);
    private final Color boxColorDark = new Color(0xC3C3C3);
    
    private BufferedImage createBackgroundImage()
    {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        
        int ax = (int) Math.ceil(getWidth() / (double) boxSize);
        int ay = (int) Math.ceil(getHeight() / (double) boxSize);
        
        // Slight change so that an integer number of boxes fit.
        double boxRecalculatedSizeX = (double) getWidth() / ax;
        double boxRecalculatedSizeY = (double) getHeight() / ay;
        
        for(int i = 0; i < ax; i++)
        {
            for(int j = 0; j < ay; j++)
            {
                graphics.setColor((i + j) % 2 == 0 ? boxColorLight : boxColorDark);
                Rectangle2D.Double box = new Rectangle2D.Double(i * boxRecalculatedSizeX, j * boxRecalculatedSizeY, boxRecalculatedSizeX, boxRecalculatedSizeY);
                graphics.fill(box);
            }
        }
        
        graphics.dispose();
        return image;
    }
    
    @Override
    public Simulation getSimulation()
    {
        return simulation;
    }
    
    @Override
    public void setSimulation(Simulation simulation)
    {
        this.simulation = simulation;
    }
    
    public JLabel getImageLabel()
    {
        return imageLabel;
    }
    
    public void setImageSize(int width, int height)
    {
        setSize(new Dimension(width, height));
    }
    
    public void setSize(Dimension size)
    {
        this.size = size;
        getFrame().getContentPane().setPreferredSize(getSize());
        getFrame().pack();
        JavaSim.getLogger().info("{}", imageLabel.getVisibleRect());
    }
    
    public Dimension getSize()
    {
        return size;
    }
    
    public void setWidth(int width)
    {
        setImageSize(width, getHeight());
    }
    
    public int getWidth()
    {
        return (int) getSize().getWidth();
    }
    
    public void setHeight(int height)
    {
        setImageSize(getWidth(), height);
    }
    
    public int getHeight()
    {
        return (int) getSize().getHeight();
    }

    @Override
    public void setMenu(JavaSimMenu menu)
    {
        this.menu = menu;
    }

    @Override
    public JavaSimMenu getMenu()
    {
        return this.menu;
    }
}
